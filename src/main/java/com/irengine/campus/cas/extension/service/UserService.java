package com.irengine.campus.cas.extension.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.irengine.campus.cas.extension.domain.Device;
import com.irengine.campus.cas.extension.domain.Role;
import com.irengine.campus.cas.extension.domain.UploadedFile;
import com.irengine.campus.cas.extension.domain.User;
import com.irengine.campus.cas.extension.repository.RoleRepository;
import com.irengine.campus.cas.extension.repository.UploadedFileRepository;
import com.irengine.campus.cas.extension.repository.UserRepository;
import com.irengine.commons.DateProvider;

@Service
@Transactional
public class UserService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	UploadedFileRepository ufr;
	@Autowired
	RoleRepository roleRepository;

	public List<User> list() {
		List<User> users = (List<User>) userRepository.findAll();
		for (User user : users) {
			List<UploadedFile> largeFiles = ufr.findByEntityTypeAndEntityId(
					"large", "user", user.getId());
			if(largeFiles.size()>0){
				largeFiles.get(0).setThumbnailUrl(largeFiles.get(0).getUrl());
				user.setAvatar(largeFiles.get(0));
			}
		}
		return users;
	}

	public String create(User user) {
		user.setCreatedTime(DateProvider.DEFAULT.getDate());
		user.setUpdatedTime(DateProvider.DEFAULT.getDate());
		boolean jMoble = user.getMobile().matches(
				"^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		boolean j1 = user.getPlainPassword().matches("[\\da-zA-Z]{6,16}");
		boolean j2 = user.getPlainPassword().matches(
				"[a-zA-Z]+[\\d]+||[\\d]+[a-zA-Z]+");
		String mes = "";
		if (j1 == true && j2 == true && jMoble == true) {
			userRepository.save(user);
			mes = "success";
		} else {
			mes = "error";
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
		User user=userRepository.findByCode(code);
		List<UploadedFile> largeFiles = ufr.findByEntityTypeAndEntityId(
				"large", "user", user.getId());
		if(largeFiles.size()>0){
			largeFiles.get(0).setThumbnailUrl(largeFiles.get(0).getUrl());
			user.setAvatar(largeFiles.get(0));
		}
		return user;
	}

	public User findByMobile(String mobile) {
		User user=userRepository.findByMobile(mobile);
		if(user!=null){
			List<UploadedFile> largeFiles = ufr.findByEntityTypeAndEntityId(
					"large", "user", user.getId());
			if(largeFiles.size()>0){
				largeFiles.get(0).setThumbnailUrl(largeFiles.get(0).getUrl());
				user.setAvatar(largeFiles.get(0));
			}
		}
		return user;
	}

	public String login(String mobile, String plainPassword) {
		User user = userRepository.findByMobile(mobile);
		if (user != null
				&& User.encode(plainPassword).equals(user.getPassword())) {
			return "success";
		} else if (user == null) {
			return "notExist";
		} else if (!User.encode(plainPassword).equals(user.getPassword())) {
			return "Wrong";
		} else {
			return "down";
		}
	}

	/** 删除user */
	public void delete(Long id) {
		userRepository.delete(id);
	}

	/** 修改user(除头像信息) */
	public String update(User user) {
		user.setUpdatedTime(DateProvider.DEFAULT.getDate());
		boolean jMoble = user.getMobile().matches(
				"^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		boolean j1 = user.getPlainPassword().matches("[\\da-zA-Z]{6,16}");
		boolean j2 = user.getPlainPassword().matches(
				"[a-zA-Z]+[\\d]+||[\\d]+[a-zA-Z]+");
		String mes = "";
		if (j1 == true && j2 == true && jMoble == true) {
			userRepository.save(user);
			mes = "success";
		} else {
			mes = "error";
		}
		return mes;
	}

	public void updatePassword(String password, long id) {
		User user=userRepository.findOne(id);
		user.setPlainPassword(password);
		userRepository.save(user);
	}

	public User findById(long id) {
		User user=userRepository.findOne(id);
		if(user!=null){
			List<UploadedFile> largeFiles = ufr.findByEntityTypeAndEntityId(
					"large", "user", user.getId());
			if(largeFiles.size()>0){
				largeFiles.get(0).setThumbnailUrl(largeFiles.get(0).getUrl());
				user.setAvatar(largeFiles.get(0));
			}
		}
		return user;
	}

	public List<User> findByIds(String[] ids) {
		List<User>users=new ArrayList<User>();
		for(String id:ids){
			long id1=Long.parseLong(id);
			User user=userRepository.findOne(id1);
			if(user!=null){
				List<UploadedFile> largeFiles = ufr.findByEntityTypeAndEntityId(
						"large", "user", user.getId());
				if(largeFiles.size()>0){
					largeFiles.get(0).setThumbnailUrl(largeFiles.get(0).getUrl());
					user.setAvatar(largeFiles.get(0));
				}
			}
			users.add(user);
		}
		return users;
	}

	public void setRole(long userId, long roleId) {
		User user=userRepository.findOne(userId);
		Role role=roleRepository.findOne(roleId);
		user.getRoles().add(role);
	}

}


