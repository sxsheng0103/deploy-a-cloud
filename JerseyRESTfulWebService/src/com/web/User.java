package com.web;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {
 
    private int id;
 
    private String username;
 
    public User() {
    }
 
    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }
 
    // setter getter //
}
