package cn.itcast.mybatis;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.itcast.mybatis.domain.UserPo;
import cn.itcast.mybatis.service.IUserService;

public class TestSpringMybatis {

    @Test
    public void test(){
    		ApplicationContext ac = new ClassPathXmlApplicationContext("spring-beans.xml");
            IUserService us = (IUserService) ac.getBean("userService");
            List<UserPo> users = us.list();
            System.out.println(users.size());
    }
    
    @Test
    public void testDelete(){
        ApplicationContext ac = new ClassPathXmlApplicationContext("spring-beans.xml");
        IUserService us = (IUserService) ac.getBean("userService");
        try{
        	
        	us.deleteById("1");
        }catch(Exception e) {
        	e.printStackTrace();
        }
    }
    
}