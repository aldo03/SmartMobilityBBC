package model;

import model.interfaces.ICoordinates;

public class Coordinates implements ICoordinates {
	
	private double latitude;
	private double longitude;
	
	public Coordinates(double lat, double lon){
		this.latitude = lat;
		this.longitude = lon;
	}

	@Override
	public double getLatitude() {
		return this.latitude;
	}

	@Override
	public double getLongitude() {
		return this.longitude;
	}

}
