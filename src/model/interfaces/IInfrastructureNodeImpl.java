package model.interfaces;

import java.util.Map;

/**
 * interface that models the node with references to other nodes
 * @author BBC
 *
 */
public interface IInfrastructureNodeImpl extends IInfrastructureNode {
	
	/**
	 * method invoked to get near nodes with time of travelling 
	 * @return weighted near nodes
	 */
	Map<String,Integer> getNearNodesWeighted();
	
	/**
	 * method invoked to set a new node in the near nodes weighted
	 * @param node
	 */
	void setNearNodeWeighted(String nodeID, Integer distance);

	
	
}
