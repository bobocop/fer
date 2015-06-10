package hr.fer.zemris.java.tecaj_10.local;

/**
 * Classes which provide the localization information
 * implement this interface.
 */
public interface ILocalizationProvider {

	public void addLocalizationListener(ILocalizationListener l);
	public void removeLocalizationListener(ILocalizationListener l);
	
	/**
	 * Gets the properly localized string denoted by key.
	 * @param key of the string
	 * @return Localized string which corresponds to key.
	 */
	public String getString(String key);
	
	/**
	 * @return The current language of the provider.
	 */
	public String getLanguage();
}
