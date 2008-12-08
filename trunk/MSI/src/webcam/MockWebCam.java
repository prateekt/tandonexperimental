package webcam;

import java.awt.image.BufferedImage;
import util.*;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import java.util.*;
import javax.swing.*;
import java.awt.*;

/**
 * A mock web cam feeds the simulation previously stored images
 * from a directory. This feature can be used to test the simulation
 * in the absense of a real web cam.
 * @author Prateek Tandon
 *
 */
public class MockWebCam extends WebCam {
	
	/**
	 * Current time step
	 */
	private int t=0;

	/**
	 * The directory of images to draw from.
	 */
	private String imgDir;
	
	/**
	 * The component to draw images on
	 */
	private ImagePanel panel;
	
	/**
	 * Constructor
	 * @param imgDir The directory of images to feed
	 * to the simulation.
	 */
	public MockWebCam(String imgDir) {
		this.imgDir = imgDir;
		panel = new ImagePanel();
		panel.setSize(300,300);
	}
	
	/**
	 * Reset Implementation
	 */
	protected void reset() {
		t =0;
	}
	
	/**
	 * Used to grab frames from the camera.
	 */
	public java.util.List<BufferedImage> grabFrame(int numCopies) {
		java.util.List<BufferedImage> rtn = new ArrayList<BufferedImage>();
		
		try {
			if(t < Constants.NUMBER_TIME_STEPS) {
				for(int x=0; x < numCopies; x++) {
					BufferedImage b = ImageIO.read(new File(imgDir + "shot" + t +".jpg"));
					rtn.add(b);
				}
				
				//update panel
				BufferedImage update = ImageIO.read(new File(imgDir + "shot" + t +".jpg"));
				panel.setCurrent(update);
				panel.repaint();
//				this.printDebug(""+t);
				t++;
			}
		}
		catch(Exception e) {
//			e.printStackTrace();
		}
		
		return rtn;
	}
	
	public Component getCameraFeed() {
		return panel;
	}
	
	public void setImageDir(String imageDir) {
		this.imgDir = imageDir;
	}
}

/**
 * Panel to draw images from camera on.
 * @author Prateek Tandon
 *
 */
class ImagePanel extends JPanel {
		
	private BufferedImage current= null;
		
	public void paint(Graphics g) {
		if(current!=null) {
			g.drawImage(current, 0, 0, 300, 300, this);
		}
	}
	
	public void setCurrent(BufferedImage i) {
		this.current  = i;
	}
}
