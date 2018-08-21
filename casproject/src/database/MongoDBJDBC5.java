package database;

import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
/**
 *  updateMany() ���������¼����е��ĵ�
 * @author sheng
 *
 */
public class MongoDBJDBC5{
   public static void main( String args[] ){
      try{   
         // ���ӵ� mongodb ����
         MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
         
         // ���ӵ����ݿ�
         MongoDatabase mongoDatabase = mongoClient.getDatabase("mongodb");  
         System.out.println("Connect to database successfully");
         
         MongoCollection<Document> collection = mongoDatabase.getCollection("test");
         System.out.println("���� test ѡ��ɹ�");
         
         //�����ĵ�   ���ĵ���likes=100���ĵ��޸�Ϊlikes=200   
         collection.updateMany(Filters.eq("likes", 100), new Document("$set",new Document("likes",200)));  
         //�����鿴���  
         FindIterable<Document> findIterable = collection.find();  
         MongoCursor<Document> mongoCursor = findIterable.iterator();  
         while(mongoCursor.hasNext()){  
            System.out.println(mongoCursor.next());  
         }  
      
      }catch(Exception e){
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      }
   }
}