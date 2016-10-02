package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.JToolBar;

/**
 * Class which extends JToolBar to localize it. It changes toolbar name to
 * localized string every time localization changes
 * 
 * @author Marin Kukovacec
 * @version 1.0
 */
public class LJToolBar extends JToolBar {

	/**
	 * generated version UID
	 */
	private static final long serialVersionUID = -160398316422769875L;

	/**
	 * Constructor which initializes this class with key for localization
	 * and instance of localization provider which will generate string
	 * 
	 * @param key is key which will be used to get localized string
	 * @param provider is provider which will get localized string with key
	 */
	public LJToolBar(String key, ILocalizationProvider provider) {
		setName(provider.getString(key));
		provider.addLocalizationListener(new ILocalizationListener() {

			@Override
			public void localizationChanged() {
				setName(provider.getString(key));
			}
		});
	}

}
