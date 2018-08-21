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
     * �����ѶQQ����״̬
     *
     * ���������QQ���� String��Ĭ��QQ���룺8698053���������ݣ�String��Y = ���ߣ�N = ���ߣ�E = QQ�������A = ��ҵ�û���֤ʧ�ܣ�V = ����û���������
     * @throws Exception
     */
    @Test
    public void sendSms() throws Exception {
        String qqCode = "416501600";//qq����
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
        //��ӡ���ؽ��
        System.out.println("result:" + result);
    }

    /**
     * �ļ������滻
     * 
     * @param inFileName Դ�ļ�
     * @param from
     * @param to
     * @return �����滻���ļ�
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
        //infile.delete(); //ɾ��Դ�ļ�
        //outfile.renameTo(infile); //����ʱ�ļ�������
        return outFile;
    }
    
    /**
     * ���������ж�ȡ����
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
        byte[] data = outStream.toByteArray();//��ҳ�Ķ���������
        outStream.close();
        inStream.close();
        return data;
    }

    
    /**
      * �����������һ��SOAPMessage�����������ݴ�ӡ���ļ���
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
      * ʹ��SOAP��ʾ�������ļ�����һ��SOAP message�����͸�web service
     * @return
     * @throws Exception
     */
    public SOAPMessage sendSOAPMessage() throws Exception {
        //�ù�������һ��SOAPMessage
        MessageFactory mf = MessageFactory.newInstance()  ;
        SOAPMessage loginSOAPRequest = mf.createMessage();

        //�����SOAPMessge������Ϣ����Դ����d:\\testSOAP.txt�ļ��������Ǳ�׼��SOAP
        SOAPPart sp = loginSOAPRequest.getSOAPPart();
        StreamSource prepMsg = new StreamSource(
                    new FileInputStream("d:\\testSOAP.txt"));
                    sp.setContent(prepMsg);
          
        final String SOAPACTION = "http://ASAP.services.tfn.thomson.com/2010-03-01/Login";
        //��SOAPAction���ӵ�MimeHeaders��ȥ
        MimeHeaders hd = loginSOAPRequest.getMimeHeaders();
        hd.setHeader("SOAPAction", SOAPACTION);

        //������ĸ��ı���
        loginSOAPRequest.saveChanges();
        //����һ��SOAPConnection�����巽����ǰ���SOAPConnection���Ѿ�˵��          
//      SOAPConnection con = getSoapConnection();
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection con = soapConnectionFactory.createConnection();
        //����SOAP request ������Ŀ���ַ������web services��tornado��
        SOAPMessage reply = con.call(loginSOAPRequest, new URL("http://172.28.85.6/ASAPService/ASAPService_V1.svc"));
        con.close();
        //����response
        return reply;        
    }
}