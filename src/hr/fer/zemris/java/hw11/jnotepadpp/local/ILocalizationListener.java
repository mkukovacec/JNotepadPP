package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Interface which provides localized listener which triggers when localization
 * changes
 * 
 * @author Marin Kukovacec
 * @version 1.0
 */
public interface ILocalizationListener {

	/**
	 * Method which determines what will be done when localization changes
	 */
	public void localizationChanged();
	
}
