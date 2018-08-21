package com.springcloud.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class RibbonController {

        @Autowired
        RestTemplate restTemplate;

        @GetMapping("/ribbon")
        public String nohy(@RequestParam String name) {
            return restTemplate.getForObject("http://SERVICE-HI/hi?name=" + name, String.class);
        }

    
        @Autowired
        HelloService ser;

        @GetMapping("/ribbonhy")
        public String hy(@RequestParam String name) {
            return ser.hiService(name);
        }
}