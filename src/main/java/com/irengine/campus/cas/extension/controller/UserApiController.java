package com.irengine.campus.cas.extension.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.irengine.campus.cas.extension.domain.IM;
import com.irengine.campus.cas.extension.domain.Password;
import com.irengine.campus.cas.extension.domain.Result;
import com.irengine.campus.cas.extension.domain.UploadedFile;
import com.irengine.campus.cas.extension.domain.User;
import com.irengine.campus.cas.extension.service.IMService;
import com.irengine.campus.cas.extension.service.UserService;
import com.irengine.campus.cas.extension.service.UtilityService;

@RestController
@RequestMapping(value = "/users")
public class UserApiController {

	@Autowired
	UserService userService;
	@Autowired
	UtilityService utilityService;
	@Autowired
	IMService imService;

	/**
	 * 查询所有user信息,通过环信用户名查找user信息,通过name查找user,通过imId查找user, 通过ids查找user,
	 * 通过id查找user,通过queryTime查找user,通过code查找user,通过mobile查找user
	 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> list(
			@RequestParam(value = "imUsernames", required = false) String imUsernames,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "imIds", required = false) String imIds,
			@RequestParam(value = "ids", required = false) String ids,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "queryTime", required = false) Date queryTime,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "mobile", required = false) String mobile) {
		List<User> users = new ArrayList<User>();
		if (imUsernames != null && !"".equals(imUsernames)) {
			users = userService.findByImUsernames(imUsernames);
		} else if (name != null && !"".equals(name)) {
			User user = userService.findByName(name);
			if (user != null) {
				users.add(user);
			}
		} else if (imIds != null && !"".equals(imIds)) {
			String[] strs = imIds.split(",");
			users = userService.findByImIds(strs);
		} else if (ids != null && !"".equals(ids)) {
			String[] strs = ids.split(",");
			users = userService.findByIds(strs);
		} else if (id != null && !"".equals(id)) {
			User user = userService.findById(id);
			if (user != null) {
				users.add(user);
			}
		} else if (queryTime != null && !"".equals(queryTime)) {
			users = userService.getLastUpdated(queryTime);
		} else if (code != null && !"".equals(code)) {
			User user = userService.findByCode(code);
			if (user != null) {
				users.add(user);
			}
		} else if (mobile != null && !"".equals(mobile)) {
			User user = userService.findByMobile(mobile);
			if (user != null) {
				users.add(user);
			}
		} else {
			users = userService.list();
		}
		return new ResponseEntity<>(new Result<User>("ok", users),
				HttpStatus.OK);
	}

	/** 审核用户 */
	@RequestMapping(value = "/{userId}/audit", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> audit(@PathVariable("userId") long userId) {
		User user = userService.findById(userId);
		user.setAudit(true);
		userService.update2(user);
		return new ResponseEntity<>(new Result<User>("ok", null), HttpStatus.OK);
	}

	/** 输入手机号修改密码 */
	@RequestMapping(value = "/forgetPass", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> forgetPasswordByMobile(
			@RequestParam("mobile") String mobile, @RequestBody Password pw) {
		User user = userService.findByMobile(mobile);
		if (user != null) {
			String msg = userService.updatePassword(pw.getPassword(),
					user.getId());
			if ("success".equals(msg)) {
				return new ResponseEntity<>(new Result<User>("ok", null),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new Result<User>("error", null),
						HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<>(new Result<User>("can not find user",
					null), HttpStatus.BAD_REQUEST);
		}
	}

	/** 忘记密码 */
	@RequestMapping(value = "/{userId}/forgetPass", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> forgetPassword(
			@PathVariable("userId") long userId, @RequestBody Password pw) {
		String msg = userService.updatePassword(pw.getPassword(), userId);
		if ("success".equals(msg)) {
			return new ResponseEntity<>(new Result<User>("ok", null),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new Result<User>("error", null),
					HttpStatus.OK);
		}
	}

	/** 修改用户头像 */
	@RequestMapping(value = "/{id}/avatar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> modifyAvatar(@PathVariable("id") long id,
			@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		User user = userService.findById(id);
		List<MultipartFile> files = new ArrayList<MultipartFile>();
		List<UploadedFile> files1 = new ArrayList<UploadedFile>();
		if (!file.isEmpty()) {
			files.add(file);
			try {
				files1 = utilityService.uploadFiles("user", id, files, request,
						"");
				if (files1.size() > 0) {
					user.setAvatar(files1.get(0));
					userService.update2(user);
				}
			} catch (IOException e) {
				return new ResponseEntity<>(new Result<User>("upload error",
						null), HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>(
					new Result<User>("file is empty", null),
					HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(new Result<User>("ok", null), HttpStatus.OK);
	}

	/** 注册用户 */
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody User user,
			HttpServletRequest request) {
		System.out.println(user);
		String content = request.getContentType();
		System.out.println(content);
		if (user.getName().trim().trim() == null
				|| "".equals(user.getName().trim())
				|| user.getGender().trim() == null
				|| "".equals(user.getGender().trim())
				|| user.getCategory().trim() == null
				|| "".equals(user.getCategory().trim())
				|| user.getPosition().trim() == null
				|| "".equals(user.getPosition().trim())
				|| user.getLocation().trim() == null
				|| "".equals(user.getLocation().trim())
				|| user.getAddress().trim() == null
				|| "".equals(user.getAddress().trim())
				|| user.getMobile().trim() == null
				|| "".equals(user.getMobile().trim())
				|| user.getPlainPassword().trim() == null
				|| "".equals(user.getPlainPassword().trim())
				|| user.getNotes().trim() == null
				|| "".equals(user.getNotes().trim())) {
			return new ResponseEntity<>(new Result<User>("信息不能为空", null),
					HttpStatus.BAD_REQUEST);
		} else {
			if (user.getCode() == null || "".equals(user.getCode())) {
				user.setCode(user.getMobile());
			}
			User user1 = userService.findByMobile(user.getMobile());
			if (user1 != null) {
				return new ResponseEntity<>(new Result<User>("该手机已被注册", null),
						HttpStatus.BAD_REQUEST);
			} else {
				String IMMsg = imService.create(
						ProcessMobile(user.getMobile()), "123456a");
				if (!IMMsg.equals("error")) {
					IMMsg = "success";
				}
				IM im = null;
				if ("success".equals(IMMsg)) {
					im = imService.findByUsername(ProcessMobile(user
							.getMobile()));
					user.setEnableIM(true);
				} else {
					user.setEnableIM(false);
				}
				user.setIm(im);
				user.setAudit(false);
				String mes = userService.create(user);
				if ("success".equals(mes)) {
					return new ResponseEntity<>(new Result<User>(
							"用户注册:success,环信注册:" + IMMsg, null), HttpStatus.OK);
				} else {
					return new ResponseEntity<>(new Result<User>(
							"手机号或密码格式错误,注册失败", null), HttpStatus.BAD_REQUEST);
				}
			}

		}
	}

	/**  */
	@RequestMapping(value = "/{id}/devices", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> addChild(@PathVariable("id") Long id,
			@RequestBody Device device) {

		userService.addDevice(id, device);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**  */
	@RequestMapping(value = "/{id}/devices", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> removeChild(@PathVariable("id") Long id,
			@RequestBody Device device) {

		userService.removeDevice(id, device);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**  */
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

	/** 登录功能 */
	@RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> login(@RequestParam("mobile") String mobile,
			@RequestParam("plainPassword") String plainPassword) {
		String str = userService.login(mobile, plainPassword);
		if ("success".equals(str)) {
			User user = userService.findByMobile(mobile);
			List<User> users = new ArrayList<User>();
			users.add(user);
			/* 检测环信是否被注册上 */
			String IMMsg = "";
			if (!user.isEnableIM()) {
				IMMsg = imService.create(ProcessMobile(user.getMobile()),
						"123456a");
				IM im = null;
				if ("环信注册成功".equals(IMMsg)) {
					im = imService.findByUsername(ProcessMobile(user
							.getMobile()));
					user.setEnableIM(true);
				} else {
					user.setEnableIM(false);
				}
				user.setIm(im);
			}
			return new ResponseEntity<>(
					new Result<User>("登录成功." + IMMsg, users), HttpStatus.OK);
		} else if ("notExist".equals(str)) {
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
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		User user=userService.findById(id);
		String msg=imService.deleteIm(user.getIm().getUsername());
		if(!"error".equals(msg)){
			userService.delete(id);
			return new ResponseEntity<>(new Result<User>("ok", null), HttpStatus.OK);
		}else{
			return new ResponseEntity<>(new Result<User>("error", null), HttpStatus.BAD_REQUEST);
		}
	}

	/** 修改用户 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> update(@PathVariable("id") Long id,
			@RequestBody User user) {
		user.setId(id);
		String mes = userService.update(user);
		if ("success".equals(mes)) {
			return new ResponseEntity<>(new Result<User>("修改成功", null),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new Result<User>("修改失败", null),
					HttpStatus.OK);
		}
	}

	/** 修改用户密码 */
	@RequestMapping(value = "/password/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> updatePassword(@RequestBody Password pw,
			@PathVariable("id") long id) {
		User user = userService.findById(id);
		if (User.encode(pw.getOldPassword()).equals(user.getPassword())) {
			String msg = userService.updatePassword(pw.getPassword(), id);
			if ("success".equals(msg)) {
				return new ResponseEntity<>(new Result<User>("ok", null),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new Result<User>("error", null),
						HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>(new Result<User>("原密码错误", null),
					HttpStatus.BAD_REQUEST);
		}
	}

	/** 给用户指定角色 */
	@RequestMapping(value = "/{userId}/role/{roleId}/setRole", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> setRole(@PathVariable("userId") long userId,
			@PathVariable("roleId") long roleId) {
		userService.setRole(userId, roleId);
		return new ResponseEntity<>(new Result<User>("ok", null), HttpStatus.OK);
	}

	/** 处理手机号 */
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

}
