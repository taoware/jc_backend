package com.irengine.campus.cas.extension.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    	Unit unit = unitService.getRoot();
    	
        return new ResponseEntity<>(unit, HttpStatus.OK);
    }

}
