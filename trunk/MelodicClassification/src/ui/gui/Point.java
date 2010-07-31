package ui.gui;

/**
 * Represents a geometric point.
 * @author Prateek Tandon
 *
 */
public class Point {
	
	/**
	 * x_value
	 */
	private double x;
	
	/**
	 * y_value
	 */
	private double y;
	
	/**
	 * The point
	 * @param x x_value
	 * @param y y_value
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}
