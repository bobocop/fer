package hr.fer.zemris.java.hw06.part1;

public class Complex {
	private double a;
	private double b;
	
	public static final Complex ZERO = new Complex(0, 0);
	public static final Complex ONE = new Complex(1, 0);
	public static final Complex ONE_NEG = new Complex(-1, 0);
	public static final Complex IM = new Complex(0, 1);
	public static final Complex IM_NEG = new Complex(0, -1);
	
	/**
	 * Instead of this default constructor, one may use the static
	 * variable ZERO instead.
	 */
	public Complex() {
		this.a = 0;
		this.b = 0;
	}
	
	/**
	 * Constructs the Complex number based on its real and imaginary part.
	 * @param re the real component
	 * @param im the imaginary component
	 */
	public Complex(double re, double im) {
		this.a = re;
		this.b = im;
	}
	
	/**
	 * Returns the module of this Complex number.
	 * @return Double module
	 */
	public double module() {
		return Math.sqrt(a*a + b*b);
	}
	
	/**
	 * @param Complex c
	 * @return this * c
	 */
	public Complex multiply(Complex c) {
		return new Complex(this.a * c.a - this.b * c.b,
							this.b * c.a + this.a * c.b);
	}
	
	/**
	 * @param Complex c
	 * @return this / c
	 */
	public Complex divide(Complex c) {
		double div = c.a * c.a + c.b * c.b;
		return new Complex((this.a * c.a + this.b * c.b) / div,
							(this.b * c.a - this.a * c.b) / div);
	}
	
	/**
	 * @param Complex c
	 * @return this + c
	 */
	public Complex add(Complex c) {
		return new Complex(this.a + c.a, this.b + c.b);
	}
	
	/**
	 * @param Complex c
	 * @return this - c
	 */
	public Complex sub(Complex c) {
		return new Complex(this.a - c.a, this.b - c.b);
	}
	
	/**
	 * @return -this
	 */
	public Complex negate() {
		return new Complex(-this.a, -this.b);
	}
	
	@Override
	public String toString() {
		return Double.toString(a) + (b >= 0 ? "+" : "") + Double.toString(b) + "i";
	}
	
	// additional
	
	/**
	 * Calculates the n-th power of a Complex number.
	 * @param n the exponent
	 * @return Complex this^n
	 */
	public Complex power(int n) {
		if(n == 0) {
			return Complex.ONE;
		} else {
			Complex ret = new Complex(this.a, this.b);
			while(n > 1) {
				ret = ret.multiply(new Complex(this.a, this.b));
				n--;
			}
			return ret;
		}
	}
	
	/**
	 * Calculates the distance in a complex plane, from this to z.
	 * @param z Complex number to calculate the distance from.
	 * @return Double distance
	 */
	public Double distance(Complex z) {
		return Double.valueOf(this.sub(z).module());
	}
	
	/**
	 * Creates a Complex object from a String. Valid for the complex numbers
	 * that this parser will accept are:
	 * a + ib
	 * a - ib
	 * ib
	 * -ib
	 * b
	 * -b
	 * Where b is positive real number and a is any real number.
	 * @param c String representing a complex number
	 * @return Complex number
	 * @throws NumberFormatException
	 */
	public static Complex parseComplex(String c) throws NumberFormatException {
		String[] parts = c.split("\\s");
		if(parts.length == 1) {
			if(parts[0].startsWith("i")) {
				if(parts[0].length() == 1) {
					return new Complex(0, 1);
				}
				return new Complex(
						0, Double.parseDouble(parts[0].substring(1))
						);
			} else if(parts[0].substring(0, parts[0].length()).equals("-i")) {
				return new Complex(0, -1);
			} else {
				return new Complex(
						Double.parseDouble(parts[0]), 0
						);
			}
		} else if(parts.length == 3) {
			if(parts[2].length() == 2) {
				parts[2] = parts[2].substring(1);
			} else {
				parts[2] = "1";
			}
			if(parts[1].equals("+")) {
				return new Complex(
						Double.parseDouble(parts[0]),
						Double.parseDouble(parts[2])
						);
			} else if(parts[1].equals("-")) {
				return new Complex(
						Double.parseDouble(parts[0]),
						-Double.parseDouble(parts[2])
						);
			} else {
				throw new NumberFormatException();
			}
		} else {
			throw new NumberFormatException();
		}
	}
}