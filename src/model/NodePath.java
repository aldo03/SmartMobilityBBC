package model;

import java.util.List;

import model.interfaces.IInfrastructureNode;
import model.interfaces.INodePath;

public class NodePath implements INodePath {
	
	private List<IInfrastructureNode> pathNodes;
	
	public NodePath(List<IInfrastructureNode> pathNodes){
		this.pathNodes = pathNodes;
	}

	@Override
	public List<IInfrastructureNode> getPathNodes() {
		return this.pathNodes;
	}

	@Override
	public void removeFirstNode() {
		this.pathNodes.remove(0);
	}

}
