package com.irengine.campus.cas.extension.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.irengine.campus.cas.extension.domain.Role;
import com.irengine.campus.cas.extension.repository.RoleRepository;

@Service
@Transactional
public class RoleService {

	@Autowired
	RoleRepository roleRepository;
	
	public void save(Role role) {
		roleRepository.save(role);
	}

	public Role findByName(String role) {
		Role role1=roleRepository.findByName(role);
		return role1;
	}

	public List<Role> findAll() {
		List<Role> roles=(List<Role>) roleRepository.findAll();
		return roles;
	}

	public void delete(long id) {
		roleRepository.delete(id);
	}

}
