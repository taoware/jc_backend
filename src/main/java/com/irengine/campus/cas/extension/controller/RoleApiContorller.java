package com.irengine.campus.cas.extension.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.irengine.campus.cas.extension.domain.Result;
import com.irengine.campus.cas.extension.domain.Role;
import com.irengine.campus.cas.extension.service.RoleService;

@RestController
@RequestMapping(value = "/role")
public class RoleApiContorller {
	
	@Autowired
	RoleService rs;
	
	/** 创建角色 */
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody Role role){
		rs.create(role);
		return new ResponseEntity<>(new Result<Role>("ok",null),HttpStatus.OK);
	}
	
	/**查询角色权限*/
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> findAll(){
		List<Role> roles=rs.findAll();
		return new ResponseEntity<>(new Result<Role>("ok",roles),HttpStatus.OK);
	}
	
	/**修改角色权限*/
	@RequestMapping(value="/update",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody Role role){
		rs.update(role);
		return new ResponseEntity<>(new Result<Role>("ok",null),HttpStatus.OK);
	}
	
	/**删除角色*/
	@RequestMapping(value="/delete/{id}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> delete(@PathVariable("id") long id){
		rs.delete(id);
		return new ResponseEntity<>(new Result<Role>("ok",null),HttpStatus.OK);
	}
	
}



