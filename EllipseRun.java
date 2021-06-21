// Ellipse Run by Jason Kim
// made during a physics class bc i was bored

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class EllipseRun {
	public static void main(String[] args) {
		// graphics
		JFrame frame = new JFrame("3D Graphics");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		EllipseRunPanel panel = new EllipseRunPanel(screenSize.getWidth(), screenSize.getHeight());
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);
		frame.setVisible(true);
	}
}

class EllipseRunPanel extends JPanel {

	// debug
	boolean debug = true;

	// screen
	int screenWidth, screenHeight;

	// keys
	private KeyHandler keyHandler;

	// objects
	ArrayList<Rectangle> blocks = new ArrayList<Rectangle>();
	boolean pause = true;

	// player
	Point player;
	int jumpLimit = -250;
	boolean jump = true;
	long startTime, timeLength;
	boolean lose = false;
	int score = 0;

	public EllipseRunPanel(double width, double height) {
		// screen
		screenWidth = (int) width;
		screenHeight = (int) height;

		// keys
		this.keyHandler = new KeyHandler();
		addKeyListener(this.keyHandler);
		setFocusable(true);

		// blocks
		blocks.add(new Rectangle(screenWidth - 50, -100, 25, 50));

		// player
		player = new Point(screenWidth - 1100, -100);
	}

	// painting
	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// background
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, screenWidth, screenHeight);

		g.setColor(Color.BLACK);
		g.setFont(new Font("Sans Serif", Font.PLAIN, 40));
		g.drawString("Ellipse Run", 10, 50);
		g.setFont(new Font("Sans Serif", Font.PLAIN, 14));
		g.drawString("Score: " + score, 10, 80);

		// setting origin
		g.translate(0, screenHeight);

		// objects
		g.fillOval((int) player.getX(), (int) player.getY(), 25, 50);
		for (int i = 0; i < blocks.size(); i++) {
			g.fillRect((int) blocks.get(i).getLocation().getX(), (int) blocks.get(i).getLocation().getY(), 25, 50);
		}

		// time
		try {
			TimeUnit.MICROSECONDS.sleep(8333);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// detection
		for (int i = 0; i < blocks.size(); i++) {
			if (blocks.get(i).contains(player)) {
				lose = true;
				break;
			}
		}

		// player
		if (timeLength > 125) {
			jumpLimit = -250;
		} else {
			jumpLimit = -200;
		}
		if (player.getY() != -100) {
			if (player.getY() <= jumpLimit) {
				jump = false;
			}
			if (jump == true) {
				player.setLocation(player.getX(), player.getY() - 10);
			} else if (jump == false) {
				player.setLocation(player.getX(), player.getY() + 10);
			}
		}

		// blocks
		for (int i = 0; i < blocks.size(); i++) {
			blocks.get(i).setLocation((int) blocks.get(i).getLocation().getX() - 10, (int) blocks.get(i).getLocation().getY());

			// attempts to remove blocks off the screen
			if (blocks.get(i).getLocation().getX() == -50) {
				blocks.remove(i);
				i--;
			}
		}

		// generating
		if (Math.random() > 0.9) {
			if (pause == false) {
				blocks.add(new Rectangle(screenWidth - 50, -100, 5, 50));
				pause = true;
			} else {
				if (blocks.get(blocks.size() - 1).getLocation().getX() < screenWidth - (Math.random() * 100 + 200)) {
					pause = false;
				}
			}
		}

		if (lose == false) {
			repaint();
			score++;
		}
	}

	class KeyHandler extends KeyAdapter {

		public void keyPressed(KeyEvent e) {
			// jumping
			if ((e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_SPACE) && player.getY() == -100) {
				player.setLocation(player.getX(), player.getY() - 10);
				jump = true;
				startTime = System.currentTimeMillis();
			}
		}

		public void keyReleased(KeyEvent e) {
			// stop program
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				System.exit(0);
			}
			// jump time
			if ((e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_SPACE)) {
				timeLength = System.currentTimeMillis() - startTime;

				// debug
				if (debug == true) {
					System.out.println("Time: " + timeLength);
				}

				if (lose == true) {
					lose = false;
					blocks.clear();
					blocks.add(new Rectangle(screenWidth - 50, -100, 25, 50));
					pause = true;
					score = 0;
					repaint();
				}

			}
		}

		public void keyTyped(KeyEvent e) {
		}
	}

}