package src.model;

public class NailBendManager {
	
	public static final double BEND_CONSTANT = 0.5;
	
	public double getBendUpdate(double x1, double x2, double x3, double x4, double impactForce) {
		
		//case 1 - if hammer touches only one side 
		//--> bend inverse prop to coverage and prop to force
		if(touchRightSide(x1,x2,x3,x4) && !touchLeftSide(x1,x2,x3,x4)) {
			return BEND_CONSTANT *impactForce / getRightCoverageProp(x1,x2,x3,x4);
		}
		if(!touchRightSide(x1,x2,x3,x4) && touchLeftSide(x1,x2,x3,x4)) {
			return -1* BEND_CONSTANT * impactForce / getLeftCoverageProp(x1,x2,x3,x4);
		}
		
		//case 2 - if hammer touches both sides but covers only one side
		//--> bend inverse prop to coverage and prop to impactForce
		if(coverRightSide(x1,x2,x3,x4) && !coverLeftSide(x1,x2,x3,x4)) {
			return BEND_CONSTANT *impactForce / getRightCoverageProp(x1,x2,x3,x4);
		}
		if(!coverRightSide(x1,x2,x3,x4) && coverLeftSide(x1,x2,x3,x4)) {
			return -1* BEND_CONSTANT * impactForce / getLeftCoverageProp(x1,x2,x3,x4);
		}
		
		//case 3 - hammer touches and covers both sides --> NO_BEND
		return 0.0;
	}
	
	public double getRightCoverageProp(double x1, double x2, double x3, double x4) {
		double cent = (x4 - x3) /2;
		if(!touchRightSide(x1,x2,x3,x4)) {
			return 0.0;
		}
		else if(coverRightSide(x1,x2,x3,x4)) {
			return 1.0;
		}
		else {
			if(x2 < x4) {
				return (x2 - cent) / (x4 - cent);
			}
			else {
				return (x4-x1) / (x4 - cent);
			}
		}
	}
	
	public double getLeftCoverageProp(double x1, double x2, double x3, double x4) {
		double cent = (x4 - x3) /2;
		if(!touchLeftSide(x1,x2,x3,x4)) {
			return 0.0;
		}
		else if(coverLeftSide(x1,x2,x3,x4)) {
			return 1.0;
		}
		else {
			if(x1 > x3) {
				return (cent - x1) / (cent - x3);
			}
			else {
				return (x2 - x3) /  (cent - x3);
			}
		}
	}
	
	public boolean touchRightSide(double x1, double x2, double x3, double x4) {
		double cent = (x4 - x3) /2;
		return !(x2 < cent) && !(x1 > x4);
	}
	
	public boolean coverRightSide(double x1, double x2, double x3, double x4) {
		double cent = (x4 - x3) /2;
		return touchRightSide(x1,x2,x3,x4) && !(x1 > cent) && !(x2 < x4);
	}
	
	public boolean touchLeftSide(double x1, double x2, double x3, double x4) {
		double cent = (x4 - x3) /2;
		return !(x1 > cent) && !(x2 < x3);
	}
	
	public boolean coverLeftSide(double x1, double x2, double x3, double x4) {	
		double cent = (x4 - x3) /2;
		return touchLeftSide(x1,x2,x3,x4) && !(x2 < cent) && !(x1 > x3);
	}
}