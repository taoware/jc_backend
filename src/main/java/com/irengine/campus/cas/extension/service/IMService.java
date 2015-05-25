package com.irengine.campus.cas.extension.service;

import http.Url;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.TypeReference;
import com.irengine.campus.cas.extension.controller.FileUploadController;
import com.irengine.campus.cas.extension.domain.Data;
import com.irengine.campus.cas.extension.domain.IM;
import com.irengine.campus.cas.extension.domain.IMGroup2;
import com.irengine.campus.cas.extension.domain.Member;
import com.irengine.campus.cas.extension.domain.SimpleIMGroup;
import com.irengine.campus.cas.extension.domain.User;
import com.irengine.campus.cas.extension.repository.IMRepository;
import com.irengine.commons.JSONUtils;

@Service
@Transactional
public class IMService {

	@Autowired
	IMRepository imRepository;
	@Autowired
	UserService userService;
	@Autowired
	SimpleIMGroupService simpleIMGroupService;

	private static Logger logger = LoggerFactory
			.getLogger(FileUploadController.class);
	
	/**向组添加成员
	 * @throws Exception */
	public String addMembers(String chatgroupid,String userIds) throws Exception{
		/*把userIds转成imUsername并且拼成字符串,得到requestBody的json*/
		//{"usernames":["5cxhactgdj","mh2kbjyop1"]}
		String json="{\"usernames\":[";
		String[] userIds2=userIds.split(",");
		for(String userId:userIds2){
			User user=userService.findById(Long.parseLong(userId));
			if(user.getIm()!=null){
				json+="\""+user.getIm().getUsername()+"\",";
			}
		}
		json=json.substring(0, json.length()-1)+"]}";
		/*调用post请求*/
		///{org_name}/{app_name}/chatgroups/{chatgroupid}/users
		String path=Url.addMembersToGroup+chatgroupid+"/users";
		String token = getToken();
		String header1 = "Authorization";
		String header2 = "Bearer " + token;
		Map<String,String> msgMap=sendPost2(json, path, header1, header2, "POST");
		return msgMap.get("msg");
	}

	/**
	 * 取得token
	 * 
	 * @throws Exception
	 */
	public String getToken() throws Exception {
		String path = "https://a1.easemob.com/gxcm/jycs/token";
		String json = "{\"grant_type\": \"client_credentials\",\"client_id\":\"YXA65PuJ4OPqEeSEbUF2XJP4QQ\",\"client_secret\":\"YXA6l0wPCJtBbPcBnGX1F5yRtUau7OU\"}";
		String msg = sendPost(json, path, "", "", "POST");
		String msg1 = msg.substring(msg.indexOf("access_token")
				+ "access_token\":\"".length(), msg.indexOf("\",\"expires_in"));
		return msg1;
	}

	/** 注册环信 
	 * @throws Exception */
	public String create(String username, String password) throws Exception{
		String msg = "error";
		IM im = new IM(username, password);
		if (!imIsExist(username)) {
			/* 环信账户不存在:新建环信用户,jc_im插入数据 */
			String json = "{\"username\":\"" + username + "\",\"password\":\""
					+ password + "\"}";
			try {
				msg = sendPost(json, Url.imUsers, "", "", "POST");
				if (!"error".equals(msg)) {
					List<IM> ims = findByUsername(username);
					if (ims.size() > 0) {
						/* jc_im表中存在该用户环信信息:不做操作 */
					} else {
						/* jc_im表中不存在该用户环信信息:插入一条im信息数据 */
						imRepository.save(im);
					}
					msg = "success";
				}
			} catch (Exception e) {
				msg = "im already exist";
			}
		} else {
			/* 环信用户存在:不新建,查看jc_im表中是否存在对应环信用户环信信息 */
			List<IM> ims = findByUsername(username);
			if (ims.size() > 0) {
				/* jc_im表中存在该用户环信信息:不做操作 */
			} else {
				/* jc_im表中不存在该用户环信信息:插入一条im信息数据 */
				imRepository.save(im);
				msg = "success";
			}
		}
		return msg;
	}

	/** 验证该环信用户是否存在(web端) 
	 * @throws Exception */
	public boolean imIsExist(String username) throws Exception {
		String token = getToken();
		String header1 = "Authorization";
		String header2 = "Bearer " + token;
		String path = Url.findImByUsername + username;
		Map<String,String> msg = sendPost2("", path, header1, header2, "GET");
		String code=msg.get("code");
		if("200".equals(code)){
			return true;
		}else{
			return false;
		}
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
		String token = "";
		try {
			token = getToken();
		} catch (Exception e1) {
			
		}
		String path = Url.imChatgroupsUrl;
		String header1 = "Authorization";
		String header2 = "Bearer " + token;
		try {
			Map<String,String> msgMap = sendPost2(json, path, header1, header2, "POST");
			msg=msgMap.get("msg");
			String responseBody=msgMap.get("responseBody");
			System.out.println(responseBody);
			String groupId=responseBody.substring(responseBody.indexOf("groupid")+12, responseBody.indexOf("timestamp")-8);
			System.out.println(groupId);
			/*怎样的到新建群组的groupId:通过responseBody返回内容取得*/
			SimpleIMGroup group=new SimpleIMGroup();
			group.setGroupId(groupId);
			simpleIMGroupService.create(group);
		} catch (Exception e) {
			e.printStackTrace();
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

	String sendPost(String json, String path, String header1, String header2,
			String requestMethod) throws Exception {
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
		if (!"GET".equals(requestMethod)) {
			conn.setRequestProperty("Content-Length",
					String.valueOf(data.length));
			OutputStream outStream = conn.getOutputStream();// 返回写入到此连接的输出流
			outStream.write(data);
			outStream.close();// 关闭流
		}
		String msg = "";// 保存调用http服务后的响应信息
		// 如果请求响应码是200，则表示成功
		StringBuffer str = new StringBuffer();
		if (conn.getResponseCode() == 200) {
			/* 得到返回数据 */
			InputStream in = conn.getInputStream();
			BufferedReader read = new BufferedReader(new InputStreamReader(in,
					"UTF-8"));
			String valueString = "";

			while ((valueString = read.readLine()) != null) {
				str.append(valueString);
			}
			in.close();
			msg = "success" + str;
			logger.info("----------send:" + msg);
		} else {
			msg = "error";
			logger.error("----------sendHttp error:" + str);
		}
		conn.disconnect();// 断开连接
		return msg;
	}

	public List<IM> findByUsername(String username) {
		List<IM> im = new ArrayList<IM>();
		im = imRepository.findByUsername(username);
		return im;
	}

	/** 通过userId查找其对应的组,并且通过组向其发送消息 */
	public void sendMessageToGroup(String message, String imUsername) {
		try {
			String token = getToken();
			List<String> groups = findGroupByUserId(imUsername, token);
			sendMessageToGroupByGroupIds(groups, message, token);
		} catch (Exception e) {
			System.out.println("error");
		}
	}

	/** 将message发送给组 */
	private void sendMessageToGroupByGroupIds(List<String> groups,
			String message, String token) {
		if (groups.size() > 0) {
			String target = "[";
			for (String groupId : groups) {
				target += "\"" + groupId + "\",";
			}
			target = target.substring(0, target.length() - 1) + "]";
			String header1 = "Authorization";
			String header2 = "Bearer " + token;
			System.out.println(header2);
			String path = Url.sendMessageUrl;
			String json = "{\"target_type\" : \"chatgroups\",\"target\" : "
					+ target + ",\"msg\" : {\"type\" : \"txt\",\"msg\" : \""
					+ message + "\"}}";
			System.out.println(json);
			try {
				String msg = sendPost(json, path, header1, header2, "POST");
				System.out.println(msg);
			} catch (Exception e) {
				System.out.println("error");
			}
		}
	}

	/** 通过userId查找组 */
	private List<String> findGroupByUserId(String imUsername, String token) {
		List<String> result = new ArrayList<String>();
		String header1 = "Authorization";
		String header2 = "Bearer " + token;
		String path = Url.getGroupsUrl + imUsername + "/joined_chatgroups";
		try {
			String msg = sendPost("", path, header1, header2, "GET");
			if (!"error".equals(msg)) {
				String data = msg.substring(msg.indexOf("data"),
						msg.lastIndexOf("]"));
				String[] groupsMsg = data.split("},");
				for (String groupMsg : groupsMsg) {
					String groupId = groupMsg.substring(
							groupMsg.indexOf("groupid\" : \"")
									+ "groupid\" : \"".length() + 1,
							groupMsg.indexOf("\","));
					result.add(groupId);
				}
			}
		} catch (Exception e) {

		}
		return result;
	}

	/** 根据环信用户名删除环信帐号 */
	public String deleteIm(String username) {
		String token;
		String msg = "error";
		try {
			token = getToken();
			String path = Url.deleteIm + username;
			String header1 = "Authorization";
			String header2 = "Bearer " + token;
			msg = sendPost("", path, header1, header2, "DELETE");
		} catch (Exception e) {

		}
		return msg;
	}

	Map<String, String> sendPost2(String json, String path, String header1,
			String header2, String requestMethod) throws Exception {
		Map<String, String> msgMap = new HashMap<String, String>();
		String code = "";
		String msg = "";
		String responseBody = "";
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
		if (!"GET".equals(requestMethod)) {
			conn.setRequestProperty("Content-Length",
					String.valueOf(data.length));
			OutputStream outStream = conn.getOutputStream();// 返回写入到此连接的输出流
			outStream.write(data);
			outStream.close();// 关闭流
		}
		
		StringBuffer str = new StringBuffer();
		code = "" + conn.getResponseCode();
		// 如果请求响应码是200，则表示成功
		if (conn.getResponseCode() == 200) {
			InputStream in = conn.getInputStream();
			BufferedReader read = new BufferedReader(new InputStreamReader(in,
					"UTF-8"));
			String valueString = "";

			while ((valueString = read.readLine()) != null) {
				str.append(valueString);
			}
			in.close();
			msg = "success";
		} else {
			msg = "error";
			logger.error("----------sendHttp error:" + str);
		}
		conn.disconnect();// 断开连接
		responseBody = "" + str;
		msgMap.put("code", code);
		msgMap.put("msg", msg);
		msgMap.put("responseBody", responseBody);
		return msgMap;
	}

	/**查找所有组以及对应的成员*/
	public List<IMGroup2> findAllGroup(String groupIdsStr) {
		/*发送请求查询环信上组的详细信息*/
		String token="";
		Map<String,String> mapMsg=new HashMap<String, String>();
		if("".equals(groupIdsStr)){
			return null;
		}else{
			try {
				token = getToken();
				String header1 = "Authorization";
				String header2 = "Bearer " + token;
				String path=Url.getGroupUrl2+groupIdsStr;
				mapMsg=sendPost2("", path, header1, header2, "GET");
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			/*将得到的responseBody信息注入到IMGroup类中*/
			String responseBody=mapMsg.get("responseBody");
			String groupsMsg=responseBody.substring(responseBody.indexOf("data")-1, responseBody.indexOf("timestamp")-4);
			@SuppressWarnings({ "unchecked", "rawtypes" })
			Data<IMGroup2> data=(Data) JSONUtils.parseObject("{"+groupsMsg+"}",new TypeReference<Data<IMGroup2>>(){});
			List<IMGroup2> list = data.getData();
			for(IMGroup2 group:list){
				group.setName(group.getName().substring(6));
				for(Member member:group.getAffiliations()){
					/*给member对应user*/
					if(member.getOwner()!=null){
						/*判定为群主*/
						User user=userService.findByImUsernames(member.getOwner()).get(0);
						group.setOwner(user);
					}else{
						/*判定为成员*/
						User user=userService.findByImUsernames(member.getMember()).get(0);
						group.getMembers().add(user);
					}
				}
			}
			return list;
		}
	}
	
}
