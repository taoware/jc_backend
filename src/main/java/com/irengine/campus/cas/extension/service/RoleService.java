package com.irengine.campus.cas.extension.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.irengine.campus.cas.extension.domain.Role;
import com.irengine.campus.cas.extension.repository.RoleRepository;

//@Transactional事务管理
@Service
@Transactional
public class RoleService {
	@Autowired
	RoleRepository rr;

	public void create(Role role) {
		rr.save(role);
	}

	public List<Role> findAll() {
		List<Role> roles =(List<Role>) rr.findAll();
		return roles;
	}

	public void update(Role role) {
		rr.save(role);
	}

	public void delete(long id) {
		rr.delete(id);
	}
}
