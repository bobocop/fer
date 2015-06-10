package hr.fer.zemris.java.tecaj_10.local;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

/**
 * LocalizationProviderBridge which removes the references to the
 * components of the windows that were closed.
 */
public class FormLocalizationProvider extends LocalizationProviderBridge
		implements WindowListener {

	public FormLocalizationProvider(ILocalizationProvider provider, JFrame frame) {
		super(provider);
		frame.addWindowListener(this);
	}

	@Override
	public void windowOpened(WindowEvent e) {
		connect();
	}

	@Override
	public void windowClosed(WindowEvent e) {
		disconnect();
	}
	
	// unused
	@Override
	public void windowClosing(WindowEvent e) {	}
	@Override
	public void windowIconified(WindowEvent e) {}
	@Override
	public void windowDeiconified(WindowEvent e) {}
	@Override
	public void windowActivated(WindowEvent e) {}
	@Override
	public void windowDeactivated(WindowEvent e) {}
}
