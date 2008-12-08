package evaluation;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import webcam.*;

public class ImageAnnotator extends JFrame implements MouseListener {
	
	private MockWebCam webcam;
	Point[] points;
	int index =0;
	private Image img;
	int t=0;
	
	public ImageAnnotator(MockWebCam webcam) {
		this.webcam = webcam;
		points = new Point[4];
		img = webcam.grabFrame(1).get(0);
		addMouseListener(this);
		this.setSize(300,240);
		this.setVisible(true);
	}
	
	public void paint(Graphics g) {
		g.drawImage(img, 0, 0, null);
	}
	
	public void mouseClicked(MouseEvent e) {
		points[index] = e.getPoint();
		index++;
		
		if(index==4) {
			StringBuffer rtn = new StringBuffer();
			rtn.append(t);
			for(int x=0; x  < index; x++) {
				rtn.append(",");
				rtn.append(points[x].getX() + "," + points[x].getY());
			}
			System.out.println(rtn.toString());
			
			//reset
			t++;
			index = 0;
			img = webcam.grabFrame(1).get(0);
			repaint();
		}
	}
	
	public void mousePressed(MouseEvent e) {
	}
	
	public void mouseReleased(MouseEvent e) {
	}
	
	public void mouseExited(MouseEvent e) {
	}
	
	public void mouseEntered(MouseEvent e) {
	}
	
	public static void main(String[] args) {
		new ImageAnnotator(new MockWebCam("trials/trial1/"));
	}
}
