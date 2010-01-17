package src.env_model;

import java.awt.*;


public class Nail {
	
	//constants
	protected static final Vector2 nailStart = new Vector2(50,200);
	protected static final double NAIL_OFFSET = 10;
	protected static final double NAIL_BENT_RIGHT = 180;
	protected static final double NAIL_BENT_LEFT = 0;
	protected static final double BOARD_LENGTH = 10;
	
	protected double r1;
	protected double r2;
	protected double length;
	protected double nailMass;
	protected double maxNormalForce = 1000;
	protected double frictionCoeff = 0.1;
	
	//state variables
	protected Vector2 nailHeadStart;
	protected double amountIntoBoard;
	protected double amountBent;
	protected double lastAmountIntoBoardUpdate;
	protected double lastAmountBentUpdate;
	
	//old vars
	protected Vector2 oldNailHeadStart;
	
	//managers
	protected NailBendManager nailBendManager;
	
	//hammering arm
	protected HammeringArm arm;
	
	public Nail(double r1, double r2, double length, double nailMass) {
		this.r1 = r1;
		this.r2 = r2;
		this.length = length;
		this.nailMass = nailMass;
		
		nailHeadStart = new Vector2(nailStart.getX(), nailStart.getY());
		amountIntoBoard = 0;
		amountBent = 90;
		oldNailHeadStart = new Vector2(-1,-1);
		nailBendManager = new NailBendManager();
	}
	
	public void reset() {
		nailHeadStart = new Vector2(nailStart.getX(), nailStart.getY());
		amountIntoBoard = 0;
		amountBent = 90;
		oldNailHeadStart = new Vector2(-1,-1);
		nailBendManager = new NailBendManager();
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
	
	public void draw_bend(Graphics g) {
		g.drawLine(200, 200, 200, 150);
		g.setColor(Color.blue);
		g.drawLine(200, 150, (int) (200 + 100*Math.cos(Math.toRadians(amountBent))), (int) (150 - 100*Math.sin(Math.toRadians(amountBent))));
	}
	
	public Rectangle getNailBoundingRect() {
		return new Rectangle((int)nailHeadStart.getX(), (int)nailHeadStart.getY(), (int)r1*2,(int)r1);
	}
	
	public void hit(double impactForce, double zNoise, double time) {
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
			
			//compute new bend
			double x1 = zNoise;
			double x2 = zNoise + arm.getHammerRadius()*2;
			double x3 = -1*NAIL_OFFSET/90 * amountBent + 2*NAIL_OFFSET;
			double f = NAIL_OFFSET + 2*r1;
			double cent = NAIL_OFFSET + r1;
			double x4 = -1*(f - cent)/90 * amountBent + (2*f - cent);
			double bendUpdate = nailBendManager.getBendUpdate(x1, x2, x3, x4, impactForce);
			amountBent+=bendUpdate;
			lastAmountIntoBoardUpdate = posUpdate;
			lastAmountBentUpdate = bendUpdate;
//			System.out.println("BEND UPDATE: " + bendUpdate + " " + x1 + " " + x2 + " " + x3 + " " + x4);		
//			System.out.println("HIT: " + " " + impactForce + " " + posUpdate + " " + amountIntoBoard);
		}
		else {
//			System.out.println("HIT: " + " " + impactForce + " " + getFrictionForce() + " STATIC FRICTION FAIL");
		}
	}
	
	public double getFrictionForce() {
		return frictionCoeff * getNormalForce(amountIntoBoard);
	}
	
	public double getNormalForce(double x) {
		return maxNormalForce * (x/length);
	}

	public double getNailMass() {
		return nailMass;
	}

	public void setNailMass(double nailMass) {
		this.nailMass = nailMass;
	}

	public double getR1() {
		return r1;
	}

	public void setR1(double r1) {
		this.r1 = r1;
	}

	public double getR2() {
		return r2;
	}

	public void setR2(double r2) {
		this.r2 = r2;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getMaxNormalForce() {
		return maxNormalForce;
	}

	public void setMaxNormalForce(double maxNormalForce) {
		this.maxNormalForce = maxNormalForce;
	}

	public double getFrictionCoeff() {
		return frictionCoeff;
	}

	public void setFrictionCoeff(double frictionCoeff) {
		this.frictionCoeff = frictionCoeff;
	}

	public Vector2 getNailHeadStart() {
		return nailHeadStart;
	}

	public void setNailHeadStart(Vector2 nailHeadStart) {
		this.nailHeadStart = nailHeadStart;
	}

	public double getAmountIntoBoard() {
		return amountIntoBoard;
	}

	public void setAmountIntoBoard(double amountIntoBoard) {
		this.amountIntoBoard = amountIntoBoard;
	}

	public double getAmountBent() {
		return amountBent;
	}

	public void setAmountBent(double amountBent) {
		this.amountBent = amountBent;
	}

	public Vector2 getOldNailHeadStart() {
		return oldNailHeadStart;
	}

	public void setOldNailHeadStart(Vector2 oldNailHeadStart) {
		this.oldNailHeadStart = oldNailHeadStart;
	}

	public NailBendManager getNailBendManager() {
		return nailBendManager;
	}

	public void setNailBendManager(NailBendManager nailBendManager) {
		this.nailBendManager = nailBendManager;
	}

	public HammeringArm getArm() {
		return arm;
	}

	public void setArm(HammeringArm arm) {
		this.arm = arm;
	}

	public static Vector2 getNailStart() {
		return nailStart;
	}
	
	public boolean nailBent() {
		return amountBent >= NAIL_BENT_RIGHT || amountBent <= NAIL_BENT_LEFT;
	}
	
	public boolean nailIn() {
		return amountIntoBoard >= BOARD_LENGTH;
	}

	public double getLastAmountIntoBoardUpdate() {
		return lastAmountIntoBoardUpdate;
	}

	public void setLastAmountIntoBoardUpdate(double lastAmountIntoBoardUpdate) {
		this.lastAmountIntoBoardUpdate = lastAmountIntoBoardUpdate;
	}

	public double getLastAmountBentUpdate() {
		return lastAmountBentUpdate;
	}

	public void setLastAmountBentUpdate(double lastAmountBentUpdate) {
		this.lastAmountBentUpdate = lastAmountBentUpdate;
	}
	
	public String getNailStateDesc() {
		if(nailIn()) {
			return "The nail is in.";
		}
		if(nailBent()) {
			return "The nail is bent.";
		}
		
		return "The nail is " + amountIntoBoard + " into board w/ " + amountBent + " bent.";
	}

	public int getNailState() {

		//state 0 - HIT_STATE
		//state 1 - NAILING_COMPLETE
		//state 2 - NAIL_BENT
		//nail in takes precedence over nail bent
		
		if(nailIn()) {
			return 1;
		}
		if(nailBent()) {
			return 2;
		}
		
		return 0;
	}

}