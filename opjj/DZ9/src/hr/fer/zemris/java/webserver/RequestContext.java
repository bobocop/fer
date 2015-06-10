package hr.fer.zemris.java.webserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * A class that stores the relevant information about a request that has
 * been received. The server uses its write methods to write a response to
 * the client's socket. The default encoding used for textual responses
 * is UTF-8.
 */
public class RequestContext {
	private String encoding = "UTF-8";
	private int statusCode = 200;
	private String statusText = "OK";
	private String mimeType = "text/html";
	private Map<String, String> parameters;
	private Map<String, String> persistentParameters;
	private List<RCCookie> outputCookies;
	private boolean headerGenerated = false;
	private OutputStream outputStream;
	private Charset charset;
	private final String HEAD_GEN_NO_SET = 
			"Cannot set encoding, statusCode, statusText, mimeType or " +
			"change the outputCookies: the header has already been generated";
	
	/**
	 * Constructs a new RequestContext object.
	 * @param outputstream the stream this object will write to
	 * @param parameters the parameters received from the client
	 * @param persistentParameters the parameters which are stored on the server
	 * @param outputCookies a list of cookies to be placed into the header
	 */
	public RequestContext(OutputStream outputstream, 
			Map<String, String> parameters, 
			Map<String, String> persistentParameters,
			List<RCCookie> outputCookies) {
		if(outputstream == null) {
			throw new IllegalArgumentException("'outpustream' must not be null");
		} else {
			this.outputStream = outputstream;
		}
		this.parameters = parameters;
		this.persistentParameters = persistentParameters;
		this.outputCookies = outputCookies;
	}
	
	/**
	 * Retrieves a value from the 'parameters' map.
	 * @param name the alias of the required parameter
	 * @return the parameter that corresponds to 'name', null if
	 * such does not exist
	 */
	public String getParameter(String name) {
		if(parameters == null) {
			return null;
		} else {
			return parameters.get(name);
		}
	}
	
	/**
	 * Retrieves a value from the 'persistentParameters' map.
	 * @param name the alias of the required parameter
	 * @return the parameter that corresponds to 'name', null if 
	 * such does not exist
	 */
	public String getPersistentParameter(String name) {
		if(persistentParameters == null) {
			return null;
		} else {
			return persistentParameters.get(name);
		}
	}
	
	/**
	 * Stores a parameter to the 'persistentParameters' map.
	 * @param name the name of the parameter
	 * @param value parameter value
	 */
	public void setPersistentParameter(String name, String value) {
		if(persistentParameters == null) {
			persistentParameters = new HashMap<>();
		}
		persistentParameters.put(name, value);
	}
	
	/**
	 * Writes the data to this RequestContext object's output stream. The
	 * text is converted according to the encoding property of this object. 
	 * The first time a 'write' method is used, any attempts to change
	 * a public property of this object will result in RuntimeException
	 * being thrown.
	 * @param data the data to be written to this objects output stream
	 * @return
	 * @throws IOException
	 */
	public RequestContext write(byte[] data) throws IOException {
		if(!headerGenerated) {
			outputStream.write(generateHeader());
			charset = Charset.forName(encoding);
			headerGenerated = true;
		}
		outputStream.write(data);
		outputStream.flush();
		return this;
	}
	
	/**
	 * Writes the data to this RequestContext object's output stream. The
	 * text is converted according to the encoding property of this object.
	 * The first time a 'write' method is used, any attempts to change
	 * a public property of this object will result in RuntimeException
	 * being thrown.
	 * @param text the text to be written to this objects output stream
	 * @return
	 * @throws IOException
	 */
	public RequestContext write(String text) throws IOException {
		if(!headerGenerated) {
			outputStream.write(generateHeader());
			charset = Charset.forName(encoding);
			headerGenerated = true;
		}
		outputStream.write(text.getBytes(charset));
		outputStream.flush();
		return this;
	}

	/**
	 * @return The encoding this object is set to when 
	 * converting text to bytes.
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * Sets the 'encoding' property to the value of the parameter.
	 * Setting is not permitted after the header was generated and if 
	 * that happens, RuntimeException will be thrown.
	 * @param encoding The string denoting the desired encoding to be
	 * used when converting textual input to bytes.
	 */
	public void setEncoding(String encoding) {
		if(headerGenerated) {
			throw new RuntimeException(HEAD_GEN_NO_SET);
		}
		this.encoding = encoding;
	}

	/**
	 * @return The status code of this object.
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * Sets the 'statusCode' property to the value of the parameter.
	 * Setting is not permitted after the header was generated and if 
	 * that happens, RuntimeException will be thrown.
	 * @param statusCode the new status code (corresponds to the status text)
	 */
	public void setStatusCode(int statusCode) {
		if(headerGenerated) {
			throw new RuntimeException(HEAD_GEN_NO_SET);
		}
		this.statusCode = statusCode;
	}
	
	/**
	 * @return The status text of this object.
	 */
	public String getStatusText() {
		return statusText;
	}

	/**
	 * Sets the 'statusText' property to the value of the parameter.
	 * Setting is not permitted after the header was generated and if 
	 * that happens, RuntimeException will be thrown.
	 * @param statusText the new status text (corresponds to the status code)
	 */
	public void setStatusText(String statusText) {
		if(headerGenerated) {
			throw new RuntimeException(HEAD_GEN_NO_SET);
		}
		this.statusText = statusText;
	}

	/**
	 * @return The mime type of this object.
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * Sets the 'mimeType' property to the value of the parameter.
	 * Setting is not permitted after the header was generated and if 
	 * that happens, RuntimeException will be thrown.
	 * @param mimeType a string denoting the mime type
	 */
	public void setMimeType(String mimeType) {
		if(headerGenerated) {
			throw new RuntimeException(HEAD_GEN_NO_SET);
		}
		this.mimeType = mimeType;
	}
	
	/**
	 * Returns the 'outputCookies' property as a new list. When the changes to this
	 * property are required, the user may call this method, do the neccesary
	 * changes on the returned list and finally pass the altered list to the 
	 * 'setOutputCookies()' method. After a certain moment (header generation), 
	 * the changes to this property will not be permitted, but this method 
	 * may still be called.
	 * @return The copy of 'outputCookies' property, as an ArrayList.
	 */
	public List<RCCookie> getOutputCookies() {
		return new ArrayList<RCCookie>(outputCookies);
	}

	/**
	 * Sets the 'outputCookies' property to the value of the parameter.
	 * Setting is not permitted after the header was generated and if 
	 * that happens, RuntimeException will be thrown.
	 * @param outputCookies a List containing RCCookies
	 */
	public void setOutputCookies(List<RCCookie> outputCookies) {
		if(headerGenerated) {
			throw new RuntimeException(HEAD_GEN_NO_SET);
		}
		this.outputCookies = outputCookies;
	}
	
	/**
	 * Adds a new RCCookie to the list. Adding is not permitted after
	 * the header has been created.
	 * @param cookie a cookie to be added
	 */
	public void addRCCookie(RCCookie cookie) {
		if(headerGenerated) {
			throw new RuntimeException(HEAD_GEN_NO_SET);
		}
		if(outputCookies == null) {
			outputCookies = new ArrayList<>();
		}
		outputCookies.add(cookie);
	}
	
	/**
	 * Removes a RCCookie from the list. Removing is not permitted after
	 * the header has been created.
	 * @param cookie a cookie to be removed
	 */
	public void removeRCCookie(RCCookie cookie) {
		if(headerGenerated) {
			throw new RuntimeException(HEAD_GEN_NO_SET);
		}
		if(outputCookies != null) {
			outputCookies.remove(cookie);
		}
	}

	/**
	 * @return This objects output stream.
	 */
	public OutputStream getOutputStream() {
		return outputStream;
	}
	
	/**
	 * Removes a persistent parameter from the map.
	 * @param name the name of the parameter that should be removed
	 */
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}
	
	/**
	 * Retrieves the names of all the persistent parameters and returns
	 * them as a read-only set.
	 */
	public Set<String> getPersistentParameterNames() {
		return Collections.unmodifiableSet(persistentParameters.keySet());
	}
	
	/**
	 * Retrieves the names of all the parameters and returns them as
	 * a read-only set.
	 */
	public Set<String> getParameterNames() {
		return Collections.unmodifiableSet(parameters.keySet());
	}
	
	/**
	 * Generates and returns the sequence of bytes to be used as a header.
	 * @return The header as an array of bytes.
	 */
	private byte[] generateHeader() {
		if(mimeType.substring(0, 5).equals("text/")) {
			mimeType += "; charset=" + encoding;
		}
		String header = 
				"HTTP/1.1 " + statusCode + " " + statusText 
				+ "\r\n"
				+ "Content-Type: " + mimeType
				+ "\r\n";
		if(outputCookies != null) {
			for(RCCookie c : outputCookies) {
				header += "Set-Cookie: " + c.toString() + "\r\n";
			}
		} else {
			header += "\r\n";
		}
		header += "\r\n";
		System.out.println(header);
		return header.getBytes(StandardCharsets.ISO_8859_1);
	}

	public static class RCCookie {
		private String name;
		private String value;
		private String domain;
		private String path;
		private Integer maxAge;
		
		public RCCookie(String name, String value, Integer maxAge, String domain,
				String path) {
			super();
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}

		/**
		 * Returns the string representation of this object 
		 * in the following form:
		 * 'name'=”'value'”; Domain='domain'; Path='path'; maxAge='maxAge'
		 * domain, path and maxAge are included only if they are not null
		 * in given cookie object. For example, for acookie
		 * with only name set to 'korisnik' and value set to 'perica'
		 * it would emit:
		 * korisnik="perica"
		 * If a cookie also included maxAge set to 3600 it emits a line:
		 * korisnik="perica"; maxAge=3600
		 */
		@Override
		public String toString() {
			String cookie = name + "=" + "\"" + value + "\"";
			if(domain != null) {
				cookie += "; Domain=" + domain;
			}
			if(path != null) {
				cookie += "; Path=" + path;
			}
			if(maxAge != null) {
				cookie += "; Max-Age=" + maxAge;
			}
			return cookie;
		}
		
		/**
		 * @return the cookie's name
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * @return the associated value
		 */
		public String getValue() {
			return value;
		}
		
		/**
		 * @return the domain this cookie was received from
		 */
		public String getDomain() {
			return domain;
		}
		
		/**
		 * @return the path the information in this cookie is reffering to
		 */
		public String getPath() {
			return path;
		}
		
		/**
		 * @return the time before the browser may delete this cookie
		 */
		public int getMaxAge() {
			return maxAge;
		}
	}
}
