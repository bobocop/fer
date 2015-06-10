package hr.fer.zemris.java.custom.scripting.demo;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.tokens.Token;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TreeWriter {

	public static void main(String[] args) {
		Path smscrPath = null;
		if(args.length != 1) {
			System.out.println("Expected one argument: a path to the "
					+ "SmartScript file. Exiting...");
			System.exit(-1);
		} else {
			smscrPath = Paths.get(args[0]);
		}
		
		String docBody = null;
		try {
			docBody = new String(Files.readAllBytes(smscrPath));
		} catch (IOException e) {
			System.err.println("Error: cannot read SmartScript file: "
					+ smscrPath.toString());
			System.exit(-1);
		}
		
		SmartScriptParser p = new SmartScriptParser(docBody);
		WriterVisitor visitor = new WriterVisitor();
		p.getDocNode().accept(visitor);
	}
	
	public static class WriterVisitor implements INodeVisitor {

		@Override
		public void visitTextNode(TextNode node) {
			System.out.print(node.getText());
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			System.out.print("[FOR ");
			String loopExps = node.getVariable().asText() + " "
					+ node.getStartExpression().asText() + " "
					+ node.getEndExpression().asText();
			if(node.getStepExpression() != null) {
				loopExps += " " + node.getStepExpression().asText();
			}
			System.out.print(loopExps + "]");
			int childCount = node.numberOfChildren();
			for(int i = 0; i < childCount; i++) {
				node.getChild(i).accept(this);
			}
			System.out.print("[END]");
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			Token[] echoTokens = node.getTokens();
			System.out.print("[=");
			for(int i = 0; i < echoTokens.length; i++) {
				System.out.print(" " + echoTokens[i].asText());
			}
			System.out.print("]");
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			int childCount = node.numberOfChildren();
			for(int i = 0; i < childCount; i++) {
				node.getChild(i).accept(this);
			}
		}
	}
	
	
}
