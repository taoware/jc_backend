package com.irengine.campus.cas.extension;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.commons.io.IOUtils;

import com.irengine.campus.cas.extension.TestCon.miTM;

public class TestCon4 {
    private static void trustAllHttpsCertificates() throws Exception {  
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];  
        javax.net.ssl.TrustManager tm = new miTM();  
        trustAllCerts[0] = tm;  
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext  
                .getInstance("SSL");  
        sc.init(null, trustAllCerts, null);  
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc  
                .getSocketFactory());  
    }  

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String URL = "https://a1.easemob.com/gxcm/jycs/users";
        String Json = findXmlInfo();

        Json = "username=jsdlfsad&password=123456a";
        String postResult =  doHttpPost(Json,URL);
       System.out.println("postResult:"+postResult);      

    }

    private static String findXmlInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    public static String doHttpPost(String xmlInfo,String URL){        
         System.out.println("发起的数据:"+xmlInfo);      
        byte[] xmlData = xmlInfo.getBytes();           
         InputStream instr  = null;
         java.io.ByteArrayOutputStream out = null;             
         HostnameVerifier hv = new HostnameVerifier() { 
             public boolean verify(String urlHostName, SSLSession session) {  
                 System.out.println("Warning: URL Host: " + urlHostName + " vs. "  
                                    + session.getPeerHost());  
                 return true;  
             }  
         };  
          try{                         
                 URL url = new URL(URL);               
             	trustAllHttpsCertificates();  
            	HttpsURLConnection.setDefaultHostnameVerifier(hv); 
                 URLConnection urlCon = url.openConnection();              
                 urlCon.setDoOutput(true);             
                 urlCon.setDoInput(true);              
                 urlCon.setUseCaches(false);                           
                 urlCon.setRequestProperty("Content-Type", "text/xml");      
                 urlCon.setRequestProperty("Content-length",String.valueOf(xmlData.length));
                 System.out.println(String.valueOf(xmlData.length));
                 DataOutputStream printout = new DataOutputStream(urlCon.getOutputStream());     
                 printout.write(xmlData);              
                 printout.flush();             
                 printout.close();             
                 instr = urlCon.getInputStream();  
                 byte[] bis = IOUtils.toByteArray(instr);
                 String ResponseString = new String(bis, "UTF-8"); 
                if ((ResponseString == null) || ("".equals(ResponseString.trim()))) {
                     System.out.println("返回空");
                    }
               System.out.println("返回数据为:" + ResponseString);
              return ResponseString;   

          }catch(Exception e){             
                 e.printStackTrace();
                return "0";
          }            
          finally {            
                 try {         
                        out.close();  
                        instr.close(); 

                 }catch (Exception ex) {     
                        return "0";
                     }                 
                 }                 
          }   
}