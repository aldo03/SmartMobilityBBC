package model.msg;

import model.interfaces.msg.IResponseTravelTimeMsg;

public class ResponseTravelTimeMsg implements IResponseTravelTimeMsg {
	
	private String msgId;
	private int travelTime;
	private int travelId;
	
	public ResponseTravelTimeMsg(String msgId, int travelTime, int travelId){
		this.msgId = msgId;
		this.travelTime = travelTime;
		this.travelId = travelId;
	}

	@Override
	public String getMsgID() {
		return this.msgId;
	}

	@Override
	public int getTravelTime() {
		return this.travelTime;
	}

	@Override
	public int getTravelID() {
		return this.travelId;
	}

}
