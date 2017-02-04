package model;

import java.util.Map;
import java.util.Set;

import model.interfaces.ICoordinates;
import model.interfaces.IInfrastructureNodeImpl;

public class InfrastructureNodeImpl implements IInfrastructureNodeImpl {

	private String nodeID;
	private ICoordinates coordinates;
	private Set<IInfrastructureNodeImpl> nearNodes;
	private Map<String,Integer> nearNodesWeighted;

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
	public Map<String,Integer> getNearNodesWeighted() {
		return this.nearNodesWeighted;
	}

	@Override
	public void setNearNodeWeighted(String nodeID, Integer distance) {
		this.nearNodesWeighted.put(nodeID, distance);
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


	@Override
	public Integer getIntNodeID() {
		String s = this.nodeID;
		s.replaceFirst("id", "");
		Integer idInt = Integer.parseInt(s);
		return idInt;
	}
}