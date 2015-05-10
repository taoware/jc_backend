package com.irengine.campus.cas.extension.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import com.irengine.campus.cas.extension.domain.Store;
import com.irengine.campus.cas.extension.domain.UploadedFile;
import com.irengine.campus.cas.extension.service.StoreService;
import com.irengine.campus.cas.extension.service.UtilityService;

@RestController
@RequestMapping(value = "/store")
public class StoreApiController {
	@Autowired
	StoreService storeService;
	@Autowired
	UtilityService utilityService;
	
	@RequestMapping(value="/{id}/send")
	@ResponseBody
	public void sendRedirect(@PathVariable("id") Long id,HttpServletResponse response) throws ServletException, IOException{
		Store store=storeService.findById(id);
		String storeName=store.getStoreName();
		String address=store.getAddress();
		String summary=store.getSummary();
		String phone=store.getPhone();
		String introduction=store.getIntroduction();
		String photoUrl="";
		if(store.getPhoto()!=null){
			photoUrl=store.getPhoto().getUrl();
		}
		String url="http://vps1.taoware.com:8080/jc/html5/shop_page/shop.html?storeName="+storeName+
				"&address="+address+"&summary="+summary+"&phone="+phone+"&introduction="
				+introduction+"&photoUrl="+photoUrl;
		System.out.println(url);
		String str = new String(url.getBytes("utf-8"),"ISO-8859-1");
		response.sendRedirect(str);
	}
	
	/** 添加门店(参数store) */
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(	@RequestParam("storeName") String storeName1,
			@RequestParam("summary") String summary1,
			@RequestParam("address") String address1,
			@RequestParam("province") String province1,
			@RequestParam("phone") String phone1,
			@RequestParam("introduction") String introduction1,
			@RequestParam("type") String type1,
			@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request) {
		String storeName="";
		String summary="";
		String address="";
		String province="";
		String phone="";
		String introduction="";
		String type="";
		try {
			storeName=new String(storeName1.getBytes("ISO-8859-1"), "utf-8");
			summary=new String(summary1.getBytes("ISO-8859-1"), "utf-8");
			address=new String(address1.getBytes("ISO-8859-1"), "utf-8");
			province=new String(province1.getBytes("ISO-8859-1"), "utf-8");
			phone=new String(phone1.getBytes("ISO-8859-1"), "utf-8");
			introduction=new String(introduction1.getBytes("ISO-8859-1"), "utf-8");
			type=new String(type1.getBytes("ISO-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			return new ResponseEntity<>(new Result<Information>("error", null),
					HttpStatus.BAD_REQUEST);
		}
		Store store=new Store(storeName, summary, address, province, phone, introduction, type);
		storeService.create(store);
		long maxId = storeService.findMaxId();
		if (file != null) {
			List<MultipartFile> files = new ArrayList<MultipartFile>();
			files.add(file);
			List<UploadedFile> files1 = null;
			try {
				files1 = utilityService.uploadFiles("store", maxId,
						files, request,"");
			} catch (IOException e) {
				return new ResponseEntity<>(new Result<Information>(
						"upload file error", null),
						HttpStatus.BAD_REQUEST);
			}
			if (files1.size() > 0) {
				store.setPhoto(files1.get(0));
				storeService.update(store);
			}
		}
		return new ResponseEntity<>(new Result<Store>("ok", null),
				HttpStatus.OK);
	}

	/** 删除门店(参数:id) */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		storeService.delete(id);
		return new ResponseEntity<>(new Result<Store>("ok", null),
				HttpStatus.OK);
	}

	/** 修改门店(参数:store) */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> update(@PathVariable("id") Long id,@RequestBody Store store) {
		store.setId(id);
		storeService.update(store);
		return new ResponseEntity<>(new Result<Store>("ok", null),
				HttpStatus.OK);
	}

	/** 列出所有门店,按门店类型查询门店,根据id查找门店,根据省份查找门店 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> findByType(
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "province", required = false) String province) {
		List<Store> stores = new ArrayList<Store>();
		if (type != null && !"".equals(type)) {
			stores = storeService.findByType(type);
		}else if(id != null && !"".equals(id)){
			stores.add(storeService.findById(id));
		}else if(province != null && !"".equals(province)){
			stores = storeService.findByProvince(province);
		} else {
			stores = storeService.findAll();
		}
		return new ResponseEntity<>(new Result<Store>("ok", stores),
				HttpStatus.OK);
	}

}
