import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class Zoom extends JFrame implements Runnable, ActionListener {

	private Robot rob;
	private int scale = 1;
	JButton up, down;

	public Zoom() {
		try {
			rob = new Robot();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		getContentPane().setLayout(new FlowLayout());
		up = new JButton("+");
		down = new JButton("-");
		getContentPane().add(up);
		getContentPane().add(down);
		up.addActionListener(this);
		down.addActionListener(this);
		this.setSize(500,500);
		this.setVisible(true);
	}

	public void paint(Graphics g) {
		BufferedImage bi = rob.createScreenCapture(new Rectangle(20,20,400,400));
		g.drawImage(bi,20,20,400*scale,400*scale, 20,20,400,400,null);
		System.out.println("Painted");
	}

	public static void main(String[] args) {
		Thread t = new Thread(new Zoom());
		t.start();
	}

	public void run() {
		for(;;) {
			try {
				Thread.sleep(300);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			this.repaint();
		}
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==up) {
			scale=scale*2;
			System.out.println("ZOOM");
		}
		if(e.getSource()==down) {
			scale=scale/2;
			System.out.println("ZOOM");
		}
		this.repaint();
	}
}

