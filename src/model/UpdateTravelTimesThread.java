package model;

import java.util.HashSet;
import java.util.Set;

import model.interfaces.ICurrentTimes;
import model.interfaces.ITravelTimesByNumberOfVehicles;

public class UpdateTravelTimesThread extends Thread {

	private static final int RANGE = 3000;  // same RANGE as the one set in
											// ExpectedNumberOfVehicles.
											// the sampling frequency is the
											// same
	private ICurrentTimes curTimes;
	private ITravelTimesByNumberOfVehicles travelTimes;
	private Set<String> nodeIds;

	public UpdateTravelTimesThread(ICurrentTimes curTimes, ITravelTimesByNumberOfVehicles travelTimes) {
		this.curTimes = curTimes;
		this.travelTimes = travelTimes;
		this.nodeIds = new HashSet<>();
	}

	public void initNode(String nodeId) {
		this.nodeIds.add(nodeId);
	}

	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(RANGE*60);
				for (String id : this.nodeIds) {
					int avgTime = this.curTimes.getAverageTime(id);
					int vehicleCount = this.curTimes.getVehicleCount(id);
					this.travelTimes.setTravelTime(id, vehicleCount, avgTime);
				}
				this.curTimes.removeData();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
