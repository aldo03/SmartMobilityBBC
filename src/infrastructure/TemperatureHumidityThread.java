package infrastructure;

import model.interfaces.ITemperatureHumidityObserver;
import model.interfaces.ITemperatureHumiditySensor;

public class TemperatureHumidityThread extends Thread {

	private ITemperatureHumiditySensor sensor;
	private ITemperatureHumidityObserver observer;

	public TemperatureHumidityThread(ITemperatureHumiditySensor sensor) {
		this.sensor = sensor;
	}

	public void attachObserver(ITemperatureHumidityObserver observer) {
		this.observer = observer;
	}

	@Override
	public void run() {
		while (true) {
			this.sensor.senseTemperatureAndHumidity();
			System.out.println("humidity:" + this.sensor.getHumidity());
			if (this.observer != null)
				this.observer.setHumidity(this.sensor.getHumidity());
			System.out.println("temperature:" + this.sensor.getTemperature());
			if (this.observer != null)
				this.observer.setTemperature(this.sensor.getTemperature());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
