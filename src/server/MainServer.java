package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import edu.asu.emit.qyan.alg.control.YenTopKShortestPathsAlg;
import edu.asu.emit.qyan.alg.model.Graph;
import edu.asu.emit.qyan.alg.model.Path;
import edu.asu.emit.qyan.alg.model.Vertex;
import edu.asu.emit.qyan.alg.model.abstracts.BaseVertex;
import model.NodePath;
import model.interfaces.IInfrastructureNode;
import model.interfaces.IInfrastructureNodeImpl;
import model.interfaces.INodePath;
import model.interfaces.msg.IPathAckMsg;
import model.interfaces.msg.IRequestPathMsg;
import model.interfaces.msg.IResponsePathMsg;
import model.msg.PathAckMsg;
import model.msg.ResponsePathMsg;
import utils.json.JSONMessagingUtils;
import utils.messaging.MessagingUtils;

/**
 * class that model Server logic
 * 
 * @author BBC
 *
 */

public class MainServer {

	private final static String USER_ID = "User-Device-";
	private final static Integer K_SHORTEST_PATHS = 10;
	private Graph graph;
	private Set<IInfrastructureNodeImpl> nodesSet;
	private Map<String, IInfrastructureNodeImpl> nodeMapId;
	private int userSeed;

	public MainServer() throws Exception {
		this.graph = new Graph();
		this.nodesSet = new HashSet<>();
		this.nodeMapId = new HashMap<>();
		this.userSeed = 10000;
		this.initHTTP();
	}

	private void initHTTP() throws Exception{
		HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new HttpHandler(){
        	 @Override
             public void handle(HttpExchange t) throws IOException {
        		 BufferedReader reader = new BufferedReader(new InputStreamReader(t.getRequestBody()));
                 String msg = reader.readLine();
  				 System.out.println("data received: " + msg);
                 try {
 					int n;
 					n = MessagingUtils.getIntId(msg);
 					switch (n) {
 					case 1:
 						Thread t1 = new Thread() {
 							@Override
 							public void run() {
 								try {
 									handlePathAckMsg(t, msg);
 								} catch (JSONException e) {
 									e.printStackTrace();
 								} catch (IOException e) {
									e.printStackTrace();
								}
 							}

 						};
 						t1.start();
 						break;
 					case 2:
 						Thread t2 = new Thread() {
 							@Override
 							public void run() {
 								try {
 									handleRequestPathMsg(t, msg);
 								} catch (JSONException e) {
 									e.printStackTrace();
 								} catch (IOException e) {
									e.printStackTrace();
								}
 							}

 						};
 						t2.start();
 						break;
 					}
 				} catch (JSONException e) {
 					e.printStackTrace();
 				}
        	 }
        });
        server.setExecutor(null); // creates a default executor
        server.start();
	}

	/**
	 * a Request Path msg is received
	 * @param t
	 * @param msg the message received
	 */
	private void handleRequestPathMsg(HttpExchange t, String msg) throws JSONException, IOException{
		IRequestPathMsg requestPathMsg = JSONMessagingUtils.getRequestPathMsgFromString(msg);
		List<INodePath> pathList = this.getShortestPaths(this.findNearNode(requestPathMsg.getStartingNode()),this.findNearNode(
				requestPathMsg.getEndingNode()));
		String brokerAddress = this.getBrokerAddress(requestPathMsg.getStartingNode(), requestPathMsg.getEndingNode());
		IResponsePathMsg responsePathMsg = new ResponsePathMsg(MessagingUtils.RESPONSE_PATH, this.generateUserID(requestPathMsg.getUserID()),
				pathList, brokerAddress);
		String response = JSONMessagingUtils.getStringfromResponsePathMsg(responsePathMsg);
		t.sendResponseHeaders(200, response.length());
		OutputStreamWriter os = new OutputStreamWriter(t.getResponseBody());
		os.write(response);
		os.close();
		
	}

	/**
	 * a Path Ack msg is received
	 * @param msg the message received
	 */
	private void handlePathAckMsg(HttpExchange t, String msg) throws JSONException, IOException {
		IPathAckMsg pathAckMsg = JSONMessagingUtils.getPathAckMsgFromString(msg);
		List<IInfrastructureNode> pathWithCoordinates = new ArrayList<>();
		INodePath pathFromMsg = pathAckMsg.getPath();
		for (IInfrastructureNode node : pathFromMsg.getPathNodes()) {
			for (IInfrastructureNode n : this.nodesSet) {
				if (node.getNodeID().equals(n.getNodeID()))
					pathWithCoordinates.add(n);
			}
		}
		INodePath path = new NodePath(pathWithCoordinates);
		IPathAckMsg coordinatesMsg = new PathAckMsg(pathAckMsg.getUserID(), MessagingUtils.PATH_ACK, path,
				pathAckMsg.getTravelID());
		String response = JSONMessagingUtils.getStringfromPathAckMsg(coordinatesMsg);
		t.sendResponseHeaders(200, response.length());
		OutputStreamWriter os = new OutputStreamWriter(t.getResponseBody());
		os.write(response);
		os.close();
	}

	/**
	 * gets the MOM broker address for a certain path
	 * @param startingNode the first node of the path
	 * @param endingNode the last node of the path
	 * @return the MOM broker address for the given path
	 */
	private String getBrokerAddress(IInfrastructureNode startingNode, IInfrastructureNode endingNode) {
		return "192.168.43.240";
	}

	private String generateUserID(String string) {
		if(string.equals("newuser")){
			return USER_ID + this.userSeed++;
		} else{
			return string;
		}
	}

	/**
	 * method invoked to obtain a list of shortest path
	 * 
	 * @param start
	 * @param finish
	 * @return list of shortest path
	 */
	public List<INodePath> getShortestPaths(IInfrastructureNode start, IInfrastructureNode finish) {
		YenTopKShortestPathsAlg algorithm = new YenTopKShortestPathsAlg(this.graph);
		BaseVertex startNode = this.graph.get_vertex(start.getIntNodeID());
		BaseVertex endNode = this.graph.get_vertex(finish.getIntNodeID());
		List<Path> paths = algorithm.get_shortest_paths(startNode, endNode, K_SHORTEST_PATHS);
		return this.getNodePathFromPath(paths);
	}

	/**
	 * gets Node Paths from Paths
	 * @param paths
	 * @return a list of INodePath
	 */
	private List<INodePath> getNodePathFromPath(List<Path> paths){
		List<INodePath> pathList = new ArrayList<>();
		for(Path path: paths){
			System.out.println(path);
			INodePath nodePath = new NodePath(new ArrayList<>());
			List<IInfrastructureNode> nodeList = new ArrayList<>();
			for(BaseVertex vertex: path.get_vertices()){
				String id = "id"+vertex.get_id();
				nodeList.add(this.nodeMapId.get(id));
			}
			nodePath.setPath(nodeList);
			pathList.add(nodePath);
		}
		return pathList;
	}
	
	/**
	 * method invoked to set the graph and nodeSet
	 * 
	 * @param nodesSet
	 */
	public void setNodes(Set<IInfrastructureNodeImpl> nodesSet) {
		this.nodesSet = nodesSet;
		for (IInfrastructureNodeImpl node : this.nodesSet) {
			BaseVertex v = new Vertex();
			v.set_id(node.getIntNodeID());
			this.graph.add_vertex(v);
			this.nodeMapId.put(node.getNodeID(), node);
		}
		for (IInfrastructureNodeImpl start : this.nodesSet) {
			Integer idStart = start.getIntNodeID();
			for (String end : start.getNearNodesWeighted().keySet()) {
				Integer idEnd = this.getIntNodeID(end);
				this.graph.add_edge(idStart, idEnd, start.getNearNodesWeighted().get(end));
			}
		}
	}
	
	public void setGraph(){
		for (IInfrastructureNodeImpl start : this.nodesSet) {
			Integer idStart = start.getIntNodeID();
			for (String end : start.getNearNodesWeighted().keySet()) {
				Integer idEnd = this.getIntNodeID(end);
				this.graph.add_edge(idStart, idEnd, start.getNearNodesWeighted().get(end));
			}
		}
	}

	/**
	 * method invoked to set a new node in the system
	 * 
	 * @param node
	 */
	public void setNewNode(IInfrastructureNodeImpl node) {
		this.nodesSet.add(node);
		this.nodeMapId.put(node.getNodeID(), node);
		BaseVertex v = new Vertex();
		v.set_id(node.getIntNodeID());
		this.graph.add_vertex(v);
	}
	
	private Integer getIntNodeID(String s) {		
		String str = s.substring(2, s.length());
		Integer idInt = Integer.parseInt(str);
		return idInt;
	}
	
	public void printNodes(){
		for(IInfrastructureNodeImpl node : this.nodesSet){
			System.out.println();
			System.out.println("ID: "+node.getNodeID());
			System.out.println("COORDS: "+node.getCoordinates().getLatitude()+"-"+node.getCoordinates().getLongitude());
			System.out.println("NEAR NODES:");
			for(String s : node.getNearNodesWeighted().keySet()){
				System.out.println(s+" : "+node.getNearNodesWeighted().get(s));
			}
		}
	}
	
	/**
	 * method invoked in order to find the nearest node from the point selected by the user
	 * @param node the node selected from the user
	 * @return the nearest node of nodesSet
	 */
	private IInfrastructureNode findNearNode(IInfrastructureNode node){
		double distance = Double.MAX_VALUE;
		IInfrastructureNode near = node;
		for(IInfrastructureNode n: this.nodesSet){
			double temp = n.getCoordinates().getDistance(node.getCoordinates());
			if(temp<distance){
				distance = temp;
				near = n;
			}
		}
		return near;
	}
	
}
