package utils.gps;

import java.util.List;

import model.interfaces.IGPSObserver;
import model.interfaces.INodePath;

public class GpsMock extends Thread {
	private INodePath path;
	private List<Integer> prefixedTimes;
	private int currentIndex;
	private IGPSObserver observer;
	
	public GpsMock(INodePath path, List<Integer> prefixedTimes){
		this.path = path;
		this.path.removeFirstNode();    //first node is removed. We assume that the user starts from that node
		this.prefixedTimes = prefixedTimes;
		this.currentIndex = 0;
	}
	
	public void attachObserver(IGPSObserver observer){
		this.observer = observer;
	}
	
	@Override
	public void run() {
		while(this.currentIndex<this.prefixedTimes.size()){
			try {
				Thread.sleep(this.prefixedTimes.get(currentIndex)*1000);
				this.observer.notifyGps(this.path.getPathNodes().get(currentIndex).getCoordinates());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}	
}
