package hr.fer.zemris.java.tecaj_10.local;

import java.util.ArrayList;
import java.util.List;

/**
 * Used by 'LocalizationListeners' to get information about the
 * UI language change.
 */
public abstract class AbstractLocalizationProvider implements
		ILocalizationProvider {
	List<ILocalizationListener> listeners = new ArrayList<>();
	
	@Override
	public void addLocalizationListener(ILocalizationListener l) {
		if(!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	@Override
	public void removeLocalizationListener(ILocalizationListener l) {
		if(listeners.contains(l)) {
			listeners.remove(l);
		}
	}
	
	protected void fire() {
		ILocalizationListener[] array = listeners.toArray(
				new ILocalizationListener[listeners.size()]);
		for(ILocalizationListener l : array) {
			l.localizationChanged();
		}
	}
}
