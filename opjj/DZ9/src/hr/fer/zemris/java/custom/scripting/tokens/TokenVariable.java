package hr.fer.zemris.java.custom.scripting.tokens;

public class TokenVariable extends Token {
	private String name;
	
	/**
	 * Konstruktor, ime varijable mora pocinjati slovom.
	 * @param name
	 */
	public TokenVariable(String name) {
		this.name = name;
	}
	
	/**
	 * @return ime varijable
	 */
	public String asText() {
		return name;
	}
	/**
	 * @return isto sto i asText
	 */
	public String getName() {
		return name;
	}
	
	public void accept(ITokenVisitor visitor) {
		visitor.visitTokenVariable(this);
	}
}
