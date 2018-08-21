package com.springcloud.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/**
 * 注解@EnableEurekaClient 表明自己是一个eurekaclient.
 * @author sheng
 *
 */
@SpringBootApplication
@EnableEurekaClient
@RestController
public class ServiceHiApplication {
	/**
	 * 1.启动springcloud - >EurekaServerApplication
	 * 2.启动eurekaclient - >EurekaServerApplication
	 * 3.http://localhost:8762/hi?name=forezp
	 * 你会在浏览器上看到 :hi forezp,i am from port:8762
	 * @param args
	 */
    public static void main(String[] args) {
        SpringApplication.run(ServiceHiApplication.class, args);
    }

    @Value("${server.port}")
    String port;

    @RequestMapping("/hi")
    public String home(@RequestParam String name) {
        return "hi " + name + ",i am from port:" + port;
    }

}