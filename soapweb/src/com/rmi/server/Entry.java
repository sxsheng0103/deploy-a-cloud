package com.rmi.server;


import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import com.rmi.server.imp.UserManagerImp;
/**
 * 定义server端的主程序入口
 * @author sheng
 *
 */
public class Entry {
    public static void main(String []args) throws AlreadyBoundException, RemoteException{
        UserManagerImp userManager=new UserManagerImp();
        UserManagerInterface userManagerI=(UserManagerInterface)UnicastRemoteObject.exportObject(userManager,0);
        // Bind the remote object's stub in the registry
        Registry registry = LocateRegistry.createRegistry(2002);
       
        registry.rebind("userManager", userManagerI);
        System.out.println("server is ready");
        }
}