package com.irengine.campus.cas.extension.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.irengine.campus.cas.extension.domain.BaseUser;
import com.irengine.campus.cas.extension.repository.BaseUserRepository;

@Service
@Transactional
public class BaseUserService {
	
	@Autowired
	BaseUserRepository baseUserRepository;

	public BaseUser findByNameAndMobile(String name, String mobile) {
		
		BaseUser user=baseUserRepository.findByNameAndMobile(name,mobile);
		return user;
	}

}
