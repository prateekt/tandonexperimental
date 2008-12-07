package schemas;

import schema_output.LargestBlob;
import schema_output.VisualCortexOutput;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.util.concurrent.*;
import gui.*;
/**
 * The visual cortex is responsible for color segmenting the image.
 * It passes the color segmented image to the parietal cortex which
 * will use that in control variable computation.
 * @author Prateek Tandon
 */
public class VisualCortex extends BrainSchema {
	
	/**
	 * The visual input stream from the webcam. Listing of snapshots
	 * as they come.
	 */
	private Queue<java.util.List<BufferedImage>> visualInputStream;

	/**
	 * Connection  to gui
	 */
	private GUISchema guiSchema;	
	
	/**
	 * Connection to parietal cortex
	 */
	private ParietalCortex  pc;
	
	/**
	 * Constructor
	 */
	public VisualCortex() {
		super("Visual Cortex");
		visualInputStream = new ConcurrentLinkedQueue<java.util.List<BufferedImage>>();
	}
	
	/**
	 * Used by webcam to send snapshots to the visual cortex.
	 * @param snapshot The new snapshot of the scenes
	 */
	public void input(java.util.List<BufferedImage> snapshot) {
//		this.printDebug("Recieved snapshot from webcam");
		visualInputStream.add(snapshot);
		receivedInput();
	}
	
	/**
	 * Produces output.
	 */
	public boolean produceOutput() {
		if(resetSignals.size() > 0) {
			resetSignals.clear();
			visualInputStream.clear();
			return false;
		}
		
		if(visualInputStream.size() > 0) {
//			this.printDebug("Sending tagged image to GUISchema");
			VisualCortexOutput output = tagImage(visualInputStream.remove());
			
			//send output to GUI
			guiSchema.sendVisInput(output);

			//send output to parietal cortex
			pc.sendVisualInput(output);
			
			return true;
		}
		return false;
	}
	
	/**
	 * Tags image with 4 salient color blobs. Returns the visual cortex output.
	 * @param images The current screenshot (multiple copies of it)
	 * @return output of visual cortex
	 */
	public VisualCortexOutput tagImage(java.util.List<BufferedImage> images)  {
		BufferedImage img1 =  images.get(0);
		BufferedImage img2 =  images.get(1);
		BufferedImage img3 =  images.get(2);
		BufferedImage img4 =  images.get(3);

		int[][][] hsvImg = imgToHSV(img1);
		BufferedImage i1 = createFilteredImage(img1, hsvImg, 30, 50, 3, 110, 0, 100000); //green
		BufferedImage i2 = createFilteredImage(img2, hsvImg, -25, 15, 20, 45, 0, 100000); //red
		BufferedImage i3 = createFilteredImage(img3, hsvImg, 20, 40, 20, 45, 0, 100000); //yellow
		BufferedImage i4 = createFilteredImage(img4, hsvImg, 50, 290, 0, 40, 0, 100000); //blue

//		BufferedImage i1 = createFilteredImage(img1, hsvImg, 50, 70, 3, 110, 0, 100000); //green
//		BufferedImage i2 = createFilteredImage(img2, hsvImg, -20, 30, 3, 20, 0, 100000); //red
//		BufferedImage i3 = createFilteredImage(img3, hsvImg, 20, 60, 5, 25, 0, 100000); //yellow
//		BufferedImage i4 = createFilteredImage(img4, hsvImg, 80, 140, 0, 10, 0, 100000); //blue

//		BufferedImage i1 = createFilteredImage(img, 90, 190, 0, 255, 90, 190); //green
//		BufferedImage i2 = createFilteredImage(img2, 150, 255, 40, 80, 90, 120); //pink
//		BufferedImage i3 = createFilteredImage(img3, 70, 120, 60, 130, 50, 255); //blue
		LargestBlob f1 = markLargest(i1, Color.green);
		LargestBlob f2 = markLargest(i2, Color.red);
		LargestBlob f3 = markLargest(i3, Color.yellow);
		LargestBlob f4 = markLargest(i4, Color.blue);

		i1 = f1.getImg();
		i2 = f2.getImg();
		i3 = f3.getImg();
		i4 = f4.getImg();

		//add marker for other colors
		for(int x=0; x < i1.getWidth(); x++) {
		  for(int y=0; y < i1.getHeight(); y++) {
			  int pixel2 = i2.getRGB(x, y);
			  int pixel3 = i3.getRGB(x, y);
			  int pixel4 = i4.getRGB(x, y);

			  //marker condition
			  if(pixelColorful(pixel2))
				i1.setRGB(x, y, pixel2);
			  if(pixelColorful(pixel3))
			  	i1.setRGB(x,y,pixel3);
			  if(pixelColorful(pixel4))
			  	i1.setRGB(x,y,pixel4);
		  }
		}

		return new VisualCortexOutput(f4, f1, f2, f3, i1);
	}
	
	/**
	 * Returns black/white image filtered with given threshold values.
	 * Utilizes RGB space.
	 * 
	 * @param image The image to filter
	 * @param rmin The red threshold minimum
	 * @param rmax The red threshold maximum
	 * @param gmin The green threshold minimum
	 * @param gmax The green threshold maximum
	 * @param bmin The blue threshold minimum
	 * @param bmax The blue theshold maximum
	 * @return The filtered image in black and white
	 */
	private BufferedImage createFilteredImage(BufferedImage image, int rmin, int rmax, int gmin, int gmax, int bmin, int bmax) {
		for (int i = 0; i < image.getHeight(); i++) {
		  for (int j = 0; j < image.getWidth(); j++) {
			int pixel = image.getRGB(j, i);
			int alpha = (pixel >> 24) & 0xff;
			int red = (pixel >> 16) & 0xff;
			int green = (pixel >> 8) & 0xff;
			int blue = (pixel) & 0xff;

		//	System.out.println(red + " " + green + " " +blue);

			if(red > rmin && red < rmax && green > gmin && green < gmax && blue > bmin && blue < bmax) {
				image.setRGB(j, i, Color.black.getRGB());
			}
			else {
				image.setRGB(j, i, Color.white.getRGB());
			}
		  }
		}
		return image;
	}
	
	/**
	 * Given an image segmented for a specific color, finds the center
	 * of the largest blob of that color in the image.
	 * @param img The image to check
	 * @param c The color to mark the blob with
	 * @return LargestBlob struct that contains the center coordinates
	 * in the image of the designated largest blob
	 */
	private LargestBlob markLargest(BufferedImage img, Color c) {
	  	int[][] regionMatrix = new int[img.getWidth()][img.getHeight()];
	  	int[] regionSizes = new int[10000];
	  	int regionCounter = 1;

		//create regions
	  	for(int x=0;  x < img.getWidth(); x++)  {
			for(int y=0; y < img.getHeight(); y++) {
				int pixel = img.getRGB(x, y);
				if(pixelBlack(pixel)) {
					boolean connected = false;

					//check left
					if(x-1 > 0 && regionMatrix[x-1][y]!=0) {
						regionMatrix[x][y] = regionMatrix[x-1][y];
						regionSizes[regionMatrix[x-1][y]]++;
						connected =  true;
					}

					//check up
					if(y-1 > 0 && regionMatrix[x][y-1]!=0) {
						regionMatrix[x][y] = regionMatrix[x][y-1];
						regionSizes[regionMatrix[x][y-1]]++;
						connected = true;
					}

					//create new region if not connected
					if(!connected) {
						regionMatrix[x][y] = regionCounter;
						regionCounter++;
					}
				}
			}
		}

		//find  largest region
		int maxRegion=0;
		for(int x=1; x< regionSizes.length; x++) {
			if(regionSizes[x] > regionSizes[maxRegion])
				maxRegion = x;
		}

		//update image, while getting bounding box
		int sx = Integer.MAX_VALUE;
		int lx = Integer.MIN_VALUE;
		int sy = Integer.MAX_VALUE;
		int ly = Integer.MIN_VALUE;
		for(int x=0; x < img.getWidth(); x++)  {
			for(int y=0; y < img.getHeight(); y++) {
				if(regionMatrix[x][y]==maxRegion) {
					if(x < sx)
						sx = x;
					if(x > lx)
						lx = x;
					if(y < sy)
						sy = y;
					if(y > ly)
						ly = y;
					img.setRGB(x, y, c.getRGB());
				}
			}
		}

		//compute center
		double cX = (sx + lx) / 2.0;
		double cY = (sy + ly) / 2.0;

		LargestBlob rtn = new LargestBlob();
		rtn.setCX(cX);
		rtn.setCY(cY);
		rtn.setImg(img);

		return rtn;
  }
	/**
	 * Returns true If a pixel is black.
	 * @param pixel The pixel to check
	 * @return True If pixel is black
	 */
	private boolean pixelBlack(int pixel) {
		int alpha = (pixel >> 24) & 0xff;
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel) & 0xff;
		return red==0 && blue==0 && green==0;
	}
	
	/**
	 * Returns true whether a pixel is not white.
	 * @param pixel The pixel to check
	 * @return True if pixel is black
	 */
	private boolean pixelColorful(int pixel) {
		int alpha = (pixel >> 24) & 0xff;
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel) & 0xff;
		return !(red==255 && blue==255 && green==255);
	}
	
	/**
	 * Sets connection to gui schema
	 * @param guiSchema connection
	 */
	public void setGUISchema(GUISchema guiSchema) {
		this.guiSchema =  guiSchema;
	}
	
	/**
	 * Sets connection to parietal cortex
	 * @param pc parietal cortex connection
	 */
	public void setParietalCortex(ParietalCortex pc) {
		this.pc =  pc;
	}
	
	/**
	 * Helper method to convert from rgb to hsv space.
	 * @param r Red value
	 * @param g Green value
	 * @param b Blue value
	 * @return Color in HSV space
	 */
	private int[] rgb2hsv(int r, int g, int b) {
		int min;    //Min. value of RGB
		int max;    //Max. value of RGB
		int delMax; //Delta RGB value

		if (r > g) { min = g; max = r; }
		else { min = r; max = g; }
		if (b > max) max = b;
		if (b < min) min = b;

		delMax = max - min;

		float H = 0, S;
		float V = max;

		if ( delMax == 0 ) { H = 0; S = 0; }
		else {
			S = delMax/255f;
			if ( r == max )
				H = (      (g - b)/(float)delMax)*60;
			else if ( g == max )
				H = ( 2 +  (b - r)/(float)delMax)*60;
			else if ( b == max )
				H = ( 4 +  (r - g)/(float)delMax)*60;
		}
		int[] hsv = new int[3];
		hsv[0] = (int)(H);
		hsv[1] = (int)(S*100);
		hsv[2] = (int)(V*100);
		return hsv;
	}
	
	/**
	 * Converts an image with RGB pixel to an image in HSV
	 * @param i The image
	 * @return A 3-D array that pairs each (x,y) to an array[3] of hsv values
	 */
	public int[][][] imgToHSV(BufferedImage i) {
		int[][][] rtn = new int[i.getHeight()][i.getWidth()][3];

		for(int y=0; y < i.getHeight(); y++) {
			for(int x=0; x < i.getWidth(); x++) {
				int pixel = i.getRGB(x, y);
				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				int[] hsv = rgb2hsv(red,green,blue);

				//System.out.println(y + " " + x + ": " + hsv[0] + "," + hsv[1] + "," + hsv[2]);

				rtn[y][x] = hsv;
			}
		}

		return rtn;
	}
	
	/**
	 * Creates filtered image in hsv space instead of RGB space.
	 * I experimented with both RGB and HSV filtering and stuck with HSV
	 * because it had better performance. This uses the same
	 * technique as the above RGB method in terms of filtering
	 * pixels based on thresholds.
	 * 
	 * @param image The image
	 * @param hsv The hsv image representation
	 * @param hmin h minimum
	 * @param hmax h maximum
	 * @param smin s minimum
	 * @param smax s maximum
	 * @param vmin v minimum
	 * @param vmax v maximum
	 * @return
	 */
	public BufferedImage createFilteredImage(BufferedImage image, int[][][] hsv, int hmin, int hmax, int smin, int smax, int vmin, int vmax) {
		for (int i = 0; i < image.getHeight(); i++) {
		  for (int j = 0; j < image.getWidth(); j++) {
			  int h = hsv[i][j][0];
			  int s = hsv[i][j][1];
			  int v = hsv[i][j][2];

			if(h > hmin && h < hmax && s > smin && s < smax && v > vmin && v < vmax) {
				image.setRGB(j, i, Color.black.getRGB());
			}
			else {
				image.setRGB(j, i, Color.white.getRGB());
			}
		  }
		}
		return image;
	}
}
