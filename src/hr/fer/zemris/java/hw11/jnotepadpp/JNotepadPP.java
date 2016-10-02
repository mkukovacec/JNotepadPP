package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadpp.local.*;

/**
 * Class which is implementation of improved version of notepad. JNotepadPP
 * allows user to have multiple tabs open and work with them. It supports
 * three languages which set UI names. It is consisted of menu bar, tabs,
 * JTextArea and statusbar with clock on it. It allows user to open and save
 * file. It support various editing operations as well as sorting operations.
 * 
 * @author Marin Kukovacec
 * @version 1.0
 */
public class JNotepadPP extends JFrame {

	/** serial verison UID */
	private static final long serialVersionUID = 1L;
	
	/** List of MyTabs used */
	private List<MyTab> tabs;
	
	/** map of actions */
	private Map<String, Action> actions;
	
	/** JTabbedPane which holds tabs */
	private JTabbedPane tabbedPane;

	/** String builder used to change title when needed */
	private StringBuilder titleBuilder = new StringBuilder();

	/** current MyTab opened */
	private MyTab currentTab;

	/** MyClock instance which is used */
	private final MyClock clock = new MyClock();

	/** counter which counts number of blank documents open */
	private int counter;

	/** provider used to localize strings */
	private ILocalizationProvider provider = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);

	/** icon of new file */
	private ImageIcon newFile = MyTab.createIcon("icons/new.png", this);
	/** icon of open file */
	private ImageIcon open = MyTab.createIcon("icons/open.png", this);
	/** icon of save file */
	private ImageIcon save = MyTab.createIcon("icons/save.png", this);
	/** icon of saveas file */
	private ImageIcon saveAs = MyTab.createIcon("icons/saveas.png", this);
	/** icon of close file */
	private ImageIcon close = MyTab.createIcon("icons/close.png", this);
	/** icon of stats */
	private ImageIcon stats = MyTab.createIcon("icons/stats.png", this);
	/** icon of copy */
	private ImageIcon copy = MyTab.createIcon("icons/copy.png", this);
	/** icon of cut */
	private ImageIcon cut = MyTab.createIcon("icons/cut.png", this);
	/** icon of paste operation */
	private ImageIcon paste = MyTab.createIcon("icons/paste.png", this);
	/** icon of delete operation */
	private ImageIcon delete = MyTab.createIcon("icons/delete.png", this);
	/** icon of exit application */
	private ImageIcon exit = MyTab.createIcon("icons/exit.png", this);
	/** /** icon of toUpper operation */
	private ImageIcon toUpper = MyTab.createIcon("icons/uppercase.png", this);
	/** icon of toLower operation */
	private ImageIcon toLower = MyTab.createIcon("icons/lowercase.png", this);
	/** icon of invert case */
	private ImageIcon invert = MyTab.createIcon("icons/invertcase.png", this);
	/** icon of croatian flag */
	private ImageIcon hr = MyTab.createIcon("icons/hr.png", this);
	/** icon of english flag */
	private ImageIcon en = MyTab.createIcon("icons/en.png", this);
	/** icon of german flag */
	private ImageIcon de = MyTab.createIcon("icons/de.png", this);

	/**
	 * Constructor which initializes this class. It prevents application from
	 * closing when close operation is closed and it activates our custom exit
	 * operation which will check if some files need to be saved. It calls
	 * initGUI to initialize graphical user interface
	 */
	public JNotepadPP() {
		super();
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setLocation(0, 0);
		setSize(640, 480);

		titleBuilder = new StringBuilder();
		counter = 1;
		currentTab = null;

		initGUI();

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				exitAction.actionPerformed(null);
			}

		});

	}

	/**
	 * Method which initializes graphical user interface and sets listeners
	 * to tabbed pane
	 */
	private void initGUI() {

		tabs = new ArrayList<MyTab>();
		actions = new HashMap<>();

		tabbedPane = new JTabbedPane();
		setLayout(new BorderLayout());
		this.getContentPane().add(tabbedPane);

		createActions();
		createMenus();
		createToolbars();

		tabbedPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				int index = tabbedPane.getSelectedIndex();

				if (index != -1) {
					refreshTitle(index);

				} else {
					setTitle("JNotepadPP");
				}
				if (tabs.size() == 0) {
					saveDocumentAction.setEnabled(false);
					saveAsDocumentAction.setEnabled(false);
					closeTabAction.setEnabled(false);
					statsAction.setEnabled(false);
					pasteSelectedPartAction.setEnabled(false);
				} else {
					saveDocumentAction.setEnabled(true);
					saveAsDocumentAction.setEnabled(true);
					closeTabAction.setEnabled(true);
					statsAction.setEnabled(true);
					pasteSelectedPartAction.setEnabled(true);
					if (tabs.get(index).isChanged()) {
						saveDocumentAction.setEnabled(true);
					} else {
						saveDocumentAction.setEnabled(false);
					}
				}

			}
		});

		newDocumentAction.actionPerformed(null);
	}

	/**
	 * Method used to generate new title of this JFrame
	 * @param index
	 */
	private void refreshTitle(int index) {
		currentTab = tabs.get(index);
		titleBuilder.setLength(0);
		if (tabs.get(index).getPath() != null) {
			titleBuilder.append(tabs.get(index).getPath());
		} else {
			titleBuilder.append(tabbedPane.getTitleAt(index));
		}
		setTitle(titleBuilder.toString() + " - JNotepad++");
	}

	/**
	 * Action for creating new blank document
	 */
	private Action newDocumentAction = new LocalizableAction("newaction", provider) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			MyTab tab = new MyTab(actions, provider);
			tabs.add(tab);
			MyPanel panel = new MyPanel(tab.getText(), clock);
			tabbedPane.addTab("Untitled " + (counter++), tab.getIcon(), panel,
					(tab.getPath() == null) ? "" : tab.getPath().toString());
			tabbedPane.setSelectedIndex(tabs.size() - 1);
			tab.setTabbedPane(tabbedPane);
			tab.setLabels(panel.getLabels());
		}

	};

	/**
	 * Action for opening of document from file path
	 */
	private Action openDocumentAction = new LocalizableAction("openaction", provider) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Open file");
			if (fc.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}

			File fileName = fc.getSelectedFile();

			for (int i = 0; i < tabs.size(); i++) {
				MyTab tab = tabs.get(i);
				if (fileName.toPath().equals(tab.getPath())) {
					tabbedPane.setSelectedIndex(i);
					return;
				}
			}

			MyTab tab = new MyTab(fileName.toPath(), actions, provider);

			tabs.add(tab);
			MyPanel panel = new MyPanel(tab.getText(), clock);
			tabbedPane.addTab(fileName.getName(), tab.getIcon(), panel, tab.getPath().toString());
			tabbedPane.setIconAt(tabs.size() - 1, tab.getIcon());
			tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
			tab.setTabbedPane(tabbedPane);
			tab.setLabels(panel.getLabels());
		}
	};

	/**
	 * Action for saving currently opened tab
	 */
	private Action saveDocumentAction = new LocalizableAction("saveaction", provider) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (currentTab.getPath() == null) {
				saveAsDocumentAction.actionPerformed(e);
				return;
			}

			currentTab.save();
		}
	};

	/**
	 * Action for saving currently opened tab in format which user wants
	 */
	private Action saveAsDocumentAction = new LocalizableAction("saveasaction", provider) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc = new JFileChooser();
			jfc.setDialogTitle("Save document");
			if (jfc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(JNotepadPP.this, provider.getString("nothingsavedmess"),
						provider.getString("warning"), JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (jfc.getSelectedFile().exists()) {
				int desicion = JOptionPane.showConfirmDialog(JNotepadPP.this,
						jfc.getSelectedFile().getName() + provider.getString("fileexistsmess"), "system message",
						JOptionPane.YES_NO_OPTION);
				if (desicion != JOptionPane.YES_OPTION) {
					JOptionPane.showMessageDialog(JNotepadPP.this, provider.getString("nothingsavedmess"),
							provider.getString("warning"), JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
			currentTab.setFilePath(jfc.getSelectedFile().toPath());
			tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), jfc.getSelectedFile().getName());
			refreshTitle(tabbedPane.getSelectedIndex());
			currentTab.save();
		}
	};

	/**
	 * Action used to close currently opened tab
	 */
	private Action closeTabAction = new LocalizableAction("close", provider) {

		/**
		 * default ID
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			int index = tabbedPane.getSelectedIndex();

			if (currentTab.isChanged()) {
				int desicion = JOptionPane.showConfirmDialog(JNotepadPP.this, provider.getString("changesexistsmess"),
						"System message", JOptionPane.YES_NO_OPTION);
				if (desicion == JOptionPane.YES_OPTION) {
					if (currentTab.getPath() == null) {
						saveAsDocumentAction.actionPerformed(e);
					} else {
						currentTab.save();
					}
				}
			}

			if (tabs.size() <= 1) {
				currentTab = null;
			} else {
				int temp = (index + 1) % tabs.size();
				currentTab = tabs.get(temp);
				tabbedPane.setSelectedIndex(temp);
			}
			tabs.remove(index);
			tabbedPane.removeTabAt(index);

		}

	};

	/**
	 * Action used to exit application and offer user an option to save his
	 * unsaved work before exiting
	 */
	private Action exitAction = new LocalizableAction("exit", provider) {

		/**
		 * default ID
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean change = false;
			for (MyTab tab : tabs) {
				if (tab.isChanged()) {
					change = true;
				}
			}
			if (change) {
				int desicion = JOptionPane.showConfirmDialog(JNotepadPP.this, provider.getString("changesexistsmess"),
						"System message", JOptionPane.YES_NO_CANCEL_OPTION);
				if (desicion == JOptionPane.YES_OPTION) {
					for (MyTab tab : tabs) {
						if (tab.isChanged()) {
							if (tab.getPath() == null) {
								tabbedPane.setSelectedIndex(tabs.indexOf(tab));
								currentTab = tab;
								saveAsDocumentAction.actionPerformed(e);
							} else {
								tab.save();
							}
						}
					}
				} else if (desicion == JOptionPane.CANCEL_OPTION) {
					return;
				}
				// else dispose or exit
			}
			clock.stop();
			dispose();

		}
	};

	/**
	 * Action used to display currently opened tab statistics
	 */
	private Action statsAction = new LocalizableAction("stats", provider) {

		/**
		 * Default UID
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			char[] text = currentTab.getText().getText().toCharArray();
			int lines = 0;
			int nonBlank = 0;
			if (text.length > 0) {
				for (int i = 0; i < text.length; i++) {
					if (!Character.isWhitespace(text[i])) {
						nonBlank++;
					}
					if (text[i] == '\n') {
						lines++;
					}
				}
				lines += 1;
			}
			JOptionPane.showMessageDialog(JNotepadPP.this,
					String.format(provider.getString("statsmess"), text.length, nonBlank, lines));
		}

	};

	/**
	 * Action used to copy selected text
	 */
	private Action copySelectedPartAction = new LocalizableAction("copyaction", provider) {

		/**
		 * Default UID
		 */
		private static final long serialVersionUID = 1L;
		private Action defaultAction = new DefaultEditorKit.CopyAction();

		@Override
		public void actionPerformed(ActionEvent e) {
			defaultAction.actionPerformed(e);
		}

	};

	/**
	 * Action used to cut selected part
	 */
	private Action cutSelectedPartAction = new LocalizableAction("cutaction", provider) {

		/**
		 * default UID
		 */
		private static final long serialVersionUID = 1L;
		private Action defaultAction = new DefaultEditorKit.CutAction();

		@Override
		public void actionPerformed(ActionEvent e) {
			defaultAction.actionPerformed(e);
		}

	};

	/**
	 * Action used to paste already copied content
	 */
	private Action pasteSelectedPartAction = new LocalizableAction("pasteaction", provider) {

		/**
		 * default uid
		 */
		private static final long serialVersionUID = 1L;
		private Action defaultAction = new DefaultEditorKit.PasteAction();

		@Override
		public void actionPerformed(ActionEvent e) {
			defaultAction.actionPerformed(e);
		}

	};

	/**
	 * Action which deletes selected part of text
	 */
	private Action deleteSelectedPartAction = new LocalizableAction("deleteaction", provider) {

		/**
		 * default version UID
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Document doc = currentTab.getText().getDocument();

			int len = Math.abs(currentTab.getText().getCaret().getDot() - currentTab.getText().getCaret().getMark());
			int offset = Math.min(currentTab.getText().getCaret().getDot(), currentTab.getText().getCaret().getMark());
			try {
				doc.remove(offset, len);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}

	};

	/**
	 * Action used to invert case in selected text
	 */
	private Action invertCaseAction = new LocalizableAction("invertcase", provider) {

		/**
		 * default id
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			caseSwitch(t -> t);
		}

	};

	/**
	 * Action used to change case of selected text to upper case
	 */
	private Action toUpperCaseAction = new LocalizableAction("uppercase", provider) {

		/**
		 * default id
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

			caseSwitch(t -> t.toUpperCase());
		}

	};

	/**
	 * Action used to change case of selected text to lower case
	 */
	private Action toLowerCaseAction = new LocalizableAction("lowercase", provider) {

		/**
		 * default uid
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

			caseSwitch(t -> t.toLowerCase());
		}

	};

	/**
	 * Method used to perform given operation as parameter
	 * @param func is function which will be performed over selected text
	 */
	private void caseSwitch(Function<String, String> func) {
		Document doc = currentTab.getText().getDocument();

		int len = Math.abs(currentTab.getText().getCaret().getDot() - currentTab.getText().getCaret().getMark());

		int offset = Math.min(currentTab.getText().getCaret().getDot(), currentTab.getText().getCaret().getMark());

		try {
			String text = doc.getText(offset, len);
			text = changeCase(text);
			text = func.apply(text);
			doc.remove(offset, len);
			doc.insertString(offset, text, null);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Method which changes case of given string and returns it
	 * @param text is text which case will be changed
	 * @return new string with inverted cases
	 */
	private String changeCase(String text) {
		char[] znakovi = text.toCharArray();
		for (int i = 0; i < znakovi.length; i++) {
			char c = znakovi[i];
			if (Character.isLowerCase(c)) {
				znakovi[i] = Character.toUpperCase(c);
			} else if (Character.isUpperCase(c)) {
				znakovi[i] = Character.toLowerCase(c);
			}
		}
		return new String(znakovi);
	}

	/**
	 * Action which sorts selected lines ascending
	 */
	private Action ascendingSortAction = new LocalizableAction("ascsort", provider) {

		/**
		 * default id
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

			actionImpl(new Function<List<String>, List<String>>() {

				@Override
				public List<String> apply(List<String> t) {
					t.sort((s, r) -> s.compareTo(r));
					return t;
				}

			});
		}

	};

	/**
	 * Action which sorts selected lines descending
	 */
	private Action descendingSortAction = new LocalizableAction("descsort", provider) {

		/**
		 * default you know what
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

			actionImpl(new Function<List<String>, List<String>>() {

				@Override
				public List<String> apply(List<String> t) {
					t.sort((s, r) -> -s.compareTo(r));
					return t;
				}

			});
		}

	};

	/**
	 * Action used to remove line duplicates from selected lines
	 */
	private Action uniqueAction = new LocalizableAction("unique", provider) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

			actionImpl(new Function<List<String>, List<String>>() {

				@Override
				public List<String> apply(List<String> t) {
					Set<String> set = new LinkedHashSet<>(t);
					t = set.stream().collect(Collectors.toList());
					return t;
				}

			});
		}

	};

	/**
	 * Implementation of action for methods of sorting and uniqueness
	 * @param func is function which will be done
	 */
	private void actionImpl(Function<List<String>, List<String>> func) {
		Document doc = currentTab.getText().getDocument();

		int offset = Math.min(currentTab.getText().getCaret().getDot(), currentTab.getText().getCaret().getMark());
		int endset = Math.max(currentTab.getText().getCaret().getDot(), currentTab.getText().getCaret().getMark());

		try {
			offset = currentTab.getText().getLineStartOffset(currentTab.getText().getLineOfOffset(offset));
			endset = currentTab.getText().getLineEndOffset(currentTab.getText().getLineOfOffset(endset));
			String text = doc.getText(offset, endset - offset);
			String[] splitted = text.split("\\r?\\n");
			List<String> list = new ArrayList<>();
			for (String str : splitted) {
				list.add(str);
			}
			list = func.apply(list);
			doc.remove(offset, endset - offset);
			StringBuilder builder = new StringBuilder();
			for (String str : list) {
				builder.append(str + '\n');
			}
			doc.insertString(offset, builder.toString(), null);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Action which sets language to croatian
	 */
	private Action hrLanguageAction = new LocalizableAction("hr", provider) {

		/**
		 * default id
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("hr");
		}

	};

	/**
	 * Action which sets language to english
	 */
	private Action enLanguageAction = new LocalizableAction("en", provider) {

		/**
		 * default id
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("en");
		}

	};

	/**
	 * Action which sets language to german
	 */
	private Action deLanguageAction = new LocalizableAction("de", provider) {

		/**
		 * default id
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("de");
		}

	};

	/**
	 * Method which defines accelerator key and mnemonic key for each action,
	 * and put those actions in map of actions
	 */
	private void createActions() {

		newDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
		newDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);

		openDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
		openDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);

		saveDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		saveDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);

		saveAsDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift S"));
		saveAsDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);

		copySelectedPartAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
		copySelectedPartAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);

		cutSelectedPartAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
		cutSelectedPartAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);

		pasteSelectedPartAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
		pasteSelectedPartAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_V);

		deleteSelectedPartAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control D"));
		deleteSelectedPartAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);

		invertCaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F3"));
		invertCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);

		toUpperCaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control U"));
		toUpperCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);

		toLowerCaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control L"));
		toLowerCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);

		ascendingSortAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F"));
		ascendingSortAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F);

		descendingSortAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift F"));
		descendingSortAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F);

		uniqueAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control B"));
		uniqueAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_B);

		statsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control H"));
		statsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_H);

		closeTabAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control W"));
		closeTabAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_W);

		exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q"));
		exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Q);

		actions.put("New", newDocumentAction);
		actions.put("Open", openDocumentAction);
		actions.put("Save", saveDocumentAction);
		actions.put("Save as", saveAsDocumentAction);
		actions.put("Copy", copySelectedPartAction);
		actions.put("Cut", cutSelectedPartAction);
		actions.put("Paste", pasteSelectedPartAction);
		actions.put("Delete", deleteSelectedPartAction);
		actions.put("Invert case", invertCaseAction);
		actions.put("Upper case", toUpperCaseAction);
		actions.put("Lower case", toLowerCaseAction);
		actions.put("Ascending Sort", ascendingSortAction);
		actions.put("Descending Sort", descendingSortAction);
		actions.put("Unique", uniqueAction);
		actions.put("Stats", statsAction);
		actions.put("Close", closeTabAction);
		actions.put("Exit", exitAction);
		actions.put("hrlang", hrLanguageAction);
		actions.put("enlang", enLanguageAction);
		actions.put("delang", deLanguageAction);

		closeTabAction.setEnabled(false);
		statsAction.setEnabled(false);
		uniqueAction.setEnabled(false);
		descendingSortAction.setEnabled(false);
		ascendingSortAction.setEnabled(false);
		toLowerCaseAction.setEnabled(false);
		toUpperCaseAction.setEnabled(false);
		invertCaseAction.setEnabled(false);
		deleteSelectedPartAction.setEnabled(false);
		pasteSelectedPartAction.setEnabled(false);
		cutSelectedPartAction.setEnabled(false);
		copySelectedPartAction.setEnabled(false);
		saveAsDocumentAction.setEnabled(false);
		saveDocumentAction.setEnabled(false);

	}

	/**
	 * Method which creates menu of this JNotepadPP
	 */
	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new LJMenu("file", provider);
		menuBar.add(fileMenu);

		fileMenu.add(new JMenuItem(newDocumentAction));
		fileMenu.add(new JMenuItem(openDocumentAction));
		fileMenu.add(new JMenuItem(saveDocumentAction));
		fileMenu.add(new JMenuItem(saveAsDocumentAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(statsAction));
		fileMenu.add(new JMenuItem(closeTabAction));
		fileMenu.add(new JMenuItem(exitAction));

		JMenu editMenu = new LJMenu("edit", provider);
		menuBar.add(editMenu);

		editMenu.add(new JMenuItem(copySelectedPartAction));
		editMenu.add(new JMenuItem(cutSelectedPartAction));
		editMenu.add(new JMenuItem(pasteSelectedPartAction));
		editMenu.add(new JMenuItem(deleteSelectedPartAction));

		JMenu toolsMenu = new LJMenu("tools", provider);
		JMenu changeCaseMenu = new LJMenu("changecase", provider);

		toolsMenu.add(changeCaseMenu);
		menuBar.add(toolsMenu);
		changeCaseMenu.add(new JMenuItem(toUpperCaseAction));
		changeCaseMenu.add(new JMenuItem(toLowerCaseAction));
		changeCaseMenu.add(new JMenuItem(invertCaseAction));

		JMenu sortMenu = new LJMenu("sort", provider);
		sortMenu.add(new JMenuItem(ascendingSortAction));
		sortMenu.add(new JMenuItem(descendingSortAction));
		toolsMenu.add(sortMenu);
		toolsMenu.add(uniqueAction);

		JMenu langMenu = new LJMenu("languages", provider);
		langMenu.add(new JMenuItem(hrLanguageAction));
		langMenu.add(new JMenuItem(enLanguageAction));
		langMenu.add(new JMenuItem(deLanguageAction));

		menuBar.add(langMenu);
		this.setJMenuBar(menuBar);
	}

	/**
	 * Method which creates toolbar of this JNotepadPP
	 */
	private void createToolbars() {
		JToolBar toolBar = new LJToolBar("toolbar", provider);

		toolBar.setFloatable(true);

		toolBar.add(new LJButton(newDocumentAction, newFile, provider));
		toolBar.add(new LJButton(openDocumentAction, open, provider));
		toolBar.add(new LJButton(saveDocumentAction, save, provider));
		toolBar.add(new LJButton(saveAsDocumentAction, saveAs, provider));
		toolBar.add(new LJButton(statsAction, stats, provider));
		toolBar.add(new LJButton(closeTabAction, close, provider));
		toolBar.add(new LJButton(exitAction, exit, provider));
		toolBar.addSeparator();
		toolBar.add(new LJButton(copySelectedPartAction, copy, provider));
		toolBar.add(new LJButton(cutSelectedPartAction, cut, provider));
		toolBar.add(new LJButton(pasteSelectedPartAction, paste, provider));
		toolBar.add(new LJButton(deleteSelectedPartAction, delete, provider));
		toolBar.addSeparator();
		toolBar.add(new LJButton(toUpperCaseAction, toUpper, provider));
		toolBar.add(new LJButton(toLowerCaseAction, toLower, provider));
		toolBar.add(new LJButton(invertCaseAction, invert, provider));
		toolBar.addSeparator();
		toolBar.add(new LJButton(hrLanguageAction, hr, provider));
		toolBar.add(new LJButton(enLanguageAction, en, provider));
		toolBar.add(new LJButton(deLanguageAction, de, provider));

		this.getContentPane().add(toolBar, BorderLayout.PAGE_START);
	}

	/**
	 * Main method which starts when program is executed
	 * @param args are args from command line, none are needed
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new JNotepadPP().setVisible(true);
		});
	}

}
