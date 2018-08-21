package unziporar;

import java.io.File;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;

public class unrar07 {
	public static void main(String[] args) throws Exception{

		unrar07 u=new unrar07();

	      u.unrar("D:\\abc.rar","D:\\abc\\");

	   }

	  

	   public  void unrar(String srcRarPath,String dstDirectoryPath)throws Exception { 

	        if (!srcRarPath.toLowerCase().endsWith(".rar")) {

	             System.out.println("��rar�ļ���");

	              return;

	          }

	          File dstDiretory = new File(dstDirectoryPath);

	          if (!dstDiretory.exists()) {// Ŀ��Ŀ¼������ʱ���������ļ���

	              dstDiretory.mkdirs();

	          }

	          File fol=null,out=null;

	          Archive a = null;

	          try {

	              a = new Archive(new File(srcRarPath));

	              if (a != null){

	                  a.getMainHeader().print(); // ��ӡ�ļ���Ϣ.

	                  FileHeader fh = a.nextFileHeader();

	                  while (fh != null){

	                      if (fh.isDirectory()) { // �ļ���

	                      // ���������·��������getFileNameW()�������������getFileNameString()������������ʹ��if(fh.isUnicode())

	                      if(existZH(fh.getFileNameW())){

	                         fol = new File(dstDirectoryPath + File.separator

	                                      + fh.getFileNameW());

	                      }else{

	                          fol = new File(dstDirectoryPath + File.separator

	                                      + fh.getFileNameString());

	                      }

	                       

	                          fol.mkdirs();

	                      } else { // �ļ�

	                      if(existZH(fh.getFileNameW())){

	                           out = new File(dstDirectoryPath + File.separator

	                                      + fh.getFileNameW().trim());

	                      }else{

	                          out = new File(dstDirectoryPath + File.separator

	                                      + fh.getFileNameString().trim());

	                      }

	                       

	                          //System.out.println(out.getAbsolutePath());

	                          try {// ֮������ôдtry������Ϊ��һ�����������쳣����Ӱ�������ѹ.

	                              if (!out.exists()) {

	                                  if (!out.getParentFile().exists()){// ���·�����ܶ༶��������Ҫ������Ŀ¼.

	                                      out.getParentFile().mkdirs();

	                                  }

	                                  out.createNewFile();

	                              }

	                              FileOutputStream os = new FileOutputStream(out);

	                              a.extractFile(fh, os);

	                              os.close();

	                          } catch (Exception ex) {

	                              ex.printStackTrace();

	                          }

	                      }

	                      fh = a.nextFileHeader();

	                  }

	                 a.close();

	              }

	          } catch (Exception e) {

	              e.printStackTrace();

	          }

	     }

	   

	   /*

	     * �ж��Ƿ�������

	     */

	   public static boolean existZH(String str){ 

	        String regEx = "[\\u4e00-\\u9fa5]"; 

	        Pattern p = Pattern.compile(regEx); 

	        Matcher m = p.matcher(str); 

	        while (m.find()) { 

	            return true; 

	        } 

	        return false; 

	   }

	   

	 
}