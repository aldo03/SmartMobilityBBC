package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import model.InfrastructureNode;
import model.Pair;
import model.interfaces.IInfrastructureNode;

public class NodeView extends JFrame implements WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static int extraWindowWidth = 500;
	final static String TRAVEL_TIMES = "Travel Times";
    final static String EXPECTED_VEHICLES = "Expected vehicles";
    final static String CURRENT_TIMES = "Current Times";
    
	public NodeView(String nodeID){
		initGUI(this);
		this.addComponentToPane(this.getContentPane());
	}
	
	private void initGUI(JFrame frame){
		frame.setResizable(true);
		Dimension d = new Dimension(800,600);
		frame.setMaximumSize(d);
		frame.setSize(d);
		frame.setTitle("Main view");
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		//frame.setVisible(true);
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
        //card2.add(this.addExpectedVehiclesTable(tableContent));
        
        JPanel card3 = new JPanel();
        //card3.add(this.addCurrentTimesTable(tableContent))
        tabbedPane.addTab(TRAVEL_TIMES, card1);
        tabbedPane.addTab(EXPECTED_VEHICLES, card2);
        tabbedPane.addTab(CURRENT_TIMES,card3);

        pane.add(tabbedPane, BorderLayout.CENTER);
    }
    
    
    private JScrollPane addTravelTimesTable(List<InfrastructureNode> neighbors){
    	JTable table = new JTable(neighbors.size(), 201);
    	table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    	table.getTableHeader().getColumnModel().getColumn(0).setHeaderValue("Node");
    	int range = 5;
    	for(int i = 1; i < table.getColumnCount(); i++){
    		table.getTableHeader().getColumnModel().getColumn(i).setHeaderValue(range+"/"+(range+5));
    		range+=5;
    	}
    	for(int j = 0; j < table.getRowCount(); j++){
    		table.setValueAt(neighbors.get(j).getNodeID(), j, 0);
    	}
    	JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    	
    	scrollPane.repaint();
    	
    	return scrollPane;
    }
    
    private JScrollPane addExpectedVehiclesTable(Map<String, List<Integer>> tableContent){
    	JTable table = new JTable();
    	table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    	/*for(Pair<String, String[]> s : tableContent){
    		for(int i = 0; i < tableContent.size(); i++){
    			
    		}
    	}*/
    	JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.repaint();
    	return scrollPane;
    }
    
    private JScrollPane addCurrentTimesTable(Map<String, List<Integer>> tableContent){
    	JTable table = new JTable();
    	table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    	/*
    	for(Map<String, List<Integer>> s : tableContent){
    		for(int i = 0; i < tableContent.size(); i++){
    			
    		}
    	}
    	*/
    	JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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
