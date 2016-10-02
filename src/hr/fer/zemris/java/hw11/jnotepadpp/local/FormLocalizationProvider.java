package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * Class which extends LocalizationProviderBridge class to add window listener
 * which will connect provider to listeners when windows is opened and 
 * disconnect provider from listeners when window is closed.
 * 
 * @author Marin Kukovacec
 * @version 1.0
 */
public class FormLocalizationProvider extends LocalizationProviderBridge{

	/**
	 * Constructor which initializes this class with localization provider
	 * and JFrame
	 * 
	 * @param provider is provider which will be connected
	 * @param frame is frame which will be opened and closed
	 */
	public FormLocalizationProvider(ILocalizationProvider provider, JFrame frame){
		
		super(provider);
		
		frame.addWindowListener(new WindowAdapter(){
			
			@Override
			public void windowOpened(WindowEvent e){
				connect();
			}
			
			@Override
			public void windowClosed(WindowEvent e){
				disconnect();
			}
			
		});
		
	}
}
