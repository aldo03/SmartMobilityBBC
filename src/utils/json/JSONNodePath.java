package utils.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import model.NodePath;
import model.interfaces.IInfrastructureNode;
import model.interfaces.INodePath;

public class JSONNodePath extends JSONArray {
	public JSONNodePath(INodePath path){
		for(IInfrastructureNode node : path.getPathNodes()){
			this.put(new JSONInfrastructureNode(node));
		}
	}
	
	public static INodePath getNodePathfromJSONArray(JSONArray array) throws JSONException{
		INodePath path;
		List<IInfrastructureNode> list = new ArrayList<>();
		for(int i = 0; i < array.length(); i++){
			IInfrastructureNode node = JSONInfrastructureNode.getInfrastructureNodeFromJSONObject(array.getJSONObject(i));
			list.add(node);
		}
		path = new NodePath(list);
		return path;
	}
	
	public static INodePath getNodePathWithCoordinatesfromJSONArray(JSONArray array) throws JSONException{
		INodePath path;
		List<IInfrastructureNode> list = new ArrayList<>();
		for(int i = 0; i < array.length(); i++){
			IInfrastructureNode node = JSONInfrastructureNode.getInfrastructureNodeWithCoordinatesFromJSONObject((array.getJSONObject(i)));
			list.add(node);
		}
		path = new NodePath(list);
		return path;
	}
}
