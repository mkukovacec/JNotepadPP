package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.JMenu;

/**
 * Class which extends JMenu and implement it to be localizable. It means
 * that every time localization changes, text which menu display will be 
 * changed to localized string.
 * 
 * @author Marin Kukovacec
 * @version 1.0
 */
public class LJMenu extends JMenu{

	/**
	 * generated serial version UID
	 */
	private static final long serialVersionUID = -7023744767214979149L;

	/**
	 * Constructor which initializes this JMenu with key which will be used
	 * to get localized string for JMenu and provider which be used to get
	 * localized string when localization changes
	 * 
	 * @param key is key which is used to get localized string
	 * @param provider is localized provider which gets strings with keys
	 */
	public LJMenu(String key, ILocalizationProvider provider){
		setText(provider.getString(key));
		provider.addLocalizationListener(new ILocalizationListener() {
			
			@Override
			public void localizationChanged() {
				setText(provider.getString(key));
			}
		});
	}
	
}
