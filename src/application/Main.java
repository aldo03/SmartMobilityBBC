package application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import infrastructure.InfrastructureDevice;
import model.interfaces.IPair;
import server.MainServer;
import model.Coordinates;
import model.InfrastructureNodeImpl;
import model.Pair;
import model.interfaces.IInfrastructureNode;
import model.interfaces.IInfrastructureNodeImpl;
import user.UserDevice;
import utils.mongodb.MongoDBUtils;
import view.MainView;

public class Main {
 
	public static void main(String[] args) throws Exception {
		Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
		mongoLogger.setLevel(Level.SEVERE); 
		MainServer server = new MainServer();	
		MongoDBUtils.initDb();
		List<IInfrastructureNode> nodes = new ArrayList<>();
		try {
			Scanner s = new Scanner(new FileReader("graph.txt"));
			int id;
			int tempId = -1;
			double lat;
			double longit;
			tempId = s.nextInt();
			Set<IPair<String, Integer>> set;
			while(s.hasNextLine()){
				set = new HashSet<>();
				id = tempId;
				IInfrastructureNodeImpl node = new InfrastructureNodeImpl("id"+id);
				lat = s.nextDouble();
				longit = s.nextDouble();
				node.setCoordinates(new Coordinates(lat, longit));
				s.nextLine();
				tempId = s.nextInt();
				while(tempId==id){
					int near = s.nextInt();
					int dist = s.nextInt();
					node.setNearNodeWeighted("id"+near, dist);
					set.add(new Pair<String,Integer>("id"+near, dist));
					if(s.hasNextLine()){
						s.nextLine();
						tempId = s.nextInt();
					}
					else break;
				}
				if(id!=1){
					InfrastructureDevice infDev = new InfrastructureDevice("id"+id,set,"localhost", true);
					infDev.start();
				}
				server.setNewNode(node);
				nodes.add(node);
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		server.setGraph();
		List<UserDevice> users = new ArrayList<>();
		
		MainView view = new MainView(nodes, users);
		view.setVisible(true);
	}

}
