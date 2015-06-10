package opjj.hw1;

import static org.junit.Assert.*;
import org.junit.Test;

public class FactorialTest {
	
	@Test
	public void factorialOfZero() {
		assertEquals("0! == 0", 0, Factorial.calc(0));
	}
	
	@Test
	public void factorialOfOne() {
		assertEquals("1! == 1", 1, Factorial.calc(1));
	}
	
	@Test
	public void factorialOfN() {
		assertEquals("5! = 120", 120,  Factorial.calc(5));
		assertEquals("7! = 5040", 5040, Factorial.calc(7));
		assertEquals("3! = 6", 6, Factorial.calc(3));
		assertEquals("10! = 3628800", 3628800, Factorial.calc(10));
	}
	
	@Test
	public void factorialRecursive() {
		assertEquals("5! = (4)! * 5", Factorial.calc(5), Factorial.calc(4) * 5);
		assertEquals("7! = (6)! * 7", Factorial.calc(7), Factorial.calc(6) * 7);
		assertEquals("3! = (2)! * 3", Factorial.calc(3), Factorial.calc(2) * 3);
		assertEquals("10! = (9)! * 10", Factorial.calc(10), Factorial.calc(9) * 10);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void exceptionOnNegativeArgument() {
		Factorial.calc(-2);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void exceptionOnTooLarge() {
		Factorial.calc(22);
	}
}
