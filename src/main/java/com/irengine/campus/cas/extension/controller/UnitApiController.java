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

import com.irengine.campus.cas.extension.domain.Unit;
import com.irengine.campus.cas.extension.service.UnitService;

@RestController
@RequestMapping(value = "/units")
public class UnitApiController {
	
	@Autowired
	UnitService unitService;
	
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> list() {

    	List<Unit> units = unitService.list();
    	
        return new ResponseEntity<>(units, HttpStatus.OK);
    }
    //method = RequestMethod.PUT?
    //设置最上级unit
    @RequestMapping(value = "/root", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> createRoot(@RequestBody Unit root) {

    	unitService.setRoot(root);
    	
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/root", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getRoot() {

    	Unit unit = unitService.getRoot();
    	
        return new ResponseEntity<>(unit, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}/children", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> addChild(@PathVariable("id") Long id, @RequestBody Unit child) {

    	unitService.addChild(id, child);
    	
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}/children", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> removeChild(@PathVariable("id") Long id, @RequestBody Unit child) {

    	unitService.removeChild(id, child);
    	
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}/children", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> listChildren(@PathVariable("id") Long id) {

    	List<Unit> children = unitService.listChildren(id);
    	
        return new ResponseEntity<>(children, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/ancestor", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> listAncestor(@PathVariable("id") Long id) {

    	List<Unit> ancestor = unitService.listAncestor(id);
    	
        return new ResponseEntity<>(ancestor, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}/user/{userId}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> addUser(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {

    	unitService.addUser(id, userId);
    	
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}/user/{userId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<?> removeUser(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {

    	unitService.removeUser(id, userId);
    	
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
