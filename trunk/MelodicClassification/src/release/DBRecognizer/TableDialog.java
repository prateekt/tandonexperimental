package release.DBRecognizer;
import javax.swing.*;
import javax.swing.table.*;

import java.util.*;

public class TableDialog extends JFrame{
	
	private JTable table;
	
	public static void main(String[] args) {
		new TableDialog();
	}
	
	public TableDialog() {
		super("Melodic Event Monitor");
		table = new JTable();
		table.setModel(createModel(new ArrayList<RecognitionEvent>()));
		JScrollPane tablePane = new JScrollPane(table);
		getContentPane().add(tablePane);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setResizable(false);
		setSize(450,100);
	}
	
	public void updateTable(java.util.List<RecognitionEvent> events) {
		DefaultTableModel m = createModel(events);
		table.setModel(m);
	}
	
	
	public DefaultTableModel createModel(java.util.List<RecognitionEvent> events) {
		
		//column names
		String[] columnNames = {"Time of Event", "Melody Recognized", "Notes Transcribed"};
		
		//objects
		Object[][] data = new Object[events.size()][3];
		for(int x=0; x < data.length; x++) {
			RecognitionEvent e = events.get(x);
			data[x][0] = e.getTime().toString();
			data[x][1] = e.getLabel();
			data[x][2] = e.getNotes();
		}
	    DefaultTableModel dataModel = new DefaultTableModel(data,columnNames); 
	    return dataModel;
	}
}
