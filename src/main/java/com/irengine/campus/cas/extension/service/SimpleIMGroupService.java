package com.irengine.campus.cas.extension.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.irengine.campus.cas.extension.domain.SimpleIMGroup;
import com.irengine.campus.cas.extension.repository.SimpleIMGroupRepository;

@Service
@Transactional
public class SimpleIMGroupService {

	@Autowired
	SimpleIMGroupRepository simpleIMGroupRepository;
	
	/**查找本地保存的所有组id*/
	public List<SimpleIMGroup> findAll() {
		List<SimpleIMGroup> groups=(List<SimpleIMGroup>) simpleIMGroupRepository.findAll();
		return groups;
	}
	
}
