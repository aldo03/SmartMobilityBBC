package application;

import server.MainServer;
import user.UserDevice;

public class Main {

	public static void main(String[] args) throws Exception {
		MainServer server = new MainServer();
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
		device7.start();
		device1.requestCoordinates();
	}

}
