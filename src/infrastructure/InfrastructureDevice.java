package infrastructure;

import java.util.Set;

import com.rabbitmq.client.ConnectionFactory;

import model.interfaces.IPair;

public class InfrastructureDevice extends Thread {
	private String id;
	private Set<IPair<String, Integer>> nearNodesWeighted;
	ConnectionFactory factory;
	
	public InfrastructureDevice(String id, Set<IPair<String, Integer>> nearNodesWeighted, String brokerHost){
		this.id = id;
		this.nearNodesWeighted = nearNodesWeighted;
	}

	@Override
	public void run() {
		
	}
	
	
}
