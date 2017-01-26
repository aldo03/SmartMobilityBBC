package model.interfaces.msg;

/**
 * Interface that models the response from the last node of a path to the user
 * @author BBC
 *
 */
public interface IResponseTravelTimeMsg extends IMobilityMsg {
	/**
	 * gets (in seconds) the travel time
	 * @return travel time
	 */
	int getTravelTime();
	
	/**
	 * gets the ID of the travel
	 * @return travel ID
	 */
	int getTravelID();
}
