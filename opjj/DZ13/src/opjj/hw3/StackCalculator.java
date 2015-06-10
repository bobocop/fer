package opjj.hw3;

import java.util.Stack;

/**
 * This simple calculator will evaluate some arithmetic expressions. It pops
 * the arguments from its internal stack, calculates and pushes the result on
 * the stack.
 */
public final class StackCalculator {

    private final Stack<Integer> stack = new Stack<Integer>();

    /**
     * Push an integer on the calculator's stack.
     * @param n int to be pushed.
     * @return <code>this</code>
     */
    public StackCalculator push(int n) {
        stack.push(n);
        return this;
    }

    /**
     * Adds the two numbers found on the stack. If less than two numbers can
     * be popped, it will throw an exception.
     * @return <code>this</code>
     */
    public StackCalculator add() {
        if (size() < 2) {
            throw new IllegalStateException(
                    "Not enough operands; expected 2, got " + size());
        }

        Integer a = stack.pop();
        Integer b = stack.pop();
        stack.push(a + b);

        return this;
    }
    
    /**
     * Subtracts the two numbers found on the stack. The second number popped is
     * substracted from the first, the result calculated and stored back on the stack.
     * If less than two numbers can be popped, it will throw an exception.
     * @return <code>this</code>
     */
    public StackCalculator minus() {
    	if(size() < 2) {
    		throw new IllegalStateException(
                    "Not enough operands; expected 2, got " + size());
    	}
    	
    	Integer a = stack.pop();
    	Integer b = stack.pop();
    	stack.push(a-b);
    	
    	return this;
    }
    
    /**
     * Multiplies the two numbers found on the stack. 
     * The result is stored back on the stack. If less than two numbers can
     * be popped, it will throw an exception.
     * @return <code>this</code>
     */
    public StackCalculator multiply() {
    	if(size() < 2) {
    		throw new IllegalStateException(
                    "Not enough operands; expected 2, got " + size());
    	}
    	
    	Integer a = stack.pop();
    	Integer b = stack.pop();
    	stack.push(a*b);
    	
    	return this;
    }
    
    /**
     * Divides the two numbers found on the stack. The first number popped is
     * divided by the second, the result calculated and stored back on the stack. 
     * If less than two numbers can be popped, it will throw an exception.
     * @return <code>this</code>
     */
    public StackCalculator divide() {
    	if(size() < 2) {
    		throw new IllegalStateException(
                    "Not enough operands; expected 2, got " + size());
    	}
    	
    	Integer a = stack.pop();
    	Integer b = stack.pop();
    	if(b.compareTo(Integer.valueOf(0)) == 0) {
    		throw new ArithmeticException("Division by zero");
    	} else {
    		stack.push(a/b);
    	}
    	
    	return this;
    }

    /**
     * @return Result from the last arithmetic operation performed.
     */
    public int result() {
        return size() > 0 ? stack.peek() : 0;
    }

    /**
     * @return The size of calculator's internal stack.
     */
    public int size() {
        return stack.size();
    }

}
