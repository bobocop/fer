package hr.fer.zemris.java.custom.scripting.nodes;

public class TextNode extends Node {
	private String text;
	
	/**
	 * Konstruktor
	 * @param String tekst koji sadrzi tekstualni
	 */
	public TextNode(String text) {
		this.text = text;
	}
	
	/**
	 * @return String vraca se tekstualni sadrzaj cvora
	 */
	public String getText() {
		return text;
	}
	
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitTextNode(this);
	}
}
