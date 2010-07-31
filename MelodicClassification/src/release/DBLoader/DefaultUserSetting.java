package release.DBLoader;

public class DefaultUserSetting extends UserSetting {
	
	public DefaultUserSetting() {
		trackSetting = UserSetting.SINGLE_TRACK;
		minTrack = 0;
		maxTrack = 0;
		dbFile = "db/db_classifiers/melody_db.obj";
		algorithms = new boolean[2];
		algorithms[REST_BASED_ALGORITHM] = true;
		algorithms[MELODIC_STRUCTURE_ALGORITHM]=false;		
	}	
}
