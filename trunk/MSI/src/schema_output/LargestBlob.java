package schema_output;
import java.awt.image.*;

public class LargestBlob {
	private BufferedImage img;
	private double cX;
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
