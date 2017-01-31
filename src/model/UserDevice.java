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
	
	public void sendMsg(String msg, String host) throws Exception{
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost(host);
	    Connection connection = factory.newConnection();
	    
	    Channel channel = connection.createChannel();
	    channel.queueDeclare("userQueue", false, false, false, null);
	    	  
	    channel.basicPublish("", "userQueue", null, msg.getBytes("UTF-8"));  
	    System.out.println(" [x] Sent '" + msg + "'");

	    channel.close();
	    connection.close();
	}
	
	@Override
	public void run() {
		try {
			this.sendMsg("Want ID from server", "serverHost");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		this.userID = "id1"; //lo getterà dal server
		this.host = "Host: " + userID; //anche questo
		try {
			this.startReceiving();
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
		try {
			this.sendMsg("Want paths from server", "serverHost");
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
		        JSONObject json = null;
		        try {
					json = new JSONObject(message);
				} catch (JSONException e) {
					e.printStackTrace();
				}
		        
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
	
	
	
	
}
