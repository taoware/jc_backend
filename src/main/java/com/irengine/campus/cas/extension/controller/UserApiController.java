package com.irengine.campus.cas.extension.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.irengine.campus.cas.extension.domain.Device;
import com.irengine.campus.cas.extension.domain.Result;
import com.irengine.campus.cas.extension.domain.User;
import com.irengine.campus.cas.extension.service.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserApiController {
	
	@Autowired
	UserService userService;
	
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> list() {

    	List<User> users = userService.list();
//    	Map<String,Object> map=new HashMap<String, Object>();
//    	map.put("Results", users);
//    	map.put("msg", "ok");
        return new ResponseEntity<>(new Result<User>("ok",users), HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody User user) {
    	String mes=userService.create(user);
    	if("success".equals(mes)){
            return new ResponseEntity<>(new Result<User>("注册成功",null),HttpStatus.OK);
    	}else{
    		return new ResponseEntity<>(new Result<User>("注册失败",null),HttpStatus.BAD_REQUEST);
    	}
    }
    
    @RequestMapping(value = "/lastUpdated", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> list(@RequestParam("queryTime") Date queryTime) {

    	List<User> users = userService.getLastUpdated(queryTime);
    	
        return new ResponseEntity<>(new Result<User>("ok",users), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}/devices", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> addChild(@PathVariable("id") Long id, @RequestBody Device device) {

    	userService.addDevice(id, device);
    	
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}/devices", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> removeChild(@PathVariable("id") Long id, @RequestBody Device device) {

    	userService.removeDevice(id, device);
    	
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/machineId", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> findByMachineId(@RequestParam("machineId") String machineId) {

    	User user = userService.findByMachineId(machineId);
    	List<User> users=new ArrayList<User>();
    	users.add(user);
        return new ResponseEntity<>(new Result<User>("ok",users), HttpStatus.OK);
    }

    @RequestMapping(value = "/code", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> findByCode(@RequestParam("code") String code) {

    	User user = userService.findByCode(code);
    	List<User> users=new ArrayList<User>();
    	users.add(user);
        return new ResponseEntity<>(new Result<User>("ok",users), HttpStatus.OK);
    }

    @RequestMapping(value = "/mobile", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> findByMobile(@RequestParam("mobile") String mobile) {

    	User user = userService.findByMobile(mobile);
    	List<User> users=new ArrayList<User>();
    	users.add(user);
        return new ResponseEntity<>(new Result<User>("ok",users), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> login(@RequestParam("mobile") String mobile,@RequestParam("plainPassword") String plainPassword) {
    	String str=userService.login(mobile, plainPassword);
    	Map<String,Object> map=new HashMap<String, Object>();
    	if("success".equals(str)){
    		User user=userService.findByMobile(mobile);
        	List<User> users=new ArrayList<User>();
        	users.add(user);
    		return new ResponseEntity<>(new Result<User>("登录成功.",users),HttpStatus.OK);
    	}else if("notExist".equals(str)){
    		map.put("msg", "用户名不存在");
    		return new ResponseEntity<>(new Result<User>("用户名不存在.",null),HttpStatus.BAD_REQUEST);
    	}else if("Wrong".equals(str)){
    		return new ResponseEntity<>(new Result<User>("密码错误",null),HttpStatus.BAD_REQUEST);
    	}else{
    		return new ResponseEntity<>(new Result<User>("服务器异常",null),HttpStatus.BAD_REQUEST);
    	}
    }
    /**删除用户*/
    @RequestMapping(value = "/delete", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> delete(@RequestParam("id") Long id){
    	userService.delete(id);
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /**修改用户*/
    @RequestMapping(value="/update",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody User user){
    	String mes=userService.update(user);
    	if("success".equals(mes)){
            return new ResponseEntity<>(new Result<User>("修改成功",null),HttpStatus.OK);
    	}else{
    		return new ResponseEntity<>(new Result<User>("修改失败",null),HttpStatus.BAD_REQUEST);
    	}
    }
}












