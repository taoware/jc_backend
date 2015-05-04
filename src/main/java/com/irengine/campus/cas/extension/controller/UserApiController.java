package com.irengine.campus.cas.extension.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
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

	private static String DIRECTORY_UPLOAD = "/uploaded/";
	
	private static String getWebDirectory(HttpServletRequest request) {
		return request.getSession().getServletContext()
				.getRealPath(DIRECTORY_UPLOAD);
	}

	@Autowired
	UserService userService;
	@Autowired
	UtilityService utilityService;
	@Autowired
	IMService imService;

	/**通过name(真实姓名)查找user(用户信息)*/
	@RequestMapping(value="/name",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> findByName(@RequestParam("name") String name) {
		List<User> users=new ArrayList<User>();
		User user=userService.findByName(name);
		if(user!=null){
			users.add(user);
		}
		return new ResponseEntity<>(new Result<User>("ok", users),
				HttpStatus.OK);
	}
	/**根据环信id数组查找用户信息*/
	@RequestMapping(value = "/imIds", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> findByImIds(@RequestParam("imIds") String imIds) {
		String[] strs = imIds.split(",");
		List<User> users = userService.findByImIds(strs);
		return new ResponseEntity<>(new Result<User>("ok", users),
				HttpStatus.OK);
	}
	
	/**审核用户*/
	@RequestMapping(value = "/audit/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> audit(@PathVariable("userId") long userId){
		User user=userService.findById(userId);
		user.setAudit(true);
		userService.update2(user);
		return new ResponseEntity<>(new Result<User>("ok", null), HttpStatus.OK);
	}
	
	/** 忘记密码 */
	@RequestMapping(value = "/forgetPass/{userId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> forgetPassword(
			@PathVariable("userId") long userId,
			@RequestBody Password pw) {
		String msg=userService.updatePassword(pw.getPassword(), userId);
		if("success".equals(msg)){
			return new ResponseEntity<>(new Result<User>("ok", null), HttpStatus.OK);
		}else{
			return new ResponseEntity<>(new Result<User>("error", null), HttpStatus.OK);
		}
	}

	/** 通过ids查找user */
	@RequestMapping(value = "/ids", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> findByIds(@RequestParam("ids") String ids) {
		String[] strs = ids.split(",");
		List<User> users = userService.findByIds(strs);
		return new ResponseEntity<>(new Result<User>("ok", users),
				HttpStatus.OK);

	}

	/** 通过id查找user */
	@RequestMapping(value = "/id", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> findById(@RequestParam("id") long id) {
		User user = userService.findById(id);
		List<User> users = new ArrayList<User>();
		users.add(user);
		return new ResponseEntity<>(new Result<User>("ok", users),
				HttpStatus.OK);
	}

	/** 修改用户头像 */
	@RequestMapping(value = "/avatar/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> modifyAvatar(@PathVariable("id") long id,
			@RequestParam("file") MultipartFile file, HttpServletRequest request)
			throws IOException {
		String uploadDirectoryName = getWebDirectory(request);
		File uploadDirectory = new File(uploadDirectoryName);
		FileUtils.forceMkdir(uploadDirectory);
		String uploadFileName = String.format(
				"%d%s",
				System.currentTimeMillis(),
				file.getOriginalFilename().substring(
						file.getOriginalFilename().lastIndexOf(".")));
		File uploadFile = new File(StringUtils.join(new Object[] {
				uploadDirectoryName, "user", id, uploadFileName }, "/"));
		if (!file.isEmpty()) {
			FileUtils.writeByteArrayToFile(uploadFile, file.getBytes());
			UploadedFile uploadedFile = utilityService.createFile("large",
					"user", id, uploadFileName, file.getSize());
			List<UploadedFile> files = utilityService
					.findByEntityTypeAndEntityId("large", "user", id);
			if (files.size() > 1) {
				long fileId = files.get(files.size() - 1).getFileId();
				utilityService.deleteByTypeAndEntityTypeAndEntityId("large",
						"user", id, fileId);
			}
			return new ResponseEntity<>(uploadedFile, HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/** 查询所有user信息,通过环信用户名查找user信息 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> list(@RequestParam(value="imUsernames",required=false) String imUsernames) {
		List<User> users=new ArrayList<User>();
		if(imUsernames!=null&&!"".equals(imUsernames)){
			users=userService.findByImUsernames(imUsernames);
		}else{
			users = userService.list();
		}
		return new ResponseEntity<>(new Result<User>("ok", users),
				HttpStatus.OK);
	}

	/** 注册用户 */
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody User user) {
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
					return new ResponseEntity<>(new Result<User>("用户注册:success,环信注册:"
							+ IMMsg, null), HttpStatus.OK);
				} else {
					return new ResponseEntity<>(new Result<User>(
							"手机号或密码格式错误,注册失败", null), HttpStatus.BAD_REQUEST);
				}
			}

		}
	}

	/** 指定时间查询该时间之后注册的用户(用不到?) */
	@RequestMapping(value = "/lastUpdated", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> list(@RequestParam("queryTime") Date queryTime) {

		List<User> users = userService.getLastUpdated(queryTime);

		return new ResponseEntity<>(new Result<User>("ok", users),
				HttpStatus.OK);
	}

	/** (用不到?) */
	@RequestMapping(value = "/{id}/devices", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> addChild(@PathVariable("id") Long id,
			@RequestBody Device device) {

		userService.addDevice(id, device);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	/** (用不到?) */
	@RequestMapping(value = "/{id}/devices", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> removeChild(@PathVariable("id") Long id,
			@RequestBody Device device) {

		userService.removeDevice(id, device);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	/** 用不到? */
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

	/** 通过code查找用户信息(用不到?) */
	@RequestMapping(value = "/code", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> findByCode(@RequestParam("code") String code) {

		User user = userService.findByCode(code);
		List<User> users = new ArrayList<User>();
		users.add(user);
		return new ResponseEntity<>(new Result<User>("ok", users),
				HttpStatus.OK);
	}

	/** 通过手机号查找对应用户*/
	@RequestMapping(value = "/mobile", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> findByMobile(@RequestParam("mobile") String mobile) {
		User user = userService.findByMobile(mobile);
		List<User> users = new ArrayList<User>();
		if(user!=null){
			users.add(user);
		}
		return new ResponseEntity<>(new Result<User>("ok", users),
				HttpStatus.OK);
	}

	/** 登录功能 */
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
		return new ResponseEntity<>(new Result<User>("ok", null), HttpStatus.OK);
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
			String msg=userService.updatePassword(pw.getPassword(), id);
			if("success".equals(msg)){
				return new ResponseEntity<>(new Result<User>("ok", null),
						HttpStatus.OK);
			}else{
				return new ResponseEntity<>(new Result<User>("error", null),
						HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>(new Result<User>("原密码错误", null),
					HttpStatus.BAD_REQUEST);
		}
	}

	/** 给用户指定角色 */
	@RequestMapping(value = "/role/{userId}/{roleId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> setRole(@PathVariable("userId") long userId,
			@PathVariable("roleId") long roleId) {
		userService.setRole(userId, roleId);
		return new ResponseEntity<>(new Result<User>("ok", null), HttpStatus.OK);
	}

	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
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
