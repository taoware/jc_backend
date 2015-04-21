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

import com.irengine.campus.cas.extension.domain.Message;
import com.irengine.campus.cas.extension.domain.Result;
import com.irengine.campus.cas.extension.domain.Square;
import com.irengine.campus.cas.extension.service.SquareService;

@RestController
@RequestMapping(value = "/square")
public class SquareApiController {
	@Autowired
	SquareService squareService;

	/** 创建广场 */
	@RequestMapping(value = "/{userId}/{unitId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody Square square,
			@PathVariable("userId") long userId,
			@PathVariable("unitId") long unitId) {
		square.setUserId(userId);
		square.setUnitId(unitId);
		long nextId=squareService.getNextId();
		squareService.create(square);
		return new ResponseEntity<>(new Message(""+nextId),
				HttpStatus.OK);
	}

	/** 查询所有广场信息 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> list() {
		List<Square> squares = squareService.findAll();
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
		List<Square> squares = squareService.findById(id);
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
