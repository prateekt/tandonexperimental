package schema_output;

import java.awt.image.*;

/**
 * Visual cortex output struct
 * @author Prateek Tandon
 *
 */
public class VisualCortexOutput {
	
	/**
	 * The tagged largest green object in the scene.
	 */
	private LargestBlob greenTag;
	
	/**
	 * The tagged largest pink object in the scene.
	 */
	private LargestBlob pinkTag;

	/**
	 * The tagged largest blue object in the scene.
	 */
	private LargestBlob blueTag;
	
	/**
	 * The tagged largest yellow object in the scene.
	 */
	private LargestBlob yellowTag;
	
	/**
	 * The tagged image with all the blobs segmented.
	 */
	private BufferedImage taggedImage;
	
	/**
	 * Constructor
	 * @param blueTag
	 * @param greenTag
	 * @param pinkTag
	 * @param yellowTag
	 * @param taggedImage
	 */
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