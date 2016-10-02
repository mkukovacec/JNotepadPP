package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.text.Caret;

/**
 * Class which represents each tab that is displayed in JTabbedPane. This pane
 * is consisted of JTextArea in middle and "status bar" at bottom which
 * calculates text length, line, column and number of selected numbers. It also
 * contains clock which displays time at bottom right corner.
 * 
 * @author Marin Kukovacec
 * @version 1.0
 */
public class MyPanel extends JPanel {
	
	/**
	 * generated serial version UID
	 */
	private static final long serialVersionUID = -5654572524122610770L;

	/**
	 * list of JLabels that are updated by MyTab
	 */
	private List<JLabel> labels;
	
	/**
	 * Constructor which initializes this Panel with JTextArea and MyClock
	 * instances. Those instances are added to this Panel.
	 * 
	 * @param text is JTextArea which will be added to this panel
	 * @param clock is MyClock which will be added to this panels statusbar
	 */
	public MyPanel(JTextArea text, MyClock clock) {
		super(new BorderLayout());
		labels = new ArrayList<>();
		super.add(new JScrollPane(text), BorderLayout.CENTER);
		JLabel length = new JLabel("length: " + text.getText().length());
		length.setHorizontalAlignment(SwingConstants.LEFT);
		
		char[] txt = text.getText().toCharArray();
		int lns = 1;
		for (int i = 0; i < txt.length; i++) {
			if (txt[i] == Character.LINE_SEPARATOR)
				lns++;
		}
		
		JLabel ln = new JLabel("Ln: " + lns);
		length.setHorizontalAlignment(SwingConstants.LEFT);
		JLabel col = new JLabel("Col: " + text.getCaretPosition());
		Caret car = text.getCaret();
		JLabel sel = new JLabel("Sel: " + Math.abs(car.getMark() - car.getDot()));
		ln.setHorizontalAlignment(SwingConstants.RIGHT);
		col.setHorizontalAlignment(SwingConstants.RIGHT);
		sel.setHorizontalAlignment(SwingConstants.RIGHT);

		JPanel subpanel = new JPanel(new GridLayout(1, 3));
		JPanel statusbar = new JPanel(new GridLayout(1, 3));

		subpanel.add(ln);
		subpanel.add(col);
		subpanel.add(sel);

		JLabel labela = new JLabel(clock.time);
		labela.setHorizontalAlignment(SwingConstants.RIGHT);
		clock.addLabel(labela);

		super.add(statusbar, BorderLayout.PAGE_END);
		statusbar.add(length);
		statusbar.add(subpanel);
		statusbar.add(labela);

		labels.add(length);
		labels.add(ln);
		labels.add(col);
		labels.add(sel);
	}

	/**
	 * Getter for list of JLabels which are updated with MyTab
	 * 
	 * @return list of JLabels
	 */
	public List<JLabel> getLabels() {
		return labels;
	}
	
}