package gui;
import javax.swing.*;

import schema_output.VisualCortexOutput;

import webcam.*;

import java.awt.*;
import java.awt.image.*;

/**
 * Represents the main JFrame object.
 * @author Prateek Tandon
 *
 */
public class GUI extends JFrame {
	
	/**
	 * The panel for drawing the color segmented image.
	 */
	private TaggedImagePanel taggedImagePanel = null;

	/**
	 * The panel for displaying the discounted difference graph.
	 */
	private TriGraphPanel ddPanel = null;

	/**
	 * The panel for displaying the believed inferred mental state.
	 */
	private TriGraphPanel probPanel = null;

	/**
	 * The panel for displaying the simulated and observed history
	 * of the distance of the hand from the hammer center.
	 */
	private CVComparisonPanel docPanel;

	/**
	 * The panel for displaying the simulated and observed angular
	 * orientation difference between the knuckle vector and the 
	 * hammer handle vector.
	 */
	private CVComparisonPanel oaPanel;
	
	/**
	 * Panel for buttons
	 */
	private ButtonPanel buttonPanel;
	
	/**
	 * Constructor for GUI
	 * @param cam The camera being used (either mock or real)
	 */
	public GUI(WebCam cam) {
		super("Mental State Inference (Prateek Tandon)");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		taggedImagePanel = new TaggedImagePanel(this);
		ddPanel = new TriGraphPanel(this, "Discounted Difference");
		probPanel = new TriGraphPanel(this, "Belief Probability");
		docPanel = new CVComparisonPanel(this, "Distance to Hammer");
		oaPanel = new CVComparisonPanel(this, "Angular Orientation Difference");
//		add(cam.getCameraFeed(), BorderLayout.NORTH);
//		add(taggedImagePanel, BorderLayout.SOUTH);

		//MS Panel
		JPanel msPanel = new JPanel();
		msPanel.setLayout(null);
		msPanel.add(probPanel);
		msPanel.add(ddPanel);
		probPanel.setBounds(0,300,300,300);
		ddPanel.setBounds(0,0,300,300);

		//CV Panel
		JPanel cvPanel = new JPanel();
		cvPanel.setLayout(null);
		cvPanel.add(oaPanel);
		cvPanel.add(docPanel);
		oaPanel.setBounds(0,0,300,300);
		docPanel.setBounds(0,300,300,300);		
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Belief Inference", msPanel);
		tabbedPane.addTab("Control Variables", cvPanel);
		
		add(tabbedPane);
		tabbedPane.setBounds(10,10,300,630);
		
		JPanel tvPanel = new JPanel();
		tvPanel.setLayout(null);
		tvPanel.add(cam.getCameraFeed());
		cam.getCameraFeed().setBounds(0,0,300,300);
		tvPanel.add(taggedImagePanel);
		taggedImagePanel.setBounds(0,300,300,300);
		
		JTabbedPane tvTabbedPane = new JTabbedPane();
		tvTabbedPane.addTab("Video Input", tvPanel);
		
		add(tvTabbedPane);
		tvTabbedPane.setBounds(325,10,300,630);
		
		buttonPanel = new ButtonPanel(this);
		getContentPane().add(buttonPanel);
		buttonPanel.setBounds(50, 645, 600, 100);
		
		this.pack();
		this.setSize(new Dimension(645,715));
		this.setResizable(false);
		this.setVisible(true);		
	}
	
	/**
	 * Sets the color segmented image onto necessary panel.
	 * @param input  The input from the visual cortex that does the
	 * color segmentation
	 */
	public void setVisualInput(VisualCortexOutput input) {
		BufferedImage displayImage = guize(input);

		// show the image
		taggedImagePanel.setImage(displayImage);		
	}

	/**
	 * Draws the hammer handle vector and knuckle vector
	 * on the color segmented image.
	 * @param out The color segmented image from the visual
	 * cortex
	 * @return The modified image
	 */
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
					if(y <= Math.max(cY1, cY2) && y >= Math.min(cY1, cY2) && x <= Math.max(cX1, cX2) && x >= Math.min(cX1,cX2))
						img.setRGB(x, y, Color.CYAN.getRGB());
				}
			}
		}
		
		double dX1 = out.getPinkTag().getCX();
		double dY1 = out.getPinkTag().getCY();
		double dX2 = out.getYellowTag().getCX();
		double dY2 = out.getYellowTag().getCY();
		double m2 = (dY2 - dY1) / (dX2 - dX1);

		//draw line for hand
		for(int x=0; x < img.getWidth(); x++) {
			for(int y=0; y < img.getHeight(); y++) {
				//line condition
				if(y==((int) (m2*x + dY1 - m2*dX1))) {
					if(y <= Math.max(dY1, dY2) && y >= Math.min(dY1, dY2) && x <= Math.max(dX1, dX2) && x >= Math.min(dX1,dX2))
						img.setRGB(x, y, Color.MAGENTA.getRGB());
				}
			}
		}		
			
		return img;
	}
	
	/**
	 * Returns the discounted difference panel
	 * @return The discounted difference panel
	 */
	public TriGraphPanel getDDPanel() {
		return ddPanel;
	}
	
	/**
	 * Returns the prob panel.
	 * @return The beliefs about inferred mental state graph
	 */
	public TriGraphPanel getProbPanel() {
		return probPanel;
	}
	
	/**
	 * Returns the distance from center graph panel.
	 * @return DOCPanel
	 */
	public CVComparisonPanel getDOCPanel() {
		return docPanel;
	}
	
	/**
	 * Returns the angular orientation graph panel.
	 * @return OAPanel
	 */
	public CVComparisonPanel getOAPanel() {
		return oaPanel;
	}
	
	/**
	 * Returns Button Panel
	 */
	public ButtonPanel getButtonPanel() {
		return buttonPanel;
	}
}

