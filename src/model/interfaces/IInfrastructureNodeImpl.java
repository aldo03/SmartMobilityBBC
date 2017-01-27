package model.interfaces;

import java.util.Set;

/**
 * interface that models the node with references to other nodes
 * @author BBC
 *
 */
public interface IInfrastructureNodeImpl extends IInfrastructureNode {

	/**
	 * method invoked to get near nodes
	 * @return near nodes
	 */
	Set<IInfrastructureNodeImpl> getNearNodes();
	
	/**
	 * method invoked to set a new node in the near nodes
	 * @param node
	 */
	void setNearNodes(IInfrastructureNodeImpl node);
	
}
