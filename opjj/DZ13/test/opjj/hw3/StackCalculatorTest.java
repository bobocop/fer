package opjj.hw3;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StackCalculatorTest {

    private StackCalculator calc = new StackCalculator();

    @Test
    public void pushShouldAddNumberToTheStack() {
        int n = 42;
        calc.push(n);

        assertEquals(n, calc.result());
        assertEquals(1, calc.size());
    }

    @Test(expected = IllegalStateException.class)
    public void addShouldThrowExceptionOnNoOperands() {
        calc.add();
    }

    @Test(expected = IllegalStateException.class)
    public void addShouldThrowExceptionOnOnlyOneOperand() {
        calc.push(42).add();
    }

    @Test
    public void addShouldAddTwoOperandsAndPushResultOnTheStack() {
        int a = 1;
        int b = 5;
        calc.push(a).push(b).add();

        assertEquals(b + a, calc.result());
        assertEquals(1, calc.size());
    }
    @Test
    public void minusShouldSubtractTwoOperandsAndPushResultOnTheStack() {
    	int a = 2;
    	int b = 3;
    	
    	calc.push(a).push(b).minus();
    	
    	assertEquals(b-a, calc.result());
    	assertEquals(1, calc.size());
    }
    
    @Test
    public void multiplyShouldMultiplyTwoOperandsAndPushTheResultOnTheStack() {
    	int a = 7;
    	int b = 8;
    	
    	calc.push(a).push(b).multiply();
    	
    	assertEquals(b*a, calc.result());
    	assertEquals(1, calc.size());
    }
    
    @Test
    public void divideShouldDivideTwoOperandsAndPushTheResultOnTheStack() {
    	int a = 4;
    	int b = 9;
    	
    	calc.push(a).push(b).divide();
    	
    	assertEquals(b/a, calc.result());
    	assertEquals(1, calc.size());
    }
    
    @Test(expected=ArithmeticException.class)
    public void divisionByZeroShouldThrowAnException() {
    	calc.push(0).push(512).divide();
    }

    @Test
    public void resultShouldReturn0OnEmptyStack() {
        assertEquals(0, calc.result());
    }
    
    
    @Test
    public void combindedOperationsTesting() {
    	assertEquals(calc.push(4).push(3).multiply().result(), 12);
    	assertEquals(calc.push(3).push(4).minus().result(), 1);
    	assertEquals(calc.push(3).push(4).divide().result(), 1);
    	assertEquals(calc.push(3).push(6).divide().result(), 2);
    }
}
