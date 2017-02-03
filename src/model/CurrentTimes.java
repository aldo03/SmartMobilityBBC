package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.interfaces.ICurrentTimes;
import utils.mongodb.MongoDBUtils;

public class CurrentTimes implements ICurrentTimes {

	private Map<String, List<Integer>> currentTimes;
	private String nodeId;
	
	public CurrentTimes(String nodeId){
		this.currentTimes = new HashMap<>();
		this.nodeId = nodeId;
		MongoDBUtils.initCurrentTime(nodeId);
	}
	
	@Override
	public void addTime(String nodeId, int time) {
		this.currentTimes.get(nodeId).add(time);
	}

	
	@Override
	public int getAverageTime(String nodeId) {
		List<Integer> l = this.currentTimes.get(nodeId);
		int sum = 0;
		for(int i : l){
			sum+=i;
		}
		return sum/l.size();
	}



	@Override
	public void initTimes(String nodeId) {
		this.currentTimes.put(nodeId, new ArrayList<>());
	}

	@Override
	public void removeData() {
		for(List<Integer> l : this.currentTimes.values()){
			l.clear();
		}
	}

	@Override
	public int getVehicleCount(String nodeId) {
		return this.currentTimes.get(nodeId).size();
	}

}
