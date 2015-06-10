package hr.fer.zemris.java.hw07.students;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.nio.file.Paths;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class StudentBrowser {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new StudBrowserFrame().setVisible(true);
			}
		});
	}

	/**
	 * StudBrowsers frame contains three panels: one conetaining the table,
	 * another a list and the last one contains the adding panel used to
	 * input additional information to the database.
	 */
	public static class StudBrowserFrame extends JFrame {
		private static final long serialVersionUID = 1L;
		private static final StudentDatabase db = new FileStudentDatabase(
				Paths.get("students.txt"));
		private StudentTable table;
		private StudentList list;
		private AddingPanel add;

		public StudBrowserFrame() {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setTitle("Student Browser");
			getContentPane().setLayout(
					new BorderLayout());
			this.table = new StudentTable();
			this.list = new StudentList();
			this.add = new AddingPanel();
			initGUI();
			pack();
		}

		private void initGUI() {
			JPanel panelList = new JPanel();
			JScrollPane panelTable = new JScrollPane(table);
			panelTable.setSize(table.getMinimumSize());
			
			panelList.setBorder(BorderFactory
					.createTitledBorder("Lista studenata"));
			panelList.add(list);
	 
			panelTable.setBorder(BorderFactory
					.createTitledBorder("Tablica studenata"));
			panelTable.setPreferredSize(new Dimension(
					panelTable.getPreferredSize().width, 128));
			
			add.setBorder(BorderFactory
					.createTitledBorder("Dodavanje"));

			this.getContentPane().add(panelList, BorderLayout.PAGE_START);
			this.getContentPane().add(panelTable, BorderLayout.CENTER);
			this.getContentPane().add(add, BorderLayout.PAGE_END);
		}

		/**
		 * A list that displays some information about the students. A student
		 * may be selected and if 'Delete' is pressed, information about them is
		 * deleted from the databasse.
		 */
		public static class StudentTable extends JTable implements 
			KeyListener, FocusListener {
			private static final long serialVersionUID = 1L;

			public StudentTable() {
				setModel(new TableSDBModel(db));
				addMouseListener(new MouseClickListener(this));
				addKeyListener(this);
				getModel().addTableModelListener(this);
				addFocusListener(this);
			}
			
			/**
			 * Removes the selected student when 'Delete' is pressed.
			 */
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_DELETE) {
					int index;
					if((index = this.getSelectedRow()) != -1) {
						db.remove(index);
					}
				}
			}
			
			/**
			 * Removes the highlight when the focus is lost.
			 */
			@Override
			public void focusLost(FocusEvent e) {
				clearSelection();
			}
			
			// unused
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void focusGained(FocusEvent e) {}
		}

		/**
		 * A table that displays some information about the students. A student
		 * may be selected and if 'Delete' is pressed, information about them is
		 * deleted from the databasse.
		 */
		public static class StudentList extends JList<Student> implements
				KeyListener, FocusListener {
			private static final long serialVersionUID = 1L;

			public StudentList() {
				setModel(new ListSDBModel(db));
				setLayoutOrientation(JList.VERTICAL);
				setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				addKeyListener(this);
				addMouseListener(new MouseClickListener(this));
				addFocusListener(this);
			}
			
			/**
			 * Removes the selected student when 'Delete' is pressed.
			 */
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_DELETE) {
					int index;
					if((index = this.getSelectedIndex()) != -1) {
						db.remove(index);
					}
				}
				
			}

			/**
			 * Removes the highlight when the focus is lost.
			 */
			@Override
			public void focusLost(FocusEvent e) {
				clearSelection();
			}
			
			// unused
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void focusGained(FocusEvent e) {}
		}
		
		/**
		 * A panel that is used to input some information about the student
		 * in the textfields and to submit that information to the database.
		 */
		public static class AddingPanel extends JPanel implements
				ActionListener {
			private static final long serialVersionUID = 1L;
			private JTextField sid;
			private JTextField ln;
			private JTextField fn;
			JButton button = new JButton("Dodaj");

			public AddingPanel() {
				setBorder(BorderFactory.createTitledBorder("Dodavanje"));
				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				this.sid = new JTextField();
				this.ln = new JTextField();
				this.fn = new JTextField();

				JLabel labelSid = new JLabel();
				JLabel labelFn = new JLabel();
				JLabel labelLn = new JLabel();
				labelSid.setText("Student ID: ");
				labelSid.setLabelFor(sid);
				labelLn.setText("Last name: ");
				labelLn.setLabelFor(ln);
				labelFn.setText("First name: ");
				labelFn.setLabelFor(fn);

				JPanel inputFields = new JPanel();
				inputFields.setLayout(new GridLayout(3, 2));
				inputFields.add(labelSid);
				inputFields.add(sid);
				inputFields.add(labelLn);
				inputFields.add(ln);
				inputFields.add(labelFn);
				inputFields.add(fn);

				button.addActionListener(this);

				this.add(inputFields);
				this.add(button);
			}

			/**
			 * Gather the entered information when the button is pressed.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				db.addStudent(sid.getText(), ln.getText(), fn.getText());
				sid.setText(null);
				ln.setText(null);
				fn.setText(null);
			}
		}
		
		/**
		 * The component which is listening will request focus when
		 * it has been clicked on.
		 */
		public static class MouseClickListener implements MouseListener {
			private JComponent component;
			
			public MouseClickListener(JComponent component) {
				this.component = component;
			}
			
			public void mouseClicked(MouseEvent e) {
				if(!component.isFocusOwner()) {
					component.requestFocusInWindow();
				}
			}
			
			// unused
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}	
		}
	}
}
