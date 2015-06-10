package hr.fer.zemris.java.tecaj_10.local.swing;

import javax.swing.JMenu;

import hr.fer.zemris.java.tecaj_10.local.ILocalizationListener;
import hr.fer.zemris.java.tecaj_10.local.ILocalizationProvider;

/**
 * Localized JMenu.
 */
public class LJMenu extends JMenu implements ILocalizationListener {
	private static final long serialVersionUID = 1L;
	private ILocalizationProvider provider;
	private String title;
	
	public LJMenu(ILocalizationProvider provider, String key) {
		super();
		this.provider = provider;
		this.title = key;
		this.provider.addLocalizationListener(this);
		setText(provider.getString(key));
	}

	@Override
	public void localizationChanged() {
		setText(provider.getString(title));
	}

}
