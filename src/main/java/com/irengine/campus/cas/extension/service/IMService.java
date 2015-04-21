package com.irengine.campus.cas.extension.service;

import java.io.OutputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.irengine.campus.cas.extension.domain.IM;
import com.irengine.campus.cas.extension.repository.IMRepository;

@Service
@Transactional
public class IMService {

	@Autowired
	IMRepository imRepository;
	
	/**注册环信*/
	public String create(String username, String password) {
		IM im=new IM(username,password);
		String url="https://a1.easemob.com/gxcm/jycs/users";
		String msg;
		try {
			msg = sendPost(im, url);
		} catch (Exception e) {
			msg="环信服务器异常";
			e.printStackTrace();
		}
		return msg;
	}

	static class miTM implements javax.net.ssl.TrustManager,
			javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}

		public void checkClientTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}
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

	String sendPost(IM im, String path) throws Exception{
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String urlHostName, SSLSession session) {
				return true;
			}
		};
		String json="{\"username\":\""+im.getUsername()+"\",\"password\":\""+im.getPassword()+"\"}";
		byte[] data = json.getBytes();
		trustAllHttpsCertificates();
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
		java.net.URL url = new java.net.URL(path);
		java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url
				.openConnection();
		conn.setRequestMethod("POST");
		conn.setConnectTimeout(5 * 1000);// 设置连接超时时间为5秒
		conn.setReadTimeout(20 * 1000);// 设置读取超时时间为20秒
		// 使用 URL 连接进行输出，则将 DoOutput标志设置为 true
		conn.setDoOutput(true);

		conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
		// conn.setRequestProperty("Content-Encoding","gzip");
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));
		OutputStream outStream = conn.getOutputStream();// 返回写入到此连接的输出流
		outStream.write(data);
		outStream.close();// 关闭流
		String msg = "";// 保存调用http服务后的响应信息
		// 如果请求响应码是200，则表示成功
		if (conn.getResponseCode() == 200) {
			msg="环信注册成功";
			imRepository.save(im);
		} else {
			msg = "环信注册失败";
	}
		conn.disconnect();// 断开连接
		return msg;
	}

	public IM findByUsername(String username) {
		IM im=imRepository.findByUsername(username);
		return im;
	}

}
