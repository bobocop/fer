package hr.fer.zemris.java.tecaj_10.local;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationProvider extends AbstractLocalizationProvider {
	private static LocalizationProvider instance = new LocalizationProvider();
	private String language;
	private ResourceBundle bundle;
	
	/**
	 * Sets the language to English by default.
	 */
	private LocalizationProvider() {
		setLanguage("en");
	}
	
	/**
	 * Set the language of the provider. At the moment only English 
	 * and Croatian are supported.
	 * @param language can be "en" or "hr"
	 */
	public void setLanguage(String language) {
		this.language = language;
		Locale locale = Locale.forLanguageTag(this.language);
		this.bundle = ResourceBundle.getBundle(
				"hr.fer.zemris.java.tecaj_10.local.Poruke",
				locale);
		fire();
	}
	
	/**
	 * Use this method instead of 'new' when this class is needed.
	 * @return An instance of this LocalizationProvider
	 */
	public static LocalizationProvider getInstance() {
		return instance;
	}
	
	
	@Override
	public String getString(String key) {
		try {
			return bundle.getString(key);
		} catch(Exception e) {
			e.printStackTrace();
			return "?"+key;
		}
	}

	@Override
	public String getLanguage() {
		return language;
	}

}
