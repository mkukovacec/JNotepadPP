package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Class which extends Abstract localization provider and implements its
 * method getstring. This class is used to connect and disconnect provider
 * from localization in order to ensure that there is no reference to JFrame
 * when its disposed so that garbage collector can realise it from memory
 * 
 * @author Marin Kukovacec
 * @version 1.0
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider{

	/**
	 * provider which is going to be connected
	 */
	private ILocalizationProvider provider;
	
	/**
	 * listener which will be added to connected provider
	 */
	private ILocalizationListener listener = ()->fire();
	
	/**
	 * boolean variable which tells if connected
	 */
	private boolean connected;
	
	/**
	 * Constructor which initializes this class with provider
	 * 
	 * @param provider is localization provider we are going to use
	 */
	public LocalizationProviderBridge(ILocalizationProvider provider) {
		this.provider=provider;
	}
	
	@Override
	public String getString(String key) {
		return provider.getString(key);
	}
	
	/**
	 * Method which connects listeners to provider.
	 */
	public void connect(){
		if (connected){
			return;
		}
		
		connected=true;
		provider.addLocalizationListener(listener);
	}
	
	/**
	 * Method which disconnects listeners from provider
	 */
	public void disconnect(){
		if (!connected){
			return;
		}
		connected=false;
		provider.removeLocalizationListener(listener);
	}

}
