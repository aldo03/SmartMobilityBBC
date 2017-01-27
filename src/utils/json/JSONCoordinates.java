package utils.json;

import org.json.JSONException;
import org.json.JSONObject;

import model.interfaces.ICoordinates;

public class JSONCoordinates extends JSONObject {
	private static final String LAT = "latitude";
	private static final String LONG = "longitude";
	public JSONCoordinates(ICoordinates coordinates){
		try {
			this.put(LAT, coordinates.getLatitude());
			this.put(LONG, coordinates.getLongitude());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public static ICoordinates getCoordinatesFromJSONObject(JSONObject obj){
		ICoordinates coordinates = null;
		return coordinates;
	}
}
