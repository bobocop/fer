package hr.fer.zemris.java.tecaj_10.local;

/**
 * Provides a connection between the LocalizationListeners
 * (GUI components) and the main LocalizationProvider which
 * should not keep any references to the said components so
 * the GC can delete them when their containing windows are
 * closed.
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {
	private boolean connected;
	private ILocalizationProvider parent;
	private String rememberedLang;
	private ILocalizationListener listener = new ILocalizationListener() {
		
		@Override
		public void localizationChanged() {
			fire();
		}
	};

	/**
	 * @param parent is the main LocalizationProvider, 
	 * one that does actual work
	 */
	public LocalizationProviderBridge(ILocalizationProvider parent) {
		this.parent = parent;
		this.connected = false;
		this.rememberedLang = "";
	}

	/**
	 * Called when a component (window) requests 'LocalizationProvider'.
	 */
	void connect() {
		if(!connected) {
			parent.addLocalizationListener(listener);
			connected = true;
		}
		if(!rememberedLang.equals(getLanguage())) {
			fire();
		}
	}

	/**
	 * Called when the window is closed, so as to remove all references
	 * and enable deletion.
	 */
	void disconnect() {
		if(connected) {
			parent.removeLocalizationListener(listener);
			connected = false;
			rememberedLang = getLanguage();
		}
	}

	@Override
	public String getString(String key) {
		if(connected) {
			return parent.getString(key);
		} else {
			return null;
		}
	}

	@Override
	public String getLanguage() {
		return parent.getLanguage();
	}
}
