package release.DBLoader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import util.sys.StringBuffer2;

public class SettingsDialog extends JFrame implements ActionListener {
	
	//components
	private JRadioButton singleButton, multiButton, allButton;
	private JTextField trackMinField, trackMaxField;
	private JTextField dbFileField;
	private JButton loadNewDBFileButton;
	private JCheckBox restSegBox, melSegBox;
	private JButton saveButton;
	private JFileChooser fileChooser;
	private UserSetting currentSetting;
	private static SettingsDialog instance;
		
	public static void showSettingsDialog(UserSetting s) {
		if(instance==null) {
			if(s==null) {
				s=new DefaultUserSetting();
			}
			instance = new SettingsDialog(s);
		}
		
		instance.setVisible(true);
	}
	
	protected void visSettings(UserSetting u) {
		if(u.getTrackSetting()==UserSetting.SINGLE_TRACK) {
			singleButton.setSelected(true);
			trackMaxField.setEnabled(false);
			trackMinField.setText(""+u.getMinTrack());
			trackMaxField.setText("");
		}
		if(u.getTrackSetting()==UserSetting.MULTI_TRACK) {
			multiButton.setSelected(true);
			trackMinField.setText(""+u.getMinTrack());
			trackMaxField.setText(""+u.getMaxTrack());
		}
		if(u.getTrackSetting()==UserSetting.ALL_TRACK) {
			allButton.setSelected(true);
			trackMaxField.setEnabled(false);
			trackMinField.setEnabled(false);
			trackMinField.setText("MIN_TRACK");
			trackMaxField.setText("MAX_TRACK");
		}
		dbFileField.setText(u.getDbFile());
		boolean[] algorithms = u.getAlgorithms();
		if(algorithms[UserSetting.REST_BASED_ALGORITHM])
			restSegBox.setSelected(true);
		if(algorithms[UserSetting.MELODIC_STRUCTURE_ALGORITHM])
			melSegBox.setSelected(true);
	}
	
	protected SettingsDialog(UserSetting s) {
		super("Change Settings");
		currentSetting = s;
		
		fileChooser = new JFileChooser();
		singleButton = new JRadioButton("Single track");
		multiButton = new JRadioButton("Multiple track");
		allButton = new JRadioButton("All tracks");
		trackMinField = new JTextField();
		trackMaxField = new JTextField();
		ButtonGroup g = new ButtonGroup();
		g.add(singleButton);
		g.add(multiButton);
		g.add(allButton);
		dbFileField = new JTextField(20);
		dbFileField.setEnabled(false);
		loadNewDBFileButton = new JButton("Change Database File Location");
		restSegBox = new JCheckBox("Musical Rest Based Segmentation");
		melSegBox = new JCheckBox("Note Structure Based Segmentation");
		saveButton = new JButton("Save New Settings");
		singleButton.addActionListener(this);
		multiButton.addActionListener(this);
		allButton.addActionListener(this);
		loadNewDBFileButton.addActionListener(this);
		saveButton.addActionListener(this);
		
		//prepare button panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2,4));
		buttonPanel.add(new JLabel("Tracks to extract: "));
		buttonPanel.add(singleButton);
		buttonPanel.add(multiButton);
		buttonPanel.add(allButton);
		JLabel l1 = new JLabel("Extract from track");
		l1.setAlignmentX(CENTER_ALIGNMENT);
		buttonPanel.add(l1);
		buttonPanel.add(trackMinField);
		JLabel l2 = new JLabel("     to track");
		l2.setAlignmentX(CENTER_ALIGNMENT);
		buttonPanel.add(l2);
		buttonPanel.add(trackMaxField);
		TitledBorder title;
		title = BorderFactory.createTitledBorder("Which tracks should be extracted from a tab?");
		buttonPanel.setBorder(title);

		//DB File Panel
		JPanel dbFilePanel = new JPanel();
		dbFilePanel.setLayout(new GridLayout(1,2));
		dbFilePanel.add(dbFileField);
		dbFilePanel.add(loadNewDBFileButton);
		TitledBorder title2;
		title2 = BorderFactory.createTitledBorder("Set Database File Location");
		dbFilePanel.setBorder(title2);
		
		//Algorithm
		JPanel algoPanel = new JPanel();
		algoPanel.setLayout(new GridLayout(0,2));
		algoPanel.add(restSegBox);
		algoPanel.add(melSegBox);
		TitledBorder title3;
		title3 = BorderFactory.createTitledBorder("Select Extraction Algorithm(s) to apply");
		algoPanel.setBorder(title3);
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill=GridBagConstraints.VERTICAL;
		c.gridx=0;
		c.gridy=0;
		c.ipadx=100;
		this.getContentPane().add(buttonPanel,c);
		c.fill=GridBagConstraints.VERTICAL;
		c.gridx=0;
		c.gridy=1;
		c.ipadx=110;
		this.getContentPane().add(dbFilePanel,c);
		c.fill=GridBagConstraints.VERTICAL;
		c.gridx=0;
		c.gridy=2;
		c.ipadx=100;
		this.getContentPane().add(algoPanel,c);	
		c.fill=GridBagConstraints.VERTICAL;
		c.gridx=0;
		c.gridy=3;
		c.ipadx=100;
		this.getContentPane().add(saveButton,c);
		
		this.setResizable(false);
		visSettings(s);
		setSize(600, 235);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o==singleButton) {
			singleButtonSelected();
		}
		if(o==multiButton) {
			multiButtonSelected();
		}
		if(o==allButton) {
			allButtonSelected();
		}
		if(o==loadNewDBFileButton) {
			loadNewDBFileButtonPressed();
		}
		if(o==saveButton) {
			saveButtonPressed();
		}
		
	}
	
	public void singleButtonSelected() {
		trackMinField.setText("");
		trackMaxField.setText("");
		trackMaxField.setEnabled(false);
		trackMinField.setEnabled(true);
	}
	
	public void multiButtonSelected() {
		trackMaxField.setText("");
		trackMinField.setText("");
		trackMaxField.setEnabled(true);
		trackMinField.setEnabled(true);
	}
	
	public void allButtonSelected() {
		trackMinField.setText("MIN_TRACK");
		trackMaxField.setText("MAX_TRACK");
		trackMaxField.setEnabled(false);
		trackMinField.setEnabled(false);
	}
	
	public void loadNewDBFileButtonPressed() {
		int rtn = fileChooser.showOpenDialog(this);
		
		//return if file not chosen
		if (rtn != JFileChooser.APPROVE_OPTION)
        	return;
		
		//get file
        File file = fileChooser.getSelectedFile();
        
        String path = file.getAbsolutePath();
        dbFileField.setText(path);
	}
	
	public void saveButtonPressed() {
		
		//parse track setting
		int trackSetting=-1;
		if(singleButton.isSelected()) {
			trackSetting = UserSetting.SINGLE_TRACK;
		}
		if(multiButton.isSelected()) {
			trackSetting = UserSetting.MULTI_TRACK;
		}
		if(allButton.isSelected()) {
			trackSetting = UserSetting.ALL_TRACK;
		}
		
		//parse min field
		boolean minTrackFieldOkay=false;
		String minTrack = trackMinField.getText();
		int minTrackParsed=-1;
		if(singleButton.isSelected()) {
			try {
				minTrackParsed = Integer.parseInt(minTrack);
				minTrackFieldOkay = true;
			}
			catch(Exception e) {
				minTrackFieldOkay = false;
			}
		}
		if(multiButton.isSelected()) {
			try {
				minTrackParsed = Integer.parseInt(minTrack);
				minTrackFieldOkay = true;
			}
			catch(Exception e) {
				minTrackFieldOkay = false;
			}
		}
		if(allButton.isSelected()) {
			minTrackFieldOkay = true;
		}
		
		//parse max field
		boolean maxTrackFieldOkay=false;
		String maxTrack = trackMaxField.getText();
		int maxTrackParsed=-1;
		if(singleButton.isSelected()) {
			maxTrackFieldOkay = true;
		}
		if(multiButton.isSelected()) {
			try {
				maxTrackParsed = Integer.parseInt(maxTrack);
				if(minTrackFieldOkay && maxTrackParsed > minTrackParsed)
					maxTrackFieldOkay = true;
			}
			catch(Exception e){
				maxTrackFieldOkay=false;
			}
		}
		if(allButton.isSelected()) {
			maxTrackFieldOkay = true;
		}
		
		//parse db file
		boolean dbFileFieldOkay = false;
		String dbFile = dbFileField.getText();
		if(!dbFile.trim().equals(""))
			dbFileFieldOkay = true;
		
		//parse algorithm
		boolean algorithmOkay=false;
		boolean[] algorithm = new boolean[2];
		boolean restSegChosen = restSegBox.isSelected();
		boolean melSegChosen = melSegBox.isSelected();
		if(restSegChosen || melSegChosen) {
			algorithmOkay=true;
			if(restSegChosen) {
				algorithm[UserSetting.REST_BASED_ALGORITHM] = true;
			}
			if(melSegChosen) {
				algorithm[UserSetting.MELODIC_STRUCTURE_ALGORITHM] = true;
			}
		}
		
		//get errors
		StringBuffer2 errors = new StringBuffer2();
		if(!minTrackFieldOkay)
			errors.appendN("Minimum Track field must be a valid integer.");
		if(!maxTrackFieldOkay)
			errors.appendN("Maximum Track field must be a valid integer greater than minimum track.");
		if(!dbFileFieldOkay)
			errors.appendN("Please choose a database file.");
		if(!algorithmOkay)
			errors.appendN("Please choose at least one algorithm.");
		
		boolean formOkay = errors.toString().length()==0;
		
		if(formOkay) {
			UserSetting s = new UserSetting();
			s.setTrackSetting(trackSetting);
			s.setMinTrack(minTrackParsed);
			s.setMaxTrack(maxTrackParsed);
			s.setDbFile(dbFile);
			s.setAlgorithms(algorithm);
			UserSettingSaver.saveUserSetting(UserSettingSaver.USER_SETTINGS_LOC, s);
			JOptionPane.showMessageDialog(this, "Settings sucessfully saved.", "Success", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			JOptionPane.showMessageDialog(this, "ERRORS:\n" + errors.toString(), "Please fix the form.", JOptionPane.ERROR_MESSAGE);
		}
	}
}
