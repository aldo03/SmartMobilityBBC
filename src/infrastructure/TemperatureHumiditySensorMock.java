package infrastructure;

import model.interfaces.ITemperatureHumiditySensor;

public class TemperatureHumiditySensorMock implements ITemperatureHumiditySensor{

	private double temperature;
	private double humidity;
	
	
	
	public TemperatureHumiditySensorMock() {
		this.temperature = 0;
		this.humidity = 0;
	}

	@Override
	public void senseTemperatureAndHumidity() {
		
	}

	@Override
	public double getTemperature() {
		return this.temperature;
	}

	@Override
	public double getHumidity() {
		return this.humidity;
	}

}
