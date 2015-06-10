package hr.fer.zemris.java.custom.scripting.nodes;
import hr.fer.zemris.java.custom.collections.*;

public class Node {
	private ArrayBackedIndexedCollection children = null;
	
	/**
	 * Dodaje cvor zadan kao parametar u niz djece tog cvora.
	 * @param cvor dijete
	 */
	public void addChildNode(Node child) {
		if(children == null) { 
			children = new ArrayBackedIndexedCollection();
		}
		children.add(child);
	}
	
	/**
	 * Vraca broj cvorova djece.
	 * @return broj djece ili nula ukoliko je cvor list
	 */
	public int numberOfChildren() {
		if(children != null) {
			return children.size();
		}
		return 0;
	}
	
	/**
	 * Vraca dijete cvora pohranjeno u nizu indeksa zadanog parametrom.
	 * @param indeks trazenog djeteta
	 * @return cvor dijete ili null ukoliko cvor nema djece.
	 */
	public Node getChild(int index) {
		if(children != null) {
			return (Node) children.get(index);
		}
		return null;
	}
}
