package model.interfaces;
/**
 * Interface that models an infrastructure node
 * @author BBC
 *
 */
public interface IInfrastructureNode {
	/**
	 * gets the ID of the node
	 * @return the node ID
	 */
	String getNodeID();
	
	/**
	 * method invoked to get the id as a number
	 * @return node id in integer form
	 */
	Integer getIntNodeID();
	
	/**
	 * gets the coordinates of the node
	 * @return the node coordinates
	 */
	ICoordinates getCoordinates();
	
	/**
	 * sets the coordinates of the node
	 */
	void setCoordinates(ICoordinates coordinates);
}
