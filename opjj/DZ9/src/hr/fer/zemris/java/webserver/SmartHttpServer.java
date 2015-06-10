package hr.fer.zemris.java.webserver;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Using the server:<br><br>
 * To start the server, enter 'start'. Once started, to be safely shut down,
 * it must first be stopped with a 'stop' command, and later turned off
 * with the 'quit' command.<br><br>
 * Unless explicitly specified, the server will 
 * send its responses using the UTF-8 encoding. This is determined by the
 * {@link RequestContext} objects which encapsulate the client requests.
 * One may use {@link RequestContext.setEncoding()} to change this for a
 * certain request.
 */
public class SmartHttpServer {
	private String address;
	private int port;
	private int workerThreads;
	private int sessionTimeout;
	private Map<String, String> mimeTypes = new HashMap<>();
	private ServerThread serverThread;
	private ExecutorService threadPool;
	private Path documentRoot;
	private Map<String, IWebWorker> workersMap = new HashMap<>();
	private Map<String, SessionMapEntry> sessions = new ConcurrentHashMap<>();
	private Random sessionRandom = new Random();
	private final String SID_ALLOWED_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private final int SID_LEN = 20;
	
	public static void main(String[] args) {
		SmartHttpServer server = null;
		if(args.length == 1) {
			server = new SmartHttpServer(args[0]);
		} else {
			System.err.println("Expected one argument: a server config file path");
			System.exit(-1);
		}
		System.out.println("Server config loaded. Enter command (start/stop/quit):");
		Scanner adminInput = new Scanner(System.in);
		while(true) {
			String command = adminInput.nextLine();
			if(command.equals("start")) {
				System.out.println("Server started");
				server.start();
			} else if(command.equals("stop")) {
				System.out.println("Server stopped");
				server.stop();
			} else if(command.equals("quit")) {
				adminInput.close();
				System.exit(0);
			}
		}	
	}
	
	/**
	 * @param serverConfig the path to a file which contains the server's
	 * configuration properties.
	 */
	public SmartHttpServer(String serverConfig) {
		Properties servProps = new Properties();
		try(
				FileInputStream in = new FileInputStream(serverConfig)
				) {
			servProps.load(in);
		} catch (IOException e) {
			System.err.println("Could not read the server config file");
			System.exit(-1);
		}
		this.address = servProps.getProperty(
				"server.address");
		this.port = Integer.parseInt(servProps.getProperty(
				"server.port"));
		this.workerThreads = Integer.parseInt(servProps.getProperty(
				"server.workerThreads"));
		this.sessionTimeout = Integer.parseInt(servProps.getProperty(
				"session.timeout"));
		this.documentRoot = Paths.get(servProps.getProperty(
				"server.documentRoot"));
		loadMimeTypes(servProps.getProperty("server.mimeConfig"));
		loadWorkers(servProps.getProperty("server.workers"));
	}
	
	/**
	 * Starts the server thread. There may be only one instance of
	 * this thread running at a time. Once started, it will start
	 * listening on the defined port.
	 */
	protected synchronized void start() {
		if(serverThread == null || !serverThread.isAlive()) {
			threadPool = Executors.newFixedThreadPool(workerThreads);
			serverThread = new ServerThread();
			serverThread.start();
		}		
	}
	
	/**
	 * Stops the server thread and releases used resources.
	 */
	protected synchronized void stop() {
		if(serverThread.isAlive()) {
			try {
				serverThread.ssocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(threadPool != null) {
			threadPool.shutdown();
		}
	}
	
	/**
	 * Loads the mime types from the properties file that
	 * should be placed in this programs working directory.
	 * The properties file contains pairs in the following form:
	 * 'file_extension = mime_type'
	 * @param mimeConfig the name of a .properties file
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadMimeTypes(String mimeConfig) {
		Properties mt = new Properties();
		try(
				FileInputStream in = new FileInputStream(mimeConfig)
				) {
			mt.load(in);
		} catch (Exception e) {
			System.err.println("Unable to read the mime properties file");
			System.exit(-1);
		}
		mimeTypes.putAll((Map) mt);
	}
	
	/**
	 * Loads the workers from the properties file that
	 * should be placed in this programs working directory.
	 * The properties file contains pairs in the following form:
	 * 'worker_name = fully_qualified_class_name_of_the_worker'
	 * @param workersConfig the name of a .properties fild
	 */
	private void loadWorkers(String workersConfig) {
		Properties w = new Properties();
		try(
				FileInputStream in = new FileInputStream(workersConfig)
				) {
			w.load(in);
		} catch (IOException e) {
			System.err.println("Unable to read the workers properties file");
			System.exit(-1);
		} catch (IllegalArgumentException e) {
			System.err.println("'workers.properties' contains multiple keys");
			System.exit(-1);
		}
		for(Object key : w.keySet()) {
			String path = (String) key;
			String fqcn = (String) w.get(key);
			workersMap.put(path, stringToIWW(fqcn));
		}
	}
	
	/**
	 * Constructs a new worker object of the class which name was passed
	 * as an argument.
	 * @param fqcn the fully qualified class name of the worker object
	 * @return The desired worker instance, null if an error occured.
	 */
	private IWebWorker stringToIWW(String fqcn) {
		IWebWorker iww = null;
		try {
			Class<?> referenceToClass = this.getClass()
					.getClassLoader().loadClass(fqcn);
			Object newObject = referenceToClass.newInstance();
			iww = (IWebWorker) newObject;
		} catch (Exception e) {
			System.err.println("Could not instantiate " + fqcn);
		}
		return iww;
	}
	
	/**
	 * Generates a random session ID of a desired length, 
	 * containing only the allowed characters (these are
	 * servers properties). This method is meant to be used
	 * simultaneously by multiple ClientWorkers and therefore
	 * the lock is required, but perhaps a better idea would be to use a
	 * {@link ThreadLocalRandom} instead of {@link Random} sessionRandom
	 * object as a server property.
	 * @return The generated session ID.
	 */
	private synchronized String generateSid() {
		char[] sid = new char[SID_LEN];
	    for (int i = 0; i < sid.length; i++) {
	        sid[i] = SID_ALLOWED_CHARS.charAt(sessionRandom
	        		.nextInt(SID_ALLOWED_CHARS.length()));
	    }
	    return new String(sid);
	}
	
	/**
	 * A thread that does the typical server jobs: binding to address/port,
	 * listening for clients and creating new worker threads whenever
	 * a request is received.
	 */
	protected class ServerThread extends Thread {
		private ServerSocket ssocket;
		
		@Override
		public void run() {
			try {
				ssocket = new ServerSocket();
				ssocket.bind(new InetSocketAddress(address, port));
				while(true) {
					Socket client = ssocket.accept();
					ClientWorker cw = new ClientWorker(client);
					threadPool.execute(cw);
				}
			} catch (SocketException e) {
				// blocking Serversocket.accept() throws this when close()'d
				System.out.println("The server socket has been closed");
			} catch (IOException e) {
				System.err.println("Could not set up the server socket");
			}
		}
	}
	
	/**
	 * Contains information about sessions the server has
	 * participated in. The properties may be accessed directly, so
	 * no getters/setters need be implemented.
	 */
	private static class SessionMapEntry {
		String sid;
		long validUntil;
		Map<String, String> map;
		
		/**
		 * @param sid the session ID; and 20-char string, uppercased
		 * @param validUntil the time in seconds the stored data is valid until
		 * @param map the data in name/value pairs
		 */
		public SessionMapEntry(String sid, long validUntil,
				Map<String, String> map) {
			super();
			this.sid = sid;
			this.validUntil = validUntil;
			this.map = map;
		}
	}
	
	/**
	 * Instances of this class handle client requests. Every time
	 * a request is received, an instance is created with a unique 
	 * session ID.
	 */
	private class ClientWorker implements Runnable {
		private Socket csocket;
		private PushbackInputStream istream;
		private OutputStream ostream;
		private String version;
		private String method;
		private Map<String, String> params = new HashMap<>();
		private Map<String, String> permParams = null;
		private List<RCCookie> outputCookies = new ArrayList<>();
		private String sid;
		
		/**
		 * @param csocket a socket the server uses to send to and receive
		 * data from the client
		 */
		public ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;
			this.sid = generateSid();
		}
		
		@Override
		public void run() {
			// open the streams associated with the client socket
			try {
				istream = new PushbackInputStream(csocket.getInputStream());
				ostream = csocket.getOutputStream();
			} catch (IOException e) {
				System.err.println("Unable to open the client streams");
				closeClient();
				return;
			}
			
			List<String> request = readRequest();
			if(request.isEmpty()) {
				sendErrorToClient("Bad Request", 400);
				return;
			}
			
			// the first line should look something like: 
			// GET /abc/def?name=joe&country=usa HTTP/1.1
			String[] firstLine = request.get(0).split(" ");
			System.out.println("New request from " + csocket.getRemoteSocketAddress() + ":");
			System.out.println(request.get(0));
			if(firstLine.length != 3) {
				sendErrorToClient("Bad Request", 400);
				return;
			}
			method = firstLine[0];	// GET
			String requested = firstLine[1];	// /abc/def?param1&param2...
			version = firstLine[2];	// HTTP/1.1
			if(!method.equals("GET")
				|| (!version.equals("HTTP/1.1") 
					&& !version.equals("HTTP/1.0"))) {
				sendErrorToClient("Bad Request", 400);
				return;
			}
			
			SessionMapEntry session = checkRequest(request);
			permParams = session.map;
			
			// split the path and parameters
			String[] reqSplit = requested.split("\\?");
			String requestedPath;
			String paramString = null;
			requestedPath = reqSplit[0].substring(1);	// abc/def
			if(reqSplit.length == 2) {
				paramString = reqSplit[1];
			}
			// get the parameters
			parseParamString(paramString);
			// check if the requested path is valid
			Path path = null;
			try {
				path = documentRoot.resolve(requestedPath);
			} catch (InvalidPathException e) {
				sendErrorToClient("Forbidden", 403);
				return;
			}
			
			// check if the requested path is mapped to a worker
			
			// check if the worker is directly called, process if it is
			if(requestedPath.startsWith("ext/")) {
				String fqcn = "hr.fer.zemris.java.webserver.workers." 
						+ requestedPath.substring(4);
				IWebWorker worker = stringToIWW(fqcn);
				if(worker == null) {
					sendErrorToClient("Not Found", 404);
				} else {
					worker.processRequest(new RequestContext(
							ostream, params, permParams, outputCookies));
				}
				closeClient();
				return;
			}
			// check the config file, process if the worker exists
			IWebWorker worker = null;
			if((worker = workersMap.get("/"+requestedPath)) != null) {
				worker.processRequest(new RequestContext(
						ostream, params, permParams, outputCookies));
				closeClient();
				return;
			}
			
			// a file was requested, not the workers
			
			// check if the requested path is a valid file
			if(!Files.exists(path) || !Files.isReadable(path)) {
				sendErrorToClient("Not Found", 404);
				return;
			}
			if(Files.isDirectory(path)) {
				sendErrorToClient("Forbbiden", 403);
				return;
			}
			// finding the extension
			String[] fileNameSplit = path.getFileName().toString().split("\\.");
			if(fileNameSplit.length != 2) {
				// unable to extract the ext
				sendErrorToClient("Bad Request", 400);
				return;
			}
			String ext = fileNameSplit[1];
			// check if it's smartscript (.smscr), execute
			if(ext.equals("smscr")) {
				RequestContext rc = new RequestContext(ostream, params, 
						permParams, outputCookies);
				rc.setStatusCode(200);
				rc.setStatusText("OK");
				rc.addRCCookie(new RCCookie("sid", session.sid, 
						sessionTimeout, address, "/"));
				try {
					new SmartScriptEngine(
							new SmartScriptParser(
									new String(Files.readAllBytes(path)
											)
									).getDocNode(), 
									
									rc)
					.execute();
				} catch (IOException e) {
					System.err.println("Could not read bytes from: "
							+ path.toString());
				} catch (SmartScriptParserException e) {
					try {
						// the script has syntax error(s)
						rc.write(requestedPath + ": " + e.getMessage());
					} catch (IOException ingnorable) {}
				}
				closeClient();
				return;
			}
			
			// get the appropriate mimetype
			String mimeType;
			if((mimeType = mimeTypes.get(ext)) == null) {
				mimeType = "application/octet-stream";
			}
			// everything went ok, send
			sendDataToClient(path, mimeType, session);
			return;
		}
		
		/**
		 * Sends an error indentified by its code/text pair. This method will
		 * automatically close any streams opened by the client, by calling
		 * {@link ClientWorker.closeClient()}
		 * @param statusText status text that corresponds to error code
		 * @param statusCode error code that corresponds to status text
		 */
		private void sendErrorToClient(String statusText, int statusCode) {
			RequestContext rc = new RequestContext(ostream, params,
					permParams, outputCookies);
			rc.setStatusCode(statusCode);
			rc.setStatusText(statusText);
			rc.setMimeType("text/html");
			try {
				rc.write("<html><body>");
				rc.write("<h1>" + statusCode + "   " + statusText + "</h1>");
				rc.write("<p>The page cannot be displayed.</p>");
				rc.write("</body></html>");
			} catch (IOException e) {
				System.err.println("Could not write to the request context");
			}
			closeClient();
		}
		
		/**
		 * Sends the requested data to client. This method will
		 * automatically close any streams opened by the client, by calling
		 * {@link ClientWorker.closeClient()}
		 * @param reqPath path to the requested content
		 * @param mimeType denotes the type of requested content
		 * @param session the session ID
		 */
		private void sendDataToClient(Path reqPath, String mimeType,
				SessionMapEntry session) {
			RequestContext rc = new RequestContext(ostream, params, 
					permParams, outputCookies);
			rc.setMimeType(mimeType);
			rc.setStatusCode(200);
			rc.setStatusText("OK");
			rc.addRCCookie(new RCCookie("sid", session.sid, 
					sessionTimeout, address, "/"));
			try {
				byte[] data = Files.readAllBytes(reqPath);
				rc.write(data);
			} catch (IOException e) {
					System.err.println("Could not write to the request context");
			}
			closeClient();
		}
		
		/**
		 * Scans the clients request for cookies, looking for the information
		 * whether the request belongs to an ongoing session. In that case, it
		 * will refresh the {@link SessionMapEntry} object representing that
		 * session by increasing its 'validUntil' property. If no such objects
		 * are found, a new session will be generated and added to the
		 * {@link SmartHttpServer}'s sessions map.
		 * @param request the clients request as a list of lines
		 * @return Either a newly created or an already existing
		 * object that was retrieved from the servers map.
		 */
		private SessionMapEntry checkRequest(List<String> request) {
			Map<String, String> extracted = new HashMap<>();
			String sidCandidate = null;
			for(String requestLine : request) {
				if(requestLine.isEmpty()) {
					// header end
					break;
				}
				if(requestLine.startsWith("Cookie: ")) {
					String[] cookies = requestLine.substring(8).split(";");
					for(String cookie : cookies) {
						String name = cookie.split("=")[0];
						String value = cookie.split("=")[1];
						value = value.substring(1, value.length()-1);	// removes " "
						if(name.equals("sid")) {
							sidCandidate = value;
						}
						extracted.put(name, value);
					}
				}
			}
			SessionMapEntry ret = null;
			long currentTime = System.currentTimeMillis() / 1000L;
			if(sidCandidate != null) {
				if((ret = sessions.get(sidCandidate)) != null) {
					if(ret.validUntil < currentTime) {
						// too old
						ret = new SessionMapEntry(sid,
								currentTime + (long) sessionTimeout,
								extracted);
						System.out.println("Session found, but expired");
					} else {
						ret.validUntil = currentTime + (long) sessionTimeout;
						System.out.println("Found an existing session");
						System.out.println(ret.sid);
					}
					return ret;
				}
			}
			// parameter 'sid' not found
			ret = new SessionMapEntry(sid, 
					currentTime + (long) sessionTimeout, 
					extracted);
			sessions.put(ret.sid, ret);
			return ret;
		}
		
		/**
		 * Reads the clients request and returns it as a list
		 * of lines.
		 * @return A list of lines that form a clients request.
		 */
		private List<String> readRequest() {
			Scanner sc = new Scanner(istream);
			List<String> request = new ArrayList<>();
			String line = sc.nextLine();
			while(!line.isEmpty()) {
				request.add(line);
				line = sc.nextLine();
			}
			return request;
		}
		
		/**
		 * Parses the parameter string in the form of:
		 * "param1=value1&param2=value2&..." and stores
		 * the retrieved names and values to the {@link ClientWorker}'s
		 * parameters map.
		 * @param paramString a correctly formatted string containing
		 * the name and value pairs.
		 */
		private void parseParamString(String paramString) {
			if(paramString == null) {
				return;
			}
			String[] nameValues = paramString.split("&");
			for(String nameValue : nameValues) {
				String name = nameValue.split("=")[0];
				String value = nameValue.split("=")[1];
				params.put(name, value);
			}
		}
		
		/**
		 * Used by the ClientWorker thread to close the streams it has
		 * been using before stopping (succesfully or otherwise). That means
		 * there usually is a 'return' immidiately after the call of this function.
		 */
		private void closeClient() {
			try {
				if(istream != null) {
					istream.close();
				}
				if(ostream != null) {
					ostream.close();
				}
			} catch (IOException e) {
				System.err.println("A stream could not be closed");
			}
		}
	}
}
