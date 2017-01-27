package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.interfaces.IInfrastructureNodeImpl;
import model.interfaces.INodePath;
import model.interfaces.IPair;

/**
 * class that model Server logic
 * @author BBC
 *
 */
public class MainServer extends Thread{

	private Map<String, Set<IPair<String,Integer>>> graph;
	private Set<IInfrastructureNodeImpl> nodesSet; 
	
	public MainServer(){
		this.graph = new HashMap<>();
		this.nodesSet = new HashSet<>();
	}
	
	/**
	 * method invoked to obtain a list of shortest path
	 * @param start
	 * @param finish
	 * @return list of shortest path
	 */
	public List<INodePath> getShortestPaths(IInfrastructureNodeImpl start, IInfrastructureNodeImpl finish){
		List<INodePath> pathList = new ArrayList<>();
		//TODO short path algorithm
		return pathList;
	}
	
	/**
	 * method invoked to set the graph and nodeSet
	 * @param nodesSet
	 */
	public void setNodes(Set<IInfrastructureNodeImpl> nodesSet){
		this.nodesSet = nodesSet;
		for(IInfrastructureNodeImpl node: this.nodesSet)
			this.graph.put(node.getNodeID(), node.getNearNodesWeighted());
	}
	
	/**
	 * method invoked to set a new node in the system
	 * @param node
	 */
	public void setNewNode(IInfrastructureNodeImpl node){
		this.nodesSet.add(node);
		this.graph.put(node.getNodeID(), node.getNearNodesWeighted());
	}
}
