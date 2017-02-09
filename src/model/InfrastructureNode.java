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
		String str = s.substring(2, s.length());
		Integer idInt = Integer.parseInt(str);
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
