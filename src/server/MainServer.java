package server;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.*;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import model.interfaces.IInfrastructureNode;
import model.interfaces.IInfrastructureNodeImpl;
import model.interfaces.INodePath;
import model.interfaces.IPair;
import model.interfaces.msg.IRequestPathMsg;
import model.interfaces.msg.IResponsePathMsg;
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
	private Map<String, Set<IPair<String, Integer>>> graph;
	private Set<IInfrastructureNode> nodesSet;
	private int userSeed;

	public MainServer() throws Exception {
		this.graph = new HashMap<>();
		this.nodesSet = new HashSet<>();
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

		vertx.createHttpServer().websocketHandler(ws -> {
			System.out.println("WebSocket opened!");
			ws.handler(hnd -> {
				System.out.println("data received: " + hnd.toString());
				try {
					int n;
					n = MessagingUtils.getIntId(hnd.toString());
					switch (n) {
					case 1:
						
						break;
					case 2:
						this.handleRequestPathMsg(ws, hnd.toString());
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
		int count = 1;
		while(count<2){

		}
		IRequestPathMsg requestPathMsg = JSONMessagingUtils.getRequestPathMsgFromString(msg);
		List<INodePath> pathList = this.getShortestPaths(requestPathMsg.getStartingNode(),requestPathMsg.getEndingNode());
		String brokerAddress = this.getBrokerAddress(requestPathMsg.getStartingNode(),requestPathMsg.getEndingNode());
		IResponsePathMsg responsePathMsg = new ResponsePathMsg(this.generateUserID(), MessagingUtils.RESPONSE_PATH, pathList, brokerAddress);
		String response = JSONMessagingUtils.getStringfromResponsePathMsg(responsePathMsg);
		Buffer buffer = Buffer.buffer().appendString(response);
		ws.write(buffer);
	}
	
	private String getBrokerAddress(IInfrastructureNode startingNode, IInfrastructureNode endingNode) {
		//TODO genearate broker address
		return "localhost";
	}

	private String generateUserID(){
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
		List<INodePath> pathList = new ArrayList<>();
		// TODO short path algorithm
		return pathList;
	}

	/**
	 * method invoked to set the graph and nodeSet
	 * 
	 * @param nodesSet
	 */
	public void setNodes(Set<IInfrastructureNode> nodesSet) {
		this.nodesSet = nodesSet;
		for (IInfrastructureNode node : this.nodesSet)
			this.graph.put(node.getNodeID(), ((IInfrastructureNodeImpl) node).getNearNodesWeighted());
	}

	/**
	 * method invoked to set a new node in the system
	 * 
	 * @param node
	 */
	public void setNewNode(IInfrastructureNodeImpl node) {
		this.nodesSet.add(node);
		this.graph.put(node.getNodeID(), node.getNearNodesWeighted());
	}
}
