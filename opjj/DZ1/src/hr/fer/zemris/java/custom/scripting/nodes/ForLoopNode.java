package hr.fer.zemris.java.custom.scripting.nodes;
import hr.fer.zemris.java.custom.scripting.tokens.*;

public class ForLoopNode extends Node {
	private TokenVariable variable;
	private Token startExpression;
	private Token endExpression;
	private Token stepExpression;
	
	public ForLoopNode(
						TokenVariable var,
						Token startExp,
						Token endExp,
						Token stepExp
						) {
		variable = var;
		startExpression = startExp;
		endExpression = endExp;
		stepExpression = stepExp;
	}
	
	/**
	 * Vraca varijablu (1. parametar) FOR taga
	 * @return TokenVariable koji oznacava varijablu u FOR petlji
	 */
	public TokenVariable getVariable() {
		return variable;
	}
	
	/**
	 * Vraca startExpression (2. parametar) FOR taga
	 * @return Token moze biti bilo koja vrsta Tokena
	 */
	public Token getStartExpression() {
		return startExpression;
	}
	
	/**
	 * Vraca endExpression (3. parametar) FOR taga
	 * @return Token moze biti bilo koja vrsta tokena
	 */
	public Token getEndExpression() {
		return endExpression;
	}
	
	/**
	 * stepExpression (4. parametar) ne mora biti zadan u FOR tagu i u
	 * tom ce slucaju ova funkcija vratiti null
	 * @return Token moze biti bilo koja vrsta Tokena
	 */
	public Token getStepExpression() {
		return stepExpression;
	}
}
