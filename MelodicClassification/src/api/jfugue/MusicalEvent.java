package api.jfugue;

public class MusicalEvent {
	
	protected int time;
	protected String content;
	protected String headContent;
	protected int timeSinceLast;
	
	public MusicalEvent(int time, String content) {
		this.time = time;
		this.content = content;
		timeSinceLast=0;
	}

	/**
	 * @return the time
	 */
	public int getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(int time) {
		this.time = time;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		if(headContent!=null) {
			return headContent.trim();
		}
		else {
			return "@" + time + " " + content.trim();
		}
	}

	/**
	 * @return the headContent
	 */
	public String getHeadContent() {
		return headContent;
	}

	/**
	 * @param headContent the headContent to set
	 */
	public void setHeadContent(String headContent) {
		this.headContent = headContent;
	}

	/**
	 * @return the timeSinceLast
	 */
	public int getTimeSinceLast() {
		return timeSinceLast;
	}

	/**
	 * @param timeSinceLast the timeSinceLast to set
	 */
	public void setTimeSinceLast(int timeSinceLast) {
		this.timeSinceLast = timeSinceLast;
	}	
}
