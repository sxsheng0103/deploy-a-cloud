package com.springcloud.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class SericeFeign {
	/**
	 * 1.启动springcloud - >EurekaServerApplication
	 * 2.启动eurekaclient - >EurekaServerApplication((启动service-hi 两次，端口分别为8762 、8763.))
	 * 3.启动servce-feign ->sericeFeign
	 * 4http://localhost:8765/hi?name=forezp(http://localhost:8765/hi?name=forezp)
	 * 5.浏览器交替显示：hi forezp,i am from port:8762/hi forezp,i am from port:8763
	 * @param args
	 */
    public static void main(String[] args) {
        SpringApplication.run(SericeFeign.class, args);
    }

}