package hr.fer.zemris.java.custom.scripting.tokens;

public class TokenOperator extends Token {
	private String symbol;
	
	/**
	 * Konstruktor
	 * @param String koji predstavlja operator; moze biti +, -, * ili /
	 */
	public TokenOperator(String symbol) {
		this.symbol = symbol;
	}
	
	/**
	 * @return String simbol operatora u tekstualnom obliku
	 */
	public String asText() {
		return symbol;
	}
	
	/**
	 * Isto kao i asText, uvedeno zbog kompletnosti
	 */
	public String getSymbol() {
		return symbol;
	}
}
