package com.irengine.campus.cas.extension.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.irengine.campus.cas.extension.domain.Result;
import com.irengine.campus.cas.extension.domain.Store;
import com.irengine.campus.cas.extension.domain.StoresInProvince;
import com.irengine.campus.cas.extension.service.StoreService;

@RestController
@RequestMapping(value = "/store")
public class StoreApiController {
	@Autowired
	StoreService storeService;
	/**添加门店(参数store)*/
	@RequestMapping(method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody Store store){
		storeService.create(store);
		return new ResponseEntity<>(new Result<Store>("ok",null),HttpStatus.OK);
	}
	
	/**删除门店(参数:id)*/
	@RequestMapping(value="/delete",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> delete(@RequestParam("id") Long id){
		storeService.delete(id);
		return new ResponseEntity<>(new Result<Store>("ok",null),HttpStatus.OK);
	}
	 
	/**修改门店(参数:store)*/
	@RequestMapping(value="/update",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody Store store){
		storeService.update(store);
		return new ResponseEntity<>(new Result<Store>("ok",null),HttpStatus.OK);
	}
	
	/**列出所有门店/查询轮播图/列表图*/
	@RequestMapping(method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> findByType(@RequestParam(value="type",required=false,defaultValue="") String type){
		List<Store> stores=new ArrayList<Store>();
		if(type!=null&&!"".equals(type)){
			stores=storeService.findByType(type);
		}else{
			stores=storeService.findAll();
		}
		return new ResponseEntity<>(new Result<Store>("ok",stores),HttpStatus.OK);
	}
	
//	/**列出所有资讯/查询轮播图/列表图*/(按省份排列)
//	@RequestMapping(method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
//	@ResponseBody
//	public ResponseEntity<?> findByType(@RequestParam(value="type", required=false, defaultValue="") String type){
//		List<Store> stores=null;
//		if(type!=null&&!"".equals(type)){
//			stores=storeService.findByType(type);
//		}else{
//			stores=storeService.findAll();
//		}
//		Result<StoresInProvince> result=new Result<StoresInProvince>();
//		List<StoresInProvince> sips=new ArrayList<StoresInProvince>();
//		
//		List<String> provinces=new ArrayList<String>();
//		for(Store store:stores){
//			String province=store.getProvince();
//			if(provinces.indexOf(province)==-1){
//				provinces.add(province);
//			}
//		}
//		for(String province:provinces){
//			List<Store> storesP=null;
//			if(type!=null&&!"".equals(type)){
//				storesP=storeService.findByProvinceAndType(province,type);
//			}else{
//				storesP=storeService.findByProvince(province);
//			}
//			StoresInProvince sip=new StoresInProvince(province,storesP);
//			sips.add(sip);
//		}
//		result.setMsg("ok");
//		result.setResults(sips);
//		return new ResponseEntity<>(result,HttpStatus.OK);
//	}
	
	/**根据id查找门店*/
	@RequestMapping(value="/id/{id}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> findById(@PathVariable("id") long id){
//		Map<String,Object> map=new HashMap<String, Object>();
//		map.put("Results", storeService.findById(id));
//		map.put("msg", "ok");
		List<Store> stores=new ArrayList<Store>();
		stores.add(storeService.findById(id));
		return new ResponseEntity<>(new Result<Store>("ok",stores),HttpStatus.OK);
	}
	/**根据province查找对应门店信息*/
	@RequestMapping(value="/province",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> findByProvince(@RequestParam("province") String province){
		List<Store> stores=storeService.findByProvince(province);
		return new ResponseEntity<>(new Result<Store>("ok",stores),HttpStatus.OK);
	}
}






