package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;

public class NodeFrame extends JFrame implements ActionListener, WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private static final String IM_BACKGROUND = "/ImmaginePiccola.png";
	private ImageIcon background;
	private int width;
	private int height;
	private JMenuBar menuBar;
	private JMenu menu;
	private JRadioButtonMenuItem travelTimes;
	private JRadioButtonMenuItem currentTimes;
	private JRadioButtonMenuItem expectedVehicles;
	private ButtonGroup group;
	private Dimension dim;

	
	
	
	public NodeFrame(String nodeID){
		super();
		this.setResizable(false);
		Dimension d = new Dimension(400,400);
		this.setMaximumSize(d);
		this.setSize(d);
		this.setTitle("View");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addWindowListener(this);
		this.dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((this.dim.width - d.width) / 2,
				(this.dim.height - d.height) / 2);
		this.setVisible(true);
		//imposta il backround del panel
		this.panel = new JPanel();
		this.background = new ImageIcon(getClass().getResource(IM_BACKGROUND));
		this.setSize(background.getIconWidth(), background.getIconHeight());

		this.width = this.getWidth();
		this.height = this.getHeight();
		
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 1000, 150));
		
		this.menuBar = new JMenuBar();
		this.menu = new JMenu();
		this.group = new ButtonGroup();
		this.travelTimes = new JRadioButtonMenuItem("Travel Times");
		this.currentTimes = new JRadioButtonMenuItem("Current times");
		this.expectedVehicles = new JRadioButtonMenuItem("Expected vehicles");
		this.group.add(this.travelTimes);
		this.group.add(this.currentTimes);
		this.group.add(this.expectedVehicles);
		this.menu.add(this.travelTimes);
		this.menu.add(this.currentTimes);
		this.menu.add(this.expectedVehicles);
		this.menuBar.add(menu);
		this.setJMenuBar(this.menuBar);

		this.panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 30));
		this.panel.setPreferredSize(new Dimension(this.width, this.height));
		this.panel.setOpaque(false);
		
		this.panel.add(new JLabel(nodeID), FlowLayout.LEFT);
		


	}




	@Override
	public void actionPerformed(ActionEvent e) {		
	}




	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
