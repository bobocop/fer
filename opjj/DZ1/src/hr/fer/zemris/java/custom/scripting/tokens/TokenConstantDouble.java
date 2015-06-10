package hr.fer.zemris.java.custom.scripting.tokens;

public class TokenConstantDouble extends Token {
	private double value;
	
	/**
	 * Konstruktor
	 * @param Double vrijednost realne konstante
	 */
	public TokenConstantDouble(double value) {
		this.value = value;
	}
	
	/**
	 * @return Vraca vrijednost konstante kao String
	 */
	public String asText() {
		return Double.toString(value);
	}
	
	/**
	 * @return Vraca vrijednost konstante kao double.
	 */
	public double getValue() {
		return value;
	}
}
