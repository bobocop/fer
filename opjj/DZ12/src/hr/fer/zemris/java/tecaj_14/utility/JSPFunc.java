package hr.fer.zemris.java.tecaj_14.utility;

import hr.fer.zemris.java.tecaj_14.model.BlogComment;
import hr.fer.zemris.java.tecaj_14.model.BlogEntry;

/**
 * Provides some functions which generate HTML code.
 * @author karlo
 *
 */
public class JSPFunc {
	
	/**
	 * Formats data from the passed BlogEntry into HTML code that
	 * can be displayed for users to read.
	 * @param entry The entry to be formatted.
	 * @return String containing the HTML code.
	 */
	public static String formatEntryAsHTML(BlogEntry entry) {
		String entryContent = "<h3>" + entry.getTitle() + "</h3>"
  			+ "<p>" + HTTPSupport.escapeForHTMLBody(entry.getText()) + "</p>"
  			+ "<p><i>~Created on " + entry.getCreatedOn() + "</i></p>";
		if(entry.getLastModifiedOn() != null) {
			entryContent += "<i>~Last modified on " + entry.getLastModifiedOn()
							+ "</i><br>";
		}
		return entryContent;
	}
	
	/**
	 * Formats the comments associated with the provided BlogEntry
	 * into HTML code that can be displayed for users to read.
	 * @param entry The entry which comments are to be displayed.
	 * @return String containing the HTML code.
	 */
	public static String formatEntryCommentsAsHTML(BlogEntry entry) {
		String comments = "<p>Comments (" +  entry.getComments().size() +"):</p>";
		for(BlogComment comment : entry.getComments()) {
			String commentContent = "<p>" 
					+ HTTPSupport.escapeForHTMLBody(comment.getMessage())
					+ "<i> -- posted on </i>" + comment.getPostedOn()
					+ "<i> by </i>" 
					+ HTTPSupport.escapeForHTMLBody(comment.getUsersEMail())
					+ "</p>";
			comments += commentContent;
		}
		if(entry.getComments().isEmpty()) {
			return "Nobody has commented on this entry yet!<br>";
		}
		return comments;
	}
}
