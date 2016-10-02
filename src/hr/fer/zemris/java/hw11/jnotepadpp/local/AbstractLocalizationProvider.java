package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which implements ILocalizationProviders interfaces methods to add
 * and remove listener from private list of listeners. It also defines its
 * method fire which triggers all listeners
 * 
 * @author Marin Kukovacec
 * @version 1.0
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {

	/**
	 * List of listeners
	 */
	final private List<ILocalizationListener> listeners = new ArrayList<>();

	@Override
	public abstract String getString(String key);

	@Override
	public void addLocalizationListener(ILocalizationListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeLocalizationListener(ILocalizationListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Method which triggers all listeners from list
	 */
	public void fire() {
		for (ILocalizationListener listener : listeners) {
			listener.localizationChanged();
		}
	}

}
