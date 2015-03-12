package com.irengine.campus.cas.extension.controller;

import java.util.Date;
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

import com.irengine.campus.cas.extension.domain.Device;
import com.irengine.campus.cas.extension.domain.User;
import com.irengine.campus.cas.extension.service.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserApiController {
	
	@Autowired
	UserService userService;
	
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> list() {

    	List<User> users = userService.list();
    	
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody User user) {

    	userService.create(user);
    	
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/lastUpdated", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> list(@RequestParam("queryTime") Date queryTime) {

    	List<User> users = userService.getLastUpdated(queryTime);
    	
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}/devices", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> addChild(@PathVariable("id") Long id, @RequestBody Device device) {

    	userService.addDevice(id, device);
    	
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}/devices", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> removeChild(@PathVariable("id") Long id, @RequestBody Device device) {

    	userService.removeDevice(id, device);
    	
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/mobile", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> findByMachineId(@RequestParam("machineId") String machineId) {

    	User user = userService.findByMachineId(machineId);
    	
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "/code", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> findByCode(@RequestParam("code") String code) {

    	User user = userService.findByCode(code);
    	
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
