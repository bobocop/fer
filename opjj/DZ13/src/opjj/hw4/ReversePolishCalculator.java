package opjj.hw4;

import java.security.InvalidParameterException;
import java.util.ArrayDeque;
import java.util.Deque;

import opjj.hw3.StackCalculator;

/**
 * The reverse Polish calculator. It is used to calculate simple arithmetic
 * expressions.
 */
public class ReversePolishCalculator {
	private Deque<Character> opQ = new ArrayDeque<>();
	private StackCalculator calc = new StackCalculator();
	
	/**
	 * Calculates the expression. The arithmetic expression may only contain
	 * numbers and operands with no spaces. Every digit is treated as a separate number.
	 * @param input
	 * @return
	 */
    public int calc(String input) {
        for(int i = 0; i < input.length(); i++) {
        	char ch = input.charAt(i);
        	if(Character.isDigit(ch)) {
        		calc.push(Integer.parseInt(String.valueOf(ch)));
        	} else if(ch == '+' || ch == '-' || ch == '*' || ch == '/') {
        		opQ.add(Character.valueOf(ch));
        	} else {
        		throw new InvalidParameterException();
        	}
        }
        
        if(opQ.isEmpty() || calc.size() == 0 || calc.size() != opQ.size()+1) {
        	throw new InvalidParameterException();
        }
        
        while(!opQ.isEmpty()) {
        	Character op = opQ.remove();
        	switch(op.charValue()) {
        	case '+':
        		calc.add();
        		break;
        	case '-':
        		calc.minus();
        		break;
        	case '*':
        		calc.multiply();
        		break;
        	case '/':
        		calc.divide();
        		break;
        	default:
        		// shouldn't ever get here
        	}
        }
        return calc.result();
    }
}
