package com.irengine.campus.cas.extension.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.irengine.campus.cas.extension.domain.Unit;
import com.irengine.campus.cas.extension.repository.UnitRepository;

@Service
@Transactional
public class UnitService {

	@Autowired
	UnitRepository unitRepository;
	
	public Unit getRoot() {
		
		return unitRepository.findOne(1L);
	}
	
}
