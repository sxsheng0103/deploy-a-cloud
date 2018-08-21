package unziporar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;

public class unrar1 {
	
	
	public static void main(String[] args) {
		try {
			Rar("D:/abc.rar","F:\\");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**

	* @param rarFile rar文件路径

	* @param savePath 要解压的路径

	*/

	public static void Rar(String rarFile,String savePath) {

	//判断是否rar文件

	if(!rarFile.endsWith(".rar")) {

	System.err.println("打开的文件不是rar文件！");

	return;

	}

	try {

	FileOutputStream fos = null;    
	Archive rarArchive = null;
	//创建一个rar档案文件
		rarArchive = new  Archive(new File(rarFile));
		

	//判断是否有加密

	if(rarArchive != null) {

		if(rarArchive.isEncrypted()) {
	
			rarArchive.close();//关闭资源
	
		return;

	}

	FileHeader fileHeader = rarArchive.nextFileHeader();

	while(fileHeader != null) {

	if(!fileHeader.isDirectory()){    

	//从压缩文件中解压出来的文件名，有可能带目录的文件名

	               String name = fileHeader.getFileNameString().trim();    

	                       String outPutFileName = savePath +"\\"+name; 

	                       

	                       //分离文件名，为了创建目录

	                   File dir = new File(outPutFileName.substring(0, outPutFileName.lastIndexOf("\\")));  

	                   //创建文件夹    

	                   if(!dir.exists()||!dir.isDirectory()){    

	                      dir.mkdirs();  

	                   }    

	                   

	                   fos = new FileOutputStream(new File(outPutFileName));    

	                   //保存解压的文件

	                   rarArchive.extractFile(fileHeader, fos);   

	                   //关闭资源

	                   fos.close();    

	                   fos = null;    

	               }    

	fileHeader = rarArchive.nextFileHeader();  

	}

	}

	rarArchive.close();//关闭资源

	} catch (RarException e) {

	e.printStackTrace();

	} catch (IOException e) {

	e.printStackTrace();

	}   

	}
}
