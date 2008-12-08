package webcam;
import javax.media.*;
import javax.imageio.*;
import javax.media.format.*;
import javax.media.util.*;
import javax.media.control.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import schemas.*;
import util.*;
import java.io.*;

/**
 * Used to hook my MSI model up to a real web cam.
 * @author Prateek Tandon
 *
 */
public class RealWebCam extends WebCam {
	
	//web cam api objects
	private Player player = null;
	private CaptureDeviceInfo di = null;
	private MediaLocator ml = null;
	private BufferToImage btoi = null;
	private Component cameraFeed = null;
	
	/**
	 * Whether to record frames from the webcam
	 * and store them as images on the hard drive.
	 */
	private boolean record = false;
	
	/**
	 * The directory to store record frames in.
	 */
	private String recordDirectory = null;

	/**
	 * The current time step.
	 */
	int t=0;
	
	/**
	 * Constructor
	 */
	public RealWebCam() {
		String str2 = "vfw:Microsoft WDM Image Capture (Win32):0";
		di = CaptureDeviceManager.getDevice(str2);
		ml = di.getLocator();
		try {
		  player = Manager.createRealizedPlayer(ml);
		  player.start();
		  if ((cameraFeed = player.getVisualComponent()) != null) {
			  this.printDebug("Camera Started");
		  }
		  Thread.sleep(5000);
		}
		catch (Exception e)    {
		  e.printStackTrace();
		}
	}
	
	/**
	 * Reset Implementation
	 */
	protected void reset() {
		t=0;
	}

	/**
	 * Returns a JPanel with the camera feed.
	 */
	public Component getCameraFeed() {
		return cameraFeed;
	}
	
	/**
	 * Grabs a frame from the camera.
	 * Used to return multiple copies of the same image.
	 */
	public java.util.List<BufferedImage> grabFrame(int numCopies) {
		//Return list
		java.util.List<BufferedImage> rtn = new ArrayList<BufferedImage>();

		// Grab a frame
		FrameGrabbingControl fgc = (FrameGrabbingControl) player.getControl("javax.media.control.FrameGrabbingControl");
		Buffer buf = fgc.grabFrame();

		// Convert it to an image
		btoi = new BufferToImage((VideoFormat)buf.getFormat());

		for(int x=0; x < numCopies; x++) {
			rtn.add(toBufferedImage(btoi.createImage(buf)));
		}
		
		if(record && recordDirectory!=null)  {
			try {
				BufferedImage toSave =toBufferedImage(btoi.createImage(buf));
				ImageIO.write(toSave, "jpeg", new File(recordDirectory + "/shot" + t +".jpg"));
				t++;
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return rtn;
	}

	/**
	 * Helper color model method
	 * @param image
	 * @return
	 */
	private static boolean hasAlpha(Image image) {
		// If buffered image, the color model is readily available
		if (image instanceof BufferedImage) {return ((BufferedImage)image).getColorModel().hasAlpha();}

		 // Use a pixel grabber to retrieve the image's color model;
		 // grabbing a single pixel is usually sufficient
		 PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
		 try {
			 pg.grabPixels();
		 } catch (InterruptedException e) {
			 e.printStackTrace();
		 }

		 // Get the image's color model
		 return pg.getColorModel().hasAlpha();
	}

	/**
	 * Converts an image into a buffered image
	 * @param image The image to convert
	 * @return The buffered image
	 */
	private static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {return (BufferedImage)image;}
		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();

		// Determine if the image has transparent pixels
		boolean hasAlpha = hasAlpha(image);

		// Create a buffered image with a format that's compatible with the screen
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;
			if (hasAlpha == true) {
				transparency = Transparency.BITMASK;
			}
			// Create the buffered image
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
		}
		catch (HeadlessException e) {} //No screen

		if (bimage == null) {
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;
			if (hasAlpha == true) {type = BufferedImage.TYPE_INT_ARGB;}
			bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
		}

		// Copy image to buffered image
		Graphics g = bimage.createGraphics();
		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimage;
	}
	
	/**
	 * Terminates camera
	 */
	public void playerclose() {
		player.close();
		player.deallocate();
	}

	/**
	 * @return Whether the camera is recording frames or not
	 */
	public boolean isRecord() {
		return record;
	}

	/**
	 * @param record Whether to record or not
	 */
	public void setRecord(boolean record) {
		this.record = record;
	}

	/**
	 * @return the recordDirectory
	 */
	public String getRecordDirectory() {
		return recordDirectory;
	}

	/**
	 * @param recordDirectory the recordDirectory to set
	 */
	public void setRecordDirectory(String recordDirectory) {
		this.recordDirectory = recordDirectory;
	}
	
}