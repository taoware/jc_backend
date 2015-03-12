package com.irengine.campus.cas.extension.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.irengine.campus.cas.extension.domain.Device;
import com.irengine.campus.cas.extension.domain.User;
import com.irengine.campus.cas.extension.repository.UserRepository;
import com.irengine.commons.DateProvider;

@Service
@Transactional
public class UserService {

	@Autowired
	UserRepository userRepository;
		
	public List<User> list() {

		return (List<User>)userRepository.findAll();
	}
	
	public void create(User user) {
		user.setCreatedTime(DateProvider.DEFAULT.getDate());
		user.setUpdatedTime(DateProvider.DEFAULT.getDate());
		userRepository.save(user);
	}
	
	public List<User> getLastUpdated(Date queryTime) {
		
		return userRepository.findLastUpdated(queryTime);
	}
	
	public void addDevice(Long userId, Device device) {
		
		User user = userRepository.findOne(userId);
		device.setUser(user);
		user.getDevices().add(device);

		userRepository.save(user);
	}
	
	// TODO: not implemented
	public void removeDevice(Long userId, Device device) {
	}
	
	public User findByMachineId(String machineId) {
		
		return userRepository.findByMachineId(machineId);
	}

	public User findByCode(String code) {

		return userRepository.findByCode(code);
	}

}
