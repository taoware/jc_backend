package http;

public class Url {
	//创建环信群组
	public static final String imChatgroupsUrl="https://a1.easemob.com/gxcm/jycs/chatgroups";
	//注册环信用户(单个)
	public static final String imUsers="https://a1.easemob.com/gxcm/jycs/users";
	//该环信token
	public static final String imToken="YWMtDORrwPLNEeSJiVlAFgFWQgAAAU5W285X-YW3b7zY2pJP9ezB-JnGMjPOf5Q";
	//取得该用户所在的群组信息
	//{org_name}/{app_name}/users/{username}/joined_chatgroups
	public static final String getGroupsUrl="https://a1.easemob.com/gxcm/jycs/users/";
	//给群发文本消息
	public static final String sendMessageUrl="https://a1.easemob.com/gxcm/jycs/messages";
	//删除环信用户
	public static final String deleteIm="https://a1.easemob.com/gxcm/jycs/users/";
	//根据环信用户名查找环信用户信息
	///{org_name}/{app_name}/users/{username}
	public static final String findImByUsername="https://a1.easemob.com/gxcm/jycs/users/";
	//根据环信组id查询组详细信息
	///{org_name}/{app_name}/chatgroups/{group_id1},{group_id2}
	public static final String getGroupUrl2="https://a1.easemob.com/gxcm/jycs/chatgroups/";
}
