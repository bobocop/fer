package hr.fer.zemris.java.hw07.layoutmans;

import hr.fer.zemris.java.hw07.layoutmans.SimpleLayout.SimpleLayoutPlacement;
import java.awt.GridLayout;
import java.awt.LayoutManager2;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class ExampleSimpleLayoutFrame extends JFrame {
	private static final long serialVersionUID = 8818691790593467664L;

	public ExampleSimpleLayoutFrame() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Primjer uporabe SimpleLayouta");
		initGUI();
		pack();
	}

	private void initGUI() {
		this.getContentPane().setLayout(new GridLayout(1, 3));
		this.getContentPane().add(
				makePanel1("Left", new SimpleLayout(),
				SimpleLayoutPlacement.LEFT));
		this.getContentPane().add(
				makePanel1("Center", new SimpleLayout(),
				SimpleLayoutPlacement.CENTER));
		this.getContentPane().add(
				makePanel2("Left + Center", new SimpleLayout()));
	}

	private JComponent makePanel1(String tekst, LayoutManager2 manager,
			SimpleLayoutPlacement placement) {
		JPanel panel = new JPanel(manager);
		panel.setBorder(BorderFactory.createTitledBorder(tekst));
		panel.add(new JButton("Gumb X"), placement);
		// panel.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		return panel;
	}
	
	private JComponent makePanel2(String tekst, LayoutManager2 manager) {
		JPanel panel = new JPanel(manager);
		panel.setBorder(BorderFactory.createTitledBorder(tekst));
		panel.add(new JButton("Gumb L"), SimpleLayoutPlacement.LEFT);
		panel.add(new JButton("Gumb C"), SimpleLayoutPlacement.CENTER);
		// panel.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		return panel;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new ExampleSimpleLayoutFrame().setVisible(true);
			}
		});
	}
}
