package model.msg;

import model.interfaces.INodePath;
import model.interfaces.msg.IRequestTravelTimeMsg;

public class RequestTravelTimeMsg implements IRequestTravelTimeMsg {
	
	private String userId;
	private String msgId;
	private int currentTravelTime;
	private INodePath path;
	private int travelId;
	
	public RequestTravelTimeMsg(String userId, String msgId, int currentTravelTime,
			INodePath path, int travelId){
		this.userId = userId;
		this.msgId = msgId;
		this.currentTravelTime = currentTravelTime;
		this.path = path;
		this.travelId = travelId;
	}

	@Override
	public String getUserID() {
		return this.userId;
	}

	@Override
	public String getMsgID() {
		return this.msgId;
	}

	@Override
	public int getCurrentTravelTime() {
		return this.currentTravelTime;
	}

	@Override
	public INodePath getPath() {
		return this.path;
	}

	@Override
	public int getTravelID() {
		return this.travelId;
	}

}
