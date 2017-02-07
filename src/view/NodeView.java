package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import utils.mongodb.MongoDBUtils;

public class NodeView extends JFrame implements WindowListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static int extraWindowWidth = 500;
	final static String TRAVEL_TIMES = "Travel Times";
    final static String EXPECTED_VEHICLES = "Expected vehicles";
    final static String CURRENT_TIMES = "Current Times";
    
    private String nodeId;
    private JButton refreshTravelTimes;
    private JButton refreshExpectedVehicles;
    private JButton refreshCurrentTimes;
    private JPanel card1, card2, card3;
    private JScrollPane sp1, sp2, sp3;
    private JTable t1, t2, t3;
    private Map<String, List<Integer>> travelTimes, expectedVehicles, currentTimes;
    
	public NodeView(String nodeId){
		this.nodeId = nodeId;
		initGUI();
		initRefreshButtons();
		initTables();
		this.addComponentToPane(this.getContentPane());
		
	}
	
	private void initGUI(){
		this.setResizable(false);
		Dimension d = new Dimension(800,600);
		this.setMaximumSize(d);
		this.setSize(d);
		this.setTitle("Main view");	
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation( (int)((dim.width) / 2 - (d.getWidth() / 2)) ,
				(int)((dim.height) / 2 - (d.getHeight() / 2)));
	}
	
	private void initRefreshButtons(){
		this.refreshTravelTimes = new JButton("REFRESH");
		this.refreshTravelTimes.addActionListener(this);
		this.refreshExpectedVehicles = new JButton("REFRESH");
		this.refreshExpectedVehicles.addActionListener(this);
		this.refreshCurrentTimes = new JButton("REFRESH");
		this.refreshCurrentTimes.addActionListener(this);
	}
	
	private void initTables(){
		//Create the "cards".
		this.card1 = new JPanel() {
            //Make the panel wider than it really needs, so
            //the window's wide enough for the tabs to stay
            //in one row.
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width += extraWindowWidth;
                return size;
            }
        };
        this.card2 = new JPanel();        
        this.card3 = new JPanel();
		this.travelTimes = MongoDBUtils.getTimeTravels(this.nodeId);
		this.t1 = this.createTable(travelTimes, true, card1, "These are the future Travel Times from node " + this.nodeId + " to its near nodes", this.refreshTravelTimes);
		this.fillTable(travelTimes, true, t1);
		this.sp1 = new JScrollPane(t1, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);	
		this.sp1.repaint();
		this.expectedVehicles = MongoDBUtils.getExpectedVehicles(this.nodeId);
		this.t2 = this.createTable(expectedVehicles, false, card2, "These are the scheduled times of Expected Vehicles", this.refreshExpectedVehicles);
		this.fillTable(expectedVehicles, false, t2);
		this.sp2 = new JScrollPane(t2, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);	
		this.sp2.repaint();
		this.currentTimes = MongoDBUtils.getCurrentTimes(this.nodeId);
		this.t3 = this.createTable(currentTimes, false, card3, "These are the lastest Travel times towards near nodes", this.refreshCurrentTimes );
		this.fillTable(currentTimes, false, t3);
		this.sp3 = new JScrollPane(t3, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);	
		this.sp3.repaint();
	}
	
    public void addComponentToPane(Container pane) {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        card1.add(sp1);
        card2.add(sp2);
        card3.add(sp3);
        tabbedPane.addTab(TRAVEL_TIMES, card1);
        tabbedPane.addTab(EXPECTED_VEHICLES, card2);
        tabbedPane.addTab(CURRENT_TIMES,card3);
        pane.add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JTable createTable(Map<String, List<Integer>> tableContent, boolean travelTimes, JPanel p, String s, JButton b){
    	int max = 0;
    	for (String l : tableContent.keySet()){
    		if(tableContent.get(l).size() > max)
    			max = tableContent.get(l).size()+1;
    	}
    	JTable table = new JTable(tableContent.keySet().size(), max);
    	table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    	table.getTableHeader().getColumnModel().getColumn(0).setHeaderValue("Node");
    	p.add(new JLabel(s));
    	p.add(b);
    	if(travelTimes){
			int range = 0;
			for (int i = 1; i < table.getColumnCount(); i++) {
				table.getTableHeader().getColumnModel().getColumn(i).setHeaderValue(range + "/" + (range + 5));
				range += 5;
			}
    	}	
    	return table;
    }
    
    private void fillTable(Map<String, List<Integer>> tableContent, boolean travelTimes, JTable table){ 		
    	int j = 0;
    	for(String s : tableContent.keySet()){
    		table.setValueAt(s, j, 0);        		
        		for(int k = 1; k < tableContent.get(s).size() + 1; k++){
        			table.setValueAt(tableContent.get(s).get(k-1), j, k);
        		}
        	j++;
    	} 	 	
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(this.refreshTravelTimes)){		
			this.fillTable(MongoDBUtils.getTimeTravels(this.nodeId), true, t1);
			this.sp1.repaint();
		} else if(e.getSource().equals(this.refreshExpectedVehicles)){
			this.fillTable(MongoDBUtils.getExpectedVehicles(this.nodeId), false, t2);
			this.sp2.repaint();
		} else if(e.getSource().equals(this.refreshCurrentTimes)){
			this.fillTable(MongoDBUtils.getCurrentTimes(this.nodeId), false, t3);
			this.sp3.repaint();
		}
		
	}

}
