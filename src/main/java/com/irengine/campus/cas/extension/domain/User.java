package com.irengine.campus.cas.extension.domain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "jc_users")
public class User extends IdEntity {

	private boolean audit;// 是否审核通过

	private boolean enableIM;// 环信是否被注册上

	private UploadedFile avatar;// 头像

	private String notes;// 备注

	private String code;// 登录名

	private String name;// 真实姓名

	private String email;// 邮箱

	private String mobile;// 手机号

	private String gender;// 性别

	private String location;// 省市区(下拉地址)

	private String category;// 职务类别

	private String position;// 职位

	private String address;// 详细地址

	private String password;// 数据库密码(加密后)

	private String plainPassword;// 密码

	private boolean enable;// 是否被禁用

	private Date createdTime;

	private Date updatedTime;

	private Date deletedTime;

	private Set<Unit> units = new HashSet<Unit>();

	// private Set<Unit> schools=new HashSet<Unit>();

	private Set<Role> roles = new HashSet<Role>();

	private User manager;

	private Set<Device> devices = new HashSet<Device>();

	private IM im;

	@Transient
	public String getScreenName() {
		String screenName = "";
		/*取address*/
		screenName=getAddress();
		/*取固定的unitName+roleName*/
//		if (getUnits().size() > 0 && getRoles().size() > 0) {
//			Set<Unit> units = getUnits();
//			Iterator<Unit> it = units.iterator();
//			long unitId = 0;
//			Unit unit1 = null;
//			while (it.hasNext()) {
//				Unit unit = it.next();
//				if (unit.getId() > unitId) {
//					unitId = unit.getId();
//					unit1 = unit;
//				}
//			}
//			Set<Role> roles = getRoles();
//			Iterator<Role> it1 = roles.iterator();
//			long roleId = 0;
//			Role role1 = null;
//			while (it1.hasNext()) {
//				Role role = it1.next();
//				if (role.getId() > roleId) {
//					roleId = role.getId();
//					role1 = role;
//				}
//			}
//			String roleName="";
//			String roleName1=role1.getRole();
//			if("clerk".equals(roleName1)){
//				roleName="店员";
//			}else if("buyer".equals(roleName1)){
//				roleName="联采";
//			}else if("admin".equals(roleName1)){
//				roleName="管理员";
//			}else if("supplier".equals(roleName1)){
//				roleName="供应商";
//			}else if("visitor".equals(roleName1)){
//				roleName="游客";
//			}else if("king".equals(roleName1)){
//				roleName="超级管理员";
//			}else{
//				roleName="人员";
//			}
//			screenName=unit1.getName()+roleName;
//		}
		return screenName;
	}

	public boolean isAudit() {
		return audit;
	}

	public void setAudit(boolean audit) {
		this.audit = audit;
	}

	@JsonIgnore
	public boolean isEnableIM() {
		return enableIM;
	}

	public void setEnableIM(boolean enableIM) {
		this.enableIM = enableIM;
	}

	@Transient
	public Set<Permission> getPermissions() {
		Set<Permission> permissions1 = new HashSet<Permission>();
		if (roles.size() > 0) {
			for (Role role : this.roles) {
				Set<Permission> permissions = role.getPermissions();
				for (Permission permission : permissions) {
					if (!permissions1.contains(permission)) {
						permissions1.add(permission);
					}
				}
			}
		}
		return permissions1;
	}

	
	@OneToOne(cascade={CascadeType.ALL})
	@JoinColumn(name = "IMId")
	public IM getIm() {
		return im;
	}

	public void setIm(IM im) {
		this.im = im;
	}

	@OneToOne
	@JoinColumn(name = "fileId")
	public UploadedFile getAvatar() {
		return avatar;
	}

	public void setAvatar(UploadedFile avatar) {
		this.avatar = avatar;
	}

	@Column(nullable = false)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public User() {
		this.enable = true;
	}

	@Column(nullable = false)
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Column(nullable = false)
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Column(nullable = false)
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Column(unique = true, nullable = false)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(unique = true, nullable = false)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Column(nullable = false)
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@JsonIgnore
	@Column(nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Transient
	@JsonIgnore
	public String getPlainPassword() {
		return plainPassword;
	}

	@JsonProperty
	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
		this.password = User.encode(this.plainPassword);
	}

	@JsonIgnore
	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	/* 把取出的时间格式化显示? */
	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getDeletedTime() {
		return deletedTime;
	}

	public void setDeletedTime(Date deletedTime) {
		this.deletedTime = deletedTime;
	}

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, targetEntity = Unit.class, fetch = FetchType.EAGER)
	@JoinTable(name = "jc_user_unit", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "unitId"))
	public Set<Unit> getUnits() {
		return units;
	}

	public void setUnits(Set<Unit> units) {
		this.units = units;
	}

	// @Transient
	// public Set<Unit> getSchools() {
	//
	// Set<Unit> schools = new HashSet<Unit>();
	//
	// for(Unit unit : this.units) {
	// Unit school = getSchool(unit);
	// if (null != school)
	// schools.add(school);
	// }
	//
	// return schools;
	// }
	//
	// private Unit getSchool(Unit unit) {
	// Unit parent = unit.getParent();
	//
	// if (null == parent)
	// return null;
	//
	// if (parent.getCategory().equals("School"))
	// return parent;
	// else {
	// return getSchool(unit.getParent());
	// }
	// }

	@JsonIgnore
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, targetEntity = Role.class, fetch = FetchType.EAGER)
	@JoinTable(name = "jc_user_role", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "roleId"))
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "managerId")
	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}

	@JsonIgnore
	@OneToMany(targetEntity = Device.class, cascade = CascadeType.ALL, mappedBy = "user")
	public Set<Device> getDevices() {
		return devices;
	}

	public void setDevices(Set<Device> devices) {
		this.devices = devices;
	}

	/** 加密处理 */
	public static String encode(String plainPassword) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(plainPassword.getBytes());

			byte byteData[] = md.digest();

			// convert the byte to hex format method 1
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
						.substring(1));
			}

			System.out.println("Digest(in hex format):: " + sb.toString());

			// convert the byte to hex format method 2
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				String hex = Integer.toHexString(0xff & byteData[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			System.out.println("Digest(in hex format):: "
					+ hexString.toString());

			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {

		}

		return "";
	}

}
