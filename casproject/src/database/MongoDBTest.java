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
 * dbCollection进行操作
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
	        // 创建一个MongoDB的数据库连接对象，无参数的话它默认连接到当前机器的localhost地址，端口是27017。
//	        mongo = new Mongo("192.168.225.101", 27017);
	        // 得到一个test的数据库，如果mongoDB中没有这个数据库，当向此库中添加数据的时候会自动创建
//	        db = mongo.getDB("test");
//	        db.authenticate("test", "test".toCharArray());
	        // 获取到一个叫做"user"的集合，相当于关系型数据库中的"表"
	        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-mvc-servlet.xml");
	        mongoTemplate = (MongoTemplate) context.getBean("mongoTemplate");
	        userCollection = mongoTemplate.getCollection("user");
	    }

	    /**
	     * 查询所有的集合名称
	     */
	    public void testGetAllCollections() {
	        Set<String> collectionNames = db.getCollectionNames();
	        for (String name : collectionNames) {
	            System.out.println("collectionName:" + name);
	        }
	    }

	    /**
	     * 查询所有的用户信息
	     */
	    @Test
	    public void testFind() {
	        testInitTestData();
	        // find方法查询所有的数据并返回一个游标对象
	        DBCursor cursor = userCollection.find();

	        while (cursor.hasNext()) {
	            print(cursor.next());
	        }
	        // 获取数据总条数
	        int sum = cursor.count();
	        System.out.println("sum===" + sum);
	    }

	    /**
	     * 查询第一条数据
	     */
	    public void testFindOne() {
	        testInitTestData();
	        // 只查询第一条数据
	        DBObject oneUser = userCollection.findOne();
	        print(oneUser);
	    }

	    /**
	     * 条件查询
	     */
	    public void testConditionQuery() {
	        testInitTestData();
	        // 查询id=50a1ed9965f413fa025166db
	        DBObject oneUser = userCollection.findOne(new BasicDBObject("_id", new ObjectId("50a1ed9965f413fa025166db")));
	        print(oneUser);

	        // 查询age=24
	        List<DBObject> userList1 = userCollection.find(new BasicDBObject("age", 24)).toArray();
	        print("        find age=24: ");
	        printList(userList1);

	        // 查询age>=23
	        List<DBObject> userList2 = userCollection.find(new BasicDBObject("age", new BasicDBObject("$gte", 23))).toArray();
	        print("        find age>=23: ");
	        printList(userList2);

	        // 查询age<=20
	        List<DBObject> userList3 = userCollection.find(new BasicDBObject("age", new BasicDBObject("$lte", 20))).toArray();
	        print("        find age<=20: ");
	        printList(userList3);

	        // 查询age!=25
	        List<DBObject> userList4 = userCollection.find(new BasicDBObject("age", new BasicDBObject("$ne", 25))).toArray();
	        print("        find age!=25: ");
	        printList(userList4);

	        // 查询age in[23,24,27]
	        List<DBObject> userList5 = userCollection
	                .find(new BasicDBObject("age", new BasicDBObject(QueryOperators.IN, new int[] { 23, 24, 27 })))
	                .toArray();
	        print("        find agein[23,24,27]: ");
	        printList(userList5);

	        // 查询age not in[23,24,27]
	        List<DBObject> userList6 = userCollection
	                .find(new BasicDBObject("age", new BasicDBObject(QueryOperators.NIN, new int[] { 23, 24, 27 })))
	                .toArray();
	        print("        find age not in[23,24,27]: ");
	        printList(userList6);

	        // 查询29>age>=20
	        List<DBObject> userList7 = userCollection.find(new BasicDBObject("age", new BasicDBObject("$gte", 20).append("$lt", 29)))
	                .toArray();
	        print("        find 29>age>=20: ");
	        printList(userList7);

	        // 查询age>24 and name="zhangguochen"
	        BasicDBObject query = new BasicDBObject();
	        query.put("age", new BasicDBObject("$gt", 24));
	        query.put("name", "zhangguochen");
	        List<DBObject> userList8 = userCollection.find(query).toArray();
	        print("        find age>24 and name='zhangguochen':");
	        printList(userList8);

	        // 和上面的查询一样,用的是QueryBuilder对象
	        QueryBuilder queryBuilder = new QueryBuilder();
	        queryBuilder.and("age").greaterThan(24);
	        queryBuilder.and("name").equals("zhangguochen");
	        List<DBObject> userList82 = userCollection.find(queryBuilder.get()).toArray();
	        print("        QueryBuilder find age>24 and name='zhangguochen':");
	        printList(userList82);

	        // 查询所有的用户，并按照年龄升序排列
	        List<DBObject> userList9 = userCollection.find().sort(new BasicDBObject("age", 1)).toArray();
	        print("        find all sort age asc: ");
	        printList(userList9);

	        // 查询特定字段
	        DBObject query1 = new BasicDBObject();// 要查的条件
	        query.put("age", new BasicDBObject("$gt", 20));
	        DBObject field = new BasicDBObject();// 要查的哪些字段
	        field.put("name", true);
	        field.put("age", true);
	        List<DBObject> userList10 = userCollection.find(query1, field).toArray();
	        print("        select name,age where age>20");
	        printList(userList10);

	        // 查询部分数据
	        DBObject query2 = new BasicDBObject();// 查询条件
	        query2.put("age", new BasicDBObject("$lt", 27));
	        DBObject fields = new BasicDBObject();// 查询字段
	        fields.put("name", true);
	        fields.put("age", true);
	        List<DBObject> userList11 = userCollection.find(query2, fields, 1, 1).toArray();
	        print("        select age,name from user skip 1 limit 1:");
	        printList(userList11);

	        // 模糊查询
	        DBObject fuzzy_query = new BasicDBObject();
	        String keyWord = "zhang";
	        Pattern pattern = Pattern.compile("^" + keyWord + ".*$", Pattern.CASE_INSENSITIVE);
	        fuzzy_query.put("name", pattern);
	        // 根据name like zhang%查询
	        List<DBObject> userList12 = userCollection.find(fuzzy_query).toArray();
	        print("        select * from user where name like 'zhang*'");
	        printList(userList12);

	    }

	    /**
	     * 删除用户数据
	     */
	    public void testRemoveUser() {
	        testInitTestData();
	        DBObject query = new BasicDBObject();
	        // 删除age>24的数据
	        query.put("age", new BasicDBObject("$gt", 24));
	        userCollection.remove(query);
	        printList(userCollection.find().toArray());
	    }

	    /**
	     * 修改用户数据
	     */
	    public void testUpdateUser() {

	        // update(query,set,false,true);
	        // query:需要修改的数据查询条件,相当于关系型数据库where后的语句
	        // set:需要设的值,相当于关系型数据库的set语句
	        // false:需要修改的数据如果不存在,是否插入新数据,false不插入,true插入
	        // true:如果查询出多条则不进行修改,false:只修改第一条

	        testInitTestData();

	        // 整体更新
	        DBObject query = new BasicDBObject();
	        query.put("age", new BasicDBObject("$gt", 15));
	        DBObject set = userCollection.findOne(query);// 一定是查询出来的DBObject,否则会丢掉一些列,整体更新
	        set.put("name", "Abc");
	        set.put("age", 19);
	        set.put("interest", new String[] { "hadoop", "study", "mongodb" });
	        DBObject zhangguochenAddress = new BasicDBObject();
	        zhangguochenAddress.put("address", "henan");
	        set.put("home", zhangguochenAddress);
	        userCollection.update(query, // 需要修改的数据条件
	                set, // 需要赋的值
	                false, // 数据如果不存在,是否新建
	                false);// false只修改第一条,true如果有多条就不修改
	        printList(userCollection.find().toArray());

	        // 局部更新,只更改某些列
	        // 加上$set会是局部更新,不会丢掉某些列,只把name更新为"jindazhong",年龄更新为123
	        BasicDBObject set1 = new BasicDBObject("$set", new BasicDBObject("name", "jindazhong").append("age", 123));
	        userCollection.update(query, // 需要修改的数据条件
	                set1, // 需要赋的值
	                false, // 数据如果不存在,是否新建
	                false);// false只修改第一条,true如果有多条就不修改
	        printList(userCollection.find().toArray());

	        // 批量更新
	        // user.updateMulti(new BasicDBObject("age",new
	        // BasicDBObject("$gt",16)),
	        // new BasicDBObject("$set", new
	        // BasicDBObject("name","jindazhong").append("age", 123)));
	        // printList(user.find().toArray());

	    }

	    /**
	     * 初始化测试数据
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
	        jindazhong.put("wife", "小龙女");
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
	        cuichongfei.put("wife", "凤姐");
	        DBObject cuichongfeiAddress = new BasicDBObject();
	        cuichongfeiAddress.put("address", "shanxi");
	        cuichongfei.put("home", cuichongfeiAddress);

	        DBObject huanghu = new BasicDBObject();
	        huanghu.put("name", "huanghu");
	        huanghu.put("age", 25);
	        huanghu.put("interest", new String[] { "shopping", "study" });
	        huanghu.put("wife", "黄蓉");
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
	        wangjuntao.put("wife", "王语嫣");
	        DBObject wangjuntaoAddress = new BasicDBObject();
	        wangjuntaoAddress.put("address", "hebei");
	        wangjuntao.put("home", wangjuntaoAddress);

	        DBObject miaojiagui = new BasicDBObject();
	        miaojiagui.put("name", "miaojiagui");
	        miaojiagui.put("age", 28);
	        miaojiagui.put("interest", new String[] { "hadoop", "study", "linux" });
	        miaojiagui.put("wife", null);
	        DBObject miaojiaguiAddress = new BasicDBObject();
	        miaojiaguiAddress.put("address", "未知");
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
	     * 打印数据
	     * 
	     * @param object
	     */
	    public void print(Object object) {
	        System.out.println(object);
	    }

	    /**
	     * 打印列表
	     * 
	     * @param objectList
	     */
	    public void printList(List<DBObject> objectList) {
	        for (Object object : objectList) {
	            print(object);
	        }
	    }
	}