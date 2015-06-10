package hr.fer.zemris.java.custom.scripting.parser;
import hr.fer.zemris.java.custom.scripting.tokens.*;
import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.custom.collections.*;
import java.text.*;
import java.util.Scanner;

public class SmartScriptParser {
	private ObjectStack nodeStack;
	private StringCharacterIterator iter;
	private DocumentNode docNode;
	
	/**
	 * Konstruktor, ulazni niz znakova (dokument) mora biti pohranjen
	 * u jednom Stringu koji se proslijeđuje kao argument, pri čemu odmah
	 * pocinje parsiranje.
	 * @param docBody argument
	 */
	public SmartScriptParser(String docBody) {
		nodeStack = new ObjectStack();
		iter = new StringCharacterIterator(docBody);
		if(docBody != null) {
			docNode = new DocumentNode();
			nodeStack.push(docNode);
			initialState();
		} else {
			throw new SmartScriptParserException();	// ulazni niz je null
		}
		/*Parsiranje...*/
		/*Ovo se izvodi na samom kraju:*/
		if(nodeStack.size() > 1) {
			throw new SmartScriptParserException();	// nezatvoreni tagovi
		}
		docNode = (DocumentNode) nodeStack.pop();	// zavrsio s parsiranjem
	}
	
	/**
	 * Vraca glavni cvor (DocumentNode) koji je korijen stabla
	 * nastalog parsiranjem.
	 * @return DocumentNode cvor
	 */
	public DocumentNode getDocNode() {
		return docNode;
	}
	
	/*
	 * Slijedece funkcije oznacavaju stanja u koja prelazi parser ovisno o
	 * znaku koji je ucitao. Predvidjeno je da u initialState osim na pocetku,
	 * ulazi i svaki puta kada zavrsi s obradom taga (odn. zavrsi tagState());
	 */
	private void initialState() {
		if(iter.getIndex() != iter.getEndIndex()) {
			if(iter.current() == '[') {
				tagState();
			}
			else {
				textState();
			}
		}
	}
	
	private void textState() {
		StringBuilder textBuild = new StringBuilder();
		for(
			char ch = iter.current(); 
			(ch != '[')
			&& (iter.getIndex() != iter.getEndIndex()); 
			ch = iter.next()
			) {
			if(ch == '\\') {
				ch = isEscChar();	// ako je, pomaknut ce se na sljedeci i upisati
			}
			textBuild.append(ch);
		}
		TextNode nodeText = new TextNode(textBuild.toString());
		Node parentNode = (Node) nodeStack.pop();
		parentNode.addChildNode(nodeText);
		nodeStack.push(parentNode);
		if(iter.current() == '[') {
			tagState();
		}	// inace zavrsi
	}
	
	private void tagState() {
		StringBuilder tagBuild = new StringBuilder();
		char ch;
		while((ch = iter.next()) != ']') {
			tagBuild.append(ch);
		}
		checkTag(tagBuild);	// provjera je li ispravno zadan
		String tagStr = tagBuild.toString().replace('$', ' ').trim();
		/*Sada je tag oblika "IMEarg1 arg2..." ili "IME arg1 arg2..."*/
		/*Slijedi prepoznavanje vrste taga*/
		if(tagStr.startsWith("FOR")) {
			processForTag(new Scanner(
										tagStr.substring(3))
										.useDelimiter("\\s+")
										);
		}
		else if(tagStr.startsWith("=")) {
			processEchoTag(tagStr.substring(1).trim());
		}
		else if(tagStr.startsWith("END")) {
			processEndTag();
		}
		else {
			throw new SmartScriptParserException();
		}
		if(iter.next() != CharacterIterator.DONE) {
			initialState();
		} //inace zavrsi s parsiranjem
	}
	
	/*
	 * Funkcije za obradu pojedine vrste taga. 
	 */
	private void processEchoTag(String echoTag) {
		ObjectStack tokenStack = new ObjectStack();
		int tokenCount = 0;
		boolean inStringToken = false;
		StringBuilder strToken = new StringBuilder();
		
		for(int i = 0; i < echoTag.length(); i++) {
			/*Razmak, osim u stringu, znaci da je gotov token*/
			if(echoTag.charAt(i) == ' ' && !inStringToken) {
				tokenStack.push(returnAsToken(strToken.toString()));
				tokenCount++;
				strToken.delete(0, strToken.length());	// resetiraj StringBuilder
				continue;
			}
			/*Sljedeci se slucajevi odnose na obradu string tokena*/
			/*Obrada escapeova unutar echo stringa*/
			if(inStringToken && echoTag.charAt(i) == '\\') {	
				char next = echoTag.charAt(++i);
				switch(next) {
					case '\\'	:	strToken.append('\\');
									break;
					case '"'	:	strToken.append('\"');
									break;
					case 'n'	:	strToken.append('\n');
									break;
					case 't'	:	strToken.append('\t');
									break;
					case 'r'	:	strToken.append('\r');
									break;
					default 	:	throw new SmartScriptParserException();
				}	// default : nepravilno escapeanje
				continue;
			}
			/*Prepoznaje pocetak, odnosno kraj string tokena*/
			if(echoTag.charAt(i) == '"' && inStringToken) {
				inStringToken = false;
			} 
			else if(echoTag.charAt(i) == '"' && !inStringToken) {
				inStringToken = true;
			}
			strToken.append(echoTag.charAt(i));
		}
		tokenStack.push(returnAsToken(strToken.toString()));
		tokenCount++;	// zadnji token (nema razmaka nakon)
		
		Token[] tokens = new Token[tokenCount];
		for(int i = tokenCount - 1; i >= 0; i--) {
			tokens[i] = (Token) tokenStack.pop();
		}
		
		EchoNode nodeEcho = new EchoNode(tokens);
		Node parentNode = (Node) nodeStack.pop();
		parentNode.addChildNode(nodeEcho);
		nodeStack.push(parentNode);
	}
	
	private void processForTag(Scanner scan) {
		String[] strTokens = new String[4];		// FOR ima max. 4 parametra
		for(int i = 0; i < 4 && scan.hasNext(); i++) {
			strTokens[i] = scan.next();
		}
		if(scan.hasNext()) {
			throw new SmartScriptParserException(); // previse parametara
		}
		
		TokenVariable var;
		Token startExp;
		Token endExp;
		Token stepExp;
		/*Ukoliko postoji stepExp argument, provjeri/postavi i njega*/
		if(strTokens[3] != null) {
			stepExp = returnAsToken(strTokens[3]);
		} else {
			stepExp = null;
		}
		/*Provjera i postavljanje ostalih*/
		if(isVar(strTokens[0])) {
			var = new TokenVariable(strTokens[0]);
		} else {
			throw new SmartScriptParserException();	// prvi param. mora biti var
		}
		startExp = returnAsToken(strTokens[1]);
		endExp = returnAsToken(strTokens[2]);
		
		ForLoopNode forNode = new ForLoopNode(var, startExp, endExp, stepExp);
		nodeStack.push(forNode);
		/*ForLoopNode ce biti dodan kao necije dijete tek nakon END taga*/
	}
	
	private void processEndTag() {
		if(nodeStack.size() < 2) {
			throw new SmartScriptParserException();	// previse END tagova
		}
		Node closingNode = (Node) nodeStack.pop();
		Node parentNode = (Node) nodeStack.pop();
		parentNode.addChildNode(closingNode);
		nodeStack.push(parentNode);
	}
	
	/**
	 * Poziva se kad naleti na backslash prilikom citanja teksta. Ukoliko 
	 * backslash sluzi kao escape, vratit ce slijedeci znak i pomaknuti 
	 * iterator. Inace ne mijenja nista.
	 * @return Znak koji treba upisati
	 */
	private char isEscChar() {
		char temp = iter.next();
		if(temp != '[' && temp != '\\') {
			return iter.previous();	// nije escapean znak, pisi ga normalno
		}
		return temp;
	}
	
	/**
	 * Provjerava ispravnost taga, odn. da li kad se izuzmu zagrade
	 * pocinje i zavrsava sa "$".
	 * @param StringBuilder koji sadrzi tek ocitanu verziju taga
	 * @throws SmartScriptParserException ako je tag neispravno zadan
	 */
	private void checkTag(StringBuilder tagBuild) {
		String tagString = tagBuild.toString();
		if(!(tagString.startsWith("$") && tagString.endsWith("$"))) {
			throw new SmartScriptParserException();
		}
		
	}
	
	/**
	 * Osim provjere ispravnosti parametara tagova, funkcija 
	 * vraca Tokene izgradjene na temelju oblika zadanog argumenta koji 
	 * je tipa String. Ukoliko je parametar pogresno zadan, baca iznimku.
	 * @param String koji predstavlja token (parametar taga)
	 * @return Token nastao iz Stringa
	 */
	private Token returnAsToken(String strToken) {
		if(isVar(strToken)) {
			return (Token) new TokenVariable(strToken);
		}
		else if(isFunc(strToken)) {
			return (Token) new TokenFunction(strToken.substring(1));
		}	// substring reze @ ispred imena prije pohrane
		else if(isConstDub(strToken)) {
			return (Token) new TokenConstantDouble(
													Double.parseDouble(
													strToken
													));
		}
		else if(isConstInt(strToken)) {
			return (Token) new TokenConstantInteger(
													Integer.parseInt(
													strToken
													));
		}
		else if(isStr(strToken)) {
			return (Token) new TokenString(strToken);
		}
		else if(isOp(strToken)) {
			return (Token) new TokenOperator(strToken);
		}
		else {
			throw new SmartScriptParserException();
		}
	}
	
	/*
	 * Pomocne funkcije za provjeravanje tipova parametara tagova (koji su
	 * u obliku stringa).
	 */
	private boolean isVar(String target) {
		return target.matches("[a-zA-Z]+[\\w]*");
	}
	
	private boolean isOp(String target) {
		return target.matches("[+-/*]");
	}
	
	private boolean isFunc(String target) {
		return target.matches("@{1}[a-zA-Z]+[\\w]*");
	}
	
	private boolean isStr(String target) {
		if(target.startsWith("\"") && target.endsWith("\"")) {
			return true;
		}
		return false;
	}
	
	private boolean isConstDub(String target) {
		return target.matches("[+-]*\\d+\\.\\d+");
	}
	
	private boolean isConstInt(String target) {
		return target.matches("[+-]*\\d+");
	}
	
}
	
