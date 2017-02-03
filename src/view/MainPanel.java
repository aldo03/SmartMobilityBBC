package view;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class MainPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String IM_BACKGROUND = "/ImmaginePiccola.png";
	private ImageIcon background;
	private int width;
	private int height;
	private JPanel panel;
	private JComboBox<String> nodes;
	private JButton open;

	public MainPanel(){
		super();
		//imposta il backround del panel
		this.panel = new JPanel();
		this.background = new ImageIcon(getClass().getResource(IM_BACKGROUND));
		this.setSize(background.getIconWidth(), background.getIconHeight());
		
		this.nodes = new JComboBox<String>();
		this.nodes.addItem("node1");
		this.nodes.addItem("node2");
		this.nodes.addItem("node3");
		this.open = new JButton("      View node information      ");

		this.width = this.getWidth();
		this.height = this.getHeight();

		this.setLayout(new FlowLayout(FlowLayout.CENTER, 1000, 150));

		this.panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 30));
		this.panel.setPreferredSize(new Dimension(this.width / 5 + 10, this.height - this.height / 3));
		this.panel.setOpaque(false);
		
		this.panel.add(this.nodes, FlowLayout.LEFT);
		this.panel.add(this.open, FlowLayout.LEFT);

		this.add(this.panel);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.open){
			new NodeFrame(this.nodes.getSelectedItem().toString());
		}
	}

}
