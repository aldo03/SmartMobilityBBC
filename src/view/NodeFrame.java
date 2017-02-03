package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import model.InfrastructureNode;
import model.interfaces.IInfrastructureNode;

public class NodeFrame extends JFrame implements WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static int extraWindowWidth = 500;
	final static String TRAVEL_TIMES = "Travel Times";
    final static String EXPECTED_VEHICLES = "Expected vehicles";
    final static String CURRENT_TIMES = "Current Times";
    
	public NodeFrame(String nodeID){
		initGUI(this);
		this.addComponentToPane(this.getContentPane());
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
	
    public void addComponentToPane(Container pane) {
        JTabbedPane tabbedPane = new JTabbedPane();

        //Create the "cards".
        JPanel card1 = new JPanel() {
            //Make the panel wider than it really needs, so
            //the window's wide enough for the tabs to stay
            //in one row.
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width += extraWindowWidth;
                return size;
            }
        };
        
        
        List<InfrastructureNode> neighbors = new ArrayList<InfrastructureNode>();
        neighbors.add(new InfrastructureNode("Node2"));
        neighbors.add(new InfrastructureNode("Node3"));
        neighbors.add(new InfrastructureNode("Node4"));
        card1.add(this.addTravelTimesTable(neighbors));

        JPanel card2 = new JPanel();
        card2.add(new JTextField("TextField", 20));

        tabbedPane.addTab(TRAVEL_TIMES, card1);
        tabbedPane.addTab(EXPECTED_VEHICLES, card2);

        pane.add(tabbedPane, BorderLayout.CENTER);
    }
    
    
    private JScrollPane addTravelTimesTable(List<InfrastructureNode> neighbors){
    	JTable table = new JTable(neighbors.size(), 201);
    	for(int j = 0; j < table.getRowCount(); j++){
    		table.setValueAt(neighbors.get(j).getNodeID(), j, 0);
    	}
    	JScrollPane scrollPane = new JScrollPane(table);
    	scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    	scrollPane.add(new JButton("AAA"));
    	scrollPane.repaint();
    	
    	return scrollPane;
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
