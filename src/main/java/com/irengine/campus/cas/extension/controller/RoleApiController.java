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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.irengine.campus.cas.extension.domain.Permission;
import com.irengine.campus.cas.extension.domain.Result;
import com.irengine.campus.cas.extension.domain.Role;
import com.irengine.campus.cas.extension.service.PermissionService;
import com.irengine.campus.cas.extension.service.RoleService;

@RestController
@RequestMapping(value = "/role")
public class RoleApiController {
	
	@Autowired
	RoleService roleService;
	@Autowired
	PermissionService permissionService;
	
	/**新建角色并赋予权限*/
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestParam("pIds") String pIds,@RequestBody Role role){
		roleService.save(role);
		Role role1=roleService.findByName(role.getRole());
		String[] pId=pIds.split(",");
		for(int i=0;i<pId.length;i++){
			Permission permission=permissionService.findById(Long.parseLong(pId[i]));
			role1.getPermissions().add(permission);
		}
		roleService.save(role1);
		return new ResponseEntity<>(new Result<Role>("ok",null),
				HttpStatus.OK);
	}
	
	/**查询所有角色以及对应的权限*/
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> list(){
		List<Role> roles=roleService.findAll();
		return new ResponseEntity<>(new Result<Role>("ok",roles),HttpStatus.OK);
	}
	
	/**删除角色*/
	@RequestMapping(value="/delete/{id}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> delete(@PathVariable("id") long id){
		roleService.delete(id);
		return new ResponseEntity<>(new Result<Role>("ok",null),HttpStatus.OK);
	}
	
	/**修改角色*/
	@RequestMapping(value="/update",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> update(@RequestParam("pIds") String pIds,@RequestBody Role role){
		roleService.save(role);
		Role role1=roleService.findByName(role.getRole());
		String[] pId=pIds.split(",");
		for(int i=0;i<pId.length;i++){
			Permission permission=permissionService.findById(Long.parseLong(pId[i]));
			role1.getPermissions().add(permission);
		}
		roleService.save(role1);
		return new ResponseEntity<>(new Result<Role>("ok",null),
				HttpStatus.OK);
	}
	
}





