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
	 * @return Vraca sadrzaj Tokena
	 */
	public String asText() {
		return value;
	}

	/**
	 * @return Kao asText, samo bez navodnika.
	 */
	public String getValue() {
		return value.substring(1, value.length()-1);
	}
	
	public void accept(ITokenVisitor visitor) {
		visitor.visitTokenString(this);
	}
}
