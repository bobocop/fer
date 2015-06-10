package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * The Visitor pattern is used to traverse the tokens of a node
 * which contains them. Classes which implement this interface must
 * provide the methods for processing each type of 
 * currently defined Tokens.
 */
public interface ITokenVisitor {
	void visitTokenConstantDouble(TokenConstantDouble token);
	void visitTokenConstantInteger(TokenConstantInteger token);
	void visitTokenFunction(TokenFunction token);
	void visitTokenOperator(TokenOperator token);
	void visitTokenString(TokenString token);
	void visitTokenVariable(TokenVariable token);
}
