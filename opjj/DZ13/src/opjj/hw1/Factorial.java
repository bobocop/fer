package opjj.hw1;

/**
 * Implementation of the Factorial function.
 */
public class Factorial {

	/**
	 * Returns the factorial of the specified number. <code>IllegalArgumentException</code>
	 * is thrown when the argument is negative or greater than 12 (13! and larger
	 * cannot be returned as an <code>int</code>).
	 * @param n the number for which the factorial function is calculated
	 * @return int the calculated value
	 */
    public static int calc(int n) {
    	if(n < 0 || n > 12) {
    		throw new IllegalArgumentException();
    	}
    	if(n == 0) {
    		return 0;
    	} else if(n == 1) {
    		return 1;
    	} else {
    		int rez = 1;
    		for(int i = 1; i <= n; i++) {
    			rez *= i;
    		}
    		return rez;
    	}
    }
}
