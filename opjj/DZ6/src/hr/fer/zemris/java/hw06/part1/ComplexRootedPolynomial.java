package hr.fer.zemris.java.hw06.part1;

import java.util.Arrays;

public class ComplexRootedPolynomial {
	Complex[] roots;
	
	/**
	 * Creates a polynomial based on its roots.
	 * @param roots Complex roots of the polynomial.
	 */
	public ComplexRootedPolynomial(Complex ... roots) {
		this.roots = Arrays.copyOf(roots, roots.length);
	}
	
	/**
	 * Plugs the value of z into the polynomial function and returns the result.
	 * @param z Complex argument for the polynomial
	 * @return Complex result of the application
	 */
	public Complex apply(Complex z) {
		Complex ret = Complex.ONE;
		for(Complex r : roots) {
			ret = ret.multiply(z.sub(r));
		}
		return ret;
	}
	
	/**
	 * Returns the same polynomial but represented in terms of its
	 * coefficients rather than roots; that is returns the same polynomial
	 * as an ComplexPolynomial object.
	 * @return ComplexPolynomial of this
	 */
	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial ret = new ComplexPolynomial(
				this.roots[0].negate(), Complex.ONE);
		for(int i = 1; i < this.roots.length; i++) {
			ComplexPolynomial temp = new ComplexPolynomial(
					this.roots[i].negate(), Complex.ONE);
			ret = ret.multiply(temp);
		}
		return ret;
	}
	
	@Override
	public String toString() {
		String ret = "";
		for(Complex r : roots) {
			ret += "(z-(" + r.toString() + ")) * ";
		}
		return ret.substring(0, ret.length() - 3);
	}
	
	/**
	 * Finds the root that is closest to the specified Complex number.
	 * @param zn Complex number for which the distance from the roots is calculated
	 * @param threshold "close enough" difference when comparing the distances
	 * @return the closest root index + 1
	 */
	public int indexOfClosestRootFor(Complex zn, double threshold) {
		int min = -1;
		for(int i = 0; i < roots.length; i++) {
			if((zn.distance(roots[i])).
					compareTo(threshold) >= 0) {
				continue;
			} else {
				min = min > -1 ? min : i;	// if it's been changed, leave it
				if((zn.distance(roots[i])).
						compareTo(zn.distance(roots[min])) < 0) {
					min = i;
				}
			}
		}
		return min >= 0 ? min + 1 : min;	// ret. -1 if it hasn't changed
	}
}
