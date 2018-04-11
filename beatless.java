import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class beatless extends JFrame implements ActionListener, MouseListener, MouseMotionListener
{
	private static boolean USE_CROSS_PLATFORM_UI = true;
	private screen sc;
	private Point location;
	private MouseEvent pressed;
	public beatless() {
		super("");
		setSize(250, 500);
		// center window
		setLocation((((Toolkit.getDefaultToolkit()).getScreenSize()).width - 250) / 2, (((Toolkit.getDefaultToolkit()).getScreenSize()).height - 500) / 2);
		this.setBackground(Color.lightGray);
		// absolute positioning
		setLayout(null);
		// no title bar
		setUndecorated(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		sc = new screen();
		sc.addKeyListener(sc);
		add(sc);
		this.addKeyListener(sc);
		JButton main = new JButton("");
		main.setForeground(Color.lightGray);
		main.setBackground(Color.lightGray);
		main.setLocation(5, 5);
		main.setSize(10, 10);
		main.addActionListener(this);
		main.addKeyListener(sc);
		add(main);
		setVisible(true);
	}
	public void mousePressed(MouseEvent me) {
		pressed = me;
	}
	public void mouseClicked(MouseEvent me) {
	}
	public void mouseReleased(MouseEvent me) {
	}
	// title bar drags window
	public void mouseDragged(MouseEvent me) {
		location = getLocation(location);
		int x = location.x - pressed.getX() + me.getX();
		int y = location.y - pressed.getY() + me.getY();
		setLocation(x, y);
	}
	public void mouseMoved(MouseEvent me) {
	}
	public void mouseEntered(MouseEvent me) {
	}
	public void mouseExited(MouseEvent me) {
	}
	// paint the title
	public void paint(Graphics g) {
		g.setColor(new Color(122, 138, 153));
		g.setFont(new Font("Verdana", Font.PLAIN, 10));
		g.drawString("/ / b e a t l e s s", 150, 13);
	}
	public void actionPerformed(ActionEvent ae) {
		System.exit(0);
	}
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			beatless mf = new beatless();
			mf.sc.enableBuffer();
			System.out
					.println("Controls:\n <- arrow: left\n -> arrow: right\n ctrl: left beam\n alt: right beam\n p: pause\n h: HARD MODE\n\nWhat to do:\n 'capture' the grey boxes and shoot the white ones.\n each missed shot is -10 points\n each shot hit is 30 points\n each box captured is 10 points\n game over when you miss a box.");
			while (true)
				mf.sc.refresh();
		} catch (Exception e) {
		}
	}
}