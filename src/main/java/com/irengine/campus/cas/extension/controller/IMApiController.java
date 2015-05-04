package com.irengine.campus.cas.extension.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.irengine.campus.cas.extension.domain.IM;
import com.irengine.campus.cas.extension.domain.IMGroup;
import com.irengine.campus.cas.extension.domain.Result;
import com.irengine.campus.cas.extension.domain.User;
import com.irengine.campus.cas.extension.service.IMService;
import com.irengine.campus.cas.extension.service.UserService;

@RestController
@RequestMapping(value = "/im")
public class IMApiController {
	
	@Autowired
	IMService imService;
	@Autowired
	UserService userService;
	
	/**环信建群组*/
	@RequestMapping(value="/chatgroups",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> chatgroups(@RequestBody IMGroup imGroup){
		/*String msg = imService.chatgroups("测试群2", "测试群2简介", true, 200, true,
				"18616949668a", "[\"12584125412a\",\"18965412365a\"]");*/
		/*群组的members属性,有的话就必须填至少一个用户*/
		/*{
			"groupname":"测试群组01",
			"desc":"测试群组",
			"public":true,
			"maxusers":300,
			"approval":true,
			"owner":"18616949668a",
			"members":["12584125412a","18965412365a"]
		}*/
		String members="";
		User user=userService.findById(imGroup.getUserId());
		if("".equals(imGroup.getMemberIds())||imGroup.getMemberIds()==null){
			
		}else{
			String[] strs = imGroup.getMemberIds().split(",");
			List<User> users=userService.findByIds(strs);
			members="[";
			for(User user2:users){
				String username=user2.getIm().getUsername();
				members+="\""+username+"\",";
			}
			members=members.substring(0, members.length()-1)+"]";
		}
		String msg=imService.chatgroups(imGroup.getGroupname(), 
				imGroup.getDesc(), imGroup.isPub(), imGroup.getMaxusers(), 
				imGroup.isApproval(), user.getIm().getUsername(), members);
		if("success".equals(msg)){
			return new ResponseEntity<>(new Result<IM>("ok",null),HttpStatus.OK);
		}else{
			return new ResponseEntity<>(new Result<IM>("error",null),HttpStatus.BAD_REQUEST);
		}
	}
}










