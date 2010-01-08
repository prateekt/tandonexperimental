import java.awt.*;
import java.awt.event.*;

public class HammeringArm {
	
	//constants
	protected double l1_length;
	protected double l2_length;
	protected Vector2 L1_START = new Vector2(200,200);
	protected double hammerMass;
	protected double hammerRadius;
	
	//state variables
	protected double theta = 90;
	
	public HammeringArm(double l1_length, double l2_length, double hammerRadius, double hammerMass) {
		this.l1_length = l1_length;
		this.l2_length = l2_length;
		this.hammerRadius = hammerRadius;
		this.hammerMass = hammerMass;
	}
	
	public void draw(Graphics g) {
		//l1
		g.drawLine((int) L1_START.getX(),(int) L1_START.getY(), (int) (L1_START.getX() + l1_length), (int) L1_START.getY());
		
		//l2
		g.drawLine((int) L1_START.getX(),(int) L1_START.getY(), (int) (L1_START.getX()- l2_length* Math.cos(Math.toRadians(theta))),(int) (L1_START.getY() - l2_length*Math.sin(Math.toRadians(theta))));
		
		//hammer
		drawCircle((int) (L1_START.getX()- (l2_length+hammerRadius)* Math.cos(Math.toRadians(theta))),(int) (L1_START.getY() - (l2_length+hammerRadius)*Math.sin(Math.toRadians(theta))),(int) hammerRadius, g);
	}
	
	private void drawCircle(int x, int y, int radius, Graphics g){
		g.drawOval(x - radius, y - radius, radius*2, radius*2);
	}	
}
