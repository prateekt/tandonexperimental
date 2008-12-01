package gui;
import java.awt.*;
import javax.swing.*;
import schemas.*;
import schema_output.*;

public class CVPanel extends JPanel {
	
	private ForwardModel fm;
	private double doc;
	private double oa;
	private int width;
	private int height;
	private int centerHori;
	private int centerVert;
	private ControlVariableSummary cv;
	
	public CVPanel(ForwardModel fm) {
    	setLayout(null);
    	setSize(320,240);
		width = 300;
		height = 300;
		centerVert = height/2;
		centerHori = width/2;
		this.fm = fm;
	}
	
	public void setControlVariableSummary(ControlVariableSummary cv) {
		this.cv = cv;
	}
	
	public void setForwardModel(ForwardModel fm) {
		this.fm = fm;
	}
	
	public void paint(Graphics g) {
		if(cv!=null) {
			double doc_screen = doc;
			
			//center line
			double xc0 = centerHori;
			double yc0 = centerVert;
			double xc1 = xc0 - 100*Math.cos(fm.getFinalOrientationDifference());
			double yc1 = xc0 - 100*Math.sin(fm.getFinalDistanceFromCenter());
			System.out.println("HELLO: " + xc0 + " " + yc0 + " " + xc1 + " " + yc1);
			
			
			g.drawLine((int)xc0, (int)yc0,(int) xc1,(int) yc1);
			
			//parallel vector
			double xc2 = centerHori;
			double yc2 = centerVert - doc_screen;
			double xc3 = xc2 - 100*Math.cos(fm.getFinalOrientationDifference());
			double yc3 = xc2 - 100*Math.sin(fm.getFinalDistanceFromCenter());
	
			g.drawLine((int)xc2, (int)yc2,(int) xc3,(int) yc3);
			System.out.println("HELLO2: " + xc2 + " " + yc2 + " " + xc3 + " " + yc3);
	
			//difference vector
			double xc4 = centerHori;
			double yc4 = centerVert - doc_screen;
			double xc5 = xc4 - 100*Math.cos(cv.getOrientationDifference()*5);
			double yc5 = yc4 - 100*Math.sin(cv.getOrientationDifference()*5);		
			System.out.println("HELLO3: " + xc4 + " " + yc4 + " " + xc5 + " " + yc5);
			g.drawLine((int)xc4, (int)yc4,(int) xc5,(int) yc5);
		}
	}
}
