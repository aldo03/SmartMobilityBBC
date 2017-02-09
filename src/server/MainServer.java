package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;

import edu.asu.emit.qyan.alg.control.YenTopKShortestPathsAlg;
import edu.asu.emit.qyan.alg.model.Graph;
import edu.asu.emit.qyan.alg.model.Path;
import edu.asu.emit.qyan.alg.model.Vertex;
import edu.asu.emit.qyan.alg.model.abstracts.BaseVertex;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.*;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
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
	private final static Integer K_SHORTEST_PATHS = 1;
	private Graph graph;
	private Set<IInfrastructureNodeImpl> nodesSet;
	private Map<String, IInfrastructureNodeImpl> nodeMapId;
	private int userSeed;

	public MainServer() throws Exception {
		this.graph = new Graph();
		this.nodesSet = new HashSet<>();
		this.nodeMapId = new HashMap<>();
		this.userSeed = 0;
		this.initVertx();
	}

	private void initVertx() {
		Vertx vertx = Vertx.vertx();
		Router router = Router.router(vertx);

		router.route("/").handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			response.putHeader("content-type", "text/html").end("<h1>Entry point.</h1>");
		});

		router.get("/api/resources").handler(routingContext -> {
			HttpServerRequest req = routingContext.request();
			HttpServerResponse response = routingContext.response();
			System.out.println(req.method().toString());
			response.end("Reading res list");
		});

		router.postWithRegex("\\/api\\/resources\\/id([0-9])+").handler(routingContext -> {
			HttpServerRequest req = routingContext.request();
			HttpServerResponse response = routingContext.response();
			response.end("creating res." + req.path());
		});

		router.putWithRegex("\\/api\\/resources\\/id([0-9])+").handler(routingContext -> {
			HttpServerRequest req = routingContext.request();
			HttpServerResponse response = routingContext.response();
			response.end("changing res." + req.path());
		});

		router.route("/assets/*").handler(StaticHandler.create("assets"));

		// Create the HTTP server and pass the "accept" method to the request
		// handler.
		vertx.createHttpServer();
		vertx.createHttpServer().websocketHandler(ws -> {
			System.out.println("WebSocket opened!");
			ws.handler(hnd -> {
				System.out.println("> Server: Data received: " + hnd.toString());
				try {
					int n;
					n = MessagingUtils.getIntId(hnd.toString());
					switch (n) {
					case 1:
						Thread t1 = new Thread() {
							@Override
							public void run() {
								try {
									handlePathAckMsg(ws, hnd.toString());
								} catch (JSONException e) {
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
									handleRequestPathMsg(ws, hnd.toString());
								} catch (JSONException e) {
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
			});
			// if (req.uri().equals("/"))
			// req.response().sendFile(path+"/ws.html");
		}).requestHandler(router::accept).listen(8080);
	}

	private void handleRequestPathMsg(ServerWebSocket ws, String msg) throws JSONException {
		IRequestPathMsg requestPathMsg = JSONMessagingUtils.getRequestPathMsgFromString(msg);
		List<INodePath> pathList = this.getShortestPaths(requestPathMsg.getStartingNode(),
				requestPathMsg.getEndingNode());
		String brokerAddress = this.getBrokerAddress(requestPathMsg.getStartingNode(), requestPathMsg.getEndingNode());
		IResponsePathMsg responsePathMsg = new ResponsePathMsg(MessagingUtils.RESPONSE_PATH, this.generateUserID(),
				pathList, brokerAddress);
		String response = JSONMessagingUtils.getStringfromResponsePathMsg(responsePathMsg);
		Buffer buffer = Buffer.buffer().appendString(response);
		ws.write(buffer);
	}

	private void handlePathAckMsg(ServerWebSocket ws, String msg) throws JSONException {
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
		Buffer buffer = Buffer.buffer().appendString(response);
		ws.write(buffer);
	}

	private String getBrokerAddress(IInfrastructureNode startingNode, IInfrastructureNode endingNode) {
		// TODO generate broker address
		return "localhost";
	}

	private String generateUserID() {
		return USER_ID + this.userSeed++;
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
		BaseVertex startNode = new Vertex();
		startNode.set_id(start.getIntNodeID());
		BaseVertex endNode = new Vertex();
		endNode.set_id(finish.getIntNodeID());
		List<Path> paths = algorithm.get_shortest_paths(startNode, endNode, K_SHORTEST_PATHS);
		return this.getNodePathFromPath(paths);
	}

	private List<INodePath> getNodePathFromPath(List<Path> paths){
		List<INodePath> pathList = new ArrayList<>();
		for(Path path: paths){
			INodePath nodePath = new NodePath(new ArrayList<>());
			for(BaseVertex vertex: path.get_vertices()){
				List<IInfrastructureNode> nodeList = new ArrayList<>();
				String id = "id"+vertex.get_id();
				nodeList.add(this.nodeMapId.get(id));
			}
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
		/*for (String nearNode : node.getNearNodesWeighted().keySet()) {
			Integer idNear = this.getIntNodeID(nearNode);
			this.graph.add_edge(node.getIntNodeID(), idNear, node.getNearNodesWeighted().get(nearNode));

		}*/
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

}
