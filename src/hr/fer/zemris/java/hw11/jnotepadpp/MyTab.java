package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Caret;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * 
 * 
 * @author Marin Kukovacec
 * @version 1.0
 */
public class MyTab {

	/**
	 * JTextArea is space for text
	 */
	private JTextArea text;
	
	/**
	 * pane holding all tabs of MyPanels
	 */
	private JTabbedPane pane;
	
	/**
	 * List of JLabels which are updated with this class
	 */
	private List<JLabel> labels;
	
	/**
	 * Map of actions which will be enabled/disabled when needed
	 */
	private Map<String,Action> actions;
	
	/**
	 * path to source file of text
	 */
	private Path filePath;
	
	/**
	 * Image icon that is present in every tab
	 */
	private ImageIcon icon;
	
	/**
	 * Image icon that is displayed when no changes are present
	 */
	private ImageIcon defaultIcon;
	
	/**
	 * Image icon that is displayed when changes are present
	 */
	private ImageIcon changeIcon;
	
	/**
	 * variable which indicates if change occured
	 */
	private boolean changed;
	
	/**
	 * provider which provides localized strings
	 */
	private ILocalizationProvider provider;

	/**
	 * Constructor for "blank document" MyTab.
	 * 
	 * @param actions is map of actions
	 * @param provider is localization provider
	 */
	public MyTab(Map<String, Action> actions, ILocalizationProvider provider){
		this(null, actions, provider);
	}
	
	/**
	 * Constructor for document which already exists.
	 * 
	 * @param filePath is path to that document
	 * @param actions is map of actions
	 * @param provider is localization provider
	 */
	public MyTab(Path filePath, Map<String, Action> actions, ILocalizationProvider provider){
		this.provider=provider;
		this.filePath=filePath;
		changed=false;
		text = new JTextArea();
		readTextArea();
		defaultIcon=createIcon("icons/info.png",this);
		labels = new ArrayList<>();
		this.actions = actions;
		
		changeIcon=createIcon("icons/icon.png",this);
		icon=defaultIcon;
		text.addKeyListener(new KeyAdapter(){
			 @Override
	            public void keyReleased(KeyEvent e) {
	                changed = true;
	                icon=changeIcon;
	                pane.setIconAt(pane.getSelectedIndex(), icon);
	                if (labels.size()>0){
	                	JLabel label = labels.get(0);
	                	label.setText("Length: "+text.getText().length());
	                }
	                actions.get("Save").setEnabled(true);
	            }
		});
		
		//used to enable/disable some actions when no text is selected
		text.addCaretListener(new CaretListener(){

			@Override
			public void caretUpdate(CaretEvent e) {
				
				int linenum = 1;
				int columnnum = 1;
				int selected = 0;
				try{
					int caretposition = text.getCaretPosition();
					linenum = text.getLineOfOffset(caretposition);
					columnnum = caretposition-text.getLineStartOffset(linenum);
					linenum+=1;
					columnnum+=1;
					Caret caret = text.getCaret();
					if (caret.getDot()!=caret.getMark()){
						selected = Math.abs(caret.getDot()-caret.getMark());
					}
					if (selected>0){
						actions.get("Delete").setEnabled(true);
						actions.get("Copy").setEnabled(true);
						actions.get("Cut").setEnabled(true);
						actions.get("Invert case").setEnabled(true);
						actions.get("Upper case").setEnabled(true);
						actions.get("Lower case").setEnabled(true);
						actions.get("Ascending Sort").setEnabled(true);
						actions.get("Descending Sort").setEnabled(true);
						actions.get("Unique").setEnabled(true);
					}else{
						actions.get("Delete").setEnabled(false);
						actions.get("Copy").setEnabled(false);
						actions.get("Cut").setEnabled(false);
						actions.get("Invert case").setEnabled(false);
						actions.get("Upper case").setEnabled(false);
						actions.get("Lower case").setEnabled(false);
						actions.get("Ascending Sort").setEnabled(false);
						actions.get("Descending Sort").setEnabled(false);
						actions.get("Unique").setEnabled(false);
					}
				}catch (Exception e1){
					if (labels.size()>0){
						JLabel label1 = labels.get(1);
						JLabel label2 = labels.get(2);
						JLabel label3 = labels.get(3);
						label1.setText("Ln: "+linenum);
						label2.setText("Col: "+columnnum);
						label3.setText("Sel: "+selected);
					}
				}
				if (labels.size()>0){
					JLabel label1 = labels.get(1);
					JLabel label2 = labels.get(2);
					JLabel label3 = labels.get(3);
					label1.setText("Ln: "+linenum);
					label2.setText("Col: "+columnnum);
					label3.setText("Sel: "+selected);
				}
			}
		});
		
	}
	
	/**
	 * Getter for document path
	 * 
	 * @return path to document
	 */
	public Path getPath(){
		return this.filePath;
	}

	/**
	 * Setter for file path
	 * 
	 * @param filePath which will be set to document
	 */
	public void setFilePath(Path filePath){
		this.filePath=filePath;
	}
	
	/**
	 * Getter for JTextArea
	 * 
	 * @return JTextArea instance
	 */
	public JTextArea getText(){
		return this.text;
	}
	
	/**
	 * Getter for changed variable
	 * 
	 * @return changed variable value
	 */
	public boolean isChanged(){
		return this.changed;
	}
	
	/**
	 * Setter for tabbed pane
	 * 
	 * @param pane is JTabbedPane which will be set
	 */
	public void setTabbedPane(JTabbedPane pane){
		this.pane=pane;
	}
	
	/**
	 * Setter for list of JLabels which needs to be updated
	 * 
	 * @param labels is list of JLabels
	 */
	public void setLabels(List<JLabel> labels){
		this.labels=labels;
	}
	
	/**
	 * Method which reads text from document at given file path and sets
	 * this JAreaText to contain that text
	 */
	public void readTextArea() {
		if (this.filePath==null){
			return;
		}
		
		if (!Files.isReadable(filePath)){
			JOptionPane.showMessageDialog(
					text,
					String.format(provider.getString("filenotexist"), filePath.toAbsolutePath()),
					provider.getString("error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		byte[] okteti;
		try{
			okteti = Files.readAllBytes(filePath);
		}catch(Exception ex){
			JOptionPane.showMessageDialog(
					text, 
					provider.getString("readerror") + filePath.toAbsolutePath(),
					provider.getString("error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		String tekst = new String(okteti, StandardCharsets.UTF_8);
		text.setText(tekst);
		
	}
	
	/**
	 * 
	 */
	public void save(){
		
		byte[] podaci = text.getText().getBytes(StandardCharsets.UTF_8);
		
		try{
			Files.write(filePath, podaci);
		}catch(IOException e1){
			JOptionPane.showMessageDialog(
					text,
					provider.getString("writeerror")+filePath,
					provider.getString("error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		JOptionPane.showMessageDialog(
				text,
				provider.getString("filesaved"),
				provider.getString("info"),
				JOptionPane.INFORMATION_MESSAGE);
		
		changed=false;
		icon=defaultIcon;
		pane.setIconAt(pane.getSelectedIndex(), icon);
		actions.get("Save").setEnabled(false);
	}

	/**
	 * Getter for currently set icon
	 * 
	 * @return icon
	 */
	public ImageIcon getIcon() {
		return icon;
	}
	
	/**
	 * Static method used to create ImageIcon from given path using given
	 * object
	 * 
	 * @param path is path to image
	 * @param o is object which is getting resource as stream
	 * @return image icon read from given file path using given object
	 */
	public static ImageIcon createIcon(String path, Object o){
		InputStream is = o.getClass().getResourceAsStream(path);
		
		if (is==null) return null;
		try {
			byte [] bytes = new byte[is.available()];
			is.read(bytes);
			is.close();
			return new ImageIcon(bytes);
		} catch (IOException e) {
			return null;
		}
		
	}
	
}
