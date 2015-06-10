package hr.fer.zemris.java.tecaj_10.notepad;

import java.nio.file.Path;
import javax.swing.JTextArea;

/**
 * A JTextArea which is placed into JTabbedPane. Contains some additional
 * information about the currently opened document, such as its path,
 * which is used to distinguish different documents.
 */
public class TabbedTextArea extends JTextArea {
	private static final long serialVersionUID = 1L;
	private Path docPath;
	
	public TabbedTextArea(String title, Path docPath) {
		super();
		setName(title);
		this.docPath = docPath;
	}
	
	/**
	 * @return Path to the document file.
	 */	
	public Path getDocPath() {
		return docPath;
	}
	
	/**
	 * Sets the documents path.
	 * @param docPath a new Path to the document file.
	 */
	public void setDocPath(Path docPath) {
		this.docPath = docPath;
	}
}
