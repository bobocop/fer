package hr.fer.zemris.java.custom.scripting.tokens;

public class TokenConstantInteger extends Token {
	private int value;
	
	/**
	 * Konstruktor
	 * @param Int zadaje cijelobrojnu vrijednost konstante
	 */
	public TokenConstantInteger(int value) {
		this.value = value;
	}
	
	/**
	 * @return String vraca vrijednost konstante kao String
	 */
	public String asText() {
		return Integer.toString(value);
	}
	
	/**
	 * @return Int vraca vrijednost konstante kao int
	 */
	public int getValue() {
		return value;
	}
}
