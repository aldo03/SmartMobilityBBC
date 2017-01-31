package infrastructure;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import model.interfaces.IPair;

public class InfrastructureDevice extends Thread {
	private String id;
	private Set<IPair<String, Integer>> nearNodesWeighted;
	private String brokerHost;
	ConnectionFactory factory;
	
	public InfrastructureDevice(String id, Set<IPair<String, Integer>> nearNodesWeighted, String brokerHost){
		this.id = id;
		this.nearNodesWeighted = nearNodesWeighted;
	}

	@Override
	public void run() {
		try {
			Channel channel = initChannel();
			Consumer consumer = new DefaultConsumer(channel) {
			      @Override
			      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
			          throws IOException {
			        String message = new String(body, "UTF-8");
			        try {
						JSONObject json = new JSONObject(message);
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
			      }
			    };
			channel.basicConsume(this.id, true, consumer);
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
	}
	
	private Channel initChannel() throws IOException, TimeoutException{
		this.factory = new ConnectionFactory();
	    this.factory.setHost(this.brokerHost);
	    Connection connection = this.factory.newConnection();
	    Channel channel = connection.createChannel();
	    channel.queueDeclare("receiveQueue", false, false, false, null);
	    return channel;
	}
}
