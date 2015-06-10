package hr.fer.zemris.java.custom.scripting.nodes;
import hr.fer.zemris.java.custom.scripting.tokens.*;

public class EchoNode extends Node {
	private Token[] tokens;
	
	/**
	 * Konstruktor
	 * @param niz parametara koje je korisnik
	 * zadao unutar ECHO taga, a parser tokenizirao i spremio u niz
	 */
	public EchoNode(Token[] tokens) {
		this.tokens = tokens;
	}
	
	/**
	 * Vraca niz tokena koji prestavljaju parametre Echo cvora. Vraceni
	 * niz ne mora sadrzavati sve Tokene iste vrste.
	 * @return Token[] niz tokena
	 */
	public Token[] getTokens() {
		return tokens;
	}
}
