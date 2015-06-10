package hr.fer.zemris.java.tecaj_10.notepad;

import hr.fer.zemris.java.tecaj_10.local.FormLocalizationProvider;
import hr.fer.zemris.java.tecaj_10.local.ILocalizationProvider;
import hr.fer.zemris.java.tecaj_10.local.LocalizationProvider;
import hr.fer.zemris.java.tecaj_10.local.swing.LJMenu;
import hr.fer.zemris.java.tecaj_10.local.swing.LJToolBar;
import hr.fer.zemris.java.tecaj_10.local.swing.LocalizableAction;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;

public class JNotepad extends JFrame {
	/*
	 * newDocCount - the number of documents created during one session,
	 * actually used to name the empty documents
	 */
	private static final long serialVersionUID = 1L;
	private JTabbedPane tabPane;
	private Path openedFilePath;
	private int newDocCount;
	private FormLocalizationProvider locProvider = new FormLocalizationProvider(
			LocalizationProvider.getInstance(), this);
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new JNotepad().setVisible(true);
			}
			
		});
	}
	
	public JNotepad() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocation(0, 0);
		setSize(600, 600);
		setTitle("JNotepad");
		this.newDocCount = 1;
		initGUI();
	}

	private void initGUI() {
		getContentPane().setLayout(new BorderLayout());
		this.tabPane = new JTabbedPane();
		getContentPane().add(new JScrollPane(tabPane));
		createActions();
		createMenus();
		createToolBar();
	}

	private void createActions() {
		newFileAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
		newFileAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control N")
				);
		
		openFileAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
		openFileAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control O")
				);
		
		saveFileAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		saveFileAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control S")
				);
		
		saveAsFileAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
		
		closeFileAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);
		closeFileAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control W")
				);
		
		exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
		
		deleteSelectedTextPartAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
		deleteSelectedTextPartAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control F2")
				);
		
		toggleCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_G);
		toggleCaseAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control F3")
				);
		
		sortAscAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		sortAscAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control A")
				);
		
		sortDescAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
		sortDescAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control D")
				);
		
		removeEmptyAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
		removeEmptyAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control E")
				);
		
		upperCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);
		upperCaseAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control U")
				);
		
		lowerCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);
		lowerCaseAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control L")
				);
		
		wordCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_W);
		wordCaseAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control W")
				);
		
		cutAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);
		cutAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control X"));
		
		copyAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
		copyAction.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control C"));
		
		pasteAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P);
		pasteAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control V"));
		
		switchToCroatian.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_H);
		switchToEnglish.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
		
		statisticsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);
		statisticsAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control F4"));
	}

	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();
		
		LJMenu fileMenu = new LJMenu(locProvider, "File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);
		fileMenu.add(new JMenuItem(newFileAction));
		fileMenu.add(new JMenuItem(openFileAction));
		fileMenu.add(new JMenuItem(saveFileAction));
		fileMenu.add(new JMenuItem(saveAsFileAction));
		fileMenu.add(new JMenuItem(closeFileAction));
		fileMenu.add(new JMenuItem(exitAction));
		
		LJMenu editMenu = new LJMenu(locProvider, "EditKey");
		editMenu.setMnemonic(KeyEvent.VK_E);
		menuBar.add(editMenu);
        editMenu.add(new JMenuItem(cutAction));
        editMenu.add(new JMenuItem(copyAction));
        editMenu.add(new JMenuItem(pasteAction));
		
		LJMenu editChangeMenu = new LJMenu(locProvider, "EditChange");
		editChangeMenu.setMnemonic(KeyEvent.VK_G);
		editChangeMenu.add(new JMenuItem(upperCaseAction));
		editChangeMenu.add(new JMenuItem(lowerCaseAction));
		editChangeMenu.add(new JMenuItem(toggleCaseAction));
		editChangeMenu.add(new JMenuItem(wordCaseAction));
		editMenu.add(editChangeMenu);
		
		LJMenu editAdvancedMenu = new LJMenu(locProvider, "AdvancedChange");
		editAdvancedMenu.setMnemonic(KeyEvent.VK_A);
		editAdvancedMenu.add(new JMenuItem(sortAscAction));
		editAdvancedMenu.add(new JMenuItem(sortDescAction));
		editAdvancedMenu.add(new JMenuItem(removeEmptyAction));
		editMenu.add(editAdvancedMenu);
		
		LJMenu langMenu = new LJMenu(locProvider, "LanguageKey");
		langMenu.setMnemonic(KeyEvent.VK_L);
		menuBar.add(langMenu);
		langMenu.add(new JMenuItem(switchToCroatian));
		langMenu.add(new JMenuItem(switchToEnglish));
		
		LJMenu info = new LJMenu(locProvider, "Info");
		info.setMnemonic(KeyEvent.VK_I);
		menuBar.add(info);
		info.add(new JMenuItem(statisticsAction));
		
		this.setJMenuBar(menuBar);	
	}

	private void createToolBar() {
		LJToolBar toolbar = new LJToolBar(locProvider, "ToolsTitle");
		toolbar.add(new JButton(openFileAction));
		toolbar.add(new JButton(saveAsFileAction));
		toolbar.addSeparator();
		toolbar.add(new JButton(lowerCaseAction));
		toolbar.add(new JButton(sortAscAction));
		toolbar.addSeparator();
		toolbar.add(new JButton(statisticsAction));
		this.getContentPane().add(toolbar, BorderLayout.PAGE_START);
	}
	
	/**
	 * New files' paths are set to null initially. That way they can
	 * be distinguished from files that have already been saved once.
	 */
	private Action newFileAction = new LocalizableAction(locProvider, "NewFile") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			TabbedTextArea editor = new TabbedTextArea(
					locProvider.getString("NewDoc"), null);
			tabPane.addTab(
					editor.getName() + newDocCount++, editor);
			tabPane.setSelectedIndex(tabPane.getComponentCount()-1);
		}
	};
	
	/**
	 * Documents are always opened in a new tab!
	 */
	private Action openFileAction = new LocalizableAction(locProvider, "OpenKey") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc = new JFileChooser();
			jfc.setDialogTitle(locProvider.getString("OpenKey"));
			if(jfc.showOpenDialog(JNotepad.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			File fileName = jfc.getSelectedFile();
			Path filePath = fileName.toPath();
			if(!Files.isReadable(filePath)) {
				JOptionPane.showMessageDialog(JNotepad.this,
						locProvider.getString("NoFile") 
						+ " " + fileName.getAbsolutePath(),
						locProvider.getString("ErrTitle"),
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			byte[] sadrzaj = null;
			try {
				sadrzaj = Files.readAllBytes(filePath);
			} catch(Exception ex) {
				JOptionPane.showMessageDialog(JNotepad.this,
						locProvider.getString("CantReadMsg") 
						+ " " + fileName.getAbsolutePath(),
						locProvider.getString("ErrTitle"),
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			openedFilePath = filePath;
			int tabIndex = isOpenedFile(openedFilePath);
			if(tabIndex != -1) {
				// already opened, switch to that tab
				tabPane.setSelectedIndex(tabIndex);
				return;
			}
			
			String text = new String(sadrzaj, StandardCharsets.UTF_8);
			String docName = openedFilePath.getFileName().toString();
			TabbedTextArea editor = new TabbedTextArea(
					docName, openedFilePath);
			editor.setText(text);
			tabPane.addTab(docName, editor);
			// switch to the newly opened tab
			tabPane.setSelectedIndex(tabPane.getComponentCount()-1);
		}
		
		/**
		 * Check wheter the file is already opened in some tab.
		 * @param filePath file to be checked
		 * @return int index of tab the file is currently
		 * opened within or -1 if the file is not opened
		 */
		private int isOpenedFile(Path filePath) {
			for(int i = 0; i < tabPane.getComponentCount(); i++) {
				TabbedTextArea tabTextArea = (TabbedTextArea) tabPane.getComponent(i);
				if(filePath.equals(tabTextArea.getDocPath())) {
					return i;
				}
			}
			return -1;
		}
	};
	
	/**
	 * Saves the file as...
	 */
	private Action saveAsFileAction = new LocalizableAction(locProvider, "SaveAs") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if(tabPane.getSelectedComponent() == null) {
				return;
			}
			JFileChooser jfc = new JFileChooser();
			jfc.setDialogTitle(locProvider.getString("SaveAs"));
			if(jfc.showSaveDialog(JNotepad.this) != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(JNotepad.this,
						locProvider.getString("SaveCancelMsg"),
						locProvider.getString("WarnTitle"),
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			openedFilePath = jfc.getSelectedFile().toPath();
			byte[] sadrzaj = ((TabbedTextArea) tabPane.getSelectedComponent())
					.getText().getBytes(StandardCharsets.UTF_8);
			try {
				Files.write(openedFilePath, sadrzaj);
				((TabbedTextArea) tabPane.getSelectedComponent()).setDocPath(openedFilePath);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(JNotepad.this,
						locProvider.getString("CantSaveMsg"),
						locProvider.getString("ErrTitle"),
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			JOptionPane.showMessageDialog(JNotepad.this,
					locProvider.getString("SavedMsg"),
					locProvider.getString("InfoTitle"),
					JOptionPane.INFORMATION_MESSAGE);
			String savedAs = openedFilePath.getFileName().toString();
			// (possibly) change the tab and text area names
			((TabbedTextArea) tabPane.getSelectedComponent()).setName(savedAs);
			tabPane.setTitleAt(tabPane.getSelectedIndex(), savedAs);
			return;
		}
	};
	
	/**
	 * Saves the file. If the file was created but not 'saved as', it will
	 * save it to the working directory.
	 */
	private Action saveFileAction = new LocalizableAction(locProvider, "SaveKey") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			
			TabbedTextArea tabTextArea = (TabbedTextArea) tabPane.getSelectedComponent();
			if(tabTextArea == null) {
				return;
			}
			try {
				if(tabTextArea.getDocPath() == null) {
					// save to default path (working_dir/name)
					openedFilePath = Paths.get(
							System.getProperty("user.dir"), 
							tabTextArea.getName()+(newDocCount-1)
							);
					tabTextArea.setDocPath(openedFilePath);
					Files.createFile(openedFilePath);
					Files.write(openedFilePath, tabTextArea.getText().getBytes());
					JOptionPane.showMessageDialog(JNotepad.this,
							locProvider.getString("SavedWorkDirMsg"),
							locProvider.getString("InfoTitle"),
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					Files.write(tabTextArea.getDocPath(), tabTextArea.getText().getBytes());
				}
			} catch (IOException e1) {
				tabTextArea.setDocPath(null);
				JOptionPane.showMessageDialog(JNotepad.this,
						locProvider.getString("CantSaveMsg")
						+ " " + tabTextArea.getDocPath().toString(),
						locProvider.getString("ErrTitle"),
						JOptionPane.ERROR_MESSAGE);
			}
		}
	};
	
	/**
	 * Deletes the selected text.
	 */
	private Action deleteSelectedTextPartAction = new LocalizableAction(locProvider, "RemSelPrt") {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			TabbedTextArea editor = (TabbedTextArea) tabPane.getSelectedComponent();
			if(editor == null) {
				return;
			}
			Document doc = editor.getDocument();
			int len = Math.abs(editor.getCaret().getMark() - editor.getCaret().getDot());
			int offs = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
			if(len == 0) {
				return;
			} else {
				try {
					doc.remove(offs, len);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
					return;
				}
			}
			editor.getCaret().setDot(offs);
		}
	};
	
	/**
	 * Toggles the case of letters in selected text.
	 * @see JNotepad.SelectedTextAction
	 */
	private Action toggleCaseAction = new SelectedTextAction(locProvider, "TglCase") {
		private static final long serialVersionUID = 1L;

		@Override
		protected String alterText(String text) {
			char[] data = text.toCharArray();
			for(int i = 0; i < data.length; i++) {
				char c = data[i];
				if(Character.isUpperCase(c)) {
					data[i] = Character.toLowerCase(c);
				} else if(Character.isLowerCase(c)) {
					data[i] = Character.toUpperCase(c);
				}
			}
			return new String(data);
		}
		
	};
		
	/**
	 * Closes the currently active document.
	 */
	private Action closeFileAction = new LocalizableAction(locProvider, "CloseFile") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			tabPane.remove(tabPane.getSelectedComponent());
		}
	};
	
	/**
	 * Exits the program. Unsaved changes are lost.
	 */
	private Action exitAction = new LocalizableAction(locProvider, "Exit") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	};
	
	/**
	 * Switches the UI language to Croatian.
	 */
	private Action switchToCroatian = new LocalizableAction(locProvider, "Cro") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("hr");
			langSwitch();
		}
	};
	
	/**
	 * Switches the UI lanugage to English.
	 */
	private Action switchToEnglish = new LocalizableAction(locProvider, "Eng") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("en");
			langSwitch();
		}
	};
	
	/**
	 * Makes all the words uppercased.
	 * @see JNotepad.SelectedTextAction
	 */
	private Action upperCaseAction = new SelectedTextAction(locProvider, "ToUpper") {
		private static final long serialVersionUID = 1L;

		@Override
		protected String alterText(String text) {
			return text.toUpperCase();
		}
	};
	
	/**
	 * Makes all the words lowercased.
	 * @see JNotepad.SelectedTextAction
	 */
	private Action lowerCaseAction = new SelectedTextAction(locProvider, "ToLower") {
		private static final long serialVersionUID = 1L;

		@Override
		protected String alterText(String text) {
			return text.toLowerCase();
		}
	};
		
	/**
	 * Makes the first letter of each word uppercase, the rest are lowercased.
	 * @see JNotepad.SelectedTextAction
	 */
	private Action wordCaseAction = new SelectedTextAction(locProvider, "ToWordCase") {
		private static final long serialVersionUID = 1L;

		@Override
		protected String alterText(String text) {
			char[] letters = text.toCharArray();
			boolean inWord = false;
			for(int i = 0; i < letters.length; i++) {
				if(!inWord) {
					if(i == 0 || (Character.isWhitespace(letters[i-1]) 
						&& Character.isAlphabetic(letters[i]))) {
					letters[i] = Character.toUpperCase(letters[i]);
					inWord = true;
					}
				} else {
					if(Character.isWhitespace(letters[i])) {
						inWord = false;
					} else {
						letters[i] = Character.toLowerCase(letters[i]);
					}
				}
			}
			return new String(letters);
		}
	};
	
	/**
	 * Sorts the lines in ascending order.
	 * @see JNotepad.SelectedTextAction
	 */
	private Action sortAscAction = new SelectedTextAction(locProvider, "SortAsc") {
		private static final long serialVersionUID = 1L;

		@Override
		protected String alterText(String text) {
			return sortLines(text, true);
		}
	};
	
	/**
	 * Sorts the lines in descending order.
	 * @see JNotepad.SelectedTextAction
	 */
	private Action sortDescAction = new SelectedTextAction(locProvider, "SortDesc") {
		private static final long serialVersionUID = 1L;

		@Override
		protected String alterText(String text) {
			return sortLines(text, false);
		}
	};
	
	/**
	 * Removes empty lines.
	 * @see JNotepad.SelectedTextAction
	 */
	private Action removeEmptyAction = new SelectedTextAction(locProvider, "RmEmpty") {
		private static final long serialVersionUID = 1L;

		@Override
		protected String alterText(String text) {
			String[] lines = text.split("\\r?\\n");
			StringBuilder sb = new StringBuilder();
			for(String s : lines) {
				if(!s.isEmpty()) {
					sb.append(s+"\n");
				}
			}
			return sb.toString();
		}
	};
	
	/**
	 * Generates the document statistics and shows them in StatsDialog. If
	 * there is no active document, it will show a message to the user.
	 */
	private Action statisticsAction = new LocalizableAction(locProvider, "Stats") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			TabbedTextArea editor = (TabbedTextArea) tabPane.getSelectedComponent();
			if(editor == null) {
				JOptionPane.showMessageDialog(JNotepad.this, 
						locProvider.getString("NoStatsMsg"),
						locProvider.getString("InfoTitle"), 
						JOptionPane.INFORMATION_MESSAGE);	
			} else {
				String dialogTitle = locProvider.getString("Stats");
				new StatsDialog(locProvider, editor, JNotepad.this,
					dialogTitle);
			}
		}
	};
	
	/*
	 * These are already implemented but had to be modified a little.
	 */
	
	/**
	 * Please see {@link DefaultEditorKit.CutAction}, 
	 * it provides the same action.
	 */
	private Action cutAction = new LocalizableAction(locProvider, "Cut") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			TabbedTextArea editor = (TabbedTextArea) tabPane.getSelectedComponent();
			if(editor == null) {
				return;
			}
			editor.cut();
		}
	};
	
	/**
	 * Please see {@link DefaultEditorKit.CopyAction}, 
	 * it provides the same action.
	 */
	private Action copyAction = new LocalizableAction(locProvider, "Copy") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			TabbedTextArea editor = (TabbedTextArea) tabPane.getSelectedComponent();
			if(editor == null) {
				return;
			}
			editor.copy();
		}
	};
	
	/**
	 * Please see {@link DefaultEditorKit.PasteAction}, 
	 * it provides the same action.
	 */
	private Action pasteAction = new LocalizableAction(locProvider, "Paste") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			TabbedTextArea editor = (TabbedTextArea) tabPane.getSelectedComponent();
			if(editor == null) {
				return;
			}
			editor.paste();
		}
	};
	
	/**
	 * Instances of this class manipulate the selected chunk of text by
	 * altering it according to the 'alterText' method which must be implemented by
	 * the extending classes.
	 */
	private abstract class SelectedTextAction extends LocalizableAction {
		private static final long serialVersionUID = 1L;

		/**
		 * Does something with the text: changes case, sorts the lines...
		 * @param String the text that was selected
		 * @return String the selected text, changed accordingly
		 */
		protected abstract String alterText(String text);
		
		public SelectedTextAction(ILocalizationProvider provider, String key) {
			super(provider, key);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			TabbedTextArea editor = (TabbedTextArea) tabPane.getSelectedComponent();
			if(editor == null) {
				return;
			}
			Document doc = editor.getDocument();
			int len = Math.abs(editor.getCaret().getMark() - editor.getCaret().getDot());	// pretp.
			int offs = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
			try {
				String text = doc.getText(offs, len);
				text = alterText(text);
				doc.remove(offs, len);
				doc.insertString(offs, text, null);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
				return;
			}
			editor.getCaret().setDot(offs);
		}
	}
	
	/**
	 * Helper method for actions which sort the text.
	 * @param text to be altered
	 * @param ascending if true, will sort in ascending order, else descending
	 * @return The modified text as String
	 */
	private static String sortLines(String text, boolean ascending) {
		String[] lines = text.split("\\r?\\n");
		if(ascending) {
			Arrays.sort(lines);
		} else {
			Arrays.sort(lines, Collections.reverseOrder());
		}
		StringBuilder sb = new StringBuilder();
		for(String s : lines) {
			sb.append(s+"\n");
		}
		return sb.deleteCharAt(sb.length()-1).toString();
	}
	
	/**
	 * Used when switching the languages to translate FileChooser and OptionPane.
	 */
	private void langSwitch() {
		UIManager.put("FileChooser.lookInLabelText", locProvider.getString("FCLookIn"));
		UIManager.put("FileChooser.saveInLabelText", locProvider.getString("FCSaveIn"));
		UIManager.put("FileChooser.upFolderToolTipText", locProvider.getString("FCUpFolderTT"));
		UIManager.put("FileChooser.homeFolderToolTipText", locProvider.getString("FCHomeFolderTT"));
		UIManager.put("FileChooser.newFolderToolTipText", locProvider.getString("FCNewFolderTT"));
		UIManager.put("FileChooser.listViewButtonToolTipText", locProvider.getString("FCListViewTT"));
		UIManager.put("FileChooser.detailsViewButtonToolTipText", locProvider.getString("FCDetailsTT"));
		UIManager.put("FileChooser.fileNameHeaderText", locProvider.getString("FCFileNameHdr"));
		UIManager.put("FileChooser.fileSizeHeaderText", locProvider.getString("FCFileSizeHdr"));
		UIManager.put("FileChooser.fileTypeHeaderText", locProvider.getString("FCFileTypeHdr"));
		UIManager.put("FileChooser.fileDateHeaderText", locProvider.getString("FCFileDateHdr"));
		UIManager.put("FileChooser.fileAttrHeaderText", locProvider.getString("FCFileAttrHdr"));
		UIManager.put("FileChooser.fileNameLabelText", locProvider.getString("FCFileName"));
		UIManager.put("FileChooser.filesOfTypeLabelText", locProvider.getString("FCFilesOfType"));
		UIManager.put("FileChooser.openButtonText", locProvider.getString("FCOpen"));
		UIManager.put("FileChooser.openButtonToolTipText", locProvider.getString("FCOpenTT"));
		UIManager.put("FileChooser.saveButtonText", locProvider.getString("FCSave"));
		UIManager.put("FileChooser.saveButtonToolTipText", locProvider.getString("FCSaveTT"));
		UIManager.put("FileChooser.directoryOpenButtonText", locProvider.getString("FCDirOpen"));
		UIManager.put("FileChooser.directoryOpenButtonToolTipText", locProvider.getString("FCDirOpenTT"));
		UIManager.put("FileChooser.cancelButtonText", locProvider.getString("FCCancel"));
		UIManager.put("FileChooser.cancelButtonToolTipText", locProvider.getString("FCCancelTT"));
		UIManager.put("FileChooser.updateButtonText", locProvider.getString("FCUpdate"));
		UIManager.put("FileChooser.updateButtonToolTipText", locProvider.getString("FCUpdateTT"));
		UIManager.put("FileChooser.helpButtonText", locProvider.getString("FCHelp"));
		UIManager.put("FileChooser.helpButtonToolTipText", locProvider.getString("FCHelpTT"));
		UIManager.put("FileChooser.newFolderErrorText", locProvider.getString("FCNewFolderError"));
		UIManager.put("FileChooser.acceptAllFileFilterText", locProvider.getString("FCAcceptAll"));
		UIManager.put("OptionPane.yesButtonText", locProvider.getString("OPYes"));
		UIManager.put("OptionPane.noButtonText", locProvider.getString("OPNo"));
		UIManager.put("OptionPane.cancelButtonText", locProvider.getString("OPCancel"));
	}
	
	/**
	 * A dialog which displays the table and a graph containing the
	 * information about the currently opened document. When called,
	 * it appears in the middle of the screen and cannot be resized.
	 * This decision was based on its modality (it is modal), so to
	 * perform any further action, the user had to remove it anyway
	 * and its components are also automatically scaled.
	 */
	private class StatsDialog extends JDialog  {
		private static final long serialVersionUID = 1L;
		public static final int DIALOG_WIDTH = 600;
		public static final int GRAPH_HEIGHT = 300;
		
		private int documentLength;
		private int upperCaseCount;
		private int lowerCaseCount;
		private int spaceCount;
		private int wordCount;
		
		private JTable statsTable;
		private StatisticsVisual statsVisual;
		
		private ILocalizationProvider provider;
		private TabbedTextArea editor;
		
		/**
		 * Constructor for {@link StatsDialog}
		 * @param locProvider localization provider
		 * @param editor textArea to get the data from
		 * @param frame see {@link JDialog}s constructor
		 * @param title see {@link JDialog}s constructor
		 */
		public StatsDialog(ILocalizationProvider locProvider, 
				TabbedTextArea editor, JFrame frame, String title) {
			super(frame, title);
			this.provider = locProvider;
			this.editor = editor;
			// will display the modal dialog in the middle of the screen
			Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
			this.setModalityType(ModalityType.APPLICATION_MODAL);
			this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setResizable(false);
			initDialog();
			this.setLocation(
					screenDim.width/2 - DIALOG_WIDTH/2, 
					screenDim.height/2
					- GRAPH_HEIGHT/2
					- statsTable.getPreferredSize().height/2);
			this.setVisible(true);
		}
		
		/**
		 * Analyzes the document, gets the required data.	
		 */
		private void getStatsFor() {
			Document doc = editor.getDocument();
			String text = "";
			try {
				text = doc.getText(0, doc.getLength());
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			documentLength = text.length();
			if(text.isEmpty()) {
				wordCount = 0;
			} else {
				wordCount = text.split("\\s+").length;
			}
			for(int i = 0; i < text.length(); i++) {
				if(Character.isUpperCase(text.charAt(i))) {
					upperCaseCount++;
				} else if(Character.isLowerCase(text.charAt(i))) {
					lowerCaseCount++;
				} else if(text.charAt(i) == ' ' || text.charAt(i) == '\t') {
					spaceCount++;
				}
			}
		}
		
		/**
		 * Initializes the dialog components and data.
		 */
		private boolean initDialog() {
			this.getStatsFor();
			// table initialization
			Object[][] tableData = {{documentLength, upperCaseCount, lowerCaseCount,
				spaceCount, wordCount}};
			String[] columnTitles = {
					provider.getString("DocLength"),
					provider.getString("UpCaseCount"),
					provider.getString("LowCaseCount"),
					provider.getString("SpcCount"),
					provider.getString("WrdCount")
			};
			this.statsTable = new JTable(tableData, columnTitles);
			this.statsTable.setFocusable(false);
			this.statsTable.setEnabled(false);
			this.statsTable.setPreferredSize(new Dimension(
					DIALOG_WIDTH, statsTable.getPreferredSize().height));
			JPanel tablePanel = new JPanel(new BorderLayout());
			tablePanel.add(statsTable.getTableHeader(), BorderLayout.NORTH);
			tablePanel.add(statsTable, BorderLayout.CENTER);
			
			// visual representation initialization
			int[] graphData = {documentLength, upperCaseCount, lowerCaseCount,
					spaceCount, wordCount};
			columnTitles[0] = provider.getString("DocLengthS");
			columnTitles[1] = provider.getString("UpCaseCountS");
			columnTitles[2] = provider.getString("LowCaseCountS");
			columnTitles[3] = provider.getString("SpcCountS");
			columnTitles[4]	= provider.getString("WrdCountS");
			this.statsVisual = new StatisticsVisual(graphData, columnTitles);
			this.statsVisual.setPreferredSize(new Dimension(
					DIALOG_WIDTH, GRAPH_HEIGHT));
			JPanel graphPanel = new JPanel();
			graphPanel.setBackground(Color.white);
			graphPanel.add(statsVisual);
			this.add(graphPanel, BorderLayout.CENTER);
			this.add(tablePanel, BorderLayout.PAGE_START);
			this.pack();
			return true;
		}
		
		/**
		 * Provides the graphical representation of the documents statistics.
		 * It scales the bars of a graph according to the documents length.
		 * Graphs behavior when resizing was not tested as such actions are
		 * not meant to be performed anyway (the dialog it is placed in has
		 * constant size).
		 */
		private class StatisticsVisual extends JComponent {
			private static final long serialVersionUID = 1L;
			private int xMin;
			private int xMax;
			private int yMin;
			private int yMax;
			private int[] data;
			private String[] columnTitles;
			public final static int HTEXTHEIGHT = 20; // horizontal text height
			public final static int VTEXTWIDTH = 40;	// vertical text width
			public final static int VERTICAL_SCALE = 6;
			public final static int VTEXTHEIGHT = 15;
			public final static int SEPHEIGHT = 5;
			
			public StatisticsVisual(int[] data, String[] columnTitles) {
				this.data = data;
				this.columnTitles = columnTitles;
			}
			
			@Override
			protected void paintComponent(Graphics g) {
				Insets compoInsets = this.getInsets();
				Dimension compoSize = this.getSize();
				xMin = compoInsets.left;
				xMax = compoSize.width - compoInsets.right;
				yMin = compoInsets.bottom;
				yMax = compoSize.height - compoInsets.top;
				
				// draw horizontal lines
				g.setColor(Color.gray);
				int vs = (yMax - HTEXTHEIGHT - yMin) / VERTICAL_SCALE;	// vertical spacing
				for(int y = yMax - HTEXTHEIGHT; y >= yMin + VTEXTHEIGHT; y -= vs) {
					g.drawLine(xMin + VTEXTWIDTH, y, xMax - VTEXTWIDTH, y);
				}
				
				// draw vertical lines
				int hs = (xMax - 2*VTEXTWIDTH - xMin) / data.length;	// horizontal spacing
				g.drawLine(VTEXTWIDTH, yMin + VTEXTHEIGHT, 
						VTEXTWIDTH, yMax - HTEXTHEIGHT);
				g.drawLine(xMax - VTEXTWIDTH, yMin + VTEXTHEIGHT, 
						xMax - VTEXTWIDTH, yMax - HTEXTHEIGHT);
				
				// get the scale (k)
				int k = 1 * VERTICAL_SCALE-1;
				while(k < data[0]) {
					k *= 10;
				}
				k /= VERTICAL_SCALE-1;
				
				// draw vertical text
				g.setColor(Color.black);
				for(int y = yMax - HTEXTHEIGHT, i = 0; 
						y >= yMin + VTEXTHEIGHT; 
						y -= vs, i++) {
					g.drawString(Integer.toString(k*i), xMin, y);
				}
				
				// draw short lines (separators)
				g.setColor(Color.gray);
				for(int x = xMin + VTEXTWIDTH; x <= xMax - VTEXTWIDTH; x += hs) {
					g.drawLine(x, yMax - HTEXTHEIGHT - SEPHEIGHT, 
							x, yMax - HTEXTHEIGHT);
				}
				
				// draw horizontal text and the bars
				double d = vs /  ((double) k);
				int rectWidth = hs / 2;
				int rectHeight;	// to be calculated in the loop
				for(int x = xMin + VTEXTWIDTH, i = 0; 
						x <= xMax - VTEXTWIDTH && i < columnTitles.length; 
						x += hs, i++) {
					rectHeight = (int) (data[i] * d);
					g.setColor(Color.black);
					g.drawString(columnTitles[i], 
							x - (hs/12) * columnTitles[i].length()/2 + hs/2, 
							yMax - HTEXTHEIGHT/3);
					g.setColor(Color.blue);
					g.fillRect(x + hs/4, yMax - HTEXTHEIGHT - rectHeight, 
							rectWidth, rectHeight);	
				}
			}
		}
	}
}
