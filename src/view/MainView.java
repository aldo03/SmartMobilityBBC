package view;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.interfaces.IInfrastructureNode;
import model.interfaces.IInfrastructureNodeImpl;
import user.UserDevice;

public class MainView extends JFrame implements WindowListener, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JComboBox<String> nodes;
	private List<IInfrastructureNode> nodesSet;
	private JButton open;
	private JButton startSimulation;
	private List<UserDevice> usersList;
	
	public MainView(List<IInfrastructureNode> nodes2, List<UserDevice> usersList){
		this.panel = new JPanel();
		this.nodesSet = nodes2;
		this.usersList = usersList;
		initGUI();
		initPanel();
		this.addWindowListener(this);
		this.add(this.panel);
		this.setVisible(true);
	}
	
	private void initPanel(){
		this.nodes = new JComboBox<String>();
		for(IInfrastructureNode n : nodesSet){
			this.nodes.addItem(n.getNodeID());
		}
		this.open = new JButton(" View info ");
		this.open.addActionListener(this);
		this.startSimulation = new JButton(" START SIMULATION ");
		this.startSimulation.addActionListener(this);
		this.setLayout(new FlowLayout());
		this.panel.setOpaque(false);
		this.panel.add(new JLabel("Select the Node you want info on "));
		this.panel.add(this.nodes);
		this.panel.add(this.open);
		this.panel.add(this.startSimulation);
	}
	
	private void initGUI(){
		this.setResizable(true);
		Dimension d = new Dimension(500,100);
		this.setMaximumSize(d);
		this.setSize(d);
		this.setTitle("Main view");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation( (int)((dim.width) / 2 - (d.getWidth() / 2)) ,
				(int)((dim.height) / 2 - (d.getHeight() / 2)));
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
		} else if(e.getSource().equals(startSimulation)){
			for(UserDevice user : usersList){
				user.start();
			}
		}
	}

}
