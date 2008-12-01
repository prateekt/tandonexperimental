package webcam;
import javax.media.*;
import javax.media.format.*;
import javax.media.util.*;
import javax.media.control.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import schemas.*;
import util.*;

public class WebCam extends BrainSchema {

	private Player player = null;
	private CaptureDeviceInfo di = null;
	private MediaLocator ml = null;
	private BufferToImage btoi = null;
	private Component cameraFeed = null;
	private VisualCortex vc;
		
	public WebCam() {
		super("Web Cam");
		String str2 = "vfw:Microsoft WDM Image Capture (Win32):0";
		di = CaptureDeviceManager.getDevice(str2);
		ml = di.getLocator();
		try {
		  player = Manager.createRealizedPlayer(ml);
		  player.start();
		  if ((cameraFeed = player.getVisualComponent()) != null) {
			System.out.println("Camera started.");
		  }
		  Thread.sleep(5000);
		}
		catch (Exception e)    {
		  e.printStackTrace();
		}
	}

	public Component getCameraFeed() {
		return cameraFeed;
	}
	
	public boolean produceOutput() {
		try {
//			this.printDebug("Sending visual to visual cortex");
			java.util.List<BufferedImage> currentImageCopies = grabFrame(4);
			vc.input(currentImageCopies);
			Thread.sleep(Constants.CLOCK);			
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

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

		return rtn;
	}

	public static boolean hasAlpha(Image image) {
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

	public static BufferedImage toBufferedImage(Image image) {
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
	
	public void setVisualCortex(VisualCortex vc) {
		this.vc = vc;
	}

	public void playerclose() {
		player.close();
		player.deallocate();
	}
}