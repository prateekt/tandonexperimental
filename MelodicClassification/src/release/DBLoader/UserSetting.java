package release.DBLoader;
import java.io.*;

public class UserSetting implements Serializable {

	public static int SINGLE_TRACK = 0;
	public static int MULTI_TRACK = 1;
	public static int ALL_TRACK = 2;
	public static int REST_BASED_ALGORITHM = 0;
	public static int MELODIC_STRUCTURE_ALGORITHM=1;
	
	//settings
	protected int trackSetting;
	protected int minTrack;
	protected int maxTrack;
	protected String dbFile;
	protected boolean[] algorithms;

	/**
	 * @return the trackSetting
	 */
	public int getTrackSetting() {
		return trackSetting;
	}
	/**
	 * @param trackSetting the trackSetting to set
	 */
	public void setTrackSetting(int trackSetting) {
		this.trackSetting = trackSetting;
	}
	/**
	 * @return the minTrack
	 */
	public int getMinTrack() {
		return minTrack;
	}
	/**
	 * @param minTrack the minTrack to set
	 */
	public void setMinTrack(int minTrack) {
		this.minTrack = minTrack;
	}
	/**
	 * @return the maxTrack
	 */
	public int getMaxTrack() {
		return maxTrack;
	}
	/**
	 * @param maxTrack the maxTrack to set
	 */
	public void setMaxTrack(int maxTrack) {
		this.maxTrack = maxTrack;
	}
	/**
	 * @return the dbFile
	 */
	public String getDbFile() {
		return dbFile;
	}
	/**
	 * @param dbFile the dbFile to set
	 */
	public void setDbFile(String dbFile) {
		this.dbFile = dbFile;
	}
	/**
	 * @return the algorithms
	 */
	public boolean[] getAlgorithms() {
		return algorithms;
	}
	/**
	 * @param algorithms the algorithms to set
	 */
	public void setAlgorithms(boolean[] algorithms) {
		this.algorithms = algorithms;
	}
}
