package model.interfaces;

/**
 * interface that defines a data structure that contains the expected travel times from a node to its
 * neighbors with a certain amount of vehicles in a prefixed time lapse of 10 minutes
 * @author BBC
 *
 */
public interface ITravelTimesByNumberOfVehicles {
	
	/**
	 * @param number of vehicles
	 * @param id of the node
	 * @return the expected travel time to the node with a certain amount of vehicles
	 */
	int getTravelTime(String nodeId, int numOfVehicles);
}
