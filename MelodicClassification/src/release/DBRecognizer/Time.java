package release.DBRecognizer;

import java.text.NumberFormat;

public class Time {
	
	private int numSeconds;
	private int numMinutes;
	
	public Time(int numSeconds, int numMinutes) {
		this.numSeconds = numSeconds;
		this.numMinutes = numMinutes;
	}
	
	@Override
	public String toString() {
		NumberFormat n = NumberFormat.getIntegerInstance();
		n.setMinimumIntegerDigits(2);
		n.setMaximumIntegerDigits(2);
		return n.format(numMinutes) + ":" + n.format(numSeconds);
	}
	
	public Time() {
		numSeconds = 0;
		numMinutes = 0;
	}
	
	public Time addSeconds(int sec) {
		numSeconds += sec;
		
		//update time
		if(numSeconds >= 60) {
			numMinutes += numSeconds/60;
			numSeconds = numSeconds % 60;
		}
		
		//return new time with that Time
		Time rtn = new Time(numSeconds,numMinutes);
		return rtn;
	}

	/**
	 * @return the numSeconds
	 */
	public int getNumSeconds() {
		return numSeconds;
	}

	/**
	 * @param numSeconds the numSeconds to set
	 */
	public void setNumSeconds(int numSeconds) {
		this.numSeconds = numSeconds;
	}

	/**
	 * @return the numMinutes
	 */
	public int getNumMinutes() {
		return numMinutes;
	}

	/**
	 * @param numMinutes the numMinutes to set
	 */
	public void setNumMinutes(int numMinutes) {
		this.numMinutes = numMinutes;
	}
}
