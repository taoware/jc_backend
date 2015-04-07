package com.irengine.campus.cas.extension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.irengine.campus.cas.extension.domain.User;

public class testCase {
	@Test
	public void test01(){
		String str="ssss.jpg";
		String str1=str.substring(str.lastIndexOf("."));
		System.out.println(str1);
	}
	@Test
	public void testConvertDate() {
		String queryTimeS="2014-1-1";
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    	Date queryTime;
		try {
			queryTime = sdf.parse(queryTimeS);
			System.out.println(queryTime);
		} catch (ParseException e) {
			System.out.println(e);
		}
	}
	
	@Test
	public void testEncoding(){
		System.out.println(User.encode("123456a"));
	}
	@Test
	public void testMobile(){
		System.out.println("18662249668".matches("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"));
	}
	@Test
	public void testPassword(){
		String password="111sdfsad";
		boolean j1=password.matches("[\\da-zA-Z]{6,16}");
		boolean j2=password.matches("[a-zA-Z]+[\\d]+||[\\d]+[a-zA-Z]+");
		System.out.println(j1+","+j2);
		if(j1==true&&j2==true){
			System.out.println(true);
		}else{
			System.out.println(false);
		}
	}
	@Test
	public void testString(){
		String str1="1";
		String str2="aaa";
		String str="{"+"\""+str1+"\":"+"\""+str2+"\"}";
		System.out.println(str);
	}
}









