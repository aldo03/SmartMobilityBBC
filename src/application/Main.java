package application;

import java.util.ArrayList;
import java.util.List;

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
		MainView view = new MainView();
		//NodeView f = new NodeView("IdNode1");
		/*List<Integer> list = new ArrayList<>();
		for(int i=0; i<200;i++){
			list.add(30);
		}
		MongoDBUtils.initTimes("id1");
		MongoDBUtils.initTimes("id2");
		MongoDBUtils.initTravelTimes("id1", "id2", list);
		MongoDBUtils.initTravelTimes("id1", "id3", list);
		MongoDBUtils.setTravelTime("id1", "id2", 30, 40);
		MongoDBUtils.initTravelTimes("id2", "id3", list);
		MongoDBUtils.setTravelTime("id2", "id3", 30, 40);*/
	}

}
