package hr.fer.zemris.java.tecaj_13.web;

/**
 * Contains a static method which is used to "clean" the user input.
 */
public class HTTPSupport {
	
	/**
	 * Same functionality as 'escapeForHTMLBody'.
	 */
	public static String escapeForTagAttribute(String text) {
		return escapeForHTMLBody(text);
	}
	
	/**
	 * Escapes the characters which are used in HTML body construction.
	 * @param text to be escaped
	 * @return the passed text, but with html body elements escaped
	 */
	public static String escapeForHTMLBody(String text) {
		if(text == null) {
			return "";
		} else {
			text =  text.replaceAll("&", "&amp;")
					.replaceAll("<", "&lt;")
					.replaceAll(">", "&gt;");
			return text;
		}
	}
}
