package hr.fer.zemris.java.tecaj_10.local;

/**
 * Classes which wish to be informed about the
 * change of the UI language implement this interface.
 */
public interface ILocalizationListener {
	
	/**
	 * What to do when localization changes.
	 */
	public void localizationChanged();
}
