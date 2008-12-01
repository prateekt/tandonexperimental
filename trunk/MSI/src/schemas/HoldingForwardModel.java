package schemas;

public class HoldingForwardModel extends ForwardModel {
	
	public HoldingForwardModel() {
		super("Holding Forward Model");
		finalDistanceFromCenter = 300.0;
		finalOrientationDifference = Math.PI / 2;
	}
}
