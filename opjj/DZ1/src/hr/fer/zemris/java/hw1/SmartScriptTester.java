package hr.fer.zemris.java.hw1;
import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.custom.scripting.tokens.*;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

public class SmartScriptTester {
	
	public static void main(String[] args) throws Exception {
		String docBody = 
		"This is sample text.\r\n[$ FOR i 1 10 1 $]\r\n This is [$= i $]-th time this message is generated.\r\n[$END$]\r\n[$FOR i 0 10 2 $]\r\n sin([$=i$]^2) = [$= i i * @sin \"0.000\" @decfmt $]\r\n[$END$]";
		SmartScriptParser parser = null;
		System.out.println("--- Input ---");
		System.out.println(docBody);
		System.out.println("--- Output ---");
		try {
			parser = new SmartScriptParser(docBody);
		} catch(SmartScriptParserException e) {
			e.printStackTrace();
			System.out.println("Unable to parse document!");
			System.exit(-1);
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("If this line ever executes, you have failed the class!");
			System.exit(-1);
		}
		DocumentNode document = parser.getDocNode();
		String originalDocumentBody = createOriginalDocumentBody(document);
		System.out.println(originalDocumentBody);
	}
	
	/**
	 * Rekurzivna funkcija koja se petljom poziva za svako djete argumenta,
	 * upisujuci pritom tekstualnu reprezentaciju njegovih tokena u jedan String
	 * koji vraca pozivajucoj funkciji. 
	 * Na kraju ispisuje sve sto je sakupljeno. Dodana je i rekonstrukcija 
	 * tagova da bi se lakse vidjela slicnost s ulazom.
	 * @param Node ciju djecu zelimo ispisati
	 * @return Tokeni tog nodea u tekstualnom obliku
	 * @throws Exception
	 */
	
	public static String createOriginalDocumentBody(Node startNode) throws Exception {
		String printChildren = "";
		for(int i = 0; i < startNode.numberOfChildren(); i++) {
			printChildren += createOriginalDocumentBody(startNode.getChild(i));
		}
		if(startNode instanceof EchoNode) {
			EchoNode node = (EchoNode) startNode;
			Token[] params = node.getTokens();
			String stringTokens = "[$= ";
			for(Token t : params) {
				stringTokens += t.asText() + " ";
			}
			return stringTokens + "$]";
		}
		else if(startNode instanceof ForLoopNode) {
			ForLoopNode node = (ForLoopNode) startNode;
			String stringTokens = "[$FOR ";
			stringTokens += node.getVariable().asText() + " ";
			stringTokens += node.getStartExpression().asText() + " ";
			stringTokens += node.getEndExpression().asText() + " ";
			if(node.getStepExpression() != null) {
				stringTokens += node.getStepExpression().asText() + " ";
			}
			return (stringTokens + "$]" + printChildren + "[$END$]");
		}
		else if(startNode instanceof TextNode) {
			TextNode node = (TextNode) startNode;
			return node.getText();
		}
		else if(startNode instanceof DocumentNode) {
			return printChildren;
		}
		else {
			throw new Exception();
		}
	}
}
