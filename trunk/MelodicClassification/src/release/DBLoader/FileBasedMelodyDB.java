package release.DBLoader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import behavior.transcriber.base.*;
public class FileBasedMelodyDB {
	
	protected String fileLoc;
	protected List<Melody> db;
	
	public FileBasedMelodyDB(String fileLoc) {
		this.fileLoc = fileLoc;
		db = new ArrayList<Melody>();
		
		//try unserialize
		unserializeDB(fileLoc);
	}
	
	public void setMelodies(List<Melody> melodies) {
		db = melodies;
	}
	
	public void addMelody(Melody m) {
		db.add(m);
	}
	
	public void deleteMelody(Melody m) {
		db.remove(m);
	}

	public List<Melody> getMelodies() {
		return db;
	}
	
	public void reload() {
		unserializeDB(fileLoc);
	}
	
	public void save() {
		serializeDB(fileLoc);
	}
	
	private void serializeDB(String file) {		
		try {
						
			// Write to disk with FileOutputStream
			FileOutputStream f_out = new FileOutputStream(file);

			// Write object with ObjectOutputStream
			ObjectOutputStream obj_out = new ObjectOutputStream (f_out);

			// Write object out to disk
			obj_out.writeObject (db);

			//close streams
			obj_out.close();
			f_out.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void unserializeDB(String file) {
		try {
			// Read from disk using FileInputStream
			FileInputStream f_in = new FileInputStream(file);

			// Read object using ObjectInputStream
			ObjectInputStream obj_in = new ObjectInputStream (f_in);

			// Read an object
			Object obj = obj_in.readObject();
			
			if(obj instanceof List<?>) {
				db = (List<Melody>)obj;
			}
		}
		catch(Exception e) {}
	}
	
}
