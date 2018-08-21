package com.cy.action;

import com.opensymphony.xwork2.ActionSupport;
/**
 * 1.Struts2直接使用Action来封装HTTP请求参数，因此Action类应该包含与请求相对应的属性，并为该属性提供对应的setter和getter方法。
 *2.为Action类里增加一个execute方法，因为Struts2框架默认会执行这个方法。这个方法本身并不做业务逻辑处理，而是调用其他业务逻辑组件完成这部分工作。
 *3.Action类返回一个标准的字符串，该字符串是一个逻辑视图名，该视图名对应实际的物理视图。
 * @author sheng
 *
 */
public class LoginAction extends ActionSupport {

    private static final long serialVersionUID = 1L;

    private String userName;
    private String password;

    public String execute() {

        if (userName.equals("hellokitty") && password.equals("123")) {

            return SUCCESS;
        } else {
            return ERROR;
        }

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
/**
 * 1.Struts2框架中Action是一个POJO，没有被代码污染。
 * 2.Struts2中的Action的execute方法不依赖于servlet API，改善了Struts1中耦合过于紧密，极大方便了单元测试。
 * 3.Struts2的Action无须用ActionForm封装请求参数。
 */
}