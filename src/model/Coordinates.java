package model;

import model.interfaces.ICoordinates;

public class Coordinates implements ICoordinates {
	
	private double latitude;
	private double longitude;
	private static final int RANGE = 10 ;
	
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

	@Override
	public boolean isCloseEnough(ICoordinates coordinates) {
		double dist = distFrom(this.latitude, this.longitude, coordinates.getLatitude(), coordinates.getLongitude()) ;
		return dist<=RANGE;
	}
	
	private double distFrom(double lat1, double lng1, double lat2, double lng2) {
	    double earthRadius = 6371000; //meters
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;
	    return dist;
	}
}
