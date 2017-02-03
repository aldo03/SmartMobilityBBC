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
	
	public MainView(){
		this.mainPanel = new MainPanel();
		initGUI(this);
		this.addWindowListener(this);
		this.getContentPane().add(this.mainPanel);
	}
	
	private void initGUI(JFrame frame){
		frame.setResizable(false);
		Dimension d = new Dimension(500,100);
		frame.setMaximumSize(d);
		frame.setSize(d);
		frame.setTitle("Main view");
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setVisible(true);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((dim.width) / 2,
				(dim.height) / 2);
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
