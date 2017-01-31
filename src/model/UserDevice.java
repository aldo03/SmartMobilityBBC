package model;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import com.rabbitmq.client.*;

import model.interfaces.IInfrastructureNode;
import model.interfaces.INodePath;
import utils.json.JSONMessagingUtils;
import utils.messaging.MessagingUtils;
import utils.mom.MomUtils;

public class UserDevice extends Thread {

	private String userID;
	private String host;
	private ConnectionFactory factory;
	
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
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
		try {
			MomUtils.sendMsg(this.factory, this.userID, "Want paths from server");
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
		        
		        System.out.println(" [x] Received A '" + message + "'");
		      }
		    };

		    try {
				channel.basicConsume("receiveQueue", true, consumer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	/*
	private INodePath evaluateBestPath(Set<NodePath> paths){
		
		return new NodePath(new List<InfrastructureNode>());
	}
	*/
	
	private void switchArrivedMsg(String msg){
		try {
        	int n = MessagingUtils.getIntId(msg);
        	switch(n){
        	case 0:
        		break;
        	case 1:
        		break;
        	case 4:
        		break;
        	case 5:
        		break;
        		
        	}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void handleCongestionAlarmMsg(JSONObject json){
	}
	
	
	
	
}
