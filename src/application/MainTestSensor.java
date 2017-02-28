package application;

import infrastructure.TemperatureHumiditySensor;
import model.interfaces.ITemperatureHumiditySensor;

public class MainTestSensor {
	public static void main(String args[]){
		ITemperatureHumiditySensor sensor = new TemperatureHumiditySensor();
		while(true){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("HUM: "+sensor.getHumidity()+"TEMP: "+sensor.getTemperature());
		}
	}
	
}
