package model.interfaces.msg;

import model.interfaces.INodePath;

/**
 * Interface that models:
 * -	the message that acknowledges the intention of a user to move through a certain path to all nodes
 * -    the message that acknowledges the intention of a user to move through a certain path to the server
 * -	the message that gives the coordinates of the nodes to the user
 * @author BBC
 *
 */
public interface IAcknowledgePathMsg extends IUserMobilityMsg {
	/**
	 * gets the path to be acknowledged
	 * @return path
	 */
	INodePath getPath();
}
