package com.irengine.campus.cas.extension.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.irengine.campus.cas.extension.domain.IM;
import com.irengine.campus.cas.extension.domain.IMGroup;
import com.irengine.campus.cas.extension.domain.IMGroup2;
import com.irengine.campus.cas.extension.domain.Result;
import com.irengine.campus.cas.extension.domain.SimpleIMGroup;
import com.irengine.campus.cas.extension.domain.User;
import com.irengine.campus.cas.extension.service.IMService;
import com.irengine.campus.cas.extension.service.SimpleIMGroupService;
import com.irengine.campus.cas.extension.service.UserService;

@RestController
@RequestMapping(value = "/im")
public class IMApiController {
	
	@Autowired
	IMService imService;
	@Autowired
	UserService userService;
	@Autowired
	SimpleIMGroupService simpleIMGroupService;
	
	/**环信用户是否存在
	 * @throws Exception */
	///im/{username}
	@RequestMapping(value="/{username}",method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> findImByUsername(@PathVariable("username") String username) throws Exception{
		boolean b=imService.imIsExist(username);
		return new ResponseEntity<>(b,HttpStatus.OK);
	}
	
	/**测试取token*/
	@RequestMapping(value="/test",method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> test(){
		String msg="";
		try {
			msg=imService.getToken();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new Result<IM>(msg,null),HttpStatus.OK);
	}
	
	/**环信建群组*/
	@RequestMapping(value="/chatgroups",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> createChatgroups(@RequestBody IMGroup imGroup){
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
		String msg=imService.chatgroups("group_"+imGroup.getGroupname(), 
				imGroup.getDesc(), imGroup.isPub(), imGroup.getMaxusers(), 
				imGroup.isApproval(), user.getIm().getUsername(), members);
		if(!"error".equals(msg)){
			return new ResponseEntity<>(new Result<IM>("ok",null),HttpStatus.OK);
		}else{
			return new ResponseEntity<>(new Result<IM>("error",null),HttpStatus.BAD_REQUEST);
		}
	}
	
	/**查询所有组*/
	///im/chatgroups,get请求
	@RequestMapping(value="/chatgroups",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> getChatgroups(){
		//得到本地数据库中的所有组信息
		List<SimpleIMGroup> groups=simpleIMGroupService.findAll();
		/*得到组id拼成的字符串:1410511142870,1408518613503*/
		String groupIdsStr="";
		if(groups.size()>0){
			for(SimpleIMGroup simpleImGroup:groups){
				groupIdsStr+=simpleImGroup.getGroupId()+",";
			}
			groupIdsStr=groupIdsStr.substring(0, groupIdsStr.length()-1);
		}
		System.out.println("-----------groupIdsStr:"+groupIdsStr);
		//查询对应环信上的组的详细信息
		List<IMGroup2> groups2=imService.findAllGroup(groupIdsStr);
		return new ResponseEntity<>(new Result<IMGroup2>("ok",groups2),HttpStatus.OK);
	}
	
}










