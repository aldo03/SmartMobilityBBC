package user;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import com.rabbitmq.client.*;

import model.InfrastructureNode;
import model.Pair;
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
import utils.json.JSONMessagingUtils;
import utils.messaging.MessagingUtils;
import utils.mom.MomUtils;

public class UserDevice extends Thread {

	private String userID;
	private String host;
	private ConnectionFactory factory;
	private List<Pair<INodePath,Integer>> pathsWithTravelID;
	private List<Pair<Integer, Integer>> travelTimes;
	private INodePath chosenPath;
	
	private Channel initChannel() throws IOException, TimeoutException{
		this.factory = new ConnectionFactory();
	    this.factory.setHost(host);
	    Connection connection = this.factory.newConnection();
	    Channel channel = connection.createChannel();
	    channel.queueDeclare("receiveQueue", false, false, false, null);
	    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
	    return channel;
	}

	@Override
	public void run() {
		this.userID = "id1"; //get from server
		this.host = "ip"; // get from server
		try {
			this.startReceiving();
			this.travelTimes = new ArrayList<Pair<Integer,Integer>>();
			IInfrastructureNode start = new InfrastructureNode("node1");
			IInfrastructureNode end = new InfrastructureNode("node2");
			IRequestPathMsg requestMsg = new RequestPathMsg(MessagingUtils.REQUEST_PATH, start, end);
			String toSend = JSONMessagingUtils.getStringfromRequestPathMsg(requestMsg);
			MomUtils.sendMsg(this.factory, this.userID, toSend);
			INodePath selectedPath = evaluateBestPath();
			IPathAckMsg ackMsgToNode = new PathAckMsg(userID, MessagingUtils.PATH_ACK, selectedPath, 0);
			String ackToSend = JSONMessagingUtils.getStringfromPathAckMsg(ackMsgToNode);
			MomUtils.sendMsg(this.factory, selectedPath.getPathNodes().get(0).getNodeID() , ackToSend);
			IPathAckMsg ackMsgToServer = new PathAckMsg(userID, MessagingUtils.PATH_ACK, selectedPath, 0);
			//invio al server
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void startReceiving() throws IOException, TimeoutException{
		Channel channel = null;
		try {
			channel = initChannel();
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}	
		Consumer consumer = new DefaultConsumer(channel) {
		      @Override
		      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
		          throws IOException {
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

	
	private INodePath evaluateBestPath(){
		int min = Integer.MAX_VALUE;
		int minTravelID = -1;
		INodePath bestPath = null;
		for(Pair<Integer, Integer> p : this.travelTimes){
			if(p.getSecond() < min){
				min = p.getSecond();
				minTravelID = p.getFirst();
			}
		}		
		for(Pair<INodePath,Integer> p : this.pathsWithTravelID){
			if(p.getSecond() == minTravelID){
				bestPath = p.getFirst();
			}
		}
		return bestPath;
	}
	
	
	private void switchArrivedMsg(String msg) throws UnsupportedEncodingException, IOException, TimeoutException{
		try {
        	int n = MessagingUtils.getIntId(msg);
        	switch(n){
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
	
	private void handleCongestionAlarmMsg(String msg) throws JSONException{
		ICongestionAlarmMsg message = JSONMessagingUtils.getCongestionAlarmMsgFromString(msg);	
	}
	
	private void handlePathAckMsg(String msg) throws JSONException{
		IPathAckMsg message = JSONMessagingUtils.getPathAckWithCoordinatesMsgFromString(msg);
		this.chosenPath = message.getPath();
	}
	
	private void handleResponsePathMsg(String msg) throws JSONException, UnsupportedEncodingException, IOException, TimeoutException{
		IResponsePathMsg message = JSONMessagingUtils.getResponsePathMsgFromString(msg);
		List<INodePath> paths;
		paths = message.getPaths();
		for(int j = 0; j < paths.size(); j++){
			this.pathsWithTravelID.add(new Pair<INodePath,Integer>(paths.get(j), j));
		}
		for(int i = 0; i < paths.size(); i++){
			IRequestTravelTimeMsg requestMsg = new RequestTravelTimeMsg(userID, MessagingUtils.REQUEST_TRAVEL_TIME, 0, paths.get(i), i);
			String toSend = JSONMessagingUtils.getStringfromRequestTravelTimeMsg(requestMsg);
			MomUtils.sendMsg(factory, userID, toSend);
		}
	}
	
	private void handleResponseTravelTimeMsg(String msg) throws JSONException{
		IResponseTravelTimeMsg message = JSONMessagingUtils.getResponseTravelTimeMsgFromString(msg);		
		int id = message.getTravelID();
		int time = message.getTravelTime();
		this.travelTimes.add(new Pair<Integer, Integer>(id, time));		
	}
	
	
	
}
