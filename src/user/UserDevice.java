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
import model.interfaces.INodePath;
import model.interfaces.msg.ICongestionAlarmMsg;
import model.interfaces.msg.IPathAckMsg;
import model.interfaces.msg.IRequestPathMsg;
import model.interfaces.msg.IRequestTravelTimeMsg;
import model.interfaces.msg.IResponsePathMsg;
import model.interfaces.msg.IResponseTravelTimeMsg;
import model.interfaces.msg.ITravelTimeAckMsg;
import model.msg.PathAckMsg;
import model.msg.RequestPathMsg;
import model.msg.RequestTravelTimeMsg;
import model.msg.TravelTimeAckMsg;
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
	private IInfrastructureNode start;
	private IInfrastructureNode end;
	private int currentIndex;
	private long timerValue;

	public UserDevice(IInfrastructureNode start, IInfrastructureNode end){
		this.travelID = 0;
		this.userID = "newuser";
		this.chosenPath = new NodePath(new ArrayList<>());
		this.pathsWithTravelID = new ArrayList<>();
		this.start = start;
		this.end = end;
	}
	
	private Channel initChannel() throws IOException, TimeoutException {
		this.factory = new ConnectionFactory();
		this.factory.setHost(brokerAddress);
		Connection connection = this.factory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(this.userID, false, false, false, null);
		return channel;
	}

	@Override
	public void run() {
		this.travelTimes = new ArrayList<Pair<Integer, Integer>>();
		this.currentIndex = 0;
		this.requestPaths(start, end);
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
				System.out.println(" [User] "+userID+" received  '" + message + "'");
				try {
					switchArrivedMsg(message);
				} catch (TimeoutException e) {
					e.printStackTrace();
				}
			}
		};

		try {
			channel.basicConsume(this.userID, true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void requestPaths(IInfrastructureNode start, IInfrastructureNode end) {
		Vertx vertx = Vertx.vertx();
		HttpClient client = vertx.createHttpClient();
		client.websocket(8080, "localhost", "/some-uri", ws -> {
			ws.handler(data -> {
				System.out.println("> [User " + this.userID + "] Received data " + data.toString("ISO-8859-1"));
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
		this.timerValue = System.currentTimeMillis();
		this.travelID = minTravelID;
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
		List<Integer> times = new ArrayList<>();
		times.add(2);
		INodePath path = new NodePath(new ArrayList<>(this.chosenPath.getPathNodes()));
		GpsMock gps = new GpsMock(path, times);  //TODO: we have to find a way to create a mock path with mock times
		gps.attachObserver(this);
		gps.start();
	}

	private void handleResponsePathMsg(String msg)
			throws JSONException, UnsupportedEncodingException, IOException, TimeoutException {
		//System.out.println(msg);
		IResponsePathMsg message = JSONMessagingUtils.getResponsePathMsgFromString(msg);
		List<INodePath> paths;
		paths = message.getPaths();
		for(INodePath path : paths){
			path.printPath();
		}
		this.userID = message.getUserID();
		this.brokerAddress = message.getBrokerAddress();
		try {
			this.startReceiving();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int j = 0; j < paths.size(); j++) {
			this.pathsWithTravelID.add(new Pair<INodePath, Integer>(paths.get(j), j));
		}
		for (int i = 0; i < paths.size(); i++) {
			IRequestTravelTimeMsg requestMsg = new RequestTravelTimeMsg(userID, MessagingUtils.REQUEST_TRAVEL_TIME, 0,
					paths.get(i), i, false);
			String toSend = JSONMessagingUtils.getStringfromRequestTravelTimeMsg(requestMsg);
			MomUtils.sendMsg(factory, paths.get(i).getPathNodes().get(0).getNodeID(), toSend);
		}
		/*List<IInfrastructureNode> path = new ArrayList<>();
		path.add(this.start);
		path.add(this.end);
		this.chosenPath.setPath(path);*/
	}

	private void handleResponseTravelTimeMsg(String msg) throws JSONException {
		IResponseTravelTimeMsg message = JSONMessagingUtils.getResponseTravelTimeMsgFromString(msg);
		this.travelID = message.getTravelID();
		int time = message.getTravelTime();
		if(message.frozenDanger()){
			System.out.println("Frozen Danger on path number "+message.getTravelID());
		}
		this.travelTimes.add(new Pair<Integer, Integer>(this.travelID, time));
		System.out.println("[User "+this.userID+" PathsWithTravelID size:"+ this.pathsWithTravelID.size());
		if(this.travelTimes.size()==this.pathsWithTravelID.size()){
			System.out.println("[User "+this.userID+": All times received");
			this.chosenPath = this.evaluateBestPath();
			this.requestCoordinates();
			try {
				this.sendAckToNode();
			} catch (IOException | TimeoutException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void sendAckToNode() throws JSONException, UnsupportedEncodingException, IOException, TimeoutException{
		IPathAckMsg ackMsgToNode = new PathAckMsg(this.userID, MessagingUtils.PATH_ACK, this.chosenPath, this.travelID);
	    String ackToSend = JSONMessagingUtils.getStringfromPathAckMsg(ackMsgToNode);
	    MomUtils.sendMsg(this.factory, this.chosenPath.getPathNodes().get(0).getNodeID(), ackToSend);
	}
	
	private void nearNextNode(int time) throws JSONException, UnsupportedEncodingException, IOException, TimeoutException{
		ITravelTimeAckMsg msg = new TravelTimeAckMsg(this.userID, MessagingUtils.TRAVEL_TIME_ACK,
				chosenPath.getPathNodes().get(this.currentIndex), this.chosenPath.getPathNodes().get(this.currentIndex + 1), time);
		String travelTimeAck = JSONMessagingUtils.getStringfromTravelTimeAckMsg(msg);
		MomUtils.sendMsg(this.factory, this.chosenPath.getPathNodes().get(this.currentIndex).getNodeID(), travelTimeAck);
	}

	@Override
	public void notifyGps(ICoordinates coordinates) {		//we always check the next node. If the signal is lost, the range is too small.
		this.chosenPath.printPath();
		if(this.chosenPath.getPathNodes().get(this.currentIndex+1).getCoordinates().isCloseEnough(coordinates)){
			int time = (int) (System.currentTimeMillis()-this.timerValue);
			time/=1000;
			this.timerValue = System.currentTimeMillis();
			try {
				this.nearNextNode(time);
			} catch (JSONException | IOException | TimeoutException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void requestCoordinates(){
		Vertx vertx = Vertx.vertx();
		HttpClient client = vertx.createHttpClient();

		client.websocket(8080, "localhost", "/some-uri", ws -> {
			ws.handler(data -> {
				System.out.println("> [User " + this.userID + "] Received data " + data.toString("ISO-8859-1"));
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
