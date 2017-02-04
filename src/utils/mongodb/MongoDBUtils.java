package utils.mongodb;

import java.util.List;

import org.bson.BSONObject;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

public class MongoDBUtils {
	private static final String DB_TIME_TRAVELS = "timetravelsdb";
	private static final String DB_EXPECTED_VEHICLES = "expectedvehiclesdb";
	private static final String DB_CURRENT_TIMES = "currenttimesdb";
	
	public static void initCurrentTime(String nodeId){
		MongoClient mongoClient = new MongoClient( "localhost" );
		MongoDatabase db = mongoClient.getDatabase(DB_CURRENT_TIMES);
		db.getCollection(nodeId).dropIndexes();
		db.getCollection(nodeId).drop();
		mongoClient.close();
	}
	
	public static void initTimes(String nodeId){
		MongoClient mongoClient = new MongoClient( "localhost" );
		MongoDatabase db = mongoClient.getDatabase(DB_TIME_TRAVELS);
		db.getCollection(nodeId).dropIndexes();
		db.getCollection(nodeId).drop();
		mongoClient.close();
	}
	
	public static void initExpectedVehicles(String nodeId) {
		MongoClient mongoClient = new MongoClient( "localhost" );
		MongoDatabase db = mongoClient.getDatabase(DB_EXPECTED_VEHICLES);
		db.getCollection(nodeId).dropIndexes();
		db.getCollection(nodeId).drop();
		mongoClient.close();
	}
	
	public static void initTravelTimes(String nodeId, String nodeId2, List<Integer> list){
		MongoClient mongoClient = new MongoClient( "localhost" );
		MongoDatabase db = mongoClient.getDatabase(DB_TIME_TRAVELS);
		MongoCollection<Document> collection = db.getCollection(nodeId);
		Document doc = new Document("_id", nodeId2)
                .append("times", list);
		collection.insertOne(doc);
		mongoClient.close();
	}

	public static void setTravelTime(String nodeId, String nodeId2, int index, int travelTime){
		MongoClient mongoClient = new MongoClient( "localhost" );
		MongoDatabase db = mongoClient.getDatabase(DB_TIME_TRAVELS);
		MongoCollection<Document> collection = db.getCollection(nodeId);
		BsonDocument updateQuery  = new BsonDocument().append("_id", new BsonString(nodeId2));
		BsonDocument updateCommand = new BsonDocument("$set", new BsonDocument("times."+index, new BsonInt32(travelTime)));
		collection.updateOne(updateQuery, updateCommand);
		mongoClient.close();
	}
	
	
	
	
}
