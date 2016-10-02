package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.AbstractAction;

/**
 * Abstract class which extends Abstract action class and implement it in way to
 * be localized, which means that NAME and SHORT_DESCRIPTION of action will be
 * changed when language changes
 * 
 * @author Marin Kukovacec
 * @version 1.0
 */
public abstract class LocalizableAction extends AbstractAction {

	/**
	 * generated serial version UID
	 */
	private static final long serialVersionUID = -5842089802379741337L;

	/**
	 * Constructor which intializes this class with key which will be used to
	 * get name and short description and provider which will be used to
	 * generate those two with a key when language changes
	 * 
	 * @param key
	 *            is key which will be used to get name and short description
	 * @param provider
	 *            is localization provider which is used to generate name and
	 *            short description with a key
	 */
	public LocalizableAction(String key, ILocalizationProvider provider) {
		putValue(NAME, provider.getString(key));
		putValue(SHORT_DESCRIPTION, provider.getString(key + "desc"));
		provider.addLocalizationListener(new ILocalizationListener() {

			@Override
			public void localizationChanged() {
				putValue(NAME, provider.getString(key));
				putValue(SHORT_DESCRIPTION, provider.getString(key + "desc"));
			}
		});
	}
}
