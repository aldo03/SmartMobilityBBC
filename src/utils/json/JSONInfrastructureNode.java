package utils.json;

import org.json.JSONException;
import org.json.JSONObject;

import model.interfaces.IInfrastructureNode;

public class JSONInfrastructureNode extends JSONObject {
	private static final String COORDINATES = "coordinates";
	private static final String ID = "id";
	public JSONInfrastructureNode(IInfrastructureNode node){
		try {
			this.put(ID, node.getNodeID());
			this.put(COORDINATES, new JSONCoordinates(node.getCoordinates()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public static IInfrastructureNode getInfrastructureNodeFromJSONObject(JSONObject obj){
		IInfrastructureNode node = null;
		return node;
	}
}
