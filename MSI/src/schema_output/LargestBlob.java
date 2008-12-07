package schema_output;
import java.awt.image.*;

/**
 * Largest blob struct. Used by Visual cortex
 * in storing color segmentation results.
 * @author Prateek Tandon
 *
 */
public class LargestBlob {
	
	/**
	 * The color segmented image
	 */
	private BufferedImage img;

	/**
	 * The x coordinate of the largest blob of a given color.
	 */
	private double cX;
	
	/**
	 * The y coordinate of the largest blob of a given color.
	 */
	private double cY;
	
	/**
	 * @return the img
	 */
	public BufferedImage getImg() {
		return img;
	}

	/**
	 * @param img the img to set
	 */
	public void setImg(BufferedImage img) {
		this.img = img;
	}
	
	/**
	 * @return the cX
	 */
	public double getCX() {
		return cX;
	}
	
	/**
	 * @param cx the cX to set
	 */
	public void setCX(double cx) {
		cX = cx;
	}
	
	/**
	 * @return the cY
	 */
	public double getCY() {
		return cY;
	}
	
	/**
	 * @param cy the cY to set
	 */
	public void setCY(double cy) {
		cY = cy;
	}
}
