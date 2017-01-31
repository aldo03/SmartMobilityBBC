package utils.messaging;

import org.json.JSONException;
import org.json.JSONObject;

public class MessagingUtils {
	public static final String CONGESTION_ALARM = "congestionalarm";
	public static final String PATH_ACK = "pathack";
	public static final String REQUEST_PATH = "requestpath";
	public static final String REQUEST_TRAVEL_TIME = "requesttraveltime";
	public static final String RESPONSE_PATH = "responsepath";
	public static final String RESPONSE_TRAVEL_TIME = "responsetraveltime";
	public static final String TRAVEL_TIME_ACK = "traveltimeack";
	private static final String MSG_ID = "msgid";
	
	public static int getIntId(JSONObject obj) throws JSONException{
		String id = obj.getString(MSG_ID);
		if(id.equals(CONGESTION_ALARM)){
			return 0;
		} else if(id.equals(PATH_ACK)){
			return 1;
		} else if(id.equals(REQUEST_PATH)){
			return 2;
		} else if(id.equals(REQUEST_TRAVEL_TIME)){
			return 3;
		} else if(id.equals(RESPONSE_PATH)){
			return 4;
		} else if(id.equals(RESPONSE_TRAVEL_TIME)){
			return 5;
		} else if(id.equals(TRAVEL_TIME_ACK)){
			return 6;
		} else return -1;
	}
}
