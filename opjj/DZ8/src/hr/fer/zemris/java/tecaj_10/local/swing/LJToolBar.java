package hr.fer.zemris.java.tecaj_10.local.swing;

import hr.fer.zemris.java.tecaj_10.local.ILocalizationListener;
import hr.fer.zemris.java.tecaj_10.local.ILocalizationProvider;

import javax.swing.JToolBar;

/**
 * Localized JToolBar.
 */
public class LJToolBar extends JToolBar implements ILocalizationListener {
	private static final long serialVersionUID = 1L;
	private ILocalizationProvider provider;
	private String title;
	
	public LJToolBar(ILocalizationProvider provider, String key) {
		super();
		this.provider = provider;
		this.title = key;
		this.provider.addLocalizationListener(this);
		setName(provider.getString(key));
	}

	@Override
	public void localizationChanged() {
		setName(provider.getString(title));
	}

}
