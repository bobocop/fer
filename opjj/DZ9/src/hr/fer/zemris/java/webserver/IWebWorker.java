package hr.fer.zemris.java.webserver;

/**
 * Interface for classes which process client requests.
 */
public interface IWebWorker {
	
	/**
	 * The actual processing done by the instances. The result of
	 * whatever must be written to the arguments output stream.
	 * @param context the context of the request which should be processed.
	 */
	public void processRequest(RequestContext context);
}
