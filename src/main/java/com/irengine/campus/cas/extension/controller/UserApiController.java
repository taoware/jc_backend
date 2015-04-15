package com.irengine.campus.cas.extension.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.multipart.MultipartFile;

import com.irengine.campus.cas.extension.domain.Device;
import com.irengine.campus.cas.extension.domain.Password;
import com.irengine.campus.cas.extension.domain.Result;
import com.irengine.campus.cas.extension.domain.UploadedFile;
import com.irengine.campus.cas.extension.domain.User;
import com.irengine.campus.cas.extension.service.UserService;
import com.irengine.campus.cas.extension.service.UtilityService;

@RestController
@RequestMapping(value = "/users")
public class UserApiController {

	private static String DIRECTORY_UPLOAD = "/uploaded/";
	   
    private static String getWebDirectory(HttpServletRequest request) {
    	return request.getSession().getServletContext().getRealPath(DIRECTORY_UPLOAD);
    }
    
	@Autowired
	UserService userService;
	@Autowired
	UtilityService utilityService;

	 /**通过ids查找user*/
	@RequestMapping(value = "/ids", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> findByIds(@RequestParam("ids") String ids) {
		String[] strs=ids.split(",");
		List<User> users=userService.findByIds(strs);
		return new ResponseEntity<>(new Result<User>("ok", users),HttpStatus.OK);
		
	}
	 /**通过id查找user*/
	@RequestMapping(value = "/id", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> findById(@RequestParam("id") long id) {
		User user=userService.findById(id);
		List<User> users=new ArrayList<User>();
		users.add(user);
		return new ResponseEntity<>(new Result<User>("ok",users),HttpStatus.OK);
	}
	
	 /** 修改用户头像 */
	 @RequestMapping(value="/avatar/{id}",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	 @ResponseBody
	public ResponseEntity<?> modifyAvatar(@PathVariable("id") long id,
			@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
		String uploadDirectoryName = getWebDirectory(request);
    	File uploadDirectory = new File(uploadDirectoryName);
    	FileUtils.forceMkdir(uploadDirectory);
    	String uploadFileName = String.format("%d%s", System.currentTimeMillis(), file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
    	File uploadFile = new File(StringUtils.join(new Object[]{ uploadDirectoryName, "user", id, uploadFileName }, "/"));
    	if (!file.isEmpty()) { 
    		FileUtils.writeByteArrayToFile(uploadFile, file.getBytes());
    		UploadedFile uploadedFile = utilityService.createFile("large","user", id, uploadFileName, file.getSize());
    		List<UploadedFile> files = utilityService.findByEntityTypeAndEntityId("large", "user", id);
    		if(files.size()>1){
    			long fileId=files.get(files.size()-1).getFileId();
    			utilityService.deleteByTypeAndEntityTypeAndEntityId("large","user",id,fileId);
    		}
    		return new ResponseEntity<>(uploadedFile, HttpStatus.CREATED);
    	}
    	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	 /**查询所有user信息*/
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> list() {

		List<User> users = userService.list();
		return new ResponseEntity<>(new Result<User>("ok", users),
				HttpStatus.OK);
	}
	/**注册用户*/
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody User user) {
		if(user.getName().trim().trim()==null||"".equals(user.getName().trim())||
				user.getGender().trim()==null||"".equals(user.getGender().trim())||
				user.getCategory().trim()==null||"".equals(user.getCategory().trim())||
				user.getPosition().trim()==null||"".equals(user.getPosition().trim())||
				user.getLocation().trim()==null||"".equals(user.getLocation().trim())||
				user.getAddress().trim()==null||"".equals(user.getAddress().trim())||
				user.getMobile().trim()==null||"".equals(user.getMobile().trim())||
				user.getPlainPassword().trim()==null||"".equals(user.getPlainPassword().trim())||
				user.getNotes().trim()==null||"".equals(user.getNotes().trim())
				){
			return new ResponseEntity<>(new Result<User>("信息不能为空",null),HttpStatus.BAD_REQUEST);
		}else{
			if(user.getCode()==null||"".equals(user.getCode())){
				user.setCode(user.getMobile());
			}
			User user1=userService.findByMobile(user.getMobile());
			if(user1!=null){
				return new ResponseEntity<>(new Result<User>("该手机已被注册", null),
						HttpStatus.BAD_REQUEST);
			}else{
				String mes = userService.create(user);
				if ("success".equals(mes)) {
					return new ResponseEntity<>(new Result<User>("注册成功", null),
							HttpStatus.OK);
				} else {
					return new ResponseEntity<>(new Result<User>("手机号或密码格式错误,注册失败", null),
							HttpStatus.BAD_REQUEST);
				}
			}
			
		}
	}
	/**指定时间查询该时间之后注册的用户(用不到?)*/
	@RequestMapping(value = "/lastUpdated", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> list(@RequestParam("queryTime") Date queryTime) {

		List<User> users = userService.getLastUpdated(queryTime);

		return new ResponseEntity<>(new Result<User>("ok", users),
				HttpStatus.OK);
	}

	/**(用不到?)*/
	@RequestMapping(value = "/{id}/devices", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> addChild(@PathVariable("id") Long id,
			@RequestBody Device device) {

		userService.addDevice(id, device);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**(用不到?)*/
	@RequestMapping(value = "/{id}/devices", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> removeChild(@PathVariable("id") Long id,
			@RequestBody Device device) {

		userService.removeDevice(id, device);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**用不到?*/
	@RequestMapping(value = "/machineId", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> findByMachineId(
			@RequestParam("machineId") String machineId) {

		User user = userService.findByMachineId(machineId);
		List<User> users = new ArrayList<User>();
		users.add(user);
		return new ResponseEntity<>(new Result<User>("ok", users),
				HttpStatus.OK);
	}

	/**通过code查找用户信息(用不到?)*/
	@RequestMapping(value = "/code", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> findByCode(@RequestParam("code") String code) {

		User user = userService.findByCode(code);
		List<User> users = new ArrayList<User>();
		users.add(user);
		return new ResponseEntity<>(new Result<User>("ok", users),
				HttpStatus.OK);
	}

	/**通过手机号查找对应用户(用不到?)*/
	@RequestMapping(value = "/mobile", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> findByMobile(@RequestParam("mobile") String mobile) {

		User user = userService.findByMobile(mobile);
		List<User> users = new ArrayList<User>();
		users.add(user);
		return new ResponseEntity<>(new Result<User>("ok", users),
				HttpStatus.OK);
	}
/**登录功能*/
	@RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> login(@RequestParam("mobile") String mobile,
			@RequestParam("plainPassword") String plainPassword) {
		String str = userService.login(mobile, plainPassword);
		Map<String, Object> map = new HashMap<String, Object>();
		if ("success".equals(str)) {
			User user = userService.findByMobile(mobile);
			List<User> users = new ArrayList<User>();
			users.add(user);
			return new ResponseEntity<>(new Result<User>("登录成功.", users),
					HttpStatus.OK);
		} else if ("notExist".equals(str)) {
			map.put("msg", "用户名不存在");
			return new ResponseEntity<>(new Result<User>("用户名不存在.", null),
					HttpStatus.BAD_REQUEST);
		} else if ("Wrong".equals(str)) {
			return new ResponseEntity<>(new Result<User>("密码错误", null),
					HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(new Result<User>("服务器异常", null),
					HttpStatus.BAD_REQUEST);
		}
	}

	/** 删除用户 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> delete(@RequestParam("id") Long id) {
		userService.delete(id);
		return new ResponseEntity<>(new Result<User>("ok",null),HttpStatus.OK);
	}

	/** 修改用户 */
	@RequestMapping(value = "/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody User user) {
		String mes = userService.update(user);
		if ("success".equals(mes)) {
			return new ResponseEntity<>(new Result<User>("修改成功", null),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new Result<User>("修改失败", null),
					HttpStatus.BAD_REQUEST);
		}
	}
	
	/**修改用户密码*/
	@RequestMapping(value = "/password/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> updatePassword(@RequestBody Password pw,@PathVariable("id") long id) {
		User user=userService.findById(id);
		if(User.encode(pw.getOldPassword()).equals(user.getPassword())){
			userService.updatePassword(pw.getPassword(),id);
			return new ResponseEntity<>(new Result<User>("ok",null),HttpStatus.OK);
		}else{
			return new ResponseEntity<>(new Result<User>("原密码错误",null),HttpStatus.BAD_REQUEST);
		}
	}
}
