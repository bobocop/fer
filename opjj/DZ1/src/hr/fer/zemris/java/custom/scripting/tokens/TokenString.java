package hr.fer.zemris.java.custom.scripting.tokens;

public class TokenString extends Token {
	private String value;
	
	/**
	 * Konstruktor
	 * @param vrijednost, odnosno tekst koji sadrzi Token
	 */
	public TokenString(String value) {
		this.value = value;
	}
	
	/**
	 * @return String vraca sadrzaj Tokena
	 */
	public String asText() {
		return value;
	}

	/**
	 * Isto kao i asText
	 */
	public String getValue() {
		return value;
	}
}
