package model.interfaces;

import java.util.List;

/**
 * Interface that models a path of nodes inside the system
 * @author BBC
 *
 */
public interface INodePath {
	/**
	 * gets the nodes of the path
	 * @return nodes of the path
	 */
	List<IInfrastructureNode> getPathNodes();
}
