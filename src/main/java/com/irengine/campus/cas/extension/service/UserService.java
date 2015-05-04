package com.irengine.campus.cas.extension.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.irengine.campus.cas.extension.domain.Device;
import com.irengine.campus.cas.extension.domain.Permission;
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
			addPermissions(user);
			List<UploadedFile> largeFiles = ufr.findByEntityTypeAndEntityId(
					"large", "user", user.getId());
			if (largeFiles.size() > 0) {
				largeFiles.get(0).setThumbnailUrl(largeFiles.get(0).getUrl());
				user.setAvatar(largeFiles.get(0));
			}
		}
		return users;
	}

	private void addPermissions(User user) {
		Set<Permission> permissions=findPermissionsByUserId(user.getId());
		user.setPermissions(permissions);
	}

	public String create(User user) {
		user.setCreatedTime(DateProvider.DEFAULT.getDate());
		user.setUpdatedTime(DateProvider.DEFAULT.getDate());
		String mes = "";
		if (verifyMobile(user.getMobile())&&verifyPassword(user.getPlainPassword())) {
			userRepository.save(user);
			mes = "success";
		} else {
			mes = "error";
		}
		return mes;
	}

	public List<User> getLastUpdated(Date queryTime) {
		List<User> users=userRepository.findLastUpdated(queryTime);
		for (User user : users) {
			addPermissions(user);
			List<UploadedFile> largeFiles = ufr.findByEntityTypeAndEntityId(
					"large", "user", user.getId());
			if (largeFiles.size() > 0) {
				largeFiles.get(0).setThumbnailUrl(largeFiles.get(0).getUrl());
				user.setAvatar(largeFiles.get(0));
			}
		}
		return users;
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
		User user = userRepository.findByCode(code);
		List<UploadedFile> largeFiles = ufr.findByEntityTypeAndEntityId(
				"large", "user", user.getId());
		if (largeFiles.size() > 0) {
			largeFiles.get(0).setThumbnailUrl(largeFiles.get(0).getUrl());
			user.setAvatar(largeFiles.get(0));
		}
		return user;
	}

	public User findByMobile(String mobile) {
		User user = userRepository.findByMobile(mobile);
		if (user != null) {
			addPermissions(user);
			List<UploadedFile> largeFiles = ufr.findByEntityTypeAndEntityId(
					"large", "user", user.getId());
			if (largeFiles.size() > 0) {
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

	/**修改user中的某个属性*/
	public void update2(User user){
		user.setUpdatedTime(DateProvider.DEFAULT.getDate());
		userRepository.save(user);
	}
	
	/** 修改user(除头像信息)(做验证)*/
	public String update(User user) {
		user.setUpdatedTime(DateProvider.DEFAULT.getDate());
		String mes = "";
		if (verifyMobile(user.getMobile())&&verifyPassword(user.getPlainPassword())) {
			userRepository.save(user);
			mes = "success";
		} else {
			mes = "error";
		}
		return mes;
	}

	public String updatePassword(String plainPassword, long id) {
		User user = userRepository.findOne(id);
		if(verifyPassword(plainPassword)){
			user.setPlainPassword(plainPassword);
			userRepository.save(user);
			return "success";
		}else{
			return "error";
		}
	}

	public User findById(long id) {
		User user = userRepository.findOne(id);
		if (user != null) {
			addPermissions(user);
			List<UploadedFile> largeFiles = ufr.findByEntityTypeAndEntityId(
					"large", "user", user.getId());
			if (largeFiles.size() > 0) {
				largeFiles.get(0).setThumbnailUrl(largeFiles.get(0).getUrl());
				user.setAvatar(largeFiles.get(0));
			}
		}
		return user;
	}

	public List<User> findByIds(String[] ids) {
		List<User> users = new ArrayList<User>();
		for (String id : ids) {
			long id1 = Long.parseLong(id);
			User user = userRepository.findOne(id1);
			if (user != null) {
				addPermissions(user);
				List<UploadedFile> largeFiles = ufr
						.findByEntityTypeAndEntityId("large", "user",
								user.getId());
				if (largeFiles.size() > 0) {
					largeFiles.get(0).setThumbnailUrl(
							largeFiles.get(0).getUrl());
					user.setAvatar(largeFiles.get(0));
				}
			}
			users.add(user);
		}
		return users;
	}

	public void setRole(long userId, long roleId) {
		User user = userRepository.findOne(userId);
		Role role = roleRepository.findOne(roleId);
		user.getRoles().add(role);
	}

	public List<Long> findRoleIdsByUserId(long userId) {
		List<Long> roleIds = userRepository.findRoleIdsByUserId(userId);
		return roleIds;
	}

	public List<Long> findPermissionIdsByRoleId(Long roleId) {
		List<Long> permissionIds = userRepository
				.findPermissionIdsByRoleId(roleId);
		return permissionIds;
	}
	
	public Set<Permission> findPermissionsByUserId(long userId){
		List<Long> roleIds = findRoleIdsByUserId(userId);
		List<Permission> permissions2=new ArrayList<Permission>();
		if (roleIds.size() > 0) {
			for (Long roleId : roleIds) {
				List<Permission> permissions=findPermissionsByRoleId(roleId);
				for (Permission permission : permissions) {
					if (permissions2.indexOf(permission) == -1) {
						permissions2.add(permission);
					}
				}
			}
		}
		Set<Permission> permissions3=new HashSet<Permission>();
		for(Permission permission:permissions2){
			permissions3.add(permission);
		}
		return permissions3;
	}
	
	public List<Permission> findPermissionsByRoleId(Long roleId) {
		List<Permission> permissions=userRepository.findPermissionsByRoleId(roleId);
		return permissions;
	}

	public List<Long> findPermissionIdsByUserId(long userId) {
		List<Long> roleIds = findRoleIdsByUserId(userId);
		List<Long> permissionIds2 = new ArrayList<Long>();
		if (roleIds.size() > 0) {
			for (Long roleId : roleIds) {
				List<Long> permissionIds = findPermissionIdsByRoleId(roleId);
				for (Long permissionId : permissionIds) {
					if (permissionIds2.indexOf(permissionId) == -1) {
						permissionIds2.add(permissionId);
					}
				}
			}
		}
		return permissionIds2;
	}

	public String findLocationByUserId(long userId) {
		return userRepository.findLocationByUserId(userId);
	}

	public long findNextId() {
		return userRepository.findMaxId()+1;
	}

	private boolean verifyPassword(String plainPassword){
		boolean j1 = plainPassword.matches("[\\da-zA-Z]{6,16}");
		boolean j2 = plainPassword.matches(
				"[a-zA-Z]+[\\d]+||[\\d]+[a-zA-Z]+");
		if(j1==true&&j2==true){
			return true;
		}else{
			return false;
		}
	}
	
	private boolean verifyMobile(String mobile){
		boolean jMobile = mobile.matches(
				"^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		return jMobile;
	}

	public List<User> findByImIds(String[] ids) {
		List<User> users = new ArrayList<User>();
		for (String id : ids) {
			long id1 = Long.parseLong(id);
			User user = userRepository.findByImIds(id1);
			if (user != null) {
				addPermissions(user);
				List<UploadedFile> largeFiles = ufr
						.findByEntityTypeAndEntityId("large", "user",
								user.getId());
				if (largeFiles.size() > 0) {
					largeFiles.get(0).setThumbnailUrl(
							largeFiles.get(0).getUrl());
					user.setAvatar(largeFiles.get(0));
				}
			}
			users.add(user);
		}
		return users;
	}

	public User findByName(String name) {
		User user=userRepository.findByName(name);
		if (user != null) {
			addPermissions(user);
			List<UploadedFile> largeFiles = ufr
					.findByEntityTypeAndEntityId("large", "user",
							user.getId());
			if (largeFiles.size() > 0) {
				largeFiles.get(0).setThumbnailUrl(
						largeFiles.get(0).getUrl());
				user.setAvatar(largeFiles.get(0));
			}
		}
		return user;
	}

	public void setInfo(User user) {
		if (user != null) {
			addPermissions(user);
			List<UploadedFile> largeFiles = ufr
					.findByEntityTypeAndEntityId("large", "user",
							user.getId());
			if (largeFiles.size() > 0) {
				largeFiles.get(0).setThumbnailUrl(
						largeFiles.get(0).getUrl());
				user.setAvatar(largeFiles.get(0));
			}
		}
	}

	public List<User> findByImUsernames(String imUsernames) {
		String[] strs = imUsernames.split(",");
		List<User> users=new ArrayList<User>();
		for(String imUsername:strs){
			User user=userRepository.findByImUsername(imUsername);
			if(user!=null){
				users.add(user);
			}
		}
		return users;
	}
}
