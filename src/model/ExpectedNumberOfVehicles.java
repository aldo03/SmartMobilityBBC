package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.interfaces.IExpectedNumberOfVehicles;
import utils.mongodb.MongoDBUtils;

public class ExpectedNumberOfVehicles implements IExpectedNumberOfVehicles {

	private Map<String, List<Integer>> vehicles;  //every element of the list is the absolute time (in seconds)
												  //in which a single vehicle is expected to perform a certain path from a node to another
	private static final int RANGE = 3000;        //range (in seconds) within which the flow of vehicles must be checked
	private String nodeId;
	public ExpectedNumberOfVehicles(String nodeId){
		this.vehicles = new HashMap<>();
		this.nodeId = nodeId;
		MongoDBUtils.intExpectedVehicles(nodeId);
	}
	
	
	
	@Override
	public int getVehicles(String nodeId, int time) {
		List<Integer> listOfVehicles = this.vehicles.get(nodeId);
		int currentTimeSeconds = (int) (System.currentTimeMillis()/1000);
		int futureTime = currentTimeSeconds+time;
		int rangeMin = futureTime-RANGE/2;
		int rangeMax = futureTime+RANGE/2;
		int vehiclesCount = 0;
		for(Integer i : listOfVehicles){       
			if(i>=rangeMin&&i<=rangeMax){         //if a vehicle is within the range the counter is incremented
				vehiclesCount++;
			}else if(i>rangeMax){
				break;
			}
		}
		return vehiclesCount;
	}

	@Override
	public void addVehicle(String nodeId, int time) {
		List<Integer> listOfVehicles = this.vehicles.get(nodeId);
		int currentTimeSeconds = (int) (System.currentTimeMillis()/1000);
		listOfVehicles.add((int) (currentTimeSeconds+time));
		Collections.sort(listOfVehicles);
		for(Integer i : listOfVehicles){       //every "non fresh" information is removed
			if(i<currentTimeSeconds){
				listOfVehicles.remove(i);
			}
		}
	}



	@Override
	public void initVehicles(String nodeId) {
		this.vehicles.put(nodeId, new ArrayList<>());
	}

}
