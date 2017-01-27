package model;

import java.util.Set;

import model.interfaces.ICoordinates;
import model.interfaces.IInfrastructureNodeImpl;

public class InfrastructureNodeImpl implements IInfrastructureNodeImpl{

	private String nodeID;
	private ICoordinates coordinates;
	private Set<IInfrastructureNodeImpl> nearNodes;
	
	@Override
	public String getNodeID() {
		return null;
	}

	@Override
	public ICoordinates getCoordinates() {
		return null;
	}

	@Override
	public void setCoordinates() {
		
	}

	@Override
	public Set<IInfrastructureNodeImpl> getNearNodes() {
		return null;
	}

}
