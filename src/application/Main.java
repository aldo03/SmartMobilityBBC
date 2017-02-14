package application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.asu.emit.qyan.alg.control.YenTopKShortestPathsAlg;
import edu.asu.emit.qyan.alg.model.Graph;
import edu.asu.emit.qyan.alg.model.Path;
import edu.asu.emit.qyan.alg.model.Vertex;
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
import view.NodeView;

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
				InfrastructureDevice infDev = new InfrastructureDevice("id"+id,set,"localhost");
				infDev.start();
				server.setNewNode(node);
				nodes.add(node);
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		server.setGraph();
		
		MainView view = new MainView(nodes);
		/*UserDevice user = new UserDevice(nodes.get(3), nodes.get(183), new ArrayList<>());
		user.run();*/
		
		
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
		
		/*List<Integer> list = new ArrayList<>();
		for(int i=0; i<200;i++){
			list.add(30);
		}
		MongoDBUtils.initTimes("id1");
		MongoDBUtils.initTimes("id2");
		MongoDBUtils.initTempHum("id1", 25.6, 33.4);
		MongoDBUtils.initTempHum("id2", 25.8, 33.2);
		MongoDBUtils.setTemp("id1", 25.7);
		MongoDBUtils.setHum("id1", 35.7);
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
		IPair<Double, Double> temp = MongoDBUtils.getTempHum("id1");
		System.out.println(temp.getFirst());
		System.out.println(temp.getSecond());
		Set<IInfrastructureNodeImpl> nodesSet = new HashSet<IInfrastructureNodeImpl>();
		Set<IInfrastructureNodeImpl> neighborsn1 = new HashSet<IInfrastructureNodeImpl>();
		Set<IInfrastructureNodeImpl> neighborsn2 = new HashSet<IInfrastructureNodeImpl>();
		Set<IInfrastructureNodeImpl> neighborsn3 = new HashSet<IInfrastructureNodeImpl>();
		
		InfrastructureNodeImpl n1 = new InfrastructureNodeImpl("id1");
		InfrastructureNodeImpl n2 = new InfrastructureNodeImpl("id2");
		InfrastructureNodeImpl n3 = new InfrastructureNodeImpl("id3");
		neighborsn1.add(n2);
		neighborsn1.add(n3);
		neighborsn2.add(n1);
		neighborsn2.add(n3);
		neighborsn3.add(n1);
		neighborsn3.add(n2);
		nodesSet.add(n1);
		nodesSet.add(n2);
		nodesSet.add(n3);				
		MainView view = new MainView(nodesSet);*/
		
		
		
		
		
		/*Graph g = new Graph();
		Vertex v0 = new Vertex();
		v0.set_id(0);
		Vertex v1 = new Vertex();
		v1.set_id(1);
		Vertex v2 = new Vertex();
		v2.set_id(2);
		g.add_vertex(v0);
		g.add_vertex(v1);
		g.add_vertex(v2);
		g.add_edge(0, 1, 45);
		g.add_edge(0, 2, 56);
		g.add_edge(1, 0, 34);
		g.add_edge(1, 2, 89);
		g.add_edge(2, 1, 23);
		g.add_edge(2, 0, 56);
		YenTopKShortestPathsAlg algorithm = new YenTopKShortestPathsAlg(g);
		List<Path> paths = algorithm.get_shortest_paths(v0, v2, 6);
		System.out.println(paths);*/
	}

}
