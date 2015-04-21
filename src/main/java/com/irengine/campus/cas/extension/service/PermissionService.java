package com.irengine.campus.cas.extension.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.irengine.campus.cas.extension.domain.Permission;
import com.irengine.campus.cas.extension.repository.PermissionRepository;

@Service
@Transactional
public class PermissionService {
	
	@Autowired
	PermissionRepository permissionRepository;

	public Permission findById(long id) {
		Permission permission=permissionRepository.findOne(id);
		return permission;
	}
}
