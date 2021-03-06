package com.irengine.campus.cas.extension.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.irengine.campus.cas.extension.domain.Square;
import com.irengine.campus.cas.extension.domain.UploadedFile;
import com.irengine.campus.cas.extension.domain.User;
import com.irengine.campus.cas.extension.service.IMService;
import com.irengine.campus.cas.extension.service.RoleService;
import com.irengine.campus.cas.extension.service.SquareService;
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
	@Autowired
	RoleService roleService;
	@Autowired
	SquareService squareService;

	private static Logger logger = LoggerFactory
			.getLogger(UserApiController.class);

	/**
	 * 测试验证手机号和密码
	 * 
	 * @param mobile
	 *            手机号
	 * @param password
	 *            密码
	 * @return boolean
	 */
	@RequestMapping(value = "/testVerify", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> testVerify(@RequestParam("mobile") String mobile,
			@RequestParam("password") String password) {
		boolean j1 = userService.testVerify(mobile, password);
		return new ResponseEntity<>(j1, HttpStatus.OK);
	}

	/** 测试 */
	/*
	 * sample for CORS
	 */
	@RequestMapping(value = "/cors", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> cors(HttpServletResponse response) {
		// Access-Control-Allow-Headers: x-requested-with
		response.addHeader("Access-Control-Allow-Origin", "*");
		// response.setHeader("Access-Control-Allow-Origin",
		// "x-requested-with");
		// response.setHeader("Access-Control-Allow-Methods",
		// "GET, PUT, POST, DELETE, OPTIONS");
		// response.setHeader("Access-Control-Max-Age", "60");
		response.addHeader("Access-Control-Allow-Credentials", "true");
		response.addHeader("Access-Control-Allow-Methods",
				"GET, PUT, POST, DELETE, OPTIONS");
		response.addHeader("Access-Control-Allow-Headers",
				"cache-control, content-type, x-http-method-override");
		return new ResponseEntity<>("CORS enabled", HttpStatus.OK);
	}

	/**
	 * 查询所有user信息,通过环信用户名查找user信息,通过name查找user,通过imId查找user, 通过ids查找user,
	 * 通过id查找user,通过queryTime查找user,通过code查找user,通过mobile查找user
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> list(
			HttpServletResponse response,
			@RequestParam(value = "imUsernames", required = false) String imUsernames,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "imIds", required = false) String imIds,
			@RequestParam(value = "ids", required = false) String ids,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "queryTime", required = false) Date queryTime,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "location", required = false) String location,
			@RequestParam(value = "address", required = false) String address,
			/*传入userIdExc起到剔除环信好友用户的功能*/
			@RequestParam(value = "userIdExc", required = false) Long userIdExc,
			@RequestParam(value = "mobile", required = false) String mobile)
			throws UnsupportedEncodingException {
		List<User> users = new ArrayList<User>();
		if (imUsernames != null && !"".equals(imUsernames)) {
			users = userService.findByImUsernames(imUsernames);
		} else if (name != null) {
			// name = new String(name.getBytes("ISO-8859-1"), "utf-8");
			System.out.println(name);
			logger.info(name);
			/* 精确搜索 */
			// User user = userService.findByName(name);
			// if (user != null) {
			// users.add(user);
			// }
			/* 模糊搜索 */
			/* 防止输入空字符串查到所有信息 */
			if (name.length() > 0) {
				users = userService.findByName(name);
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
		} else if (location != null) {
			/* 防止输入空字符串查到所有信息 */
			if (location.length() > 0) {
				/* 模糊查询地址 */
				logger.info("----------location:" + location);
				// location=new String(location.getBytes("ISO-8859-1"),
				// "utf-8");
				logger.info("----------location:" + location);
				// System.out.println(location);
				location = testDeal(location);
				users = userService.findbyLocation(location);
			}
		} else if (address != null) {
			/* 模糊查询 */
			/* 防止输入空字符串查到所有信息 */
			if (address.length() > 0) {
				users = userService.findByAddress(address);
			}
		} else {
			users = userService.list();
		}
		if(userIdExc!=null&&!"".equals(userIdExc)){
			logger.info("-----------------userIdExc:"+userIdExc);
			/*user2:需要剔除的用户*/
			List<User> users2=new ArrayList<User>();
			User user=userService.findById(userIdExc);
			users.remove(user);
			try {
				users2 = imService.findContactsByUserId(userIdExc);
			} catch (Exception e) {
				return new ResponseEntity<>(new Result<User>("ok", users),
						HttpStatus.OK);
			}
			users.removeAll(users2);
		}
		return new ResponseEntity<>(new Result<User>("ok", users),
				HttpStatus.OK);
	}

	/**
	 * 处理省市区字符串
	 * 
	 * @param str1
	 *            待处理的字符串
	 * @return
	 */
	private String testDeal(String str1) {
		// String str2=str1.replaceAll("[\\s]+","").replaceAll("省",
		// "").replaceAll("市", "");
		String str2 = str1.replaceAll("[\\s]+", "").replaceAll("省|市|县", "");
		String[] locations = new String[] { "上海", "北京", "天津", "重庆" };
		for (String location : locations) {
			int index = str2.indexOf(location);
			if (index != -1 && str2.indexOf(location, index + 1) != -1) {
				str2 = str2.substring(location.length());
				break;
			}
		}
		return str2;
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
						HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>(new Result<User>("找不到该用户", null),
					HttpStatus.BAD_REQUEST);
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
					HttpStatus.BAD_REQUEST);
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
		/* 搜索该id用户并且上传头像成功后返回信息 */
		List<User> users = new ArrayList<User>();
		User user1 = userService.findById(id);
		users.add(user1);
		return new ResponseEntity<>(new Result<User>("ok", users),
				HttpStatus.OK);
	}

	/**
	 * 注册用户
	 * 
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody User user) throws Exception {
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
				|| "".equals(user.getPlainPassword().trim())) {
			return new ResponseEntity<>(new Result<User>("信息不能为空", null),
					HttpStatus.BAD_REQUEST);
		} else {
			/* 真实姓名只能是中文 */
			String regex = "[\u4e00-\u9fa5]+";
			String name = user.getName();
			if (!name.matches(regex)) {
				return new ResponseEntity<>(
						new Result<User>("真实姓名只能为中文", null),
						HttpStatus.REQUEST_TIMEOUT);
			}
			if (user.getCode() == null || "".equals(user.getCode())) {
				user.setCode(user.getMobile());
			}
			User user1 = userService.findByMobile(user.getMobile());
			if (user1 != null) {
				// 401:该手机已被注册
				return new ResponseEntity<>(new Result<User>("该手机已被注册", null),
						HttpStatus.UNAUTHORIZED);
			} else {
				imService.create(ProcessMobile(user.getMobile()), "123456a");
				IM im = null;
				im = imService.findByUsername(ProcessMobile(user.getMobile()))
						.get(0);
				user.setEnableIM(true);
				user.setIm(im);
				user.setAudit(true);
				String mes = userService.create(user);
				if ("success".equals(mes)) {
					/* 根据基础user数据自动配权限和unit */
					// userService.matchUser(user);
					/* 自动配置权限为king(6),unit为全国(23) */
					userService.matchUser2(user);
					return new ResponseEntity<>(
					// 200:用户注册成功
							new Result<User>("用户注册成功", null), HttpStatus.OK);
				} else {
					// 402:手机号或密码格式错误,注册失败
					return new ResponseEntity<>(new Result<User>(
							"手机号或密码格式错误,注册失败", null),
							HttpStatus.PAYMENT_REQUIRED);
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

	/**
	 * 登录功能
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> login(@RequestParam("mobile") String mobile,
			@RequestParam("plainPassword") String plainPassword)
			throws Exception {
		String str = userService.login(mobile, plainPassword);
		if ("success".equals(str)) {
			User user = userService.findByMobile(mobile);
			List<User> users = new ArrayList<User>();
			users.add(user);
			/* 检测环信是否被注册上 */
			String IMMsg = "";
			if (user.getIm() == null) {
				IMMsg = imService.create(ProcessMobile(user.getMobile()),
						"123456a");
				IM im = null;
				if ("success".equals(IMMsg)) {
					im = imService.findByUsername(
							ProcessMobile(user.getMobile())).get(0);
					user.setEnableIM(true);
					user.setIm(im);
					userService.update2(user);
				}
			}
			// 200:登入成功
			return new ResponseEntity<>(
					new Result<User>("登录成功." + IMMsg, users), HttpStatus.OK);
		} else if ("notExist".equals(str)) {
			// 403:用户名不存在
			return new ResponseEntity<>(new Result<User>("用户名不存在.", null),
					HttpStatus.FORBIDDEN);
		} else if ("Wrong".equals(str)) {
			// 406:密码错误
			return new ResponseEntity<>(new Result<User>("密码错误", null),
					HttpStatus.NOT_ACCEPTABLE);
		} else {
			// 400:服务器异常
			return new ResponseEntity<>(new Result<User>("服务器异常", null),
					HttpStatus.BAD_REQUEST);
		}
	}

	/** 删除用户 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		User user = userService.findById(id);
		List<Square> squares = squareService.findByUserId(id);
		for (Square square : squares) {
			squareService.delete(square.getId());
		}
		String msg = "";
		if (user.getIm() != null) {
			msg = imService.deleteIm(user.getIm().getUsername());
		}
		if (!"error".equals(msg)) {
			userService.delete(id);
			return new ResponseEntity<>(new Result<User>("ok", null),
					HttpStatus.OK);
		} else {
			userService.delete(id);
			return new ResponseEntity<>(new Result<User>(
					"delete im error,delete user success", null),
					HttpStatus.BAD_REQUEST);
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
				return new ResponseEntity<>(new Result<User>(
						"密码格式不对,请输入6~16位字母加数字组合", null), HttpStatus.BAD_REQUEST);
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
