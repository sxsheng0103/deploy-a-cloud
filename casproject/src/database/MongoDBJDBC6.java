package database;

import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
/**
 * ɾ����һ���ĵ�
 * @author sheng
 *
 */
public class MongoDBJDBC6{
   public static void main( String args[] ){
      try{   
         // ���ӵ� mongodb ����
         MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

         // ���ӵ����ݿ�
         MongoDatabase mongoDatabase = mongoClient.getDatabase("mongodb");  
         System.out.println("Connect to database successfully");

         MongoCollection<Document> collection = mongoDatabase.getCollection("test");
         System.out.println("���� test ѡ��ɹ�");

         //ɾ�����������ĵ�һ���ĵ�  
         collection.deleteOne(Filters.eq("likes", 200));  
         //ɾ�����з����������ĵ�  
         collection.deleteMany (Filters.eq("likes", 200));  
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