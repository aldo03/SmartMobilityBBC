package view;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int width;
	private int height;
	private JPanel panel;
	private JComboBox<String> nodes;
	private JButton open;

	public MainPanel(){
		super();
		this.panel = new JPanel();
		this.setSize(500,100);
		this.nodes = new JComboBox<String>();
		this.nodes.addItem("node1");
		this.nodes.addItem("node2");
		this.nodes.addItem("node3");
		this.open = new JButton(" View info ");

		this.width = this.getWidth();
		this.height = this.getHeight();
		this.setLayout(new FlowLayout());
		this.panel.setOpaque(false);
		this.panel.add(new JLabel("Select the Node you want info on "));
		this.panel.add(this.nodes);
		this.panel.add(this.open);

		this.add(this.panel);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.open){
			new NodeView(this.nodes.getSelectedItem().toString());
		}
	}

}
