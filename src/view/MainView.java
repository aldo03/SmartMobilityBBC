package view;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainView extends JFrame implements WindowListener, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JComboBox<String> nodes;
	private JButton open;
	
	public MainView(){
		this.panel = new JPanel();
		initGUI(this);
		initPanel();
		this.addWindowListener(this);
		this.add(this.panel);
	}
	
	private void initPanel(){
		this.nodes = new JComboBox<String>();
		this.nodes.addItem("node1");
		this.nodes.addItem("node2");
		this.nodes.addItem("node3");
		this.open = new JButton(" View info ");
		this.open.addActionListener(this);
		this.setLayout(new FlowLayout());
		this.panel.setOpaque(false);
		this.panel.add(new JLabel("Select the Node you want info on "));
		this.panel.add(this.nodes);
		this.panel.add(this.open);
	}
	
	private void initGUI(JFrame frame){
		frame.setResizable(true);
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(open)){
			NodeView nv = new NodeView(this.nodes.getSelectedItem().toString());
			nv.setVisible(true);
		}
	}

}
