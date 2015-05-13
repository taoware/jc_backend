package com.irengine.campus.cas.extension.service;

import http.Url;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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
	
	/**取得token
	 * @throws Exception */
	public String getToken() throws Exception{
		String path="https://a1.easemob.com/gxcm/jycs/token";
		String json="{\"grant_type\": \"client_credentials\",\"client_id\":\"YXA65PuJ4OPqEeSEbUF2XJP4QQ\",\"client_secret\":\"YXA6l0wPCJtBbPcBnGX1F5yRtUau7OU\"}";
		String msg=sendPost(json, path, "", "","POST");
		String msg1=msg.substring(msg.indexOf("access_token")+"access_token\":\"".length(), msg.indexOf("\",\"expires_in"));
		return msg1;
	}

	/** 注册环信 */
	public String create(String username, String password) {
		IM im = new IM(username, password);
		String json = "{\"username\":\"" + username + "\",\"password\":\""
				+ password + "\"}";
		String msg;
		try {
			msg = sendPost(json, Url.imUsers, "", "","POST");
			if (!"error".equals(msg)) {
				imRepository.save(im);
			}
		} catch (Exception e) {
			msg = "error";
		}
		return msg;
	}

	/** 在环信上创建群组 */
	public String chatgroups(String groupname, String desc, boolean pub,
			int maxusers, boolean approval, String owner, String members) {
		String json = "";
		if ("".equals(members) || members == null) {
			json = "{\"groupname\":\"" + groupname + "\"," + "\"desc\":\""
					+ desc + "\"," + "\"public\":" + pub + ","
					+ "\"maxusers\":" + maxusers + "," + "\"approval\":"
					+ approval + "," + "\"owner\":\"" + owner + "\"}";
		} else {
			json = "{\"groupname\":\"" + groupname + "\"," + "\"desc\":\""
					+ desc + "\"," + "\"public\":" + pub + ","
					+ "\"maxusers\":" + maxusers + "," + "\"approval\":"
					+ approval + "," + "\"owner\":\"" + owner + "\","
					+ "\"members\":" + members + "}";
		}
		String msg = "";
		String token="";
		try {
			token = getToken();
		} catch (Exception e1) {
		}
		String path = Url.imChatgroupsUrl;
		String header1 = "Authorization";
		String header2 = "Bearer " + token;
		try {
			msg = sendPost(json, path, header1, header2,"POST");
		} catch (Exception e) {
			msg = "error";
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

	String sendPost(String json, String path, String header1, String header2,String requestMethod)
			throws Exception {
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String urlHostName, SSLSession session) {
				return true;
			}
		};
		byte[] data = json.getBytes();
		trustAllHttpsCertificates();
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
		java.net.URL url = new java.net.URL(path);
		java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url
				.openConnection();
		conn.setRequestMethod(requestMethod);
		conn.setConnectTimeout(5 * 1000);// 设置连接超时时间为5秒
		conn.setReadTimeout(20 * 1000);// 设置读取超时时间为20秒
		// 使用 URL 连接进行输出，则将 DoOutput标志设置为 true
		conn.setDoOutput(true);
		if (!"".equals(header1)) {
			conn.setRequestProperty(header1, header2);
		}
		conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
		if(!"GET".equals(requestMethod)){
			conn.setRequestProperty("Content-Length", String.valueOf(data.length));
			OutputStream outStream = conn.getOutputStream();// 返回写入到此连接的输出流
			outStream.write(data);
			outStream.close();// 关闭流
		}
		String msg = "";// 保存调用http服务后的响应信息
		// 如果请求响应码是200，则表示成功
		if (conn.getResponseCode() == 200) {
			/*得到返回数据*/
	        InputStream in = conn.getInputStream();
	        BufferedReader read = new BufferedReader(new InputStreamReader(in,"UTF-8"));
	        String valueString = null;
	        StringBuffer str = new StringBuffer();
	        while ((valueString=read.readLine())!=null){
	                str.append(valueString);
	        }
	        in.close();
			msg = "success"+str;
		} else {
			msg = "error";
		}
		conn.disconnect();// 断开连接
		return msg;
	}

	public IM findByUsername(String username) {
		IM im = imRepository.findByUsername(username);
		return im;
	}
	
	/**通过userId查找其对应的组,并且通过组向其发送消息*/
	public void sendMessageToGroup(String message, String imUsername) {
		try {
			String token=getToken();
			List<String> groups=findGroupByUserId(imUsername,token);
			sendMessageToGroupByGroupIds(groups,message,token);
		} catch (Exception e) {
			System.out.println("error");
		}
	}

	/**将message发送给组*/
	private void sendMessageToGroupByGroupIds(List<String> groups,
			String message,String token) {
		if(groups.size()>0){
			String target="[";
			for(String groupId:groups){
				target+="\""+groupId+"\",";
			}
			target=target.substring(0,target.length()-1)+"]";
			String header1 = "Authorization";
			String header2 = "Bearer " + token;
			System.out.println(header2);
			String path=Url.sendMessageUrl;
			String json="{\"target_type\" : \"chatgroups\",\"target\" : "+target+
					",\"msg\" : {\"type\" : \"txt\",\"msg\" : \""+message+"\"}}";
			System.out.println(json);
			try {
				String msg=sendPost(json, path, header1, header2, "POST");
				System.out.println(msg);
			} catch (Exception e) {
				System.out.println("error");
			}
		}
	}

	/**通过userId查找组*/
	private List<String> findGroupByUserId(String imUsername,String token) {
		List<String> result=new ArrayList<String>();
		String header1 = "Authorization";
		String header2 = "Bearer " + token;
		String path=Url.getGroupsUrl+imUsername+"/joined_chatgroups";
		try {
			String msg=sendPost("", path, header1, header2,"GET");
			if(!"error".equals(msg)){
				String data=msg.substring(msg.indexOf("data"), msg.lastIndexOf("]"));
				String[] groupsMsg=data.split("},");
				for(String groupMsg:groupsMsg){
					String groupId=groupMsg.substring(groupMsg.indexOf("groupid\" : \"")+"groupid\" : \"".length()+1, groupMsg.indexOf("\","));
					result.add(groupId);
				}
			}
		} catch (Exception e) {
			
		}
		return result;
	}

	/**根据环信用户名删除环信帐号*/
	public String deleteIm(String username) {
		String token;
		String msg="error";
		try {
			token = getToken();
			String path=Url.deleteIm+username;
			String header1 = "Authorization";
			String header2 = "Bearer " + token;
			msg=sendPost("", path, header1, header2, "DELETE");
		} catch (Exception e) {

		}
		return msg;
	}

}
