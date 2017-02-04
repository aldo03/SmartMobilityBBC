package model;

import model.interfaces.ICoordinates;
import model.interfaces.IInfrastructureNode;

public class InfrastructureNode implements IInfrastructureNode {
	
	private String id;
	private ICoordinates coordinates;
	
	public InfrastructureNode(String id){
		this.id = id;
	}


	@Override
	public Integer getIntNodeID() {
		String s = this.id;
		s.replaceFirst("id", "");
		Integer idInt = Integer.parseInt(s);
		return idInt;
	}
	
	@Override
	public String getNodeID() {
		return this.id;
	}

	@Override
	public ICoordinates getCoordinates() {
		return this.coordinates;
	}

	@Override
	public void setCoordinates(ICoordinates coordinates) {
		this.coordinates = coordinates;
		
	}



}
