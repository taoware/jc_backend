package com.irengine.campus.cas.extension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import com.irengine.campus.cas.extension.TestCon.miTM;

public class TestCon6 {
	public static void main(String[] args) throws Exception {
		String json="{\"username\":\"king44\",\"password\":\"123456a\"}";
		String url="https://a1.easemob.com/gxcm/jycs/users";
		String msg=sendPost(json, url);
		System.out.println(msg);
	}
	
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
	
	static String sendPost(String jsonStr, String path)
            throws Exception {
        HostnameVerifier hv = new HostnameVerifier() { 
            public boolean verify(String urlHostName, SSLSession session) {  
                System.out.println("Warning: URL Host: " + urlHostName + " vs. "  
                                   + session.getPeerHost());  
                return true;  
            }  
        };
        byte[] data = jsonStr.getBytes();
    	trustAllHttpsCertificates();  
    	HttpsURLConnection.setDefaultHostnameVerifier(hv); 
        java.net.URL url = new java.net.URL(path);
        java.net.HttpURLConnection conn = 
        		(java.net.HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(5 * 1000);// 设置连接超时时间为5秒 
        conn.setReadTimeout(20 * 1000);// 设置读取超时时间为20秒 
        // 使用 URL 连接进行输出，则将 DoOutput标志设置为 true
        conn.setDoOutput(true);
      
        conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
        //conn.setRequestProperty("Content-Encoding","gzip");
        conn.setRequestProperty("Content-Length", String.valueOf(data.length));
        OutputStream outStream = conn.getOutputStream();// 返回写入到此连接的输出流
        outStream.write(data);
        outStream.close();//关闭流
        String msg = "";// 保存调用http服务后的响应信息
        // 如果请求响应码是200，则表示成功
        if (conn.getResponseCode() == 200) {
            // HTTP服务端返回的编码是UTF-8,故必须设置为UTF-8,保持编码统一,否则会出现中文乱码
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    (InputStream) conn.getInputStream(), "UTF-8"));
            String line="";
            while ((line = in.readLine()) != null) {
                msg += line;
            }
            in.close();
        }else{
        	msg="注册失败";
        }
        conn.disconnect();// 断开连接
        return msg;
    }
}
