package application;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import infrastructure.InfrastructureDevice;
import model.Pair;
import model.interfaces.IPair;

public class MainRasp {
	
	public static void main(String args[]){
		Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
		mongoLogger.setLevel(Level.SEVERE); 
		Set<IPair<String,Integer>> set = new HashSet<>();
		set.add(new Pair<String,Integer>("id2",4));
		set.add(new Pair<String,Integer>("id11",8));
		InfrastructureDevice dev = new InfrastructureDevice("id1", set, "192.168.43.240",false);
		dev.start();
	}
}
