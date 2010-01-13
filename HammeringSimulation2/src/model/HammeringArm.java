package src.model;

import java.awt.*;
import java.awt.event.*;

import src.gui.View;

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
	
	public VisualResult executeSwing(double swingTheta, double angularAccel) {
		oldTheta = theta;
		theta = swingTheta;
		if(view!=null) {
			view.repaint();
		}
		
		int t=1;
		double zNoise = 0;
		while(theta >= 0) {
			
			//update thetas
			angularVel = angularAccel*simulationTimeStep*t;
			oldTheta = theta;
			theta = swingTheta - angularVel*simulationTimeStep*t; 
			
			//get noise
			double noise = getVelocityDependentNoise(angularVel);
				
			//add sign to the noise randomly
			double rand = Math.random();
			if(rand > 0.5) {
				noise*=-1;
			}
		
			//update cum noise
			zNoise+=noise;			
	
			//update view
			if(view!=null) {
				view.getGraphScreen().addInstNoiseSeriesPoint(t, noise);
				view.getGraphScreen().addCumNoiseSeriesPoint(t, zNoise);
			}
			
			//update t
			t++;
			
			//repaint view
			try {
				if(view!=null)
					view.repaint();
//				Thread.sleep(1000);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
			//do collision detection
			if(nail!=null) {
				Rectangle hammerBound = getHammerBoundingRect();
				Rectangle nailBound = nail.getNailBoundingRect();

				if(sprite_collide(hammerBound.getX(), hammerBound.getY(), hammerBound.getWidth(), hammerBound.getHeight(), nailBound.getX(), nailBound.getY(), nailBound.getWidth(), nailBound.getHeight())) {
//					System.out.println("Collision occured.");
					
					//compute impact force
					double linearVelocity = angularVel * l2_length;
					double newVel = (2*hammerMass)/(hammerMass + nail.getNailMass()) * linearVelocity;
					double changeInMomentum = nail.getNailMass()*newVel;
					double impactForce = changeInMomentum / (simulationTimeStep*100);
					
					nail.hit(impactForce, zNoise, simulationTimeStep);
					reset();
					VisualResult rtn = new VisualResult(true, nail.nailBent(), nail.nailIn(), nail.getAmountIntoBoard(), nail.getAmountBent(), nail.getLastAmountIntoBoardUpdate(), nail.getLastAmountBentUpdate());
					return rtn;
				}
			}
		}
		
//		System.out.println("No Collision occured.");
		reset();
		VisualResult rtn = new VisualResult(false, nail.nailBent(), nail.nailIn(), nail.getAmountIntoBoard(), nail.getAmountBent(), 0, 0);
		return rtn;
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
	
	public double getVelocityDependentNoise(double vel) {
		double randomNumber = Math.random();
		double rtn = computeGaussian(randomNumber, 0, Math.sqrt(vel));
		return rtn;
	}
	
	public double computeGaussian(double x, double mean, double stddev) {
		double expTerm = -1*Math.pow(x-mean, 2) / (2*stddev*stddev);
		return 1/(stddev*Math.sqrt(2*Math.PI)) * Math.exp(expTerm);
	}

	public double getL1_length() {
		return l1_length;
	}

	public void setL1_length(double l1_length) {
		this.l1_length = l1_length;
	}

	public double getL2_length() {
		return l2_length;
	}

	public void setL2_length(double l2_length) {
		this.l2_length = l2_length;
	}

	public Vector2 getL1_START() {
		return L1_START;
	}

	public void setL1_START(Vector2 l1_start) {
		L1_START = l1_start;
	}

	public double getHammerMass() {
		return hammerMass;
	}

	public void setHammerMass(double hammerMass) {
		this.hammerMass = hammerMass;
	}

	public double getHammerRadius() {
		return hammerRadius;
	}

	public void setHammerRadius(double hammerRadius) {
		this.hammerRadius = hammerRadius;
	}

	public double getSimulationTimeStep() {
		return simulationTimeStep;
	}

	public void setSimulationTimeStep(double simulationTimeStep) {
		this.simulationTimeStep = simulationTimeStep;
	}

	public double getTheta() {
		return theta;
	}

	public void setTheta(double theta) {
		this.theta = theta;
	}

	public double getAngularVel() {
		return angularVel;
	}

	public void setAngularVel(double angularVel) {
		this.angularVel = angularVel;
	}

	public double getOldTheta() {
		return oldTheta;
	}

	public void setOldTheta(double oldTheta) {
		this.oldTheta = oldTheta;
	}

	public View getView() {
		return view;
	}

	public Nail getNail() {
		return nail;
	}
}
