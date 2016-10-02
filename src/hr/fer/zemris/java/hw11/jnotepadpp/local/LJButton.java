package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Class which represents localized JButton. It means that this button does
 * action when localization changes. This JButton sets icon to itself instead
 * of text and deletes text every time localization changes
 * 
 * @author Marin Kukovacec
 * @version 1.0
 */
public class LJButton extends JButton{

	/**
	 * generated serial version UID
	 */
	private static final long serialVersionUID = 8579613505757885966L;

	/**
	 * Constructor which initializes this button with action which will be
	 * applied when button is pressed, icon which will be displayed and 
	 * provider which will add localized listener
	 * 
	 * @param action is action which will be triggered when button is pressed
	 * @param icon is image icon which will be displayed
	 * @param provider is localization provider
	 */
	public LJButton(Action action, ImageIcon icon, ILocalizationProvider provider){
		super(action);
		setIcon(icon);
		setIconTextGap(0);
		setText("");
		provider.addLocalizationListener(new ILocalizationListener() {
			
			@Override
			public void localizationChanged() {
				setText("");	
			}
		});
	}
	
}
