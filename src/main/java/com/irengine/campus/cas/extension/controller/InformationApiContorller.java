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
import com.irengine.campus.cas.extension.service.UtilityService;

@RestController
@RequestMapping(value = "/news")
public class InformationApiContorller {
	@Autowired
	private InformationService informationService;
	@Autowired
	private UtilityService utilityService;
	
//	/**根据id查找所属文件*/
//    @RequestMapping(value="/findFile",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public ResponseEntity<?> findFileById(@RequestParam("id") Long id){
//    	List<UploadedFile> files=utilityService.findByEntityTypeAndEntityId("news",id);
//    	return new ResponseEntity<>(files,HttpStatus.OK);
//    }
//    /**轮播接口,返回图片*/
//    @RequestMapping(value="/slideshow",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public ResponseEntity<?> slideShow(@RequestParam("num") int num){
//    	String type="slideshow";
//    	List<Information> list=informationService.findByType(type);//按id顺序排列
//    	List<Information> resList=new ArrayList<Information>();
//    	if(num>=list.size()){
//    		resList=list;
//    	}else{
//    		for(int i=0;i<num;i++){
//    			resList.add(list.get(list.size()-(1+i)));
//    		}
//    	}
//    	return new ResponseEntity<>(resList,HttpStatus.OK);
//    }
//    
//    /**列表接口,返回图片*/
//    @RequestMapping(value="/listshow",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public ResponseEntity<?> listShow(@RequestParam("num") int num){
//    	String type="listshow";
//    	List<Information> list=informationService.findByType(type);//按id顺序排列
//    	List<Information> resList=new ArrayList<Information>();
//    	if(num>=list.size()){
//    		resList=list;
//    	}else{
//    		for(int i=0;i<num;i++){
//    			resList.add(list.get(list.size()-(1+i)));
//    		}
//    	}
//    	return new ResponseEntity<>(resList,HttpStatus.OK);
//    }
    
//	/**列出所有资讯/查询轮播图/列表图*/
//    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public ResponseEntity<?> list(){
//    	
//    	List<Information> informations=informationService.list();
//    	return new ResponseEntity<>(informations,HttpStatus.OK);
//    }
    
	/**列出所有资讯/查询轮播图/列表图*/
	/*@RequestParam(required=false)当他为false 时  使用这个注解可以不传这个参数  true时必须传*/
  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<?> listByType(@RequestParam(value="type", required=false, defaultValue="") String type ){
//	  Map<String,Object> map=new HashMap<String, Object>();
  	if(type==null||"".equals(type)){
  	  	List<Information> informations=informationService.list();
  	  	
//  	  	map.put("Results", informations);
//  	  	map.put("msg", "ok");
  	  	return new ResponseEntity<>(new Result<Information>("ok", informations),HttpStatus.OK);
  	}else{
  		List<Information> informations=informationService.findByType(type);
//  	  	map.put("Results", list);
//  	  	map.put("msg", "ok");
  		return new ResponseEntity<>(new Result<Information>("ok",informations),HttpStatus.OK);
  	}

  }
    
    /**新建一条资讯*/
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody Information information){
    	informationService.create(information);
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /**根据时间列出资讯*/
    @RequestMapping(value = "/lastUpdated", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> list(@RequestParam("queryTime") String queryTimeS){
//    	Map<String,Object> map=new HashMap<String, Object>();
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    	Date queryTime;
		try {
			queryTime = sdf.parse(queryTimeS);
		} catch (ParseException e) {
			
//			map.put("msg", "时间格式错误.");
			return new ResponseEntity<>(new Result<Information>("时间格式错误",null),HttpStatus.BAD_REQUEST);
		}
    	List<Information> informations=informationService.getLastUpdated(queryTime);
//    	map.put("Results", informations);
//    	map.put("msg", "ok");
    	return new ResponseEntity<>(new Result<Information>("ok",informations),HttpStatus.OK);
    }
    
    /**删除资讯*/
    @RequestMapping(value = "/delete", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> delete(@RequestParam("id") Long id){
    	informationService.delete(id);
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    /**修改资讯*/
    @RequestMapping(value="/update",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody Information information){
    	informationService.update(information);
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /**根据userId查找资讯*/
    @RequestMapping(value="/findByUserId",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> findByUserId(@RequestParam("userId") Long userId){
//    	Map<String,Object> map=new HashMap<String, Object>();
    	List<Information> informations=informationService.findByUserId(userId);
//    	map.put("Results", list);
//    	map.put("msg", "ok");
    	return new ResponseEntity<>(new Result<Information>("ok",informations),HttpStatus.OK);
    }
    /**根据id查找资讯*/
    @RequestMapping(value="/findById",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> findById(@RequestParam("id") Long id){
//    	Map<String,Object> map=new HashMap<String, Object>();
//    	map.put("Results", informationService.findById(id));
//    	map.put("msg", "ok");
    	List<Information> informations=new ArrayList<Information>();
    	informations.add(informationService.findById(id));
    	return new ResponseEntity<>(new Result<Information>("ok",informations),HttpStatus.OK);
    }
}









