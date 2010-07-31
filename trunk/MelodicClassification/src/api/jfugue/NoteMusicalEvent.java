package api.jfugue;

public class NoteMusicalEvent extends MusicalEvent {
	
	private String note;
	private double duration;
	private int attackSpeed;
	private int decaySpeed;
	
	public NoteMusicalEvent(int type, String content, String note, double duration, int attackSpeed, int decaySpeed) {
		super(type,content);
		this.note = note;
		this.duration = duration;
		this.attackSpeed = attackSpeed;
		this.decaySpeed = decaySpeed;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}
	
	@Override
	public String toString() {
		String[] toks = content.trim().split(" ");
		String rtn= "@" + time + " "; 
		for(String tok : toks) {
			if(tok.indexOf("/") > -1) {
				rtn+= note.trim() + "/" + duration + "a" + attackSpeed + "d" + decaySpeed + " "; 
			}
			else {
				rtn+= tok.trim() + " ";
			}
		}
		
		return rtn.toString().trim();		
	}

	/**
	 * @return the duration
	 */
	public double getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(double duration) {
		this.duration = duration;
	}

	/**
	 * @return the attackSpeed
	 */
	public int getAttackSpeed() {
		return attackSpeed;
	}

	/**
	 * @param attackSpeed the attackSpeed to set
	 */
	public void setAttackSpeed(int attackSpeed) {
		this.attackSpeed = attackSpeed;
	}

	/**
	 * @return the decaySpeed
	 */
	public int getDecaySpeed() {
		return decaySpeed;
	}

	/**
	 * @param decaySpeed the decaySpeed to set
	 */
	public void setDecaySpeed(int decaySpeed) {
		this.decaySpeed = decaySpeed;
	}
}
