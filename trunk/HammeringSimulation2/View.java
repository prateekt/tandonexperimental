import javax.swing.*;
import java.awt.*;

/**
 * @author Prateek Tandon
 *
 */
public class View extends JFrame {

	private Nail nail;
	
	public View(Nail nail) {
		this.nail = nail;
		setSize(500,500);
		setVisible(true);
		setBackground(Color.WHITE);
	}
	
	public void paint(Graphics g) {
		nail.draw(g);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Nail n = new Nail(20,20,40,5);
		View v = new View(n);
		
		while(true) {
			try {
				Thread.sleep(1000);
				n.hit(20, 1);
				v.repaint();
			}
			catch(Exception e) {
				e.printStackTrace();
			
			}
		}
	}
}