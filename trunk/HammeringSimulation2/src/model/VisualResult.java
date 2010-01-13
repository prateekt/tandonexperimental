package src.model;

public class VisualResult {

	private boolean hitNail;
	private boolean nailBent;
	private boolean nailDone;
	private double currentInAmount;
	private double currentBentAmount;
	private double inUpdateAmount;
	
	public VisualResult(boolean hitNail, boolean nailBent, boolean nailDone, double currentInAmount, double currentBentAmount, double inUpdateAmount, double bendUpdateAmount) {
		this.hitNail = hitNail;
		this.nailBent = nailBent;
		this.nailDone = nailDone;
		this.currentInAmount = currentInAmount;
		this.currentBentAmount = currentBentAmount;
		this.inUpdateAmount = inUpdateAmount;
	}

	public boolean isHitNail() {
		return hitNail;
	}

	public void setHitNail(boolean hitNail) {
		this.hitNail = hitNail;
	}

	public double getCurrentInAmount() {
		return currentInAmount;
	}

	public void setCurrentInAmount(double currentInAmount) {
		this.currentInAmount = currentInAmount;
	}

	public double getInUpdateAmount() {
		return inUpdateAmount;
	}

	public void setInUpdateAmount(double inUpdateAmount) {
		this.inUpdateAmount = inUpdateAmount;
	}

	public boolean isNailBent() {
		return nailBent;
	}

	public void setNailBent(boolean nailBent) {
		this.nailBent = nailBent;
	}

	public boolean isNailDone() {
		return nailDone;
	}

	public void setNailDone(boolean nailDone) {
		this.nailDone = nailDone;
	}

	public double getCurrentBentAmount() {
		return currentBentAmount;
	}

	public void setCurrentBentAmount(double currentBentAmount) {
		this.currentBentAmount = currentBentAmount;
	}
}
