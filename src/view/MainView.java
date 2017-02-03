package view;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainView extends JFrame implements WindowListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MainPanel mainPanel;
	private ImageIcon background = new ImageIcon(getClass().getResource("/ImmaginePiccola.png"));
	private Dimension dim;
	
	public MainView(){
		this.mainPanel = new MainPanel();
		this.setResizable(false);
		Dimension d = new Dimension(this.background.getIconWidth(),
				this.background.getIconHeight());
		this.setMaximumSize(d);
		this.setSize(d);
		this.setTitle("View");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addWindowListener(this);
		this.getContentPane().add(this.mainPanel);
		this.dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((this.dim.width - d.width) / 2,
				(this.dim.height - d.height) / 2);
		this.setVisible(true);
	}

	@Override
	public void windowOpened(WindowEvent e) {		
	}

	@Override
	public void windowClosing(WindowEvent e) {		
	}

	@Override
	public void windowClosed(WindowEvent e) {		
	}

	@Override
	public void windowIconified(WindowEvent e) {		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {		
	}

	@Override
	public void windowActivated(WindowEvent e) {		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {		
	}
	
	public void replacePanel(JPanel panel) {
		Container c = this.getContentPane();
		c.removeAll();
		c.add(panel);
		this.repaint();
		panel.updateUI();
		this.setVisible(true);
	}
	
	public MainPanel getMainPanel(){
		return this.mainPanel;
	}

}
