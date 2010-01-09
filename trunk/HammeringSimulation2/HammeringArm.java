import java.awt.*;
import java.awt.event.*;

public class HammeringArm {
	
	//constants
	protected double l1_length;
	protected double l2_length;
	protected Vector2 L1_START = new Vector2(200,200);
	protected double hammerMass;
	protected double hammerRadius;
	protected double simulationTimeStep;
	
	//state variables
	protected double theta;
	protected double angularVel;
	protected double oldTheta;
	
	//View variable
	protected View view;
	protected Nail nail;
	
	public HammeringArm(double l1_length, double l2_length, double hammerRadius, double hammerMass) {
		this.l1_length = l1_length;
		this.l2_length = l2_length;
		this.hammerRadius = hammerRadius;
		this.hammerMass = hammerMass;
		oldTheta = Integer.MIN_VALUE;
		theta = 90;
		angularVel = 0;
		simulationTimeStep = 1;
	}
	
	public void reset() {
		oldTheta = theta;
		theta = 90;
		angularVel = 0;
		System.out.println(oldTheta);
		if(view!=null) {
			view.repaint();
		}
	}
	
	public void draw(Graphics g) {
		if(oldTheta!=Integer.MIN_VALUE) {
			g.setColor(Color.white);

			//l2
			g.drawLine((int) L1_START.getX(),(int) L1_START.getY(), (int) (L1_START.getX()- l2_length* Math.cos(Math.toRadians(oldTheta))),(int) (L1_START.getY() - l2_length*Math.sin(Math.toRadians(oldTheta))));
			
			//hammer
			drawCircle((int) (L1_START.getX()- (l2_length+hammerRadius)* Math.cos(Math.toRadians(oldTheta))),(int) (L1_START.getY() - (l2_length+hammerRadius)*Math.sin(Math.toRadians(oldTheta))),(int) hammerRadius, g);

		}
		
		g.setColor(Color.black);
		
		//l1
		g.drawLine((int) L1_START.getX(),(int) L1_START.getY(), (int) (L1_START.getX() + l1_length), (int) L1_START.getY());
		
		//l2
		g.drawLine((int) L1_START.getX(),(int) L1_START.getY(), (int) (L1_START.getX()- l2_length* Math.cos(Math.toRadians(theta))),(int) (L1_START.getY() - l2_length*Math.sin(Math.toRadians(theta))));
		
		//hammer
		drawCircle((int) (L1_START.getX()- (l2_length+hammerRadius)* Math.cos(Math.toRadians(theta))),(int) (L1_START.getY() - (l2_length+hammerRadius)*Math.sin(Math.toRadians(theta))),(int) hammerRadius, g);
	}
	
	public Rectangle getHammerBoundingRect() {
		return getHammerBoundingRectHelper((int) (L1_START.getX()- (l2_length+hammerRadius)* Math.cos(Math.toRadians(theta))),(int) (L1_START.getY() - (l2_length+hammerRadius)*Math.sin(Math.toRadians(theta))),(int) hammerRadius);
	}
	
	private Rectangle getHammerBoundingRectHelper(int x, int y, int radius) {
		return new Rectangle (x - radius, y - radius, radius*2, radius*2);
	}

	private void drawCircle(int x, int y, int radius, Graphics g){
		g.drawOval(x - radius, y - radius, radius*2, radius*2);
	}
	
	public boolean executeSwing(double swingTheta, double angularAccel) {
		oldTheta = theta;
		theta = swingTheta;
		if(view!=null) {
			view.repaint();
		}
		
		int t=1;
		
		while(theta >= 0) {
			
			angularVel = angularAccel*simulationTimeStep*t;
			oldTheta = theta;
			theta = swingTheta - angularVel*simulationTimeStep*t; 
			t++;

			try {
				if(view!=null)
					view.repaint();
				Thread.sleep(1000);
			}
			catch(Exception e) {
				e.printStackTrace();
			}

			if(nail!=null) {
				Rectangle hammerBound = getHammerBoundingRect();
				Rectangle nailBound = nail.getNailBoundingRect();

				if(sprite_collide(hammerBound.getX(), hammerBound.getY(), hammerBound.getWidth(), hammerBound.getHeight(), nailBound.getX(), nailBound.getY(), nailBound.getWidth(), nailBound.getHeight())) {
					System.out.println("Collision occured.");
					
					//compute impact force
					double newVel = (2*hammerMass)/(hammerMass + nail.getNailMass());
					double changeInMomentum = nail.getNailMass()*newVel;
					double impactForce = changeInMomentum / simulationTimeStep;
					
					nail.hit(impactForce, simulationTimeStep);
					reset();
					return true;
				}
			}

		}
		
		System.out.println("No Collision occured.");
		reset();
		return false;
	}
	
	// Object-to-object bounding-box collision detector:
	public boolean sprite_collide(double obj1_x, double obj1_y, double obj1_width, double obj1_height, double obj2_x, double obj2_y, double obj2_width, double obj2_height) {
	  
	    double left1, left2;
	    double right1, right2;
	    double top1, top2;
	    double bottom1, bottom2;

	    left1 = obj1_x;
	    left2 = obj2_x;
	    right1 = obj1_x + obj1_width;
	    right2 = obj2_x + obj2_width;
	    top1 = obj1_y;
	    top2 = obj2_y;
	    bottom1 = obj1_y + obj1_height;
	    bottom2 = obj2_y + obj2_height;

	    if (bottom1 < top2) return false;
	    if (top1 > bottom2) return false;

	    if (right1 < left2) return false;
	    if (left1 > right2) return false;
	    return true;
	}
	
	public void setView(View view) {
		this.view = view;
	}
	
	public void setNail(Nail nail) {
		this.nail = nail;
	}
}
