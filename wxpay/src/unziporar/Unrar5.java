package unziporar;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;

public class Unrar5 {
	    //服务器需要安装winrar
	    public static final String winrarPath = "C://Program Files (x86)//WinRAR//WinRAR.exe"; 
	    public static boolean unrar(String rarFile, String target) {     
	        boolean bool = false;     
	        File f=new File(rarFile);  
	        if(!f.exists()){  
	            return false;  
	        }  
	        String cmd = winrarPath + " X " + rarFile + " "+target;    
	        try {     
	            Process proc = Runtime.getRuntime().exec(cmd);     
	            if (proc.waitFor() != 0) {     
	                if (proc.exitValue() == 0) {     
	                    bool = false;     
	                }     
	            } else {     
	                bool = true;     
	            }     
	        } catch (Exception e) {     
	            e.printStackTrace();     
	        }     
	        return bool;     
	    }

	    //解压zip格式压缩包  
	   private static void unzip(String sourceZip,String destDir) throws Exception{    
	       try{    
	           Project p = new Project();    
	           Expand e = new Expand();    
	           e.setProject(p);    
	           e.setSrc(new File(sourceZip));    
	           e.setOverwrite(false);    
	           e.setDest(new File(destDir));      
	           e.setEncoding("gbk");    
	           e.execute();    
	       }catch(Exception e){    
	           throw e;    
	       }    
	   }

	    public static void main(String[] args) {     
	        String rarFile= "D://实习生.rar";  
	        String zipFile= "D://a.zip";  
	        String rartarget= "D://abc//";
	        String ziptarget= "D://456//";
			//unzip(zipFile, ziptarget);
	        boolean b = unrar(rarFile, rartarget);        
	        System.out.println(b);      
	    }
	}