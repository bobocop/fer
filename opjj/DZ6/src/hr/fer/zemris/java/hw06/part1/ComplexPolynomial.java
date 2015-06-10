package hr.fer.zemris.java.hw06.part1;

import java.util.Arrays;

public class ComplexPolynomial {
	private Complex[] coefs;
	
	/**
	 * Constructs the complex polynomial based on its coefficients.
	 * Important: the coefficients should be enetered in ascending oreder.
	 */
	public ComplexPolynomial(Complex ... factors) {
		this.coefs = Arrays.copyOf(factors, factors.length);
	}
	
	/**
	 * Returns the order of the polynomial.
	 * @return short order
	 */
	public short order() {
		return (short) (coefs.length - 1);
	}
	
	/**
	 * Creates a new polynomial which is a product of this and the parameter.
	 * @param ComplexPolynomial to multiply this with
	 * @return ComplexPolynomial multiplication result
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		Complex[] newCoefs = new Complex[this.coefs.length+p.coefs.length-1];
		Arrays.fill(newCoefs, Complex.ZERO);
		for(int i = 0; i < this.coefs.length; i++) {
			for(int j = 0; j < p.coefs.length; j++) {
				newCoefs[i+j] = newCoefs[i+j].add(
						this.coefs[i].multiply(p.coefs[j]));
			}
		}
		return new ComplexPolynomial(newCoefs);
	}
	
	/**
	 * @return ComplexPolynomial the first derivation of this
	 */
	public ComplexPolynomial derive() {
		if(this.coefs.length <= 1) { 
			return new ComplexPolynomial(Complex.ZERO);
		}
		Complex[] newCoefs = new Complex[this.coefs.length - 1];
		newCoefs[0] = coefs[1];
		for(int i = 2; i < this.coefs.length; i++) {
			newCoefs[i-1] = this.coefs[i].multiply(new Complex(i, 0));
		}
		return new ComplexPolynomial(newCoefs);
	}
	
	/**
	 * Plugs the value of z into the polynomial function and returns the result.
	 * @param Complex argument for the polynomial
	 * @return Complex result of the application
	 */
	public Complex apply(Complex z) {
		Complex ret = Complex.ZERO;
		for(int i = 0; i < this.coefs.length; i++) {
			ret = ret.add(this.coefs[i].multiply(z.power(i)));
		}
		return ret;
	}
	
	@Override
	public String toString() {
		String ret = "";
		for(int i = coefs.length - 1; i >= 1; i--) {
			if(coefs[i] != null && !coefs[i].equals(Complex.ZERO)) {
				ret += "(" + coefs[i].toString() + ")" + "z^" + i + " + ";
			}
		}
		if(coefs[0] != null) {
			ret += "(" + coefs[0].toString() + ")";
		}
		return ret;
	}
	
}
