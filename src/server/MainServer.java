package server;

import java.util.ArrayList;
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
	
	public List<INodePath> getShortestPaths(IInfrastructureNodeImpl start, IInfrastructureNodeImpl finish){
		List<INodePath> pathList = new ArrayList<>();
		//TODO short path algortihm
		return pathList;
	}
	
	public void setNewNode(IInfrastructureNodeImpl node){
		this.nodesSet.add(node);
		
	}
}
