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
	
	public String create(User user) {
		user.setCreatedTime(DateProvider.DEFAULT.getDate());
		user.setUpdatedTime(DateProvider.DEFAULT.getDate());
		boolean jMoble=user.getMobile().matches("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		boolean j1=user.getPlainPassword().matches("[\\da-zA-Z]{6,16}");
		boolean j2=user.getPlainPassword().matches("[a-zA-Z]+[\\d]+||[\\d]+[a-zA-Z]+");
		String mes="";
		if(j1==true&&j2==true&&jMoble==true){
			userRepository.save(user);
			mes="success";
		}else{
			mes="error";
		}
		return mes;
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
	
	public User findByMobile(String mobile){
		return userRepository.findByMobile(mobile);
	}

	public String login( String mobile,String plainPassword){
		User user=userRepository.findByMobile(mobile);
		if(user!=null&&User.encode(plainPassword).equals(user.getPassword())){
			return "success";
		}else if(user==null){
			return "notExist";
		}else if(!User.encode(plainPassword).equals(user.getPassword())){
			return "Wrong";
		}else{
			return "down";
		}
	}
	/**删除user*/
	public void delete(Long id) {
		userRepository.delete(id);
	}
	
	/**修改user*/
	public String update(User user) {
		user.setUpdatedTime(DateProvider.DEFAULT.getDate());
		boolean jMoble=user.getMobile().matches("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		boolean j1=user.getPlainPassword().matches("[\\da-zA-Z]{6,16}");
		boolean j2=user.getPlainPassword().matches("[a-zA-Z]+[\\d]+||[\\d]+[a-zA-Z]+");
		String mes="";
		if(j1==true&&j2==true&&jMoble==true){
			userRepository.save(user);
			mes="success";
		}else{
			mes="error";
		}
		return mes;
	}
}







