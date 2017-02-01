package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.interfaces.ITravelTimesByNumberOfVehicles;



public class TravelTimesByNumberOfVehicles implements ITravelTimesByNumberOfVehicles {
	private static final int RANGE = 5;
	private static final int ARRAY_LENGTH = 200;
	
	private Map<String, int[]> travelTimes;
	
	public TravelTimesByNumberOfVehicles(){
		this.travelTimes = new HashMap<>();
	}
	
	
	
	@Override
	public int getTravelTime(String nodeId, int numOfVehicles) {
		int time = 0;
		int[] l = this.travelTimes.get(nodeId);
		if(l.length<numOfVehicles/RANGE){
			time = l[l.length-1];
		} else{
			time = l[numOfVehicles/RANGE];
		}
		return time;
	}



	@Override
	public void initTravelTimes(String nodeId, int defaultValue) {
		int[] times = new int[ARRAY_LENGTH];
		for(int i=0; i<times.length;i++){
			times[i] = defaultValue;
		}
		this.travelTimes.put(nodeId, times);
	}


	//the travel time for a certain amount of vehicles is set. The near values are updated in order to make
	//them consistent with the new value
	@Override
	public void setTravelTime(String nodeId, int numOfVehicles, int travelTime) {
		int[] times = this.travelTimes.get(nodeId);
		int range = numOfVehicles/RANGE;
		if(range<times.length){
			if(times[range]<=travelTime){
				for(int i=range; i>=0;i--){
					if(times[i]<=travelTime){
						break;
					}
					times[i]=travelTime;
				}
			} else if(times[range]>travelTime){
				for(int i=range; i<times.length;i++){
					if(times[i]>travelTime){
						break;
					}
					times[i]=travelTime;
				}
			}
		}
		this.travelTimes.put(nodeId, times);
	}
}
