package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Interface which provides methods for adding and removing listeners, and
 * getting localized string from given key. 
 * 
 * @author Marin Kukovacec
 * @version 1.0
 */
public interface ILocalizationProvider {

	/**
	 * Method which adds listener to provider
	 * 
	 * @param listener is listener which will be added
	 */
	public void addLocalizationListener(ILocalizationListener listener);
	
	/**
	 * Method which removes given listener from provider
	 * 
	 * @param listener is listener which will be removed
	 */
	public void removeLocalizationListener(ILocalizationListener listener);
	
	/**
	 * Method which gets localized string for given key
	 * 
	 * @param key is key for getting localized string
	 * @return localicazed string
	 */
	public String getString(String key);
	
}
