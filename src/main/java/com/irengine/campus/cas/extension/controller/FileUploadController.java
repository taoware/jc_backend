package com.irengine.campus.cas.extension.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.irengine.campus.cas.extension.domain.Result;
import com.irengine.campus.cas.extension.domain.UploadedFile;
import com.irengine.campus.cas.extension.repository.UploadedFileRepository;
import com.irengine.campus.cas.extension.service.UtilityService;

@Controller
public class FileUploadController {
	//记录器
	private static Logger logger = LoggerFactory.getLogger(FileUploadController.class);
	
	@Autowired
	UtilityService utilityService;
    @RequestMapping(value="/uploadList",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> list() {
    	return new ResponseEntity<>(utilityService.findAll(),HttpStatus.OK);
    }
    @RequestMapping(value="/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }
    /**上传文件*/
    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request){
    	
    	//logger.info是可以输出log文件中
		//getRealPath取绝对路径
		//比如，有个servlet 叫 UploadServlet，它部署在tomcat 下面以后的绝对路径如下：
		//"C:\Program Files\apache-tomcat-8.0.3\webapps\UploadServlet"
		//那么，
		//ServletContext.getRealPath("/") 返回 "C:\Program Files\apache-tomcat-8.0.3\webapps\UploadServlet"
    	logger.info(request.getSession().getServletContext().getRealPath("/"));

    	//name??url??
    	String name = request.getSession().getServletContext().getRealPath("/") + "/uploaded/" + file.getOriginalFilename();
    	/*图片存入服务器uploaded文件夹内*/
    	if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(name)));
                stream.write(bytes);
                stream.close();
                return "You successfully uploaded " + name + " into " + name + " !";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }
    //DIRECTORY_UPLOAD上传文件目录?
    private static String DIRECTORY_UPLOAD = "/uploaded/";
    //得到图片文件夹url?
    private static String getWebDirectory(HttpServletRequest request) {
    	return request.getSession().getServletContext().getRealPath(DIRECTORY_UPLOAD);
    }
    
    /*
     * upload file for entity which given entity type and entity id
     * sample directory: /upload/entityType/entityId/timestamp_file
     */
    /**上传图片指定上属(按图片所属类决定图片放在哪个文件夹里)*/
    @RequestMapping(value="/upload/{type}/{eType}/{eId}", method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> createUploadedFile(@PathVariable("type") String type,@PathVariable("eType") String entityType, @PathVariable("eId") Long entityId,
    		@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
    	
    	String uploadDirectoryName = getWebDirectory(request);
    	logger.info(uploadDirectoryName);
    	//fileUtils
    	File uploadDirectory = new File(uploadDirectoryName);
    	//创建目录?
    	FileUtils.forceMkdir(uploadDirectory);
    	
		//改名字?
    	String uploadFileName = String.format("%d%s", System.currentTimeMillis(), file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
    	//决定文件放入哪个文件夹(根据该文件所属的类)
    	File uploadFile = new File(StringUtils.join(new Object[]{ uploadDirectoryName, entityType, entityId, uploadFileName }, "/"));
    	
    	if (!file.isEmpty()) { 
    		FileUtils.writeByteArrayToFile(uploadFile, file.getBytes());
    		UploadedFile uploadedFile = utilityService.createFile(type,entityType, entityId, uploadFileName, file.getSize());
    		return new ResponseEntity<>(uploadedFile, HttpStatus.CREATED);
    	}
    	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	
    }
    /**输入查找信息查找出指定图片*/
    @RequestMapping(value="/upload/{type}/{eType}/{eId}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> listUploadedFiles(@PathVariable("type") String type,@PathVariable("eType") String entityType, @PathVariable("eId") long entityId) {
    	
    	List<UploadedFile> files = utilityService.listFiles(type,entityType, entityId);
    	return new ResponseEntity<>(files, HttpStatus.OK);

    }
    
    /**删除*/
    @RequestMapping(value="/upload/{eType}/{eId}/{fId}", method=RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<?> removeUploadedFiles(@PathVariable("eType") String entityType, @PathVariable("eId") String entityId,
    		@PathVariable("fId") long fileId, HttpServletRequest request) {

    	UploadedFile file = utilityService.getFile(fileId);

    	String uploadDirectoryName = getWebDirectory(request);
    	File uploadFile = new File(StringUtils.join(new Object[]{ uploadDirectoryName, file.getEntityType(), file.getEntityId(), file.getName() }, "/"));
    	FileUtils.deleteQuietly(uploadFile);
    	
    	utilityService.removeFile(fileId);
    	
    	return new ResponseEntity<>(HttpStatus.OK);

    }
    @RequestMapping(value="/upload/list",method=RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> findAll(){
    	List<UploadedFile> files=utilityService.findAll();
    	return new ResponseEntity<>(new Result<UploadedFile>("ok",files),HttpStatus.OK);
    }

}
