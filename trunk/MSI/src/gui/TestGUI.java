package gui;
import java.awt.*;
import javax.swing.*;
import schemas.*;
import schema_output.*;

public class TestGUI extends JFrame {
	
	private ForwardModel testModel;
	
	public TestGUI() {
		testModel = new HoldingForwardModel();
//		ComparisonPanel p = new ComparisonPanel(testModel);		
//		getContentPane().add(p);
		setSize(500,500);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new TestGUI();
	}
}
