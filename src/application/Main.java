package application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import server.MainServer;
import user.UserDevice;
import utils.mongodb.MongoDBUtils;
import view.MainView;
import view.NodeView;

public class Main {

	public static void main(String[] args) throws Exception {
		/*MainServer server = new MainServer();
		UserDevice device1 = new UserDevice();
		UserDevice device2 = new UserDevice();
		UserDevice device3 = new UserDevice();
		UserDevice device4 = new UserDevice();
		UserDevice device5 = new UserDevice();
		UserDevice device6 = new UserDevice();
		UserDevice device7 = new UserDevice();
		device1.start();
		device2.start();
		device3.start();
		device4.start();
		device5.start();
		device6.start();
		device7.start();*/
		//MainView view = new MainView();
		//NodeView f = new NodeView("IdNode1");
		String s = "s";
		List<Integer> list = new ArrayList<>();
		for(int i=0; i<200;i++){
			list.add(30);
		}
		MongoDBUtils.initTimes("id1");
		MongoDBUtils.initTimes("id2");
		MongoDBUtils.initTravelTimes("id1", "id2", list);
		MongoDBUtils.initTravelTimes("id1", "id3", list);
		MongoDBUtils.setTravelTime("id1", "id2", 30, 40);
		MongoDBUtils.initTravelTimes("id2", "id3", list);
		MongoDBUtils.setTravelTime("id2", "id3", 30, 40);
		MongoDBUtils.initCurrentTime("id1");
		MongoDBUtils.initCurrentTime("id1", "id2");
		MongoDBUtils.addCurrentTime("id1", "id2", 18);
		MongoDBUtils.addCurrentTime("id1", "id2", 19);
		MongoDBUtils.addCurrentTime("id1", "id2", 20);
		Set<String> set = new HashSet<>();
		set.add("id2");
		//MongoDBUtils.clearCurrentTimes("id1", set);
		//MongoDBUtils.addCurrentTime("id1", "id2", 20);
		MongoDBUtils.initExpectedVehicles("id1");
		MongoDBUtils.initExpectedVehicles("id1", "id2");
		MongoDBUtils.initExpectedVehicles("id1", "id3");
		MongoDBUtils.addExpectedVehicle("id1", "id2", 30);
		MongoDBUtils.addExpectedVehicle("id1", "id2", 40);
		MongoDBUtils.addExpectedVehicle("id1", "id3", 50);
		MongoDBUtils.removeExpectedVehicles("id1", "id2", 35);
		MongoDBUtils.addExpectedVehicle("id1", "id2", 40);
		MongoDBUtils.removeExpectedVehicles("id1", "id2", 41);
		Map<String, List<Integer>> m = MongoDBUtils.getCurrentTimes("id1");
		List<Integer>l = m.get("id2");
		for(Integer i : l){
			System.out.println(i);
		}
		MongoDBUtils.setTravelTime("id2", "id3", 30, 40);
		
		MainView view = new MainView();
	}

}
