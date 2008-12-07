package util;

/**
 * Models a 2-D vector
 * @author Prateek Tandon
 *
 */
public class Vector2 {
	
	private double x;
	private double y;
	private double vectorStartX;
	private double vectorStartY;

	public Vector2() {
		x = 0.0;
		y = 0.0;
	}

	public Vector2(double x,double y, double vectorStartX, double vectorStartY) {
		this.x = x;
		this.y = y;
		this.vectorStartX = vectorStartX;
		this.vectorStartY = vectorStartY;
	}

	public Vector2(double x,double y) {
		this.x = x;
		this.y = y;
		this.vectorStartX = 0;
		this.vectorStartY = 0;
	}

	public double getLength() {
		return Math.sqrt(x*x + y*y);
	}

	public Vector2 getNormalized() {
		double length = getLength();
		Vector2 v = new Vector2(x/length,y/length);
		return v;
	}

	public Vector2 addVector(Vector2 v) {
		Vector2 v2 = new Vector2(x + v.x,y+v.y);
		return v2;
	}

	public Vector2 subVector(Vector2 v) {
		return new Vector2(x - v.x,y - v.y);
	}

	public double dot(Vector2 v) {
		return x*v.x + y*v.y;
	}

	public Vector2 scale(double s) {
		return new Vector2(x*s,y*s);
	}

	public String toString()
	{
		return "<"+(int)x+", "+(int)y+">";
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * @return the vectorStartX
	 */
	public double getVectorStartX() {
		return vectorStartX;
	}

	/**
	 * @param vectorStartX the vectorStartX to set
	 */
	public void setVectorStartX(double vectorStartX) {
		this.vectorStartX = vectorStartX;
	}

	/**
	 * @return the vectorStartY
	 */
	public double getVectorStartY() {
		return vectorStartY;
	}

	/**
	 * @param vectorStartY the vectorStartY to set
	 */
	public void setVectorStartY(double vectorStartY) {
		this.vectorStartY = vectorStartY;
	}
}