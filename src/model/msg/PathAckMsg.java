package model.msg;

import model.interfaces.INodePath;
import model.interfaces.msg.IPathAckMsg;

public class PathAckMsg implements IPathAckMsg {
	
	private String userId;
	private String msgId;
	private INodePath path;
	private int travelId;
	
	public PathAckMsg(String userId, String msgId, INodePath path, int travelId){
		this.userId = userId;
		this.msgId = msgId;
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
	public INodePath getPath() {
		return this.path;
	}

	@Override
	public int getTravelID() {
		return this.travelId;
	}

}
