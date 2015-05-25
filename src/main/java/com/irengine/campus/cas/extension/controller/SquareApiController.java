package com.irengine.campus.cas.extension.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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

import com.irengine.campus.cas.extension.domain.Result;
import com.irengine.campus.cas.extension.domain.Square;
import com.irengine.campus.cas.extension.domain.UploadedFile;
import com.irengine.campus.cas.extension.service.SquareService;
import com.irengine.campus.cas.extension.service.UnitService;
import com.irengine.campus.cas.extension.service.UserService;
import com.irengine.campus.cas.extension.service.UtilityService;

@RestController
@RequestMapping(value = "/square")
public class SquareApiController {
	@Autowired
	SquareService squareService;
	@Autowired
	UserService userService;
	@Autowired
	UtilityService utilityService;
	@Autowired
	UnitService unitService;

	/** 新建广场(安卓适用) */
	@RequestMapping(value = "/user/{userId}/unit/{unitId}/information/{information}/type/{type}", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> TestCreate3(
			@PathVariable("information") String information,
			@PathVariable("type") String type,
			@PathVariable("userId") long userId,
			@PathVariable("unitId") long unitId,
			@RequestParam(value = "file1", required = false) MultipartFile file1,
			@RequestParam(value = "file2", required = false) MultipartFile file2,
			@RequestParam(value = "file3", required = false) MultipartFile file3,
			@RequestParam(value = "file4", required = false) MultipartFile file4,
			@RequestParam(value = "file5", required = false) MultipartFile file5,
			@RequestParam(value = "file6", required = false) MultipartFile file6,
			@RequestParam(value = "file7", required = false) MultipartFile file7,
			@RequestParam(value = "file8", required = false) MultipartFile file8,
			@RequestParam(value = "file9", required = false) MultipartFile file9,
			HttpServletRequest request) throws IOException {
		List<MultipartFile> files = new ArrayList<MultipartFile>();
		files.add(file1);
		files.add(file2);
		files.add(file3);
		files.add(file4);
		files.add(file5);
		files.add(file6);
		files.add(file7);
		files.add(file8);
		files.add(file9);
		String information1 = "";
		String type1 = "";
		/* 解决乱码问题 */
		try {
			information1 = new String(information.getBytes("ISO-8859-1"),
					"utf-8");
			type1 = new String(type.getBytes("ISO-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			return new ResponseEntity<>(new Result<Square>("error", null),
					HttpStatus.BAD_REQUEST);
		}
		information1=information;
		type1=type;
		List<Long> permissionIds = userService
				.findPermissionIdsByUserId(userId);
		if (!type1.equals("员工") && !type1.equals("供应商") && !type1.equals("联采")) {
			/* 判断type格式 */
			return new ResponseEntity<>(new Result<Square>("广场类型错误", null),
					HttpStatus.BAD_REQUEST);
		} else {
			/* 判断是否有对应权限 */
			if (type1.equals("员工")) {
				if (permissionIds.indexOf(15L) == -1) {
					return new ResponseEntity<>(new Result<Square>(
							"无权限创建员工类广场", null), HttpStatus.BAD_REQUEST);
				}
			}
			if (type1.equals("供应商")) {
				if (permissionIds.indexOf(17L) == -1) {
					return new ResponseEntity<>(new Result<Square>(
							"无权限创建供应商类广场", null), HttpStatus.BAD_REQUEST);
				}
			}
			if (type1.equals("联采")) {
				if (permissionIds.indexOf(13L) == -1) {
					return new ResponseEntity<>(new Result<Square>(
							"无权限创建联采类广场", null), HttpStatus.BAD_REQUEST);
				}
			}
			Square square = new Square(type1, information1,
					unitService.findById(unitId), userService.findById(userId));
			squareService.create(square);
			long entityId = squareService.getMaxId();
			List<UploadedFile> uploadedFiles = new ArrayList<UploadedFile>();
			List<Square> squares = new ArrayList<Square>();
			try {
				uploadedFiles = utilityService.uploadFiles("square", entityId,
						files, request, "");
			} catch (Exception e) {
				e.printStackTrace();
				squareService.delete(entityId);
				return new ResponseEntity<>(new Result<Square>(
						"upload file error", squares), HttpStatus.BAD_REQUEST);
			}
			Square square1 = squareService.findById(entityId);
			square1.setPhotos(new HashSet(uploadedFiles));
			squareService.update(square1);
			squares.add(square1);
			return new ResponseEntity<>(new Result<Square>("ok", squares),
					HttpStatus.OK);
		}
	}
	
	/** 新建广场(不带图片) */
	@RequestMapping(value = "/user/{userId}/unit/{unitId}/test", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> TestCreate2(
			@RequestParam("information") String information,
			@RequestParam("type") String type,
			@PathVariable("userId") long userId,
			@PathVariable("unitId") long unitId) {
		List<Long> permissionIds = userService
				.findPermissionIdsByUserId(userId);
		if (!type.equals("员工") && !type.equals("供应商") && !type.equals("联采")) {
			/* 判断type格式 */
			return new ResponseEntity<>(new Result<Square>("广场类型错误", null),
					HttpStatus.BAD_REQUEST);
		} else {
			/* 判断是否有对应权限 */
			if (type.equals("员工")) {
				if (permissionIds.indexOf(15L) == -1) {
					return new ResponseEntity<>(new Result<Square>(
							"无权限创建员工类广场", null), HttpStatus.BAD_REQUEST);
				}
			}
			if (type.equals("供应商")) {
				if (permissionIds.indexOf(17L) == -1) {
					return new ResponseEntity<>(new Result<Square>(
							"无权限创建供应商类广场", null), HttpStatus.BAD_REQUEST);
				}
			}
			if (type.equals("联采")) {
				if (permissionIds.indexOf(13L) == -1) {
					return new ResponseEntity<>(new Result<Square>(
							"无权限创建联采类广场", null), HttpStatus.BAD_REQUEST);
				}
			}
			Square square = new Square(type, information,
					unitService.findById(unitId), userService.findById(userId));
			squareService.create(square);
			return new ResponseEntity<>(new Result<Square>("ok", null),
					HttpStatus.OK);
		}
	}

	/** 新建广场 */
	@RequestMapping(value = "/user/{userId}/unit/{unitId}", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	public ResponseEntity<?> TestCreate(
			@RequestParam("information") String information,
			@RequestParam("type") String type,
			@PathVariable("userId") long userId,
			@PathVariable("unitId") long unitId,
			@RequestParam(value = "file1", required = false) MultipartFile file1,
			@RequestParam(value = "file2", required = false) MultipartFile file2,
			@RequestParam(value = "file3", required = false) MultipartFile file3,
			@RequestParam(value = "file4", required = false) MultipartFile file4,
			@RequestParam(value = "file5", required = false) MultipartFile file5,
			@RequestParam(value = "file6", required = false) MultipartFile file6,
			@RequestParam(value = "file7", required = false) MultipartFile file7,
			@RequestParam(value = "file8", required = false) MultipartFile file8,
			@RequestParam(value = "file9", required = false) MultipartFile file9,
			HttpServletRequest request) throws IOException {
		List<MultipartFile> files = new ArrayList<MultipartFile>();
		files.add(file1);
		files.add(file2);
		files.add(file3);
		files.add(file4);
		files.add(file5);
		files.add(file6);
		files.add(file7);
		files.add(file8);
		files.add(file9);
		String information1 = "";
		String type1 = "";
		/* 解决乱码问题 */
		try {
			information1 = new String(information.getBytes("ISO-8859-1"),
					"utf-8");
			type1 = new String(type.getBytes("ISO-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			return new ResponseEntity<>(new Result<Square>("error", null),
					HttpStatus.BAD_REQUEST);
		}
		List<Long> permissionIds = userService
				.findPermissionIdsByUserId(userId);
		if (!type1.equals("员工") && !type1.equals("供应商") && !type1.equals("联采")) {
			/* 判断type格式 */
			return new ResponseEntity<>(new Result<Square>("广场类型错误", null),
					HttpStatus.BAD_REQUEST);
		} else {
			/* 判断是否有对应权限 */
			if (type1.equals("员工")) {
				if (permissionIds.indexOf(15L) == -1) {
					return new ResponseEntity<>(new Result<Square>(
							"无权限创建员工类广场", null), HttpStatus.BAD_REQUEST);
				}
			}
			if (type1.equals("供应商")) {
				if (permissionIds.indexOf(17L) == -1) {
					return new ResponseEntity<>(new Result<Square>(
							"无权限创建供应商类广场", null), HttpStatus.BAD_REQUEST);
				}
			}
			if (type1.equals("联采")) {
				if (permissionIds.indexOf(13L) == -1) {
					return new ResponseEntity<>(new Result<Square>(
							"无权限创建联采类广场", null), HttpStatus.BAD_REQUEST);
				}
			}
			Square square = new Square(type1, information1,
					unitService.findById(unitId), userService.findById(userId));
			squareService.create(square);
			long entityId = squareService.getMaxId();
			List<UploadedFile> uploadedFiles = new ArrayList<UploadedFile>();
			List<Square> squares = new ArrayList<Square>();
			try {
				uploadedFiles = utilityService.uploadFiles("square", entityId,
						files, request, "");
			} catch (Exception e) {
				e.printStackTrace();
				squareService.delete(entityId);
				return new ResponseEntity<>(new Result<Square>(
						"upload file error", squares), HttpStatus.BAD_REQUEST);
			}
			Square square1 = squareService.findById(entityId);
			square1.setPhotos(new HashSet(uploadedFiles));
			squareService.update(square1);
			squares.add(square1);
			return new ResponseEntity<>(new Result<Square>("ok", squares),
					HttpStatus.OK);
		}
	}

	/** 查询所有广场信息,根据userId和unitId查询广场,根据id查询广场 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> list(@RequestParam("userId") long userId,
			@RequestParam(value = "unitId", required = false) Long unitId,
			@RequestParam(value = "id", required = false) Long id) {
		List<Long> permissionIds = userService
				.findPermissionIdsByUserId(userId);
		List<Square> squares = new ArrayList<Square>();
		if (unitId != null || "".equals(unitId)) {
			/* 根据userId和unitId查询广场 */
			squares = squareService.findByUserIdAndUnitId(userId, unitId);
			Collections.sort(squares);
		} else if (id != null || "".equals(id)) {
			/* 根据id查询广场 */
			Square square = squareService.findById(id);
			squares.add(square);
		} else {
			squares = squareService.findByUserIdAndPermissions(userId,
					permissionIds);
			Collections.sort(squares);
		}
		return new ResponseEntity<>(new Result<Square>("ok", squares),
				HttpStatus.OK);
	}

	/** 修改广场信息 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> update(@PathVariable("id") Long id,
			@RequestBody Square square) {
		square.setId(id);
		squareService.update(square);
		return new ResponseEntity<>(new Result<Square>("ok", null),
				HttpStatus.OK);
	}

	/** 删除广场信息 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> delete(@PathVariable("id") long id) {
		squareService.delete(id);
		return new ResponseEntity<>(new Result<Square>("ok", null),
				HttpStatus.OK);
	}

	/** 根据userId查找广场信息 */
	@RequestMapping(value = "user/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> findByUserId(@PathVariable("userId") long userId) {
		List<Square> squares = squareService.findByUserId(userId);
		Collections.sort(squares);
		return new ResponseEntity<>(new Result<Square>("ok", squares),
				HttpStatus.OK);
	}

}
