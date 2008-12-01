package gui;
import javax.swing.*;

import schema_output.VisualCortexOutput;

import webcam.WebCam;

import java.awt.*;
import java.awt.image.*;
import util.*;

public class GUI extends JFrame {
	private TaggedImagePanel taggedImagePanel = null;
	public  CVPanel cvPanel;
//	private WebCam cam = null;
//	private VisualCortex vc;

	public GUI(WebCam cam) {
		setLayout(new BorderLayout());
		setSize(320,550);
		taggedImagePanel = new TaggedImagePanel();
		add(cam.getCameraFeed(), BorderLayout.NORTH);
		add(taggedImagePanel, BorderLayout.SOUTH);
//		cvPanel = new CVPanel(Constants.getForwardModels().get(1));
//		add(cvPanel,BorderLayout.SOUTH);
		this.pack();
		this.setSize(new Dimension(320,550));
//		Thread t = new Thread(this);
//		t.start();
		this.setVisible(true);
		
	}

/*	public void run() {
		while(true) {
			try {
				Thread.sleep(1000);
			}
			catch(Exception ez) {
				ez.printStackTrace();
			}

			java.util.List<BufferedImage> currentImageCopies = cam.grabFrame(3);
			VisualCortexOutput out = vc.tagImage(currentImageCopies);
			BufferedImage displayImage = guize(out);

			// show the image
			taggedImagePanel.setImage(displayImage);
		}
	}*/
	
	public void setVisualInput(VisualCortexOutput input) {
		BufferedImage displayImage = guize(input);

		// show the image
		taggedImagePanel.setImage(displayImage);		
	}

	public BufferedImage guize(VisualCortexOutput out) {

		BufferedImage img = out.getTaggedImage();
		if(img==null)
			return null;

		double cX1 = out.getBlueTag().getCX();
		double cY1 = out.getBlueTag().getCY();
		double cX2 = out.getGreenTag().getCX();
		double cY2 = out.getGreenTag().getCY();

		double m = (cY2 - cY1) / (cX2 - cX1);

		//draw line for hammer line
		for(int x=0; x < img.getWidth(); x++) {
			for(int y=0; y < img.getHeight(); y++) {

				//line condition
				if(y==((int) (m*x + cY1 - m*cX1))) {
					img.setRGB(x, y, Color.black.getRGB());
				}
			}
		}
		
		double dX1 = out.getPinkTag().getCX();
		double dY1 = out.getPinkTag().getCY();
		double dX2 = out.getYellowTag().getCX();
		double dY2 = out.getYellowTag().getCY();
		double m2 = (dY2 - dY1) / (dX2 - dX1);

		//draw line for hammer line
		for(int x=0; x < img.getWidth(); x++) {
			for(int y=0; y < img.getHeight(); y++) {

				//line condition
				if(y==((int) (m2*x + dY1 - m*dX1))) {
					img.setRGB(x, y, Color.black.getRGB());
				}
			}
		}		
		
		
		return img;
	}
}

class TaggedImagePanel extends Panel {
	public Image myimg = null;

    public TaggedImagePanel() {
    	setLayout(null);
    	setSize(320,240);
    }

    public void setImage(Image img) {
    	this.myimg = img;
    	repaint();
    }

    public void paint(Graphics g) {
    	if (myimg != null) {
			g.drawImage(myimg, 0, 0, this);
      	}
    }
}
