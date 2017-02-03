package user;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;

import com.rabbitmq.client.*;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import model.Coordinates;
import model.InfrastructureNode;
import model.InfrastructureNodeImpl;
import model.NodePath;
import model.Pair;
import model.interfaces.ICoordinates;
import model.interfaces.IGPSObserver;
import model.interfaces.IInfrastructureNode;
import model.interfaces.IInfrastructureNodeImpl;
import model.interfaces.INodePath;
import model.interfaces.msg.ICongestionAlarmMsg;
import model.interfaces.msg.IPathAckMsg;
import model.interfaces.msg.IRequestPathMsg;
import model.interfaces.msg.IRequestTravelTimeMsg;
import model.interfaces.msg.IResponsePathMsg;
import model.interfaces.msg.IResponseTravelTimeMsg;
import model.msg.PathAckMsg;
import model.msg.RequestPathMsg;
import model.msg.RequestTravelTimeMsg;
import utils.gps.GpsMock;
import utils.json.JSONMessagingUtils;
import utils.messaging.MessagingUtils;
import utils.mom.MomUtils;

public class UserDevice extends Thread implements IGPSObserver {

	private String userID;
	private String brokerAddress;
	private ConnectionFactory factory;
	private Integer travelID;
	private List<Pair<INodePath, Integer>> pathsWithTravelID;
	private List<Pair<Integer, Integer>> travelTimes;
	private INodePath chosenPath;
	private InfrastructureNodeImpl start;
	private InfrastructureNodeImpl end;

	public UserDevice(){
		this.travelID = 0;
		this.userID = "id";
		this.chosenPath = new NodePath(new ArrayList<>());
	}
	
	private Channel initChannel() throws IOException, TimeoutException {
		this.factory = new ConnectionFactory();
		this.factory.setHost(brokerAddress);
		Connection connection = this.factory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare("receiveQueue", false, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		return channel;
	}

	@Override
	public void run() {
		this.initNodes();
		this.requestPaths(start, end);
		try {
			this.startReceiving();
			this.travelTimes = new ArrayList<Pair<Integer, Integer>>();
			INodePath selectedPath = evaluateBestPath();
			IPathAckMsg ackMsgToNode = new PathAckMsg(userID, MessagingUtils.PATH_ACK, selectedPath, 0);
			String ackToSend = JSONMessagingUtils.getStringfromPathAckMsg(ackMsgToNode);
			MomUtils.sendMsg(this.factory, selectedPath.getPathNodes().get(0).getNodeID(), ackToSend);
			IPathAckMsg ackMsgToServer = new PathAckMsg(userID, MessagingUtils.PATH_ACK, selectedPath, 0);
			// invio al server
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initNodes() {
		this.start = new InfrastructureNodeImpl("node1", new Coordinates(1.0,1.0), new HashSet<>());
		this.end = new InfrastructureNodeImpl("node2", new Coordinates(1.0,1.0), new HashSet<>());
		this.start.setNearNode(this.end);
		this.end.setNearNode(this.start);
	}

	private void initNodes(InfrastructureNodeImpl start, InfrastructureNodeImpl end) {
		this.start = start;
		this.end = end;
	}
	
	private void startReceiving() throws IOException, TimeoutException {
		Channel channel = null;
		try {
			channel = initChannel();
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				try {
					switchArrivedMsg(message);
				} catch (TimeoutException e) {
					e.printStackTrace();
				}
				System.out.println(" [x] Received A '" + message + "'");
			}
		};

		try {
			channel.basicConsume("receiveQueue", true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void requestPaths(IInfrastructureNode start, IInfrastructureNode end) {
		Vertx vertx = Vertx.vertx();
		HttpClient client = vertx.createHttpClient();
		client.websocket(8080, "localhost", "/some-uri", ws -> {
			ws.handler(data -> {
				System.out.println("Received data " + data.toString("ISO-8859-1"));
				try {
					this.handleResponsePathMsg(data.toString());
				} catch (JSONException | IOException | TimeoutException e) {
					e.printStackTrace();
				}
			});
			IRequestPathMsg requestMsg = new RequestPathMsg(MessagingUtils.REQUEST_PATH, this.start, this.end);
			try {
				String requestPathString = JSONMessagingUtils.getStringfromRequestPathMsg(requestMsg);
				ws.writeBinaryMessage(Buffer.buffer(requestPathString));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		});
	}

	private INodePath evaluateBestPath() {
		int min = Integer.MAX_VALUE;
		int minTravelID = -1;
		INodePath bestPath = null;
		for (Pair<Integer, Integer> p : this.travelTimes) {
			if (p.getSecond() < min) {
				min = p.getSecond();
				minTravelID = p.getFirst();
			}
		}
		for (Pair<INodePath, Integer> p : this.pathsWithTravelID) {
			if (p.getSecond() == minTravelID) {
				bestPath = p.getFirst();
			}
		}
		return bestPath;
	}

	private void switchArrivedMsg(String msg) throws UnsupportedEncodingException, IOException, TimeoutException {
		try {
			int n = MessagingUtils.getIntId(msg);
			switch (n) {
			case 0:
				handleCongestionAlarmMsg(msg);
				break;
			case 1:
				handlePathAckMsg(msg);
				break;
			case 4:
				handleResponsePathMsg(msg);
				break;
			case 5:
				handleResponseTravelTimeMsg(msg);
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void handleCongestionAlarmMsg(String msg) throws JSONException {
		ICongestionAlarmMsg message = JSONMessagingUtils.getCongestionAlarmMsgFromString(msg);
	}

	private void handlePathAckMsg(String msg) throws JSONException {
		IPathAckMsg message = JSONMessagingUtils.getPathAckWithCoordinatesMsgFromString(msg);
		this.chosenPath = message.getPath();
		GpsMock gps = new GpsMock(this.chosenPath, new ArrayList<>());  //TODO: we have to find a way to create a mock path with mock times
		gps.attachObserver(this);
		gps.start();
	}

	private void handleResponsePathMsg(String msg)
			throws JSONException, UnsupportedEncodingException, IOException, TimeoutException {
		System.out.println(msg);
		IResponsePathMsg message = JSONMessagingUtils.getResponsePathMsgFromString(msg);
		List<INodePath> paths;
		paths = message.getPaths();
		this.userID = message.getUserID();
		this.brokerAddress = message.getBrokerAddress();
		for (int j = 0; j < paths.size(); j++) {
			this.pathsWithTravelID.add(new Pair<INodePath, Integer>(paths.get(j), j));
		}
		for (int i = 0; i < paths.size(); i++) {
			IRequestTravelTimeMsg requestMsg = new RequestTravelTimeMsg(userID, MessagingUtils.REQUEST_TRAVEL_TIME, 0,
					paths.get(i), i);
			String toSend = JSONMessagingUtils.getStringfromRequestTravelTimeMsg(requestMsg);
			MomUtils.sendMsg(factory, userID, toSend);
		}
		List<IInfrastructureNode> path = new ArrayList<>();
		path.add(this.start);
		path.add(this.end);
		this.chosenPath.setPath(path);
	}

	private void handleResponseTravelTimeMsg(String msg) throws JSONException {
		IResponseTravelTimeMsg message = JSONMessagingUtils.getResponseTravelTimeMsgFromString(msg);
		this.travelID = message.getTravelID();
		int time = message.getTravelTime();
		this.travelTimes.add(new Pair<Integer, Integer>(this.travelID, time));
	}
	
	@Override
	public void notify(ICoordinates coordinates) {
		
	}
	
	public void requestCoordinates(){
		Vertx vertx = Vertx.vertx();
		HttpClient client = vertx.createHttpClient();

		client.websocket(8080, "localhost", "/some-uri", ws -> {
			ws.handler(data -> {
				System.out.println("Received data " + data.toString("ISO-8859-1"));
				try {
					this.handlePathAckMsg(data.toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			});
			IPathAckMsg pathAckMsg = new PathAckMsg(this.userID, MessagingUtils.PATH_ACK, this.chosenPath, this.travelID);
			try {
				String pathAckString = JSONMessagingUtils.getStringfromPathAckMsg(pathAckMsg);
				ws.writeBinaryMessage(Buffer.buffer(pathAckString));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		});
	}


}
