package database;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
/**
 * createCollection()����������
 * @author sheng
 *
 */
public class MongoDBJDBC1{
   public static void main( String args[] ){
      try{   
      // ���ӵ� mongodb ����
      MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
         
       
      // ���ӵ����ݿ�
      MongoDatabase mongoDatabase = mongoClient.getDatabase("mongodb");  
      System.out.println("Connect to database successfully");
      mongoDatabase.createCollection("test");
      System.out.println("���ϴ����ɹ�");
        
      }catch(Exception e){
        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
     }
   }
}