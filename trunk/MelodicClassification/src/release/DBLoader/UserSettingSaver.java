package release.DBLoader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class UserSettingSaver {
	
	public static String USER_SETTINGS_LOC = "db/db_classifiers/settings.config";
	
	public static void saveUserSetting(String file, UserSetting s) {		
		try {
						
			// Write to disk with FileOutputStream
			FileOutputStream f_out = new FileOutputStream(file);

			// Write object with ObjectOutputStream
			ObjectOutputStream obj_out = new ObjectOutputStream (f_out);

			// Write object out to disk
			obj_out.writeObject (s);

			//close streams
			obj_out.close();
			f_out.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static UserSetting loadUserSetting(String file) {
		UserSetting rtn = null;
		try {
			// Read from disk using FileInputStream
			FileInputStream f_in = new FileInputStream(file);

			// Read object using ObjectInputStream
			ObjectInputStream obj_in = new ObjectInputStream (f_in);

			// Read an object
			Object obj = obj_in.readObject();
			
			if(obj instanceof UserSetting) {
				rtn = (UserSetting)obj;
			}
		}
		catch(Exception e) {}
		
		return rtn;
	}
}
