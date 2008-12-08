package webcam;

import java.awt.image.BufferedImage;

import schemas.BrainSchema;
import schemas.VisualCortex;
import util.Constants;
import java.awt.*;

/**
 * Web cam base class. You can hook my MSI model up to real live
 * web cam input via the real web cam class or feed it a folder of
 * images using the mock web cam class.
 * @author Prateek Tandon
 *
 */
public abstract class WebCam extends BrainSchema {
	
	/**
	 * Whether wecam is active or not.
	 */
	private boolean on = true;
	
	/**
	 * The visual cortex connection.
	 */
	protected VisualCortex vc;
	
	/**
	 * The constructor
	 */
	public WebCam(){
		super("Web Cam");
	}
	
	/**
	 * Sends images to visual cortex based on the clock constant.
	 */
	public boolean produceOutput() {
		//reset case
		if(resetSignals.size()> 0) {
			resetSignals.clear();
//			on = false;
			try {
				Thread.sleep(2000);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			reset();
		}
		
		//produce image
		try {
			if(on) {
				java.util.List<BufferedImage> currentImageCopies = grabFrame(4);
				if(currentImageCopies.size() >0)
					vc.input(currentImageCopies);
				Thread.sleep(Constants.CLOCK);	
				return true;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Connection to visual cortex
	 * @param vc The visual cortex
	 */
	public void setVisualCortex(VisualCortex vc) {
		this.vc = vc;
	}
	
	/**
	 * Reset function for particular web cam.
	 */
	protected abstract void reset();
	
	/**
	 * Returns a panel for the gui showing the camera output.
	 * @return
	 */
	public abstract Component getCameraFeed();	
	
	/**
	 * Used to return many copies of the same frame.
	 * @param numCopies Number of copies of the frame to get.
	 * @return List of frame images
	 */
	public abstract java.util.List<BufferedImage> grabFrame(int numCopies);

	/**
	 * @return the on
	 */
	public boolean isOn() {
		return on;
	}

	/**
	 * @param on the on to set
	 */
	public void setOn(boolean on) {
		this.on = on;
	}
}
