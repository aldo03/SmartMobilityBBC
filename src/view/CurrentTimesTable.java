package view;

import javax.swing.JTable;

public class CurrentTimesTable extends JTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public CurrentTimesTable(int x, int y){
		
	}
	@Override
	public String getColumnName(int column) {
		// TODO Auto-generated method stub
		return column + "/" + column + 5;
	}

}
