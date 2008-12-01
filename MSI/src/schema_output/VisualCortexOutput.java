package schema_output;

import java.awt.image.*;

public class VisualCortexOutput {
	private LargestBlob greenTag;
	private LargestBlob pinkTag;
	private LargestBlob blueTag;
	private LargestBlob yellowTag;
	private BufferedImage taggedImage;
	
	public VisualCortexOutput(LargestBlob blueTag, LargestBlob greenTag, LargestBlob pinkTag, LargestBlob yellowTag, BufferedImage taggedImage) {
		this.greenTag = greenTag;
		this.pinkTag =  pinkTag;
		this.blueTag = blueTag;
		this.yellowTag = yellowTag;
		this.taggedImage =  taggedImage;
	}

	/**
	 * @return the greenTag
	 */
	public LargestBlob getGreenTag() {
		return greenTag;
	}

	/**
	 * @param greenTag the greenTag to set
	 */
	public void setGreenTag(LargestBlob greenTag) {
		this.greenTag = greenTag;
	}

	/**
	 * @return the pinkTag
	 */
	public LargestBlob getPinkTag() {
		return pinkTag;
	}

	/**
	 * @param pinkTag the pinkTag to set
	 */
	public void setPinkTag(LargestBlob pinkTag) {
		this.pinkTag = pinkTag;
	}

	/**
	 * @return the blueTag
	 */
	public LargestBlob getBlueTag() {
		return blueTag;
	}

	/**
	 * @param blueTag the blueTag to set
	 */
	public void setBlueTag(LargestBlob blueTag) {
		this.blueTag = blueTag;
	}

	/**
	 * @return the yellowTag
	 */
	public LargestBlob getYellowTag() {
		return yellowTag;
	}

	/**
	 * @param yellowTag the yellowTag to set
	 */
	public void setYellowTag(LargestBlob yellowTag) {
		this.yellowTag = yellowTag;
	}

	/**
	 * @return the taggedImage
	 */
	public BufferedImage getTaggedImage() {
		return taggedImage;
	}

	/**
	 * @param taggedImage the taggedImage to set
	 */
	public void setTaggedImage(BufferedImage taggedImage) {
		this.taggedImage = taggedImage;
	}
}