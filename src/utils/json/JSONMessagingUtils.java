package utils.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.interfaces.INodePath;
import model.interfaces.msg.ICongestionAlarmMsg;
import model.interfaces.msg.IPathAckMsg;
import model.interfaces.msg.IResponsePathMsg;
import model.interfaces.msg.IRequestPathMsg;
import model.interfaces.msg.IRequestTravelTimeMsg;
import model.interfaces.msg.IResponseTravelTimeMsg;
import model.interfaces.msg.ITravelTimeAckMsg;

public class JSONMessagingUtils {
	private static final String MSG_ID = "msgid";
	private static final String USER_ID = "usrid";
	private static final String PATH = "path";
	private static final String FIRST_NODE = "firstnode";
	private static final String SECOND_NODE = "secondnode";
	private static final String PATH_LIST = "pathlist";
	private static final String TRAVEL_TIME = "traveltime";
	private static final String TRAVEL_ID = "travelid";
	public String getStringfromAcknowledgePathMsg(IPathAckMsg msg) throws JSONException{
		JSONObject obj = new JSONObject();
		obj.put(MSG_ID, msg.getMsgID());
		obj.put(USER_ID, msg.getUserID());
		obj.put(PATH, new JSONNodePath(msg.getPath()));
		return obj.toString();
	}
	
	public String getStringfromCongestionAlarmMsg(ICongestionAlarmMsg msg) throws JSONException{
		JSONObject obj = new JSONObject();
		obj.put(MSG_ID, msg.getMsgID());
		obj.put(FIRST_NODE, new JSONInfrastructureNode(msg.getFirstNode()));
		obj.put(SECOND_NODE, new JSONInfrastructureNode(msg.getSecondNode()));
		return obj.toString();
	}
	
	public String getStringfromPathResponseMsg(IResponsePathMsg msg) throws JSONException{
		JSONObject obj = new JSONObject();
		obj.put(MSG_ID, msg.getMsgID());
		obj.put(USER_ID, msg.getUserID());
		JSONArray array = new JSONArray();
		for(INodePath path : msg.getPaths()){
			array.put(new JSONNodePath(path));
		}
		obj.put(PATH_LIST, array);
		return obj.toString();	
	}
	
	public String getStringfromRequestPathMsg(IRequestPathMsg msg) throws JSONException{
		JSONObject obj = new JSONObject();
		obj.put(MSG_ID, msg.getMsgID());
		obj.put(FIRST_NODE, new JSONInfrastructureNode(msg.getStartingNode()));
		obj.put(SECOND_NODE, new JSONInfrastructureNode(msg.getEndingNode()));
		return obj.toString();	
	}
	
	public String getStringfromRequestTravelTimeMsg(IRequestTravelTimeMsg msg) throws JSONException{
		JSONObject obj = new JSONObject();
		obj.put(MSG_ID, msg.getMsgID());
		obj.put(USER_ID, msg.getUserID());
		obj.put(TRAVEL_ID, msg.getTravelID());
		obj.put(TRAVEL_TIME, msg.getCurrentTravelTime());
		obj.put(PATH, msg.getPath());
		return obj.toString();	
	}
	
	public String getStringfromResponseTravelTimeMsg(IResponseTravelTimeMsg msg) throws JSONException{
		JSONObject obj = new JSONObject();
		obj.put(MSG_ID, msg.getMsgID());
		obj.put(TRAVEL_ID, msg.getTravelID());
		obj.put(TRAVEL_TIME, msg.getTravelTime());
		return obj.toString();	
	}
	
	public String getStringfromTravelTimeAckMsg(ITravelTimeAckMsg msg) throws JSONException{
		JSONObject obj = new JSONObject();
		obj.put(MSG_ID, msg.getMsgID());
		obj.put(FIRST_NODE, new JSONInfrastructureNode(msg.getFirstNode()));
		obj.put(SECOND_NODE, new JSONInfrastructureNode(msg.getSecondNode()));
		obj.put(TRAVEL_TIME, msg.getTravelTime());
		return obj.toString();	
	}
	
	public IPathAckMsg getAcknowledgePathMsgFromString(String s){
		
		return null;
	}
	
	public ICongestionAlarmMsg getCongestionAlarmMsgFromString(String s){
		return null;
	}
	
	public IResponsePathMsg getPathResponseMsgFromString(String s){
		return null;
	}
	
	public IRequestPathMsg getRequestPathMsgFromString(String s){
		return null;
	}
	
	public IRequestTravelTimeMsg getRequestTravelTimeMsgFromString(String s){
		return null;
	}
	
	public IResponseTravelTimeMsg getResponseTravelTimeMsgFromString(String s){
		return null;
	}
	
	public ITravelTimeAckMsg getTravelTimeAckMsgFromString(String s){
		return null;
	}
}
