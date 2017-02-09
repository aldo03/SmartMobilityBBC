package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import model.interfaces.ICoordinates;
import model.interfaces.IInfrastructureNodeImpl;

public class InfrastructureNodeImpl implements IInfrastructureNodeImpl {

	private String nodeID;
	private ICoordinates coordinates;
	private Map<String,Integer> nearNodesWeighted;

	public InfrastructureNodeImpl(String nodeID) {
		super();
		this.nodeID = nodeID;
		this.nearNodesWeighted = new HashMap<>();
	}

	public InfrastructureNodeImpl(String nodeID, ICoordinates coordinates) {
		super();
		this.nodeID = nodeID;
		this.coordinates = coordinates;
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
		String str = s.substring(2, s.length());
		Integer idInt = Integer.parseInt(str);
		return idInt;
	}
}