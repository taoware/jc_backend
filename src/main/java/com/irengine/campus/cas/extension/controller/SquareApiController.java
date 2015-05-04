package com.irengine.campus.cas.extension.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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

import com.irengine.campus.cas.extension.domain.Result;
import com.irengine.campus.cas.extension.domain.Square;
import com.irengine.campus.cas.extension.domain.UploadedFile;
import com.irengine.campus.cas.extension.domain.User;
import com.irengine.campus.cas.extension.service.SquareService;
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
	
	/** 测试使用MULTIPART_FORM_DATA_VALUE创建广场,整合图片上传和创建广场在一个接口 
	 * @throws IOException */
	@RequestMapping(value = "/{userId}/{unitId}", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
		Square square = new Square(unitId, userId, type1, information1);
		squareService.create(square);
		long entityId=squareService.getMaxId();
		uploadFiles("square",entityId,files,request);
		List<Square> squares = new ArrayList<Square>();
		Square square1 = squareService.findById(entityId);
		squares.add(square1);
		return new ResponseEntity<>(new Result<Square>("ok", squares),
				HttpStatus.OK);
		}
	}

	private static String DIRECTORY_UPLOAD = "/uploaded/";
	
	private static String getWebDirectory(HttpServletRequest request) {
		return request.getSession().getServletContext()
				.getRealPath(DIRECTORY_UPLOAD);
	}
	
	private void uploadFiles(String entityType, long entityId,
			List<MultipartFile> files,HttpServletRequest request) throws IOException {
		List<UploadedFile> uploadedFiles = new ArrayList<UploadedFile>();
		String uploadDirectoryName = getWebDirectory(request);
		/*删除该目录下的文件*/
		File deleteFile = new File(StringUtils.join(new Object[] {
				uploadDirectoryName, entityType, entityId }, "/"));
			if(deleteFile.isDirectory()){
				File[] deleteFiles=deleteFile.listFiles();
				if(deleteFiles.length>0){
					for(int i=0;i<deleteFiles.length;i++){
						deleteFiles[i].delete();
					}
				}
			}
		for (MultipartFile file : files) {
			if (file == null || file.isEmpty()) {
			} else {
				File uploadDirectory = new File(uploadDirectoryName);
				FileUtils.forceMkdir(uploadDirectory);
				String uploadFileName = String
						.format("%d%s",
								System.currentTimeMillis(),
								file.getOriginalFilename().substring(
										file.getOriginalFilename().lastIndexOf(
												".")));
				File uploadFile = new File(StringUtils.join(new Object[] {
						uploadDirectoryName, entityType, entityId,
						uploadFileName }, "/"));
				// 上传文件
				FileUtils.writeByteArrayToFile(uploadFile, file.getBytes());
				UploadedFile uploadedFile = utilityService.createFile("large",
						entityType, entityId, uploadFileName, file.getSize());
				uploadedFiles.add(uploadedFile);
			}
		}
	}

	/** 根据userId和unitId查询广场 */
	@RequestMapping(value = "/{userId}/{unitId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> findByUserIdAndUnitId(
			@PathVariable("userId") long userId,
			@PathVariable("unitId") long unitId) {
		List<Square> squares = squareService.findByUserIdAndUnitId(userId,
				unitId);
		return new ResponseEntity<>(new Result<Square>("ok", squares),
				HttpStatus.OK);
	}

//	/** 创建广场 */
//	@RequestMapping(value = "/{userId}/{unitId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
//	@ResponseBody
//	public ResponseEntity<?> create(@RequestBody Square square,
//			@PathVariable("userId") long userId,
//			@PathVariable("unitId") long unitId) {
//		List<Long> permissionIds = userService
//				.findPermissionIdsByUserId(userId);
//		String type = square.getType();
//		if (!type.equals("员工") && !type.equals("供应商") && !type.equals("联采")) {
//			/* 判断type格式 */
//			return new ResponseEntity<>(new Result<Square>("广场类型错误", null),
//					HttpStatus.BAD_REQUEST);
//		} else {
//			/* 判断是否有对应权限 */
//			if (type.equals("员工")) {
//				if (permissionIds.indexOf(15L) == -1) {
//					return new ResponseEntity<>(new Result<Square>(
//							"无权限创建员工类广场", null), HttpStatus.BAD_REQUEST);
//				}
//			}
//			if (type.equals("供应商")) {
//				if (permissionIds.indexOf(17L) == -1) {
//					return new ResponseEntity<>(new Result<Square>(
//							"无权限创建供应商类广场", null), HttpStatus.BAD_REQUEST);
//				}
//			}
//			if (type.equals("联采")) {
//				if (permissionIds.indexOf(13L) == -1) {
//					return new ResponseEntity<>(new Result<Square>(
//							"无权限创建联采类广场", null), HttpStatus.BAD_REQUEST);
//				}
//			}
//			square.setUserId(userId);
//			square.setUnitId(unitId);
//			squareService.create(square);
//			long maxId = squareService.getMaxId();
//			Square square1 = squareService.findById(maxId);
//			List<Square> squares = new ArrayList<Square>();
//			squares.add(square1);
//			return new ResponseEntity<>(new Result<Square>("ok", squares),
//					HttpStatus.OK);
//		}
//	}

	/** 查询所有广场信息 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> list(@RequestParam("userId") long userId) {
		List<Long> permissionIds = userService
				.findPermissionIdsByUserId(userId);
		// String location2=userService.findLocationByUserId(userId);
		// String location=location2.substring(0,location2.indexOf("-"));
		List<Square> squares = new ArrayList<Square>();
		if (permissionIds.indexOf(14L) >= 0) {
			// List<Square>
			// squares1=squareService.findByTypeAndLocation("联采",location);
			List<Square> squares1 = squareService.findByType("联采");
			squares.addAll(squares1);
		}
		if (permissionIds.indexOf(16L) >= 0) {
			// List<Square>
			// squares1=squareService.findByTypeAndLocation("员工",location);
			List<Square> squares1 = squareService.findByType("员工");
			squares.addAll(squares1);
		}
		if (permissionIds.indexOf(18L) >= 0) {
			// List<Square>
			// squares1=squareService.findByTypeAndLocation("供应商",location);
			List<Square> squares1 = squareService.findByType("供应商");
			squares.addAll(squares1);
		}
		for(Square square:squares){
			userService.setInfo(square.getUser());
		}
		return new ResponseEntity<>(new Result<Square>("ok", squares),
				HttpStatus.OK);
	}

	/** 修改广场信息 */
	@RequestMapping(value = "/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody Square square) {
		squareService.update(square);
		return new ResponseEntity<>(new Result<Square>("ok", null),
				HttpStatus.OK);
	}

	/** 删除广场信息 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> delete(@RequestParam("id") long id) {
		squareService.delete(id);
		return new ResponseEntity<>(new Result<Square>("ok", null),
				HttpStatus.OK);
	}

	/** 根据id查找广场信息 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> findById(@PathVariable("id") long id) {
		List<Square> squares = new ArrayList<Square>();
		Square square = squareService.findById(id);
		squares.add(square);
		return new ResponseEntity<>(new Result<Square>("ok", squares),
				HttpStatus.OK);
	}

	/** 根据userId查找广场信息 */
	@RequestMapping(value = "userId/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> findByUserId(@PathVariable("userId") long userId) {
		List<Square> squares = squareService.findByUserId(userId);
		return new ResponseEntity<>(new Result<Square>("ok", squares),
				HttpStatus.OK);
	}
}
