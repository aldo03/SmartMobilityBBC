package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import model.InfrastructureNode;
import model.Pair;
import model.interfaces.IInfrastructureNode;
import utils.mongodb.MongoDBUtils;

public class NodeView extends JFrame implements WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static int extraWindowWidth = 500;
	final static String TRAVEL_TIMES = "Travel Times";
    final static String EXPECTED_VEHICLES = "Expected vehicles";
    final static String CURRENT_TIMES = "Current Times";
    
    private String nodeId;
    
	public NodeView(String nodeId){
		this.nodeId = nodeId;
		initGUI();
		this.addComponentToPane(this.getContentPane());
	}
	
	private void initGUI(){
		this.setResizable(true);
		Dimension d = new Dimension(800,600);
		this.setMaximumSize(d);
		this.setSize(d);
		this.setTitle("Main view");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		//frame.setVisible(true);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((dim.width) / 2,
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
        JPanel card2 = new JPanel();        
        JPanel card3 = new JPanel();
        
        testTables(card1,card2,card3);
        tabbedPane.addTab(TRAVEL_TIMES, card1);
        tabbedPane.addTab(EXPECTED_VEHICLES, card2);
        tabbedPane.addTab(CURRENT_TIMES,card3);
        pane.add(tabbedPane, BorderLayout.CENTER);
    }
    
    private void testTables(JPanel p1, JPanel p2, JPanel p3){
    	/*Map<String, List<Integer>> neighbors = new HashMap<String, List<Integer>>();
        List<Integer> l1 = new ArrayList<Integer>();
        l1.add(1);
        l1.add(2);
        l1.add(3);
        List<Integer> l2 = new ArrayList<Integer>();
        l2.add(4);
        l2.add(5);
        l2.add(6);
        List<Integer> l3 = new ArrayList<Integer>();
        l3.add(7);
        l3.add(8);
        l3.add(9);
        neighbors.put("node2", l1);
        neighbors.put("node3", l2);
        neighbors.put("node4", l3);   */
    	Map<String, List<Integer>> neighbors = MongoDBUtils.getTimeTravels(this.nodeId);
        p1.add(this.fillTable(neighbors, true));
        /*Map<String, List<Integer>> expectedVehicles = new HashMap<String, List<Integer>>();
        expectedVehicles.put("node2",l1);
        expectedVehicles.put("node3",l2);*/
        Map<String, List<Integer>> expectedVehicles = MongoDBUtils.getExpectedVehicles(this.nodeId);
        p2.add(this.fillTable(expectedVehicles, false));
        /*Map<String, List<Integer>> currentTimes = new HashMap<String, List<Integer>>();
        currentTimes.put("node10", l1);
        currentTimes.put("node20", l3);*/
        Map<String, List<Integer>> currentTimes = MongoDBUtils.getCurrentTimes(this.nodeId);
        p3.add(this.fillTable(currentTimes, false));
    }
    
  /*  
    private JScrollPane addTravelTimesTable(Map<String, List<Integer>> neighbors, boolean travelTimes){
    	JTable table = new JTable(neighbors.size(), 201);
    	table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    	table.getTableHeader().getColumnModel().getColumn(0).setHeaderValue("Node");
    	
    	int j = 0;
    	for(String s : neighbors.keySet()){
    		table.setValueAt(s, j, 0);        		
        		for(int k = 1; k < neighbors.get(s).size() + 1 ; k++){
        			table.setValueAt(neighbors.get(s).get(k-1), j, k);
        		}
        	j++;
    	}
    	
    	JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    	scrollPane.repaint();    	
    	return scrollPane;
    }
    */
    private JScrollPane fillTable(Map<String, List<Integer>> tableContent, boolean travelTimes){
    	int max = 0;
    	for (String l : tableContent.keySet()){
    		if(tableContent.get(l).size() > max)
    			max = tableContent.get(l).size()+1;
    	}
    	JTable table = new JTable(tableContent.keySet().size(), max);
    	table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    	if(travelTimes){
			int range = 0;
			for (int i = 1; i < table.getColumnCount(); i++) {
				table.getTableHeader().getColumnModel().getColumn(i).setHeaderValue(range + "/" + (range + 5));
				range += 5;
			}
    	}	
    	int j = 0;
    	for(String s : tableContent.keySet()){
    		table.setValueAt(s, j, 0);        		
        		for(int k = 1; k < tableContent.get(s).size() + 1; k++){
        			table.setValueAt(tableContent.get(s).get(k-1), j, k);
        		}
        	j++;
    	} 	
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
