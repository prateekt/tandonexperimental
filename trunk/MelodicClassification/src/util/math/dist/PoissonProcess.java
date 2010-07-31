package util.math.dist;

public class PoissonProcess {
	
	private double l;
	
	public PoissonProcess(double l) {
		this.l = l;
	}
	
	public double apply(int n) {
		return Math.pow(l, n) * Math.exp(-1*l)*factorial(n);
	}
	
	private int factorial(int n) {
		if(n==0)
			return 1;
		else 
			return n*factorial(n-1);
	}
	
}
