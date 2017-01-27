package model;

import java.util.Set;

import model.interfaces.ICoordinates;
import model.interfaces.IInfrastructureNodeImpl;
import model.interfaces.IPair;

public class InfrastructureNodeImpl implements IInfrastructureNodeImpl {

	private String nodeID;
	private ICoordinates coordinates;
	private Set<IInfrastructureNodeImpl> nearNodes;
	private Set<IPair<String, Integer>> nearNodesWeighted;

	public InfrastructureNodeImpl(String nodeID, Set<IInfrastructureNodeImpl> nearNodes) {
		super();
		this.nodeID = nodeID;
		this.nearNodes = nearNodes;
	}

	public InfrastructureNodeImpl(String nodeID, ICoordinates coordinates, Set<IInfrastructureNodeImpl> nearNodes) {
		super();
		this.nodeID = nodeID;
		this.coordinates = coordinates;
		this.nearNodes = nearNodes;
	}

	@Override
	public String getNodeID() {
		return this.nodeID;
	}

	@Override
	public ICoordinates getCoordinates() {
		return this.coordinates;
	}

	@Override
	public Set<IInfrastructureNodeImpl> getNearNodes() {
		return this.nearNodes;
	}

	@Override
	public void setNearNode(IInfrastructureNodeImpl node) {
		this.nearNodes.add(node);
	}

	@Override
	public void setCoordinates(ICoordinates coordinates) {
		this.coordinates = coordinates;
	}

	@Override
	public Set<IPair<String, Integer>> getNearNodesWeighted() {
		return this.nearNodesWeighted;
	}

	@Override
	public void setNearNodeWeighted(IPair<String, Integer> node) {
		this.nearNodesWeighted.add(node);
	}

	@Override
	public int hashCode() {
		return nodeID.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		InfrastructureNodeImpl node = (InfrastructureNodeImpl) obj;
		return node.getNodeID().equals(obj);
	}

}