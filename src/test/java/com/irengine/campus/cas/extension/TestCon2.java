package com.irengine.campus.cas.extension;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.junit.Test;

import com.irengine.campus.cas.extension.TestCon.miTM;
/**失败:500*/
public class TestCon2 {
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
	@Test
	public void readContentFromPost() throws Exception{ 
        HostnameVerifier hv = new HostnameVerifier() { 
            public boolean verify(String urlHostName, SSLSession session) {  
                System.out.println("Warning: URL Host: " + urlHostName + " vs. "  
                                   + session.getPeerHost());  
                return true;  
            }  
        };  
        // Post请求的url，与get不同的是不需要带参数 
		String post_url="https://a1.easemob.com/gxcm/jycs/users";
        URL postUrl = new URL(post_url);
    	trustAllHttpsCertificates();  
    	HttpsURLConnection.setDefaultHostnameVerifier(hv); 
        // 打开连接 
        HttpURLConnection connection = (HttpURLConnection) postUrl 
                .openConnection(); 
        // Output to the connection. Default is 
        // false, set to true because post 
        // method must write something to the 
        // connection 
        // 设置是否向connection输出，因为这个是post请求，参数要放在 
        // http正文内，因此需要设为true 
        connection.setDoOutput(true); 
        // Read from the connection. Default is true. 
        connection.setDoInput(true); 
        // Set the post method. Default is GET 
        connection.setRequestMethod("POST"); 
        // Post cannot use caches 
        // Post 请求不能使用缓存 
        connection.setUseCaches(false); 
        // This method takes effects to 
        // every instances of this class. 
        // URLConnection.setFollowRedirects是static函数，作用于所有的URLConnection对象。 
        // connection.setFollowRedirects(true); 

        // This methods only 
        // takes effacts to this 
        // instance. 
        // URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数 
        connection.setInstanceFollowRedirects(true); 
        // Set the content type to urlencoded, 
        // because we will write 
        // some URL-encoded content to the 
        // connection. Settings above must be set before connect! 
        // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的 
        // 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode 
        // 进行编码 
        connection.setRequestProperty("Content-Type", 
                "application/x-www-form-urlencoded"); 
        // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成， 
        // 要注意的是connection.getOutputStream会隐含的进行connect。 
        connection.connect(); 
        DataOutputStream out = new DataOutputStream(connection 
                .getOutputStream()); 
        // The URL-encoded contend 
        // 正文，正文内容其实跟get的URL中'?'后的参数字符串一致 
        String content = "username=tiking1111&password=123456a"; 
        // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面 
        out.writeBytes(content); 
        out.flush(); 
        out.close(); // flush and close 
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));//设置编码,否则中文乱码 
        String line=""; 
        System.out.println("============================="); 
        System.out.println("Contents of post request"); 
        System.out.println("============================="); 
        while ((line = reader.readLine()) != null){ 
            //line = new String(line.getBytes(), "utf-8"); 
            System.out.println(line); 
        } 
        System.out.println("============================="); 
        System.out.println("Contents of post request ends"); 
        System.out.println("============================="); 
        reader.close(); 
        connection.disconnect(); 
    } 
} 

