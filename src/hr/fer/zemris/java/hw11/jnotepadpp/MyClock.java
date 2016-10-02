package hr.fer.zemris.java.hw11.jnotepadpp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 * Class which represents simple clock which will be displayed at each tab in
 * JNotepadPP. It displays date and time in format yyyy/MM/dd HH:mm:ss.
 * 
 * @author Marin Kukovacec
 * @version 1.0
 */
public class MyClock {

	/**
	 * time which is displayed and constantly updated
	 */
	volatile String time;

	/**
	 * boolean which controls if daemon thread should stop
	 */
	volatile boolean stopRequested;
	
	/**
	 * format of string
	 */
	private DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	/**
	 * List of JLabels that need to be updated
	 */
	private List<JLabel> list = new ArrayList<>();
	
	/**
	 * Constructor which initializes and activates this clock by creating one
	 * daemon thread which updates string time
	 */
	public MyClock(){
		updateTime();
		
		Thread t = new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(500);
				} catch (Exception ex) {
				}
				if (stopRequested)
					break;
				SwingUtilities.invokeLater(() -> {
					updateTime();
				});
			}
		});

		t.setDaemon(true);
		t.start();
	}
	
	/**
	 * Method which updates time and all JLabels in list
	 */
	public void updateTime() {
		Calendar cal = Calendar.getInstance();
		time = formatter.format(cal.getTime());
		for (JLabel label : list) {
			label.setText(time);
		}
	}

	/**
	 * Method which stops
	 */
	public void stop() {
		stopRequested = true;
	}
	
	/**
	 * Method which adds given JLabel to update list
	 * 
	 * @param label which will be added to update list
	 */
	public void addLabel(JLabel label){
		list.add(label);
	}
	
}
