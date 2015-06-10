package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.text.DecimalFormat;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.tokens.ITokenVisitor;
import hr.fer.zemris.java.custom.scripting.tokens.Token;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantDouble;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantInteger;
import hr.fer.zemris.java.custom.scripting.tokens.TokenFunction;
import hr.fer.zemris.java.custom.scripting.tokens.TokenOperator;
import hr.fer.zemris.java.custom.scripting.tokens.TokenString;
import hr.fer.zemris.java.custom.scripting.tokens.TokenVariable;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Instances of this class will execute a parsed SmartScript code and
 * write the script's output to the provided {@link RequestContext}'s
 * output stream. 
 * It uses 1 as a default incremental step in FOR loops when it is
 * not user-specified.<br>
 * Important: this interpreter has one reserved variable name, "_smscrtemp".
 * Using this name in your scripts will lead to unwanted behavior.
 */
public class SmartScriptEngine {
	private final String TEMP_STACK = "_smscrtemp";
	private final int DEFAULT_STEP = 1;
	private DocumentNode documentNode;
	private RequestContext requestContext;
	private ObjectMultistack multistack = new ObjectMultistack();
	private INodeVisitor visitor = new INodeVisitor() {
		
		@Override
		public void visitTextNode(TextNode node) {
			try {
				requestContext.write(node.getText());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public void visitForLoopNode(ForLoopNode node) {
			EngineTokenVisitor visitor = new EngineTokenVisitor();
			String varName = node.getVariable().getName();
			node.getStartExpression().accept(visitor);
			node.getEndExpression().accept(visitor);
			ValueWrapper endExp = multistack.pop(TEMP_STACK);	// pushed by visitor
			ValueWrapper startExp = multistack.pop(TEMP_STACK);	// pushed by visitor
			ValueWrapper stepExp = null;
			if(node.getStepExpression() != null) {
				node.getStepExpression().accept(visitor);
				stepExp = multistack.pop(TEMP_STACK);	// pushed by visitor
			} else {
				stepExp = new ValueWrapper(DEFAULT_STEP);
			}
			multistack.push(varName, startExp);
			int childCount = node.numberOfChildren();
			while(startExp.numCompare(endExp.getValue()) <= 0) {
				for(int i = 0; i < childCount; i++) {
					node.getChild(i).accept(this);
				}
				startExp = multistack.pop(varName);
				startExp.increment(stepExp.getValue());
				multistack.push(varName, startExp);
			}
			multistack.pop(varName);
		}
		
		@Override
		public void visitEchoNode(EchoNode node) {
			Token[] echoTokens = node.getTokens();
			EngineTokenVisitor visitor = new EngineTokenVisitor();
			for(int i = 0; i < echoTokens.length; i++) {
				echoTokens[i].accept(visitor);
			}
			while(!multistack.isEmpty(TEMP_STACK)) {
				dumpStack();
			}
		}
		
		@Override
		public void visitDocumentNode(DocumentNode node) {
			int childCount = node.numberOfChildren();
			for(int i = 0; i < childCount; i++) {
				node.getChild(i).accept(this);
			}
		}
	};
	
	/**
	 * @param documentNode the Document Node of a parsed Smart script
	 * @param requestContext the request context to write the results to
	 */
	public SmartScriptEngine(DocumentNode documentNode, 
			RequestContext requestContext) {
		this.documentNode = documentNode;
		this.requestContext = requestContext;
	}
	
	/**
	 * Executes the parsed smart script.
	 */
	public void execute() {
		documentNode.accept(visitor);
	}
	
	/**
	 * Dumps the stack content to the output stream, in FIFO order.
	 */
	private void dumpStack() {
		String s = multistack.pop(TEMP_STACK).getValue().toString();
		if(!multistack.isEmpty(TEMP_STACK)) {
			dumpStack();
		}
		try {
			requestContext.write(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * A token visitor implementation. It requires {@link ObjectMultistack},
	 * {@link RequestContext} and {@link ValueWrapper} for its operation.
	 * @see ITokenVisitor
	 */
	private class EngineTokenVisitor implements ITokenVisitor {
		
		@Override
		public void visitTokenConstantDouble(TokenConstantDouble token) {
			multistack.push(TEMP_STACK, new ValueWrapper(token.getValue()));
		}

		@Override
		public void visitTokenConstantInteger(TokenConstantInteger token) {
			multistack.push(TEMP_STACK, new ValueWrapper(token.getValue()));
		}

		@Override
		public void visitTokenFunction(TokenFunction token) {
			switch(token.getName()) {
			case "sin":
				sin();
				break;
			case "decfmt":
				decfmt();
				break;
			case "dup":
				dup();
				break;
			case "setMimeType":
				setMime();
				break;
			case "paramGet":
				paramGet();
				break;
			case "pparamGet":
				pparamGet();
				break;
			case "pparamSet":
				pparamSet();
				break;
			default:
				break;	//
			}
		}

		@Override
		public void visitTokenOperator(TokenOperator token) {
			/*
			 * The copy is needed to protect the variable from being changes by
			 * the ValueWrapper arithmetic operations.
			 */
			ValueWrapper copyOfArg1 = 
					new ValueWrapper(multistack.pop(TEMP_STACK).getValue());
			ValueWrapper arg2 = multistack.pop(TEMP_STACK);
			switch(token.getSymbol()) {
			case "+":
				copyOfArg1.increment(arg2.getValue());
				break;
			case "-":
				copyOfArg1.decrement(arg2.getValue());
				break;
			case "*":
				copyOfArg1.multiply(arg2.getValue());
				break;
			case "/":
				copyOfArg1.divide(arg2.getValue());
				break;
			default:
				break;	//
			}
			multistack.push(TEMP_STACK, copyOfArg1);
		}

		@Override
		public void visitTokenString(TokenString token) {
			multistack.push(TEMP_STACK, new ValueWrapper(token.getValue()));
		}

		@Override
		public void visitTokenVariable(TokenVariable token) {
			multistack.push(TEMP_STACK, multistack.peek(token.getName()));
		}
		
		/**
		 * Calculates the sine of its argument.
		 */
		private void sin() {
			Object arg = multistack.pop(TEMP_STACK).getValue();
			if(arg instanceof Integer) {
				arg = ((Integer) arg).doubleValue();
			}
			multistack.push(TEMP_STACK, new ValueWrapper(Math.sin(
					Math.toRadians((Double) arg))));
		}
		
		/**
		 * Duplicates the current argument on the top of the stack.
		 */
		private void dup() {
			ValueWrapper val = multistack.pop(TEMP_STACK);
			multistack.push(TEMP_STACK, val);
			multistack.push(TEMP_STACK, val);
		}
		
		/**
		 * Sets the mimeType.
		 */
		private void setMime() {
			ValueWrapper val = multistack.pop(TEMP_STACK);
			requestContext.setMimeType((String) val.getValue());
		}
		
		/**
		 * Gets the parameter from its containing class' request context.
		 */
		private void paramGet() {
			String defValue = multistack.pop(TEMP_STACK).getValue().toString();
			String name = multistack.pop(TEMP_STACK).getValue().toString();
			String value = requestContext.getParameter(name);
			multistack.push(TEMP_STACK, value == null ? 
										new ValueWrapper(defValue) : 
										new ValueWrapper(value));
		}
		
		/**
		 * Gets the persistent parameter from its containing 
		 * class' request context.
		 */
		private void pparamGet() {
			String defValue = multistack.pop(TEMP_STACK).getValue().toString();
			String name = multistack.pop(TEMP_STACK).getValue().toString();
			String value = requestContext.getPersistentParameter(name);
			multistack.push(TEMP_STACK, value == null ? 
										new ValueWrapper(defValue) : 
										new ValueWrapper(value));
		}
		
		/**
		 * Sets the persistent parameter of its containing 
		 * class' request context.
		 */
		private void pparamSet() {
			String name = multistack.pop(TEMP_STACK).getValue().toString();
			String value = multistack.pop(TEMP_STACK).getValue().toString();
			requestContext.setPersistentParameter(name, value);
		}
		
		/**
		 * Formats the number according to the specified format.
		 */
		private void decfmt() {
			String pattern = (String) multistack.pop(TEMP_STACK).getValue();
			Object arg = multistack.pop(TEMP_STACK).getValue();
			Double realArg;
			if(arg instanceof String) {
				realArg = Double.parseDouble((String) arg);
			} else {
				realArg = (double) arg;
			}
			String result = new DecimalFormat(pattern).format(realArg);
			multistack.push(TEMP_STACK, new ValueWrapper(result));
		}
	}
}
