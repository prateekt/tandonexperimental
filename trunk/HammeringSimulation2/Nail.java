import java.awt.*;

public class Nail {
	
	//constants
	protected static final Vector2 nailStart = new Vector2(200,200);
	protected double r1;
	protected double r2;
	protected double length;
	protected double nailMass;
	protected double maxNormalForce = 50;
	protected double frictionCoeff = 0.5;
	
	//state variables
	protected Vector2 nailHeadStart;
	protected double amountIntoBoard;
	protected double amountBent;
	
	//old vars
	protected Vector2 oldNailHeadStart;
	
	public Nail(double r1, double r2, double length, double nailMass) {
		this.r1 = r1;
		this.r2 = r2;
		this.length = length;
		this.nailMass = nailMass;
		
		nailHeadStart = new Vector2(nailStart.getX(), nailStart.getY());
		amountIntoBoard = 0;
		amountBent = 0;
		oldNailHeadStart = new Vector2(-1,-1);
	}
	
	public boolean collision(double x, double y) {
		return (x > nailHeadStart.getX()) && 
			   (x < (nailHeadStart.getX()) + r1*2) &&
			   (y > nailHeadStart.getY()) &&
			   (y < (nailHeadStart.getY()) + r1);
	}
	
	public void draw(Graphics g) {
		if(oldNailHeadStart.getX()!=-1 && oldNailHeadStart.getY()!=-1) {
			g.setColor(Color.WHITE);
			g.drawOval((int)oldNailHeadStart.getX(), (int)oldNailHeadStart.getY(), (int)r1*2,(int)r1);
			double rectStartX = oldNailHeadStart.getX() + r1/2;
			double rectStartY = oldNailHeadStart.getY() + r1;
			g.drawRect((int) rectStartX, (int) rectStartY, (int) r2, (int) length);
		}
		
		g.setColor(Color.BLACK);
		g.drawOval((int)nailHeadStart.getX(), (int)nailHeadStart.getY(), (int)r1*2,(int)r1);
		double rectStartX = nailHeadStart.getX() + r1/2;
		double rectStartY = nailHeadStart.getY() + r1;
		g.drawRect((int) rectStartX, (int) rectStartY, (int) r2, (int) length);
	}
	
	public void hit(double impactForce, double time) {
		double netForce = impactForce - getFrictionForce();
		
		//only do update if impact force overcomes friction
		if(netForce > 0) {
			double accel = netForce / nailMass;
			
			//compute and new position
			oldNailHeadStart.setX(nailHeadStart.getX());
			oldNailHeadStart.setY(nailHeadStart.getY());
			double posUpdate = accel * time * time;
			amountIntoBoard += posUpdate;
			nailHeadStart.setY(nailHeadStart.getY() + posUpdate);
			
			System.out.println("HIT: " + " " + impactForce + " " + posUpdate + " " + amountIntoBoard);
		}
		else {
			System.out.println("HIT: " + " " + impactForce + " " + getFrictionForce() + " STATIC FRICTION FAIL");
			
		}
	}
	
	public double getFrictionForce() {
		return frictionCoeff * getNormalForce(amountIntoBoard);
	}
	
	public double getNormalForce(double x) {
		return maxNormalForce * (x/length);
	}
}