package com.cy.action;

import com.opensymphony.xwork2.ActionSupport;
/**
 * 1.Struts2ֱ��ʹ��Action����װHTTP������������Action��Ӧ�ð������������Ӧ�����ԣ���Ϊ�������ṩ��Ӧ��setter��getter������
 *2.ΪAction��������һ��execute��������ΪStruts2���Ĭ�ϻ�ִ��������������������������ҵ���߼��������ǵ�������ҵ���߼��������ⲿ�ֹ�����
 *3.Action�෵��һ����׼���ַ��������ַ�����һ���߼���ͼ��������ͼ����Ӧʵ�ʵ�������ͼ��
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
 * 1.Struts2�����Action��һ��POJO��û�б�������Ⱦ��
 * 2.Struts2�е�Action��execute������������servlet API��������Struts1����Ϲ��ڽ��ܣ����󷽱��˵�Ԫ���ԡ�
 * 3.Struts2��Action������ActionForm��װ���������
 */
}