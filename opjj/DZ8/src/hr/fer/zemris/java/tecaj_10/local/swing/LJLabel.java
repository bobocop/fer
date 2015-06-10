package hr.fer.zemris.java.tecaj_10.local.swing;

import javax.swing.JLabel;

import hr.fer.zemris.java.tecaj_10.local.ILocalizationListener;
import hr.fer.zemris.java.tecaj_10.local.ILocalizationProvider;

/**
 * Localized JLabel.
 */
public class LJLabel extends JLabel implements ILocalizationListener {
	private static final long serialVersionUID = 1L;
	private ILocalizationProvider provider;
	private String text;

	public LJLabel(ILocalizationProvider provider, String key) {
		super();
		this.provider = provider;
		this.text = key;
		this.provider.addLocalizationListener(this);
		setText(provider.getString(key));
	}
	
	@Override
	public void localizationChanged() {
		setText(text);
	}

}
