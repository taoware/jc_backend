package com.irengine.campus.cas.extension.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.irengine.campus.cas.extension.domain.Permission;
import com.irengine.campus.cas.extension.domain.User;

public interface UserRepository extends CrudRepository<User, Long>{

	@Query("SELECT u FROM User u WHERE u.updatedTime > :queryTime")
	public List<User> findLastUpdated(@Param("queryTime") Date queryTime);
	
	@Query("SELECT u FROM User u INNER JOIN u.devices d WHERE d.machineId = :machineId")
	public User findByMachineId(@Param("machineId") String machineId);
	
	@Query("SELECT u FROM User u WHERE u.code=:code")
	public User findByCode(@Param("code") String code);
	
	@Query("SELECT u FROM User u WHERE u.mobile=:mobile")
	public User findByMobile(@Param("mobile") String mobile);
	
	@Query("select r.id from User u join u.roles r where u.id=:userId")
	public List<Long> findRoleIdsByUserId(@Param("userId") long userId);

	@Query("select p.id from Role r join r.permissions p where r.id=:roleId")
	public List<Long> findPermissionIdsByRoleId(@Param("roleId") long roleId);

	@Query("select u.location from User u where u.id=:userId")
	public String findLocationByUserId(@Param("userId") long userId);
	
	@Query("select max(u.id) from User u")
	public int findMaxId();
	
	@Query("select p from Role r join r.permissions p where r.id=:roleId")
	public List<Permission> findPermissionsByRoleId(@Param("roleId") Long roleId);
	
	@Query("select u from User u where u.im.id=:imId")
	public User findByImIds(@Param("imId") long imId);

	@Query("select u from User u where u.name=:name")
	public User findByName(@Param("name") String name);

	@Query("select u from User u where u.im.username=:imUsername")
	public User findByImUsername(@Param("imUsername")String imUsername);

	@Query("SELECT u FROM User u WHERE u.mobile like :mobile")
	public List<User> findByMobile2(@Param("mobile")String mobile);

	@Query("SELECT u FROM User u WHERE u.location like :str")
	public List<User> findByLocation(@Param("str") String str);

	/**通过姓名查找(模糊搜索)*/
	@Query("SELECT u FROM User u WHERE u.name like :name")
	public List<User> findByName2(@Param("name") String name);

	/**通过所在单位查询用户(模糊搜索)*/
	@Query("SELECT u FROM User u WHERE u.address like :address")
	public List<User> findByAddress(@Param("address") String address);
	
}
