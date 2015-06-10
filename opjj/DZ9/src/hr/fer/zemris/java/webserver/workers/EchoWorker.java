package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Displays the names and values of the parameters from the 
 * clients request in an html table.
 */
public class EchoWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) {
		context.setMimeType("text/html");
		try {
			context.write("<html><body><b>Parameters table:</b><br> ");
			if(context.getParameterNames().isEmpty()) {
				context.write("<br>No parameters were passed!</body></html>");
				return;
			}
			context.write("<br><table border=\"2\">");
			for(String param : context.getParameterNames()) {
				context.write("<tr><td>" + param + "</td><td>"
						+ context.getParameter(param) + "</td></tr>");
			}
			context.write("</table></body></html>");
		} catch(IOException e) {
			System.err.println("EchoWorker: could not write to the socket");
		}
	}
}
