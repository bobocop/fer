package opjj.hw4;

import static org.junit.Assert.*;

import java.security.InvalidParameterException;

import org.junit.Test;

public class ReversePolishCalculatorTest {
	ReversePolishCalculator revPol = new ReversePolishCalculator();
	
	@Test
	public void plusShouldAddTheOperands() {
		assertEquals(3, revPol.calc("12+"));
	}
	
	@Test
	public void minusShouldSubtractTheOperands() {
		assertEquals(1, revPol.calc("34-"));
	}
	
	@Test
	public void multiplyShouldMultiplyTheOperands() {
		assertEquals(35, revPol.calc("57*"));
	}
	
	@Test
	public void divideShouldDivideTheOperands() {
		assertEquals(1, revPol.calc("34/"));
	}
	
	@Test
	public void multipleOp1() {
		assertEquals(0, revPol.calc("338//"));
	}
	
	@Test
	public void multipleOp2() {
		assertEquals(7, revPol.calc("89123*/+-"));
	}
	
	@Test
	public void multipleOp3() {
		assertEquals(6, revPol.calc("123++"));
	}
	
	@Test(expected=InvalidParameterException.class)
	public void exceptionOnNonEmptyStack() {
		revPol.calc("123+");
	}
}
