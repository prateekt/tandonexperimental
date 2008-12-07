package gui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.*;

/**
 * Panel to display the color segmented image in.
 * @author Prateek Tandon
 *
 */
public class TaggedImagePanel extends JPanel {
	
	/**
	 * The image to display.
	 */
	private Image myimg = null;

	/**
	 * The owner of the panel.
	 */
	private Component owner;
	
	/**
	 * The constructor 
	 * @param owner Owner of the panel to render images
	 */
    public TaggedImagePanel(Component owner) {
    	setLayout(null);
    	setSize(300,300);
    	this.owner = owner;
    }

    /**
     * Sets the image
     * @param img The image to draw
     */
    public void setImage(Image img) {
    	this.myimg = img;
    	repaint();
    }

    /**
     * Paints the image in the panel.
     */
    public void paint(Graphics g) {
    	if (myimg != null) {
			g.drawImage(myimg, 0, 0, 300,300,owner);
      	}
    }
}
