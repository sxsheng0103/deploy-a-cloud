package database;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.QueryBuilder;
import com.mongodb.QueryOperators;
/**
 * dbCollection���в���
 * @author sheng
 *
 */
	public class MongoDBTest {
	    Mongo mongo = null;
	    DB db = null;
	    DBCollection userCollection = null;

	    private MongoTemplate mongoTemplate;

	    @Before
	    public void setUp() throws Exception {
	        // ����һ��MongoDB�����ݿ����Ӷ����޲����Ļ���Ĭ�����ӵ���ǰ������localhost��ַ���˿���27017��
//	        mongo = new Mongo("192.168.225.101", 27017);
	        // �õ�һ��test�����ݿ⣬���mongoDB��û��������ݿ⣬����˿���������ݵ�ʱ����Զ�����
//	        db = mongo.getDB("test");
//	        db.authenticate("test", "test".toCharArray());
	        // ��ȡ��һ������"user"�ļ��ϣ��൱�ڹ�ϵ�����ݿ��е�"��"
	        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-mvc-servlet.xml");
	        mongoTemplate = (MongoTemplate) context.getBean("mongoTemplate");
	        userCollection = mongoTemplate.getCollection("user");
	    }

	    /**
	     * ��ѯ���еļ�������
	     */
	    public void testGetAllCollections() {
	        Set<String> collectionNames = db.getCollectionNames();
	        for (String name : collectionNames) {
	            System.out.println("collectionName:" + name);
	        }
	    }

	    /**
	     * ��ѯ���е��û���Ϣ
	     */
	    @Test
	    public void testFind() {
	        testInitTestData();
	        // find������ѯ���е����ݲ�����һ���α����
	        DBCursor cursor = userCollection.find();

	        while (cursor.hasNext()) {
	            print(cursor.next());
	        }
	        // ��ȡ����������
	        int sum = cursor.count();
	        System.out.println("sum===" + sum);
	    }

	    /**
	     * ��ѯ��һ������
	     */
	    public void testFindOne() {
	        testInitTestData();
	        // ֻ��ѯ��һ������
	        DBObject oneUser = userCollection.findOne();
	        print(oneUser);
	    }

	    /**
	     * ������ѯ
	     */
	    public void testConditionQuery() {
	        testInitTestData();
	        // ��ѯid=50a1ed9965f413fa025166db
	        DBObject oneUser = userCollection.findOne(new BasicDBObject("_id", new ObjectId("50a1ed9965f413fa025166db")));
	        print(oneUser);

	        // ��ѯage=24
	        List<DBObject> userList1 = userCollection.find(new BasicDBObject("age", 24)).toArray();
	        print("        find age=24: ");
	        printList(userList1);

	        // ��ѯage>=23
	        List<DBObject> userList2 = userCollection.find(new BasicDBObject("age", new BasicDBObject("$gte", 23))).toArray();
	        print("        find age>=23: ");
	        printList(userList2);

	        // ��ѯage<=20
	        List<DBObject> userList3 = userCollection.find(new BasicDBObject("age", new BasicDBObject("$lte", 20))).toArray();
	        print("        find age<=20: ");
	        printList(userList3);

	        // ��ѯage!=25
	        List<DBObject> userList4 = userCollection.find(new BasicDBObject("age", new BasicDBObject("$ne", 25))).toArray();
	        print("        find age!=25: ");
	        printList(userList4);

	        // ��ѯage in[23,24,27]
	        List<DBObject> userList5 = userCollection
	                .find(new BasicDBObject("age", new BasicDBObject(QueryOperators.IN, new int[] { 23, 24, 27 })))
	                .toArray();
	        print("        find agein[23,24,27]: ");
	        printList(userList5);

	        // ��ѯage not in[23,24,27]
	        List<DBObject> userList6 = userCollection
	                .find(new BasicDBObject("age", new BasicDBObject(QueryOperators.NIN, new int[] { 23, 24, 27 })))
	                .toArray();
	        print("        find age not in[23,24,27]: ");
	        printList(userList6);

	        // ��ѯ29>age>=20
	        List<DBObject> userList7 = userCollection.find(new BasicDBObject("age", new BasicDBObject("$gte", 20).append("$lt", 29)))
	                .toArray();
	        print("        find 29>age>=20: ");
	        printList(userList7);

	        // ��ѯage>24 and name="zhangguochen"
	        BasicDBObject query = new BasicDBObject();
	        query.put("age", new BasicDBObject("$gt", 24));
	        query.put("name", "zhangguochen");
	        List<DBObject> userList8 = userCollection.find(query).toArray();
	        print("        find age>24 and name='zhangguochen':");
	        printList(userList8);

	        // ������Ĳ�ѯһ��,�õ���QueryBuilder����
	        QueryBuilder queryBuilder = new QueryBuilder();
	        queryBuilder.and("age").greaterThan(24);
	        queryBuilder.and("name").equals("zhangguochen");
	        List<DBObject> userList82 = userCollection.find(queryBuilder.get()).toArray();
	        print("        QueryBuilder find age>24 and name='zhangguochen':");
	        printList(userList82);

	        // ��ѯ���е��û���������������������
	        List<DBObject> userList9 = userCollection.find().sort(new BasicDBObject("age", 1)).toArray();
	        print("        find all sort age asc: ");
	        printList(userList9);

	        // ��ѯ�ض��ֶ�
	        DBObject query1 = new BasicDBObject();// Ҫ�������
	        query.put("age", new BasicDBObject("$gt", 20));
	        DBObject field = new BasicDBObject();// Ҫ�����Щ�ֶ�
	        field.put("name", true);
	        field.put("age", true);
	        List<DBObject> userList10 = userCollection.find(query1, field).toArray();
	        print("        select name,age where age>20");
	        printList(userList10);

	        // ��ѯ��������
	        DBObject query2 = new BasicDBObject();// ��ѯ����
	        query2.put("age", new BasicDBObject("$lt", 27));
	        DBObject fields = new BasicDBObject();// ��ѯ�ֶ�
	        fields.put("name", true);
	        fields.put("age", true);
	        List<DBObject> userList11 = userCollection.find(query2, fields, 1, 1).toArray();
	        print("        select age,name from user skip 1 limit 1:");
	        printList(userList11);

	        // ģ����ѯ
	        DBObject fuzzy_query = new BasicDBObject();
	        String keyWord = "zhang";
	        Pattern pattern = Pattern.compile("^" + keyWord + ".*$", Pattern.CASE_INSENSITIVE);
	        fuzzy_query.put("name", pattern);
	        // ����name like zhang%��ѯ
	        List<DBObject> userList12 = userCollection.find(fuzzy_query).toArray();
	        print("        select * from user where name like 'zhang*'");
	        printList(userList12);

	    }

	    /**
	     * ɾ���û�����
	     */
	    public void testRemoveUser() {
	        testInitTestData();
	        DBObject query = new BasicDBObject();
	        // ɾ��age>24������
	        query.put("age", new BasicDBObject("$gt", 24));
	        userCollection.remove(query);
	        printList(userCollection.find().toArray());
	    }

	    /**
	     * �޸��û�����
	     */
	    public void testUpdateUser() {

	        // update(query,set,false,true);
	        // query:��Ҫ�޸ĵ����ݲ�ѯ����,�൱�ڹ�ϵ�����ݿ�where������
	        // set:��Ҫ���ֵ,�൱�ڹ�ϵ�����ݿ��set���
	        // false:��Ҫ�޸ĵ��������������,�Ƿ����������,false������,true����
	        // true:�����ѯ�������򲻽����޸�,false:ֻ�޸ĵ�һ��

	        testInitTestData();

	        // �������
	        DBObject query = new BasicDBObject();
	        query.put("age", new BasicDBObject("$gt", 15));
	        DBObject set = userCollection.findOne(query);// һ���ǲ�ѯ������DBObject,����ᶪ��һЩ��,�������
	        set.put("name", "Abc");
	        set.put("age", 19);
	        set.put("interest", new String[] { "hadoop", "study", "mongodb" });
	        DBObject zhangguochenAddress = new BasicDBObject();
	        zhangguochenAddress.put("address", "henan");
	        set.put("home", zhangguochenAddress);
	        userCollection.update(query, // ��Ҫ�޸ĵ���������
	                set, // ��Ҫ����ֵ
	                false, // �������������,�Ƿ��½�
	                false);// falseֻ�޸ĵ�һ��,true����ж����Ͳ��޸�
	        printList(userCollection.find().toArray());

	        // �ֲ�����,ֻ����ĳЩ��
	        // ����$set���Ǿֲ�����,���ᶪ��ĳЩ��,ֻ��name����Ϊ"jindazhong",�������Ϊ123
	        BasicDBObject set1 = new BasicDBObject("$set", new BasicDBObject("name", "jindazhong").append("age", 123));
	        userCollection.update(query, // ��Ҫ�޸ĵ���������
	                set1, // ��Ҫ����ֵ
	                false, // �������������,�Ƿ��½�
	                false);// falseֻ�޸ĵ�һ��,true����ж����Ͳ��޸�
	        printList(userCollection.find().toArray());

	        // ��������
	        // user.updateMulti(new BasicDBObject("age",new
	        // BasicDBObject("$gt",16)),
	        // new BasicDBObject("$set", new
	        // BasicDBObject("name","jindazhong").append("age", 123)));
	        // printList(user.find().toArray());

	    }

	    /**
	     * ��ʼ����������
	     */
	    public void testInitTestData() {
	        userCollection.drop();
	        DBObject zhangguochen = new BasicDBObject();
	        zhangguochen.put("name", "zhangguochen");
	        zhangguochen.put("age", 25);
	        zhangguochen.put("interest", new String[] { "hadoop", "study", "mongodb" });
	        DBObject zhangguochenAddress = new BasicDBObject();
	        zhangguochenAddress.put("address", "henan");
	        zhangguochen.put("home", zhangguochenAddress);

	        DBObject jindazhong = new BasicDBObject();
	        jindazhong.put("name", "jindazhong");
	        jindazhong.put("age", 21);
	        jindazhong.put("interest", new String[] { "hadoop", "mongodb" });
	        jindazhong.put("wife", "С��Ů");
	        DBObject jindazhongAddress = new BasicDBObject();
	        jindazhongAddress.put("address", "shanghai");
	        jindazhong.put("home", jindazhongAddress);

	        DBObject yangzhi = new BasicDBObject();
	        yangzhi.put("name", "yangzhi");
	        yangzhi.put("age", 22);
	        yangzhi.put("interest", new String[] { "shopping", "sing", "hadoop" });
	        DBObject yangzhiAddress = new BasicDBObject();
	        yangzhiAddress.put("address", "hubei");
	        yangzhi.put("home", yangzhiAddress);

	        DBObject diaoyouwei = new BasicDBObject();
	        diaoyouwei.put("name", "diaoyouwei");
	        diaoyouwei.put("age", 23);
	        diaoyouwei.put("interest", new String[] { "notejs", "sqoop" });
	        DBObject diaoyouweiAddress = new BasicDBObject();
	        diaoyouweiAddress.put("address", "shandong");
	        diaoyouwei.put("home", diaoyouweiAddress);

	        DBObject cuichongfei = new BasicDBObject();
	        cuichongfei.put("name", "cuichongfei");
	        cuichongfei.put("age", 24);
	        cuichongfei.put("interest", new String[] { "ebsdi", "dq" });
	        cuichongfei.put("wife", "���");
	        DBObject cuichongfeiAddress = new BasicDBObject();
	        cuichongfeiAddress.put("address", "shanxi");
	        cuichongfei.put("home", cuichongfeiAddress);

	        DBObject huanghu = new BasicDBObject();
	        huanghu.put("name", "huanghu");
	        huanghu.put("age", 25);
	        huanghu.put("interest", new String[] { "shopping", "study" });
	        huanghu.put("wife", "����");
	        DBObject huanghuAddress = new BasicDBObject();
	        huanghuAddress.put("address", "guangdong");
	        huanghu.put("home", huanghuAddress);

	        DBObject houchangren = new BasicDBObject();
	        houchangren.put("name", "houchangren");
	        houchangren.put("age", 26);
	        houchangren.put("interest", new String[] { "dota", "dq" });
	        DBObject houchangrenAddress = new BasicDBObject();
	        houchangrenAddress.put("address", "shandong");
	        houchangren.put("home", houchangrenAddress);

	        DBObject wangjuntao = new BasicDBObject();
	        wangjuntao.put("name", "wangjuntao");
	        wangjuntao.put("age", 27);
	        wangjuntao.put("interest", new String[] { "sport", "study" });
	        wangjuntao.put("wife", "������");
	        DBObject wangjuntaoAddress = new BasicDBObject();
	        wangjuntaoAddress.put("address", "hebei");
	        wangjuntao.put("home", wangjuntaoAddress);

	        DBObject miaojiagui = new BasicDBObject();
	        miaojiagui.put("name", "miaojiagui");
	        miaojiagui.put("age", 28);
	        miaojiagui.put("interest", new String[] { "hadoop", "study", "linux" });
	        miaojiagui.put("wife", null);
	        DBObject miaojiaguiAddress = new BasicDBObject();
	        miaojiaguiAddress.put("address", "δ֪");
	        miaojiagui.put("home", miaojiaguiAddress);

	        DBObject longzhen = new BasicDBObject();
	        longzhen.put("name", "longzhen");
	        longzhen.put("age", 29);
	        longzhen.put("interest", new String[] { "study", "cook" });
	        longzhen.put("wife", null);
	        DBObject longzhenAddress = new BasicDBObject();
	        longzhenAddress.put("address", "sichuan");
	        longzhen.put("home", longzhenAddress);

	        userCollection.insert(zhangguochen);
	        userCollection.insert(jindazhong);
	        userCollection.insert(yangzhi);
	        userCollection.insert(diaoyouwei);
	        userCollection.insert(cuichongfei);
	        userCollection.insert(huanghu);
	        userCollection.insert(houchangren);
	        userCollection.insert(wangjuntao);
	        userCollection.insert(miaojiagui);
	        userCollection.insert(longzhen);
	    }

	    public void testRemove() {
	        userCollection.drop();
	    }

	    /**
	     * ��ӡ����
	     * 
	     * @param object
	     */
	    public void print(Object object) {
	        System.out.println(object);
	    }

	    /**
	     * ��ӡ�б�
	     * 
	     * @param objectList
	     */
	    public void printList(List<DBObject> objectList) {
	        for (Object object : objectList) {
	            print(object);
	        }
	    }
	}