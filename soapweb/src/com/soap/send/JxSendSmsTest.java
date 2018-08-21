package com.soap.send;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;

public class JxSendSmsTest {

    /**
     * 获得腾讯QQ在线状态
     *
     * 输入参数：QQ号码 String，默认QQ号码：8698053。返回数据：String，Y = 在线；N = 离线；E = QQ号码错误；A = 商业用户验证失败；V = 免费用户超过数量
     * @throws Exception
     */
    @Test
    public void sendSms() throws Exception {
        String qqCode = "416501600";//qq号码
        String urlString = "http://www.webxml.com.cn/webservices/qqOnlineWebService.asmx";
        String xml = JxSendSmsTest.class.getClassLoader().getResource("SendInstantSms.xml").getFile();
        String xmlFile=replace(xml, "qqCodeTmp", qqCode).getPath();
        String soapActionString = "http://WebXml.com.cn/qqCheckOnline";
        URL url = new URL(urlString);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        File fileToSend = new File(xmlFile);
        byte[] buf = new byte[(int) fileToSend.length()];
        new FileInputStream(xmlFile).read(buf);
        httpConn.setRequestProperty("Content-Length", String.valueOf(buf.length));
        httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        httpConn.setRequestProperty("soapActionString", soapActionString);
        httpConn.setRequestMethod("POST");
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        OutputStream out = httpConn.getOutputStream();
        out.write(buf);
        out.close();

        byte[] datas=readInputStream(httpConn.getInputStream());
        String result=new String(datas);
        //打印返回结果
        System.out.println("result:" + result);
    }

    /**
     * 文件内容替换
     * 
     * @param inFileName 源文件
     * @param from
     * @param to
     * @return 返回替换后文件
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public static File replace(String inFileName, String from, String to)
            throws IOException, UnsupportedEncodingException {
        File inFile = new File(inFileName);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(inFile), "utf-8"));
        File outFile = new File(inFile + ".tmp");
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outFile), "utf-8")));
        String reading;
        while ((reading = in.readLine()) != null) {
            out.println(reading.replaceAll(from, to));
        }
        out.close();
        in.close();
        //infile.delete(); //删除源文件
        //outfile.renameTo(infile); //对临时文件重命名
        return outFile;
    }
    
    /**
     * 从输入流中读取数据
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len = inStream.read(buffer)) !=-1 ){
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();//网页的二进制数据
        outStream.close();
        inStream.close();
        return data;
    }

    
    /**
      * 这个方法接收一个SOAPMessage，并将其内容打印到文件中
     * @param loginSOAPRequest
     */
    private void getSoapContent(SOAPMessage loginSOAPRequest) {
        String output = "d:\\test1.txt";
        try{
        FileOutputStream tt = new FileOutputStream(output);
        loginSOAPRequest.writeTo(tt);
        tt.toString();
        }catch(Exception e){
            System.out.println();
        }
        
    }
    
    /**
      * 使用SOAP的示例，从文件读入一个SOAP message并发送给web service
     * @return
     * @throws Exception
     */
    public SOAPMessage sendSOAPMessage() throws Exception {
        //用工厂创建一个SOAPMessage
        MessageFactory mf = MessageFactory.newInstance()  ;
        SOAPMessage loginSOAPRequest = mf.createMessage();

        //给这个SOAPMessge添加信息，来源就是d:\\testSOAP.txt文件，内容是标准的SOAP
        SOAPPart sp = loginSOAPRequest.getSOAPPart();
        StreamSource prepMsg = new StreamSource(
                    new FileInputStream("d:\\testSOAP.txt"));
                    sp.setContent(prepMsg);
          
        final String SOAPACTION = "http://ASAP.services.tfn.thomson.com/2010-03-01/Login";
        //将SOAPAction添加到MimeHeaders中去
        MimeHeaders hd = loginSOAPRequest.getMimeHeaders();
        hd.setHeader("SOAPAction", SOAPACTION);

        //将上面的更改保存
        loginSOAPRequest.saveChanges();
        //创建一个SOAPConnection。具体方法在前面的SOAPConnection中已经说了          
//      SOAPConnection con = getSoapConnection();
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection con = soapConnectionFactory.createConnection();
        //发送SOAP request 。这里目标地址可以是web services或tornado。
        SOAPMessage reply = con.call(loginSOAPRequest, new URL("http://172.28.85.6/ASAPService/ASAPService_V1.svc"));
        con.close();
        //返回response
        return reply;        
    }
}