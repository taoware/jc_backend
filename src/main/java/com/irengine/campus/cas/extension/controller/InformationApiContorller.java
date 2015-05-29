package com.irengine.campus.cas.extension.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.irengine.campus.cas.extension.domain.Information;
import com.irengine.campus.cas.extension.domain.Result;
import com.irengine.campus.cas.extension.domain.UploadedFile;
import com.irengine.campus.cas.extension.service.InformationService;
import com.irengine.campus.cas.extension.service.UserService;
import com.irengine.campus.cas.extension.service.UtilityService;

@RestController
@RequestMapping(value = "/news")
public class InformationApiContorller {
	@Autowired
	private InformationService informationService;
	@Autowired
	private UtilityService utilityService;
	@Autowired
	private UserService userService;

	/** 新建一条资讯 
	 * @throws IOException */
	@RequestMapping(value="/web",method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	public void create2(
			@RequestParam(value = "summary", required = false, defaultValue = "") String summary1,
			@RequestParam("title") String title1,
			@RequestParam("address") String address1,
			@RequestParam("content") String content1,
			@RequestParam("type") String type1,
			@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "photoDescription", required = false) String photoDescription,
			@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String summary = "";
		String title = "";
		String address = "";
		String content = "";
		String type = "";
		/* 解决乱码问题 */
		try {
			summary = new String(summary1.getBytes("ISO-8859-1"), "utf-8");
			title = new String(title1.getBytes("ISO-8859-1"), "utf-8");
			address = new String(address1.getBytes("ISO-8859-1"), "utf-8");
			content = new String(content1.getBytes("ISO-8859-1"), "utf-8");
			type = new String(type1.getBytes("ISO-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e1) {

		}
		/* 权限管理 */
		List<Long> permissionIds = userService.findPermissionIdsByUserId(532L);
		if (permissionIds.indexOf(5L) == -1) {

		} else {
			if (!type.equals("listshow") && !type.equals("slideshow")) {

			} else {
				Information information = new Information(summary, title,
						address, content, 532L, type);
				informationService.create(information);
				long maxId = informationService.findMaxId();
				List<MultipartFile> files = new ArrayList<MultipartFile>();
				if (file != null && !file.isEmpty()) {
					files.add(file);
					List<UploadedFile> files1 = null;
					try {
						files1 = utilityService.uploadFiles("news", maxId,
								files, request, photoDescription);
					} catch (IOException e) {

					}
					if (files1.size() > 0) {
						information.setPhoto(files1.get(0));
						informationService.update(information);
					}
				}
				/* 跳转页面 */
				String url = "http://www.baidu.com";
				String str = new String(url.getBytes("utf-8"), "ISO-8859-1");
				response.sendRedirect(str);

			}
		}
	}
	
	@RequestMapping(value = "/{id}/send")
	@ResponseBody
	public void sendRedirect(@PathVariable("id") Long id,
			HttpServletResponse response) throws ServletException, IOException {
		Information information = informationService.findById(id);
		Date createTime1 = information.getCreateTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String createTime = sdf.format(createTime1);
		String address = information.getAddress();
		String content = information.getContent();
		String summary = information.getSummary();
		String title = information.getTitle();
		String photoUrl = "";
		String photoDescription = "";
		if (information.getPhoto() != null) {
			photoUrl = information.getPhoto().getUrl();
			photoDescription = information.getPhoto().getPhotoDescription();
		}
		String url = "http://vps1.taoware.com:8080/jc/html5/msg_page/msg.html?address="
				+ address
				+ "&content="
				+ content
				+ "&summary="
				+ summary
				+ "&title="
				+ title
				+ "&photoUrl="
				+ photoUrl
				+ "&createTime="
				+ createTime + "&photoDescription=" + photoDescription;

		String str = new String(url.getBytes("utf-8"), "ISO-8859-1");
		response.sendRedirect(str);
	}

	/** 列出所有资讯,按咨询类型列出资讯,根据时间查询资讯,根据id查询资讯 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> listByType(
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "queryTime", required = false) String queryTimeS,
			@RequestParam("userId") long userId,
			@RequestParam(value = "id", required = false) Long id) {
		List<Information> informations = new ArrayList<Information>();
		List<Long> permissionIds = userService
				.findPermissionIdsByUserId(userId);
		if (permissionIds.indexOf(6L) == -1) {
			return new ResponseEntity<>(new Result<Information>("没有权限查看资讯",
					informations), HttpStatus.BAD_REQUEST);
		} else {
			if (type != null) {
				if ("slideshow".equals(type)) {
					/* 按类型搜索(最多返回四条) */
					List<Information> informations1 = informationService
							.findByType(type);
					if (informations1.size() > 4) {
						for (int i = 0; i < 4; i++) {
							informations.add(informations1.get(i));
						}
					} else {
						informations = informations1;
					}
				} else {
					informations = informationService.findByType(type);
				}
			} else if (queryTimeS != null) {
				/* 按时间搜索 */
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date queryTime;
				try {
					queryTime = sdf.parse(queryTimeS);
				} catch (ParseException e) {
					return new ResponseEntity<>(new Result<Information>(
							"时间格式错误", informations), HttpStatus.BAD_REQUEST);
				}
				informations = informationService.getLastUpdated(queryTime);
			} else if (id != null) {
				/* 按id搜索 */
				informations.add(informationService.findById(id));
			} else {
				/* 搜索全部 */
				informations = informationService.list();
			}
			return new ResponseEntity<>(new Result<Information>("ok",
					informations), HttpStatus.OK);
		}
	}

	/** 新建一条资讯 
	 * @throws IOException */
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(
			@RequestParam(value = "summary", required = false, defaultValue = "") String summary1,
			@RequestParam("title") String title1,
			@RequestParam("address") String address1,
			@RequestParam("content") String content1,
			@RequestParam("type") String type1,
			@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "photoDescription", required = false) String photoDescription,
			@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String summary = "";
		String title = "";
		String address = "";
		String content = "";
		String type = "";
		/* 解决乱码问题 */
		try {
			summary = new String(summary1.getBytes("ISO-8859-1"), "utf-8");
			title = new String(title1.getBytes("ISO-8859-1"), "utf-8");
			address = new String(address1.getBytes("ISO-8859-1"), "utf-8");
			content = new String(content1.getBytes("ISO-8859-1"), "utf-8");
			type = new String(type1.getBytes("ISO-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e1) {
			return new ResponseEntity<>(new Result<Information>("error", null),
					HttpStatus.BAD_REQUEST);
		}
		/* 权限管理 */
		List<Long> permissionIds = userService.findPermissionIdsByUserId(532L);
		if (permissionIds.indexOf(5L) == -1) {
			return new ResponseEntity<>(new Result<Information>("没有权限新建资讯",
					null), HttpStatus.OK);
		} else {
			if (!type.equals("listshow") && !type.equals("slideshow")) {
				return new ResponseEntity<>(new Result<Information>(
						"type must be 'listshow' or 'slideshow'!", null),
						HttpStatus.BAD_REQUEST);
			} else {
				Information information = new Information(summary, title,
						address, content, 532L, type);
				informationService.create(information);
				long maxId = informationService.findMaxId();
				List<MultipartFile> files = new ArrayList<MultipartFile>();
				if (file != null && !file.isEmpty()) {
					files.add(file);
					List<UploadedFile> files1 = null;
					try {
						files1 = utilityService.uploadFiles("news", maxId,
								files, request, photoDescription);
					} catch (IOException e) {
						return new ResponseEntity<>(new Result<Information>(
								"upload error", null), HttpStatus.BAD_REQUEST);
					}
					if (files1.size() > 0) {
						information.setPhoto(files1.get(0));
						informationService.update(information);
					}
				}
				/* 跳转页面 */
//				String url = "www.baidu.com";
//				String str = new String(url.getBytes("utf-8"), "ISO-8859-1");
//				response.sendRedirect(str);
				return new ResponseEntity<>(
						new Result<Information>("ok", null), HttpStatus.OK);
			}
		}
	}

	/** 删除资讯 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		informationService.delete(id);
		return new ResponseEntity<>(new Result<Information>("ok", null),
				HttpStatus.OK);
	}

	/** 修改资讯 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> update(@PathVariable("id") long id,
			@RequestBody Information information) {
		information.setId(id);
		informationService.update(information);
		return new ResponseEntity<>(new Result<Information>("ok", null),
				HttpStatus.OK);
	}

	/** 根据userId查找资讯 */
	@RequestMapping(value = "/user/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> findByUserId(@PathVariable("userId") Long userId) {
		List<Information> informations = informationService
				.findByUserId(userId);
		return new ResponseEntity<>(
				new Result<Information>("ok", informations), HttpStatus.OK);
	}

	/** 修改咨询展示照片 */
	@RequestMapping(value = "/{id}/photo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> modifyAvatar(@PathVariable("id") long id,
			@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		Information information = informationService.findById(id);
		List<MultipartFile> files = new ArrayList<MultipartFile>();
		List<UploadedFile> files1 = new ArrayList<UploadedFile>();
		if (!file.isEmpty()) {
			files.add(file);
			try {
				files1 = utilityService.uploadFiles("news", id, files,
						request, "");
				if (files1.size() > 0) {
					information.setPhoto(files1.get(0));
					informationService.update(information);
				}
			} catch (IOException e) {
				return new ResponseEntity<>(new Result<Information>("upload error",
						null), HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>(
					new Result<Information>("file is empty", null),
					HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(new Result<Information>("ok", null),
				HttpStatus.OK);
	}
	
}
