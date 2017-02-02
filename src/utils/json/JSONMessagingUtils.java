package utils.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.interfaces.IInfrastructureNode;
import model.interfaces.INodePath;
import model.interfaces.msg.ICongestionAlarmMsg;
import model.interfaces.msg.IPathAckMsg;
import model.interfaces.msg.IResponsePathMsg;
import model.interfaces.msg.IRequestPathMsg;
import model.interfaces.msg.IRequestTravelTimeMsg;
import model.interfaces.msg.IResponseTravelTimeMsg;
import model.interfaces.msg.ITravelTimeAckMsg;
import model.msg.CongestionAlarmMsg;
import model.msg.PathAckMsg;
import model.msg.RequestPathMsg;
import model.msg.RequestTravelTimeMsg;
import model.msg.ResponsePathMsg;
import model.msg.ResponseTravelTimeMsg;
import model.msg.TravelTimeAckMsg;

public class JSONMessagingUtils {
	private static final String MSG_ID = "msgid";
	private static final String USER_ID = "usrid";
	private static final String PATH = "path";
	private static final String FIRST_NODE = "firstnode";
	private static final String SECOND_NODE = "secondnode";
	private static final String PATH_LIST = "pathlist";
	private static final String TRAVEL_TIME = "traveltime";
	private static final String TRAVEL_ID = "travelid";
	private static final String BROKER_ADDR = "brokeraddress";
	
	public static String getStringfromPathAckMsg(IPathAckMsg msg) throws JSONException{
		JSONObject obj = new JSONObject();
		obj.put(MSG_ID, msg.getMsgID());
		obj.put(USER_ID, msg.getUserID());
		obj.put(PATH, new JSONNodePath(msg.getPath()));
		obj.put(TRAVEL_ID, msg.getTravelID());
		return obj.toString();
	}
	
	public static String getStringfromCongestionAlarmMsg(ICongestionAlarmMsg msg) throws JSONException{
		JSONObject obj = new JSONObject();
		obj.put(MSG_ID, msg.getMsgID());
		obj.put(FIRST_NODE, new JSONInfrastructureNode(msg.getFirstNode()));
		obj.put(SECOND_NODE, new JSONInfrastructureNode(msg.getSecondNode()));
		return obj.toString();
	}
	
	public static String getStringfromResponsePathMsg(IResponsePathMsg msg) throws JSONException{
		JSONObject obj = new JSONObject();
		obj.put(MSG_ID, msg.getMsgID());
		obj.put(USER_ID, msg.getUserID());
		JSONArray array = new JSONArray();
		for(INodePath path : msg.getPaths()){
			array.put(new JSONNodePath(path));
		}
		obj.put(PATH_LIST, array);
		obj.put(BROKER_ADDR, msg.getBrokerAddress());
		return obj.toString();	
	}
	
	public static String getStringfromRequestPathMsg(IRequestPathMsg msg) throws JSONException{
		JSONObject obj = new JSONObject();
		obj.put(MSG_ID, msg.getMsgID());
		obj.put(FIRST_NODE, new JSONInfrastructureNode(msg.getStartingNode()));
		obj.put(SECOND_NODE, new JSONInfrastructureNode(msg.getEndingNode()));
		return obj.toString();	
	}
	
	public static String getStringfromRequestTravelTimeMsg(IRequestTravelTimeMsg msg) throws JSONException{
		JSONObject obj = new JSONObject();
		obj.put(MSG_ID, msg.getMsgID());
		obj.put(USER_ID, msg.getUserID());
		obj.put(TRAVEL_ID, msg.getTravelID());
		obj.put(TRAVEL_TIME, msg.getCurrentTravelTime());
		obj.put(PATH, msg.getPath());
		return obj.toString();	
	}
	
	public static String getStringfromResponseTravelTimeMsg(IResponseTravelTimeMsg msg) throws JSONException{
		JSONObject obj = new JSONObject();
		obj.put(MSG_ID, msg.getMsgID());
		obj.put(TRAVEL_ID, msg.getTravelID());
		obj.put(TRAVEL_TIME, msg.getTravelTime());
		return obj.toString();	
	}
	
	public static String getStringfromTravelTimeAckMsg(ITravelTimeAckMsg msg) throws JSONException{
		JSONObject obj = new JSONObject();
		obj.put(MSG_ID, msg.getMsgID());
		obj.put(FIRST_NODE, new JSONInfrastructureNode(msg.getFirstNode()));
		obj.put(SECOND_NODE, new JSONInfrastructureNode(msg.getSecondNode()));
		obj.put(TRAVEL_TIME, msg.getTravelTime());
		return obj.toString();	
	}
	
	public static IPathAckMsg getPathAckMsgFromString(String s) throws JSONException{
		JSONObject obj = new JSONObject(s);
		INodePath path = JSONNodePath.getNodePathfromJSONArray(obj.getJSONArray(PATH));
		IPathAckMsg msg = new PathAckMsg(obj.getString(USER_ID), obj.getString(MSG_ID), path, obj.getInt(TRAVEL_ID));
		return msg;
	}
	
	public static IPathAckMsg getPathAckWithCoordinatesMsgFromString(String s) throws JSONException{
		JSONObject obj = new JSONObject(s);
		INodePath path = JSONNodePath.getNodePathWithCoordinatesfromJSONArray(obj.getJSONArray(PATH));
		IPathAckMsg msg = new PathAckMsg(obj.getString(USER_ID), obj.getString(MSG_ID), path, obj.getInt(TRAVEL_ID));
		return msg;
	}
	
	public static ICongestionAlarmMsg getCongestionAlarmMsgFromString(String s) throws JSONException{
		JSONObject obj = new JSONObject(s);
		IInfrastructureNode firstNode = JSONInfrastructureNode.getInfrastructureNodeFromJSONObject(obj.getJSONObject(FIRST_NODE));
		IInfrastructureNode secondNode = JSONInfrastructureNode.getInfrastructureNodeFromJSONObject(obj.getJSONObject(SECOND_NODE));
		ICongestionAlarmMsg msg = new CongestionAlarmMsg(obj.getString(MSG_ID),firstNode, secondNode);
		return msg;
	}
	
	public static IResponsePathMsg getResponsePathMsgFromString(String s) throws JSONException{
		JSONObject obj = new JSONObject(s);
		List<INodePath> list= new ArrayList<>();
		JSONArray array = obj.getJSONArray(PATH_LIST);
		for(int i=0; i<array.length(); i++){
			INodePath path = JSONNodePath.getNodePathfromJSONArray(array.getJSONArray(i));
			list.add(path);
		}
		IResponsePathMsg msg = new ResponsePathMsg(obj.getString(MSG_ID),obj.getString(USER_ID), list, obj.getString(BROKER_ADDR));
		return msg;
	}
	
	public static IRequestPathMsg getRequestPathMsgFromString(String s) throws JSONException{
		JSONObject obj = new JSONObject(s);
		IInfrastructureNode firstNode = JSONInfrastructureNode.getInfrastructureNodeFromJSONObject(obj.getJSONObject(FIRST_NODE));
		IInfrastructureNode secondNode = JSONInfrastructureNode.getInfrastructureNodeFromJSONObject(obj.getJSONObject(SECOND_NODE));
		IRequestPathMsg msg = new RequestPathMsg(obj.getString(MSG_ID),firstNode, secondNode);
		return msg;
	}
	
	public static IRequestTravelTimeMsg getRequestTravelTimeMsgFromString(String s) throws JSONException{
		JSONObject obj = new JSONObject(s);
		INodePath path = JSONNodePath.getNodePathfromJSONArray(obj.getJSONArray(PATH));
		IRequestTravelTimeMsg msg =new RequestTravelTimeMsg(obj.getString(USER_ID), obj.getString(MSG_ID),
				obj.getInt(TRAVEL_TIME),path, obj.getInt(TRAVEL_ID));
		return msg;
	}
	
	public static IResponseTravelTimeMsg getResponseTravelTimeMsgFromString(String s) throws JSONException{
		JSONObject obj = new JSONObject(s);
		IResponseTravelTimeMsg msg = new ResponseTravelTimeMsg(obj.getString(MSG_ID),obj.getInt(TRAVEL_TIME), obj.getInt(TRAVEL_ID));
		return msg;
	}
	
	public static ITravelTimeAckMsg getTravelTimeAckMsgFromString(String s) throws JSONException{
		JSONObject obj = new JSONObject(s);
		IInfrastructureNode firstNode = JSONInfrastructureNode.getInfrastructureNodeFromJSONObject(obj.getJSONObject(FIRST_NODE));
		IInfrastructureNode secondNode = JSONInfrastructureNode.getInfrastructureNodeFromJSONObject(obj.getJSONObject(SECOND_NODE));
		ITravelTimeAckMsg msg = new TravelTimeAckMsg(obj.getString(USER_ID), obj.getString(MSG_ID), firstNode, secondNode, obj.getInt(TRAVEL_TIME));
		return msg;
	}
}
