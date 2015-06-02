package com.irengine.campus.cas.extension.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.irengine.campus.cas.extension.domain.BaseUser;
import com.irengine.campus.cas.extension.domain.Device;
import com.irengine.campus.cas.extension.domain.Permission;
import com.irengine.campus.cas.extension.domain.Role;
import com.irengine.campus.cas.extension.domain.Unit;
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
	@Autowired
	BaseUserService baseUserService;
	@Autowired
	RoleService roleService;
	@Autowired
	UnitService unitService;

	public List<User> list() {
		List<User> users = (List<User>) userRepository.findAll();
		return users;
	}

	public String create(User user) {
		user.setCreatedTime(DateProvider.DEFAULT.getDate());
		user.setUpdatedTime(DateProvider.DEFAULT.getDate());
		String mes = "";
		if (verifyMobile(user.getMobile())
				&& verifyPassword(user.getPlainPassword())) {
			userRepository.save(user);
			mes = "success";
		} else {
			mes = "error";
		}
		return mes;
	}

	public List<User> getLastUpdated(Date queryTime) {
		List<User> users = userRepository.findLastUpdated(queryTime);
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
		return user;
	}

	public User findByMobile(String mobile) {
		User user = userRepository.findByMobile(mobile);
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

	/** 修改user中的某个属性 */
	public void update2(User user) {
		user.setUpdatedTime(DateProvider.DEFAULT.getDate());
		userRepository.save(user);
	}

	/** 修改user(除头像信息)(给手机号和密码做验证) */
	public String update(User user) {
		user.setUpdatedTime(DateProvider.DEFAULT.getDate());
		String mes = "";
		if (verifyMobile(user.getMobile())
				&& verifyPassword(user.getPlainPassword())) {
			userRepository.save(user);
			mes = "success";
		} else {
			mes = "error";
		}
		return mes;
	}

	/**修改密码(密码格式做验证)*/
	public String updatePassword(String plainPassword, long id) {
		User user = userRepository.findOne(id);
		if (verifyPassword(plainPassword)) {
			user.setPlainPassword(plainPassword);
			userRepository.save(user);
			return "success";
		} else {
			return "error";
		}
	}

	public User findById(long id) {
		User user = userRepository.findOne(id);
		return user;
	}

	public List<User> findByIds(String[] ids) {
		List<User> users = new ArrayList<User>();
		for (String id : ids) {
			long id1 = Long.parseLong(id);
			User user = userRepository.findOne(id1);
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

	public Set<Permission> findPermissionsByUserId(long userId) {
		List<Long> roleIds = findRoleIdsByUserId(userId);
		List<Permission> permissions2 = new ArrayList<Permission>();
		if (roleIds.size() > 0) {
			for (Long roleId : roleIds) {
				List<Permission> permissions = findPermissionsByRoleId(roleId);
				for (Permission permission : permissions) {
					if (permissions2.indexOf(permission) == -1) {
						permissions2.add(permission);
					}
				}
			}
		}
		Set<Permission> permissions3 = new HashSet<Permission>();
		for (Permission permission : permissions2) {
			permissions3.add(permission);
		}
		return permissions3;
	}

	public List<Permission> findPermissionsByRoleId(Long roleId) {
		List<Permission> permissions = userRepository
				.findPermissionsByRoleId(roleId);
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
		return userRepository.findMaxId() + 1;
	}

	private boolean verifyPassword(String plainPassword) {
		boolean j1 = plainPassword.matches("[\\da-zA-Z]{6,16}");
		boolean j2 = plainPassword.matches("[a-zA-Z]+[\\d]+[\\da-zA-Z]*||[\\d]+[a-zA-Z]+[\\da-zA-Z]*");
		//boolean j2=true;
		if (j1 == true && j2 == true) {
			return true;
		} else {
			return false;
		}
	}

	private boolean verifyMobile(String mobile) {
		boolean jMobile = mobile
				.matches("^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
		return jMobile;
	}

	public List<User> findByImIds(String[] ids) {
		List<User> users = new ArrayList<User>();
		for (String id : ids) {
			long id1 = Long.parseLong(id);
			User user = userRepository.findByImIds(id1);
			users.add(user);
		}
		return users;
	}

	public List<User> findByName(String name) {
		/*精确搜索*/
		//User user = userRepository.findByName(name);
		/*模糊搜索*/
		List<User> users=new ArrayList<User>();
			users=userRepository.findByName2("%"+name+"%");
		return users;
	}

	/* 根据环信username查找对应用户* */
	public List<User> findByImUsernames(String imUsernames) {
		String[] strs = imUsernames.split(",");
		List<User> users = new ArrayList<User>();
		for (String imUsername : strs) {
			User user = userRepository.findByImUsername(imUsername.trim());
			if (user != null) {
				users.add(user);
			}
		}
		return users;
	}

	public List<User> findByMobile2(String mobile) {
		List<User> users = userRepository.findByMobile2(mobile + "%");
		return users;
	}

	/* 在表jc_base_user表中匹配用户,自动配置角色,并自动匹配unit* */
	public void matchUser(User user) {
		if (user != null) {
			/* 匹配user的mobile和name,找到则配置BaseUser表中对应的unit和role,找不到则配置角色5并不配置unit */
			String name=user.getName();
			String mobile=user.getMobile();
			BaseUser baseUser=baseUserService.findByNameAndMobile(name,mobile);
			if(baseUser!=null){
				/*在BaseUser中找到对应用户信息,自动配置role和unit*/
				Long roleId=baseUser.getRoleId();
				Long unitId=baseUser.getUnitId();
				if(roleId!=null){
					/*配置role*/
					Role role=roleService.findById(roleId);
					if(role!=null){
						user.getRoles().add(role);
					}
					/*配置unit*/
					Unit unit=unitService.findById(unitId);
					if(unit!=null){
						user.getUnits().add(unit);
					}
				}
			}else{
				/*没找到,配置角色5*/
				Role role=roleService.findByName("visitor");
				user.getRoles().add(role);
			}
			update2(user);
		}
	}

	public void matchUser2(User user) {
		Role role=roleService.findByName("king");
		Unit unit=unitService.findById(23L);
		user.getRoles().add(role);
		user.getUnits().add(unit);
		update2(user);
	}

	public boolean testVerify(String mobile, String password) {
		if(verifyMobile(mobile)&&verifyPassword(password)){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 根据地址查询用户(模糊查询)
	 * @param location
	 * @return
	 */
	public List<User> findbyLocation(String location) {
		String str=location.replaceAll("", "%");
		List<User> users=userRepository.findByLocation(str);
		return users;
	}

	/**
	 * 通过所在单位查找用户(模糊查询)
	 * @param address 地址
	 * @return
	 */
	public List<User> findByAddress(String address) {
		List<User> users=new ArrayList<User>();
		address=address.replaceAll("", "%");
		System.out.println("--------------address:"+address);
		users=userRepository.findByAddress(address);
		return users;
	}
}
