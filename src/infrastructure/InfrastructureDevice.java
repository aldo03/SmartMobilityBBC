package infrastructure;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import model.interfaces.IInfrastructureNode;
import model.interfaces.INodePath;
import model.interfaces.IPair;
import model.interfaces.msg.IPathAckMsg;
import model.interfaces.msg.IRequestTravelTimeMsg;
import model.interfaces.msg.IResponseTravelTimeMsg;
import model.interfaces.msg.ITravelTimeAckMsg;
import model.msg.RequestTravelTimeMsg;
import model.msg.ResponseTravelTimeMsg;
import utils.json.JSONMessagingUtils;
import utils.messaging.MessagingUtils;
import utils.mom.MomUtils;

public class InfrastructureDevice extends Thread {
	private String id;
	private Set<IPair<String, Integer>> nearNodesWeighted;
	private String brokerHost;
	ConnectionFactory factory;

	public InfrastructureDevice(String id, Set<IPair<String, Integer>> nearNodesWeighted, String brokerHost) {
		this.id = id;
		this.nearNodesWeighted = nearNodesWeighted;
	}

	@Override
	public void run() {
		try {
			Channel channel = initChannel();
			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					try {
						switchArrivedMessage(message);
					} catch (TimeoutException e) {
						e.printStackTrace();
					}
				}
			};
			channel.basicConsume(this.id, true, consumer);
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
	}

	private Channel initChannel() throws IOException, TimeoutException {
		this.factory = new ConnectionFactory();
		this.factory.setHost(this.brokerHost);
		Connection connection = this.factory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare("receiveQueue", false, false, false, null);
		return channel;
	}

	private void switchArrivedMessage(String message)
			throws UnsupportedEncodingException, IOException, TimeoutException {
		try {
			int num = MessagingUtils.getIntId(message);
			switch (num) {
			case 1:
				handlePathAckMsg(message);
				break;
			case 3:
				handleRequestTravelTimeMsg(message);
				break;
			case 6:
				handleTravelTimeAckMsg(message);
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void handlePathAckMsg(String message) throws JSONException {
		IPathAckMsg msg = JSONMessagingUtils.getPathAckMsgFromString(message);
		//At this point, sets the user among the ones that are going to move across a certain path
	}

	private void handleRequestTravelTimeMsg(String message)
			throws JSONException, UnsupportedEncodingException, IOException, TimeoutException {
		IRequestTravelTimeMsg msg = JSONMessagingUtils.getRequestTravelTimeMsgFromString(message);
		INodePath path = msg.getPath();
		path.removeFirstNode();					 //The path is forwarded without the current node
		if (path.getPathNodes().size() > 0) {    //This is not the last node of the path
			IInfrastructureNode nextNode = path.getPathNodes().get(0);
			IRequestTravelTimeMsg msgToSend = new RequestTravelTimeMsg(msg.getUserID(),
					MessagingUtils.REQUEST_TRAVEL_TIME, msg.getCurrentTravelTime() + getTravelTime(nextNode), path,
					msg.getTravelID());
			String strToSend = JSONMessagingUtils.getStringfromRequestTravelTimeMsg(msgToSend);
			MomUtils.sendMsg(factory, nextNode.getNodeID(), strToSend);
		} else { 								//This is the last node of the path
			IResponseTravelTimeMsg m = new ResponseTravelTimeMsg(MessagingUtils.RESPONSE_TRAVEL_TIME, msg.getCurrentTravelTime(), msg.getTravelID());
			String sToSend = JSONMessagingUtils.getStringfromResponseTravelTimeMsg(m);
			MomUtils.sendMsg(factory, msg.getUserID(), sToSend);
		}
	}

	private void handleTravelTimeAckMsg(String message) throws JSONException {
		ITravelTimeAckMsg msg = JSONMessagingUtils.getTravelTimeAckMsgFromString(message);
	}

	private int getTravelTime(IInfrastructureNode node) {
		return 0; // return current travel time between this and node
	}
}
