package com.irengine.campus.cas.extension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.irengine.campus.cas.extension.domain.IM;
import com.irengine.campus.cas.extension.domain.User;
import com.irengine.campus.cas.extension.service.IMService;

public class testCase {

	@Test
	public void test20150601(){
		String str="黄宋搏";
		String regex="[\u4e00-\u9fa5]+";
		if(str.matches(regex)){
			System.out.println(true);
		}else{
			System.out.println(false);
		}
	}
	
	private String ProcessMobile(String mobile) {
		String str = "";
		if (mobile.length() > 5) {
			String mobile1 = mobile.substring(0, 5);
			String mobile2 = mobile.substring(5);
			str = mobile2 + mobile1;
		} else {
			str = mobile;
		}
		return str;
	}

	private String test07(String groupname, String desc, boolean pub,
			int maxusers, boolean approval, String owner, String members) {
		String json = "{\"groupname\":\"" + groupname + "\"," + "\"desc\":\""
				+ desc + "\"," + "\"public\":" + pub + "," + "\"maxusers\":"
				+ maxusers + "," + "\"approval\":" + approval + ","
				+ "\"owner\":\"" + owner + "\"," + "\"members\":" + members
				+ "}";
		return json;
	}
	
	/*模糊查询地址*/
	@Test
	public void test11(){
		String str3="江西省宜春市上高县";
		String str2=testDeal(str3);
		System.out.println(str2);
		String str4=str2.replaceAll("", "%");
		System.out.println(str4.substring(1, str4.length()-1));
	}
	
	private String testDeal(String str1) {
		//String str2=str1.replaceAll("[\\s]+","").replaceAll("省", "").replaceAll("市", "");
		String str2=str1.replaceAll("[\\s]+","").replaceAll("省|市|县", "");
		String[] locations=new String[]{"上海","北京","天津","重庆"};
		for(String location:locations){
			int index=str2.indexOf(location);
			if(index!=-1&&str2.indexOf(location, index+1)!=-1){
				str2=str2.substring(location.length());
				break;
			}
		}
		return str2;
	}

	@Test
	public void test10() {
		int a = 11;
		for (int i = 0; i < (a > 9 ? 9 : a); i++) {
			System.out.print(i + " ");
		}
	}

	@Test
	public void test09() {
		String members = "[";
		List<String> users = new ArrayList<String>();
		users.add("king");
		users.add("t.i.");
		for (String user2 : users) {
			members += "\"" + user2 + "\",";
		}
		members = members.substring(0, members.length() - 1) + "]";
		System.out.println(members);
	}

	@Test
	public void test08() {
		String msg = test07("测试群2", "测试群2简介", true, 200, true, "18616949668a",
				"[\"12584125412a\",\"18965412365a\"]");
		System.out.println(msg);
	}

	/** 测试环信建组 */
	@Test
	public void test06() {
		IMService imService = new IMService();
		String msg = imService.chatgroups("测试群2", "测试群2简介", true, 200, true,
				"18616949668a", "[\"12584125412a\",\"18965412365a\"]");
		System.out.println(msg);
	}

	@Test
	public void test05() {
		System.out.println(ProcessMobile("18616949668"));
	}

	@Test
	public void test04() {
		String mobile = "123456";
		String str = "";
		for (int i = 0; i < mobile.length(); i++) {
			char a = (char) mobile.charAt(i);
			char c = (char) (a + 5);
			System.out.println(c);
			str += c;
		}
		System.out.println(str);
	}

	@Test
	public void test03() {
		String location2 = "上海市-上海市-浦东新区";
		String location = location2.substring(0, location2.indexOf("-"));
		System.out.println(location);
	}

	@Test
	public void test02() {
		IM im = new IM("huang", "123456a");
		String json = "{\"username\":\"" + im.getUsername()
				+ "\",\"password\":\"" + im.getPassword() + "\"}";
		System.out.println(json);
	}

	@Test
	public void test01() {
		String str = "ssss.jpg";
		String str1 = str.substring(str.lastIndexOf("."));
		System.out.println(str1);
	}

	@Test
	public void testConvertDate() {
		String queryTimeS = "2014-1-1";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date queryTime;
		try {
			queryTime = sdf.parse(queryTimeS);
			System.out.println(queryTime);
		} catch (ParseException e) {
			System.out.println(e);
		}
	}

	@Test
	public void testEncoding() {
		System.out.println(User.encode("18616949668"));
	}

	@Test
	public void testMobile() {
		System.out.println("18662249668"
				.matches("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"));
	}

	@Test
	public void testPassword() {
		String password = "111sdfsad";
		boolean j1 = password.matches("[\\da-zA-Z]{6,16}");
		boolean j2 = password.matches("[a-zA-Z]+[\\d]+||[\\d]+[a-zA-Z]+");
		System.out.println(j1 + "," + j2);
		if (j1 == true && j2 == true) {
			System.out.println(true);
		} else {
			System.out.println(false);
		}
	}

	@Test
	public void testString() {
		String str1 = "1";
		String str2 = "aaa";
		String str = "{" + "\"" + str1 + "\":" + "\"" + str2 + "\"}";
		System.out.println(str);
	}

	@Test
	public void test20150512() {
		List<TestSquare> squares = new ArrayList<TestSquare>();

		squares.add(new TestSquare("上海", "aaa"));
		squares.add(new TestSquare("江苏", "bbb"));
		squares.add(new TestSquare("上海", "ccc"));
		squares.add(new TestSquare("上海", "ddd"));
		squares.add(new TestSquare("江苏", "eee"));

		Map<String, List<TestSquare>> map = new HashMap<String, List<TestSquare>>();
		Set<String> provinces = new HashSet<String>();
		for (TestSquare square : squares) {
			provinces.add(square.getProvince());
		}
		for (String province : provinces) {
			List<TestSquare> squares1 = new ArrayList<TestSquare>();
			for (TestSquare square : squares) {
				if (province.equals(square.getProvince())) {
					squares1.add(square);
				}
				map.put(province, squares1);
			}
		}
		System.out.println(map);
	}
}
