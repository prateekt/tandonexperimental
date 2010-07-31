package release.DBRecognizer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.*;

import release.DBLoader.DefaultUserSetting;
import release.DBLoader.FileBasedMelodyDB;
import release.DBLoader.UserSetting;
import behavior.classifier.DistanceBasedClassifier;
import api.matlab.MatlabControlUtil;

public class DBRecognizerGUI extends JFrame implements ActionListener {
	
	public static final String RECORD_DIR = "db/db_unlabeled_clips/";
	public static final String CHROMA_CLASSIFY_MATLAB_FILE = "classify.m";
	public static int RECORD_TIME=10;
	
	//components
	private JLabel statusLabel;
	private JTextField lastRecField;
	private JButton statButton;
	private DistanceBasedClassifier classifier;
	private UserSetting settings;
	private Time globalTime;
	private List<RecognitionEvent> events;
	private TableDialog tableDialog;
	
	public DBRecognizerGUI(UserSetting settings) {
		super("Melody Recognizer v1.0");
		this.settings = settings;
		if(this.settings==null) {
			this.settings = new DefaultUserSetting();
		}
		classifier = new DistanceBasedClassifier();
		globalTime = new Time();
		events = new ArrayList<RecognitionEvent>();
		tableDialog = new TableDialog();
		tableDialog.setLocation(100, 100);
		
		statusLabel = new JLabel();
		statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lastRecField = new JTextField(50);
		lastRecField.setEnabled(false);
		statButton = new JButton("Show Statistics Table");
		statButton.addActionListener(this);
		
		getContentPane().setLayout(new GridLayout(1,3));
		getContentPane().add(statusLabel);
		getContentPane().add(lastRecField);
		getContentPane().add(statButton);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600,50);
		setVisible(true);
		this.setAlwaysOnTop(true);
		startup();
		
		//start threads
		LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
		RecordThread micT = new RecordThread(queue);
		RecognizerThread recT = new RecognizerThread(queue,this);
		Thread t1 = new Thread(micT);
		Thread t2 = new Thread(recT);
		t1.start();
		t2.start();		
	}
			
	public void startup() {		
		statusLabel.setText("Starting up...");
		
		//load matlab
		MatlabControlUtil.getMatlab();
		
		//train classifier
		FileBasedMelodyDB db = new FileBasedMelodyDB(settings.getDbFile());
		classifier.setMelodies(db.getMelodies());
		statusLabel.setText("Doing first record...");
	}
	
	public static void main(String[] args) {
		new DBRecognizerGUI(null);
	}

	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o==statButton) {
			tableDialog.setVisible(true);
		}		
	}

	/**
	 * @return the lastRecField
	 */
	public JTextField getLastRecField() {
		return lastRecField;
	}

	/**
	 * @param lastRecField the lastRecField to set
	 */
	public void setLastRecField(JTextField lastRecField) {
		this.lastRecField = lastRecField;
	}

	/**
	 * @return the statusLabel
	 */
	public JLabel getStatusLabel() {
		return statusLabel;
	}

	/**
	 * @return the classifier
	 */
	public DistanceBasedClassifier getClassifier() {
		return classifier;
	}

	/**
	 * @return the globalTime
	 */
	public Time getGlobalTime() {
		return globalTime;
	}

	/**
	 * @return the statButton
	 */
	public JButton getStatButton() {
		return statButton;
	}

	/**
	 * @return the settings
	 */
	public UserSetting getSettings() {
		return settings;
	}

	/**
	 * @return the events
	 */
	public List<RecognitionEvent> getEvents() {
		return events;
	}

	/**
	 * @return the tableDialog
	 */
	public TableDialog getTableDialog() {
		return tableDialog;
	}
}
