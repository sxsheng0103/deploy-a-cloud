package database;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoDBJDBC{
   public static void main( String args[] ){
      try{   
       // ���ӵ� mongodb ����
         MongoClient mongoClient = new MongoClient( "127.0.0.1" , 27017 );
       
         // ���ӵ����ݿ�
         MongoDatabase mongoDatabase = mongoClient.getDatabase("test");  
         System.out.println("Connect to database successfully");
        
      }catch(Exception e){
        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
     }
   }
}