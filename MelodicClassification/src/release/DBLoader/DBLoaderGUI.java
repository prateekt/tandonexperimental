package release.DBLoader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import behavior.transcriber.base.Melody;
import api.tuxguitar.MelodyExtractor.TGMelodyExtractor;


public class DBLoaderGUI extends JFrame implements ActionListener {
	
	//side buttons
	private JButton setConfigButton;
	private JButton addFileButton;
	private JButton reloadDBButton;
	private JButton clearDBButton;
	private JButton saveDBButton;
	
	//bottom buttons
	private JButton addMelodyButton;
	private JButton deleteMelodyButton;
	
	//lists
	private JList melodyBuffList;
	private JList melodyDBList;
	
	//bottom melody bar
	private JTextField melodyBar;
	private JFileChooser fileChooser;
	
	//model
	FileBasedMelodyDB db;
	UserSetting settings;
	
	public DBLoaderGUI(UserSetting settings) {
		super("Guitar Tab Loader v1.0");
		
		//settings
		if(settings==null) {
			settings = new DefaultUserSetting();
		}
		else {
			this.settings = settings;
		}
		
		//model
		db = new FileBasedMelodyDB(settings.getDbFile());
		
		//init buttons
		addFileButton = new JButton("Add Tab File");
		setConfigButton = new JButton("Configure Extractor");
		reloadDBButton = new JButton("Reload Melody Database");
		clearDBButton = new JButton("Clear Melody Database");
		saveDBButton = new JButton("Save Melody Database");
		addMelodyButton = new JButton("Add Melody");
		deleteMelodyButton = new JButton("Delete Melody");
		addFileButton.addActionListener(this);
		setConfigButton.addActionListener(this);
		reloadDBButton.addActionListener(this);
		clearDBButton.addActionListener(this);
		saveDBButton.addActionListener(this);
		addMelodyButton.addActionListener(this);
		deleteMelodyButton.addActionListener(this);
		
		//init lists
		melodyBuffList = new JList();
		JScrollPane melodyBuffListPane = new JScrollPane(melodyBuffList);
		melodyDBList = new JList();
		melodyDBList.setModel(new DefaultListModel());
		JScrollPane melodyDBListPane = new JScrollPane(melodyDBList);
		
		//init melody bar
		melodyBar = new JTextField();
		fileChooser = new JFileChooser();
		
		//LAYOUT
		
		//left panel
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(5,1));
		leftPanel.add(setConfigButton);
		leftPanel.add(addFileButton);
		leftPanel.add(reloadDBButton);
		leftPanel.add(clearDBButton);
		leftPanel.add(saveDBButton);
		
		//center panel
		JPanel centerPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill=GridBagConstraints.HORIZONTAL;
		c.gridx=0;
		c.gridy=0;
		c.ipady=130;
		c.ipadx=100;
		centerPanel.add(melodyBuffListPane,c);
		c.fill=GridBagConstraints.VERTICAL;
		c.gridx=0;
		c.gridy=1;
		c.ipady=1;
		c.ipadx=100;
		centerPanel.add(addMelodyButton,c);

		//right panel
		JPanel rightPanel = new JPanel(new GridBagLayout());
		c.fill=GridBagConstraints.HORIZONTAL;
		c.gridx=0;
		c.gridy=0;
		c.ipady=130;
		c.ipadx=100;
		rightPanel.add(melodyDBListPane,c);
		c.fill=GridBagConstraints.VERTICAL;
		c.gridx=0;
		c.gridy=1;
		c.ipady=1;
		c.ipadx=100;
		rightPanel.add(deleteMelodyButton,c);
		
		//main panel
		JPanel mainPanel = new JPanel(new GridLayout(1,3));
		mainPanel.add(leftPanel);
		mainPanel.add(centerPanel);
		mainPanel.add(rightPanel);
		
		//set global layout
		setLayout(null);
		mainPanel.setBounds(10,10,690,180);
		getContentPane().add(mainPanel);		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(710,220);
		setVisible(true);
		reloadDBButtonPressed();
	}
	
	public static void main(String[] args) {
		UserSetting setting = UserSettingSaver.loadUserSetting(UserSettingSaver.USER_SETTINGS_LOC);
		new DBLoaderGUI(setting);
	}

	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o==setConfigButton) {
			setConfigButtonPressed();
		}
		if(o==addFileButton) {			
			addButtonPressed();
		}
		if(o==reloadDBButton) {
			reloadDBButtonPressed();
		}
		if(o==addMelodyButton) {
			addMelodyButtonPressed();
		}
		if(o==saveDBButton) {
			saveDBButtonPressed();
		}
		if(o==clearDBButton) {
			clearDBButtonPressed();
		}
		if(o==deleteMelodyButton) {
			deleteMelodyButtonPressed();
		}
	}
	
	public void setConfigButtonPressed() {
		SettingsDialog.showSettingsDialog(settings);
	}
	
	public void deleteMelodyButtonPressed() {

		//get selected from melody buff
		Object[] objs = melodyDBList.getSelectedValues();

		//update model
		DefaultListModel model = (DefaultListModel) melodyDBList.getModel();
		for(Object o : objs)
			model.removeElement(o);		
	}
	
	public void clearDBButtonPressed() {
		//clear model
		DefaultListModel model = (DefaultListModel) melodyDBList.getModel();
		model.clear();
	}
	
	public void saveDBButtonPressed() {
		
		//get out of model
		DefaultListModel model = (DefaultListModel) melodyDBList.getModel();
		List<Melody> toSave = new ArrayList<Melody>();
		for(int x=0; x < model.getSize(); x++) {
			Melody m = (Melody) model.get(x);
			toSave.add(m);
		}
		
		//save
		db.setMelodies(toSave);
		db.save();
	}
	
	public void addMelodyButtonPressed() {
		
		//get selected from melody buff
		Object[] objs = melodyBuffList.getSelectedValues();
		DefaultListModel model = (DefaultListModel) melodyDBList.getModel();
		
		//add to db list
		for(Object o : objs) {
			Melody m = (Melody)o;
			model.addElement(m);
		}
	}
	
	public void reloadDBButtonPressed() {
		db.reload();
		List<Melody> dbMels = db.getMelodies();

		//populate melody buff
		DefaultListModel listModel = new DefaultListModel();
		for(Melody m : dbMels)
			listModel.addElement(m);
		melodyDBList.setModel(listModel);
	}
	
	public void addButtonPressed() {
		int rtn = fileChooser.showOpenDialog(this);
		
		//return if file not chosen
		if (rtn != JFileChooser.APPROVE_OPTION)
        	return;
		
		//get file and melodies
        File file = fileChooser.getSelectedFile();
        int minTrack=-1;
        int maxTrack=-1;
        if(settings.getTrackSetting()==UserSetting.SINGLE_TRACK) {
        	minTrack = settings.getMinTrack();
        	maxTrack = settings.getMinTrack()+1;
        }
        if(settings.getTrackSetting()==UserSetting.MULTI_TRACK) {
        	minTrack = settings.getMinTrack();
        	maxTrack = settings.getMaxTrack();
        }
        if(settings.getTrackSetting()==UserSetting.ALL_TRACK) {
        	minTrack=0;
        	maxTrack=Integer.MAX_VALUE;
        }
        
        //extract melodies and build list model
		Map<String,List<Melody>> melodiesMap = TGMelodyExtractor.extractMelodies(file.getAbsolutePath(),minTrack,maxTrack,0);
		DefaultListModel listModel = new DefaultListModel();
		for(String trackName : melodiesMap.keySet()) {
			List<Melody> melodies = melodiesMap.get(trackName);
			for(Melody m : melodies)
				listModel.addElement(m);
		}
		
		//populate melody buff
		melodyBuffList.setModel(listModel);
	}
}
