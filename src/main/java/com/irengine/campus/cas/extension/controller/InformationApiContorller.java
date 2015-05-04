package com.irengine.campus.cas.extension.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.irengine.campus.cas.extension.domain.Information;
import com.irengine.campus.cas.extension.domain.Result;
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

	/** 列出所有资讯/查询轮播图/列表图 */
	/*
	 * permissionIds:权限id的合集
	 */
	/* @RequestParam(required=false)当他为false 时 使用这个注解可以不传这个参数 true时必须传 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> listByType(
			@RequestParam(value = "type", required = false, defaultValue = "") String type,
			@RequestParam("userId") long userId) {
		List<Information> informations=new ArrayList<Information>();
		List<Long> permissionIds = userService
				.findPermissionIdsByUserId(userId);
		if (permissionIds.indexOf(6L) == -1) {
			return new ResponseEntity<>(new Result<Information>("没有权限查看资讯",
					informations), HttpStatus.BAD_REQUEST);
		} else {
			if (type == null || "".equals(type)) {
				informations = informationService.list();
				return new ResponseEntity<>(new Result<Information>("ok",
						informations), HttpStatus.OK);
			} else {
				informations = informationService
						.findByType(type);
				return new ResponseEntity<>(new Result<Information>("ok",
						informations), HttpStatus.OK);
			}
		}
	}

	/** 新建一条资讯 */
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody Information information,
			@RequestParam("userId") long userId) {
		List<Long> permissionIds = userService
				.findPermissionIdsByUserId(userId);
		if (permissionIds.indexOf(5L) == -1) {
			return new ResponseEntity<>(new Result<Information>("没有权限新建资讯",
					null), HttpStatus.OK);
		} else {
			informationService.create(information);
			return new ResponseEntity<>(new Result<Information>("ok", null),
					HttpStatus.OK);
		}
	}

	/** 根据时间列出资讯 */
	@RequestMapping(value = "/lastUpdated", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> list(@RequestParam("queryTime") String queryTimeS,
			@RequestParam("userId") long userId) {
		List<Long> permissionIds = userService
				.findPermissionIdsByUserId(userId);
		List<Information> informations=new ArrayList<Information>();
		if (permissionIds.indexOf(6L) == -1) {
			return new ResponseEntity<>(new Result<Information>("没有权限查看资讯",
					informations), HttpStatus.BAD_REQUEST);
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date queryTime;
			try {
				queryTime = sdf.parse(queryTimeS);
			} catch (ParseException e) {
				return new ResponseEntity<>(new Result<Information>("时间格式错误",
						informations), HttpStatus.BAD_REQUEST);
			}
			informations = informationService
					.getLastUpdated(queryTime);
			return new ResponseEntity<>(new Result<Information>("ok",
					informations), HttpStatus.OK);
		}
	}

	/** 删除资讯 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> delete(@RequestParam("id") Long id) {
			informationService.delete(id);
			return new ResponseEntity<>(new Result<Information>("ok", null),
					HttpStatus.OK);
	}

	/** 修改资讯 */
	@RequestMapping(value = "/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody Information information) {
			informationService.update(information);
			return new ResponseEntity<>(new Result<Information>("ok", null),
					HttpStatus.OK);
	}

	/** 根据userId查找资讯 */
	@RequestMapping(value = "/userId", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> findByUserId(@RequestParam("userId") Long userId) {

			List<Information> informations = informationService
					.findByUserId(userId);
			return new ResponseEntity<>(new Result<Information>("ok",
					informations), HttpStatus.OK);
	}

	/** 根据id查找资讯 */
	@RequestMapping(value = "/id", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> findById(@RequestParam("id") Long id) {
			List<Information> informations = new ArrayList<Information>();
			informations.add(informationService.findById(id));
			return new ResponseEntity<>(new Result<Information>("ok",
					informations), HttpStatus.OK);
	}
}
