package hr.fer.zemris.java.custom.scripting.parser;

/**
 * The parser will throw an instance of this class whenever a
 * syntax error in the script is discovered.
 */
public class SmartScriptParserException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	SmartScriptParserException() {
		super("Syntax error in document body");
	}
}
