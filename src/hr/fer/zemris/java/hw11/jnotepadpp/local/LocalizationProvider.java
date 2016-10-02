package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class which is extends Abstract localization provider and implements its
 * method getstring to return string received from bundle with a key. This
 * class is singleton and allows only one instance of this class to be created.
 * This class can be gotten with static method getinstance 
 * 
 * @author Marin Kukovacec
 * @version 1.0
 */
public class LocalizationProvider extends AbstractLocalizationProvider{

	/** default language string */
	private static final String DEFAULT = "en";
	
	/** String which represents language. */
	private String language;
	
	/** one and only instance of this class */
	private final static LocalizationProvider INSTANCE = new LocalizationProvider();
	
	/** Resource bundle is bundle which will be used to get strings with key */
	private ResourceBundle bundle;
	
	/**
	 * private constructor which sets language to be default language
	 */
	private LocalizationProvider(){
		setLanguage(DEFAULT);
	}
	

	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}

	/**
	 * Method which sets language to be language and receives bundle from
	 * given language and triggers all listeners
	 * 
	 * @param language is language which will be set
	 */
	public void setLanguage(String language){
		if (language.equalsIgnoreCase(this.language)){
			return;
		}
		
		this.language=language;
		
		Locale locale = Locale.forLanguageTag(language);
		bundle=ResourceBundle.getBundle(
				"hr.fer.zemris.java.hw11.jnotepadpp.local.lang", locale);
		fire();
	}
	
	/**
	 * Method which is used to get instance of this class
	 * 
	 * @return one and only instance of Localization provider
	 */
	public static LocalizationProvider getInstance(){
		return INSTANCE;
	}
	
}
