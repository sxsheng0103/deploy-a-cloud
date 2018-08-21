package unziporar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;

public class unrar000 {

	public static void Rar(String rarFile,String savePath) {

	//�ж��Ƿ�rar�ļ�

	if(!rarFile.endsWith(".rar")) {

	System.err.println("�򿪵��ļ�����rar�ļ���");

	return;

	}

	try {

	FileOutputStream fos = null;    

	//����һ��rar�����ļ�

	Archive rarArchive = new  Archive(new File(rarFile));

	//�ж��Ƿ��м���

	if(rarArchive != null) {

	if(rarArchive.isEncrypted()) {

	rarArchive.close();//�ر���Դ

	return;

	}

	FileHeader fileHeader = rarArchive.nextFileHeader();

	while(fileHeader != null) {

	if(!fileHeader.isDirectory()){    

	//��ѹ���ļ��н�ѹ�������ļ������п��ܴ�Ŀ¼���ļ���

	               String name = fileHeader.getFileNameString().trim();    

	                       String outPutFileName = savePath +"\\"+name; 

	                       

	                       //�����ļ�����Ϊ�˴���Ŀ¼

	                   File dir = new File(outPutFileName.substring(0, outPutFileName.lastIndexOf("\\")));  

	                   //�����ļ���    

	                   if(!dir.exists()||!dir.isDirectory()){    

	                      dir.mkdirs();  

	                   }    

	                   

	                   fos = new FileOutputStream(new File(outPutFileName));    

	                   //�����ѹ���ļ�

	                   rarArchive.extractFile(fileHeader, fos);   

	                   //�ر���Դ

	                   fos.close();    

	                   fos = null;    

	               }    

	fileHeader = rarArchive.nextFileHeader();  

	}

	}

	rarArchive.close();//�ر���Դ

	} catch (RarException e) {

	e.printStackTrace();

	} catch (IOException e) {

	e.printStackTrace();

	}   

	}


	public static void main(String[] args) {

	Rar("d:\\abc.rar", "d:\\Users\\Administrator\\Desktop\\rar");

	}
}