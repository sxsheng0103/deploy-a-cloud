package com.cache.cache2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class testCache {
public static void main(String[] args) {
        
        
        
        //Demo1
        Cache c1 = new Cache();
        c1.setKey("China");
        c1.setValue("�л����񹲺͹�");

        Cache c2 = new Cache();
        c2.setKey("American");
        c2.setValue("��������ڹ�");
        
        CacheManager.putCache("one", c1);
        CacheManager.putCache("two", c2);
        System.out.println("�����С��"+CacheManager.getCacheSize());
        System.out.println("keyΪone�Ļ������Value��"+CacheManager.getCacheInfo("one").getValue());
        
        
        System.out.println("-----------------------------------");
        
        
        //Demo2
        Cache c3 = new Cache();
        c3.setKey("С�ͳ�Ʒ���б�");
        
        List<String> list1 = new ArrayList<String>();
        list1.add("����");
        list1.add("����");
        list1.add("�µ�");
        list1.add("�ִ�");
        
        c3.setValue(list1);
        
        CacheManager.putCache("car", c3);
        List<String> list2 = new ArrayList<String>();
        Object obCar = CacheManager.getCacheInfo("car").getValue();
        if(obCar instanceof List){
            list2 = (List<String>) obCar;
        }
        for(String brand : list2){
            System.out.println("KeyΪcar��Ʒ�ƣ�"+brand);
        }
        
        
        System.out.println("-----------------------------------");
        
        
        //Demo3
        Cache c4 = new Cache();
        c4.setKey("����-�Զ�1.8T");
        
        Cache c5 = new Cache();
        c5.setKey("�ʾ�-˫���2.0T");
        
        Cache c6 = new Cache();
        c6.setKey("����-�Զ�1.8T");
        
        Cache c7 = new Cache();
        c7.setKey("����-˫���2.0T");
        
        Cache c8 = new Cache();
        c8.setKey("����-�Զ�1.8T");

        Cache c9 = new Cache();
        c9.setKey("����-˫���2.0T");

        Cache c10 = new Cache();
        c10.setKey("����-˫���2.0T");
        Map<String,String> map1 = new HashMap<String,String>();
        map1.put("ȫ���촰", "��");
        map1.put("����������", "2.0L");
        map1.put("�ŷű�׼", "��5");
        c10.setValue(map1);

        Cache c11 = new Cache();
        c11.setKey("Ӣ�����-1.8T���о�Ӣ��");
        
        CacheManager.putCache("����", c4);
        CacheManager.putCache("�ʾ�", c5);
        CacheManager.putCache("����", c6);
        CacheManager.putCache("����", c7);
        CacheManager.putCache("����", c8);
        CacheManager.putCache("����", c9);
        CacheManager.putCache("����", c10);
        
        //�õ���ϵ����ĳ���Ʒ
        Object carObj2 = CacheManager.getCacheListLikekey("��");
        List<String> list3 = new ArrayList<String>();
        if(carObj2 instanceof List){
            list3 = (List<String>) carObj2;
        }
        for(String product : list3){
            System.out.println("��ϵ���壺"+product);
        }

        System.out.println("-----------------------------------");
        
        //�õ�X��ϵ�еĳ���Ʒ
        Object carObj3 = CacheManager.getCacheListLikekey("��");
        List<String> list4 = new ArrayList<String>();
        if(carObj2 instanceof List){
            list4 = (List<String>) carObj3;
        }
        for(String product : list4){
            System.out.println("��ϵ���壺"+product);
        }
        
        System.out.println("-----------------------------------");
        System.out.println("��ʱ�Ļ����С��"+CacheManager.getCacheSize());
        
    }
}
