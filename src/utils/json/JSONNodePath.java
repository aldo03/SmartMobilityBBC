package utils.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import model.interfaces.IInfrastructureNode;
import model.interfaces.INodePath;

public class JSONNodePath extends JSONArray {
	public JSONNodePath(INodePath path){
		for(IInfrastructureNode node : path.getPathNodes()){
			this.put(new JSONInfrastructureNode(node));
		}
	}
	
	public static INodePath getNodePathfromJSONArray(JSONArray array){
		INodePath path = null;
		List<IInfrastructureNode> list = new ArrayList<>();
		for(int i = 0; i < array.length(); i++){
			IInfrastructureNode node = null;
			list.add(node);
		}
		return path;
	}
}
