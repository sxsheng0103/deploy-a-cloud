package test.java;

import com.zero.wxpay.WXPaySender;
import org.junit.Test;
import java.util.Map;

/**
 * 模拟请求接口测试类
 *
 * @author Zero Lee
 * @date 2017-12-20
 * @time 15:57
 */
public class TestSender{

    WXPaySender hdwx = WXPaySender.getInstance();

    public TestSender() throws Exception {
    }

    // 统一下单
    @Test
    public void doUnifiedOrder(){
    	try {
    		Map result1 = hdwx.doUnifiedOrder("测试产品", "HD000000000007", "1", "110.96.88.216");
            System.out.println(result1);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    // 查询订单
    @Test
    public void doOrderQuery(){
    	try {
    		Map result1 = hdwx.doOrderQuery("HD000000000007");
            System.out.println(result1);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    // 关闭订单
    @Test
    public void doOrderClose(){
    	try {
    		Map result1 = hdwx.doOrderClose("HD000000000005");
            System.out.println(result1);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    // 退款申请
    @Test
    public void doRefund(){
    	try {
    		Map result1 = hdwx.doRefund("HD000000000007","HD000000000007","1","1");
            System.out.println(result1);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    // 退款查询
    @Test
    public void doRefundQuery(){
    	try {
    		Map result1 = hdwx.doRefundQuery("HD000000000006");
            System.out.println(result1);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }

}
