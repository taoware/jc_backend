package com.irengine.campus.cas.extension.domain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashSet;
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
@Table(name = "cas_users")
public class User extends IdEntity {
	
	private boolean audit;//是否审核通过
	
	private boolean enableIM;//环信是否被注册上
	
	private UploadedFile avatar;//头像
	
	private String notes;//备注

	private String code;//登录名
	
	private String name;//真实姓名
	
	private String email;//邮箱
	
	private String mobile;//手机号

	private String gender;//性别
	
	private String location;//省市区(下拉地址)
	
	private String category;//职务类别
	
	private String position;//职位
	
	private String address;//详细地址
	
	private String password;//数据库密码(加密后)
	
	private String plainPassword;//密码
	
	private boolean enable;//是否被禁用
	
	private Date createdTime;
	
	private Date updatedTime;
	
	private Date deletedTime;
	
	private Set<Unit> units = new HashSet<Unit>();
	
	//private Set<Unit> schools=new HashSet<Unit>();
	
	private Set<Role> roles=new HashSet<Role>();
	
	private Set<Permission> permissions=new HashSet<Permission>();
	
	private User manager;
	
	private Set<Device> devices = new HashSet<Device>();
	
	private IM im;
	
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
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	@OneToOne
	@JoinColumn(name = "IMId")
	public IM getIm() {
		return im;
	}

	public void setIm(IM im) {
		this.im = im;
	}

	@Transient
	public UploadedFile getAvatar() {
		return avatar;
	}

	public void setAvatar(UploadedFile avatar) {
		this.avatar = avatar;
	}
	@Column(nullable=false)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public User() {
		this.enable = true;
	}
	
	@Column(nullable=false)
	public String getNotes() {
		return notes;
	}


	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Column(nullable=false)
	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}

	@Column(nullable=false)
	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}

	@Column(unique=true, nullable=false)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(nullable=false)
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

	@Column(unique=true,nullable=false)
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
	@Column(nullable=false)
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@JsonIgnore
	@Column(nullable=false)
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
	
	/*把取出的时间格式化显示?*/
	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getDeletedTime() {
		return deletedTime;
	}

	public void setDeletedTime(Date deletedTime) {
		this.deletedTime = deletedTime;
	}
	
	/*	 fetch = FetchType.EAGER:如果是EAGER，那么表示取出这条数据时，
	它关联的数据也同时取出放入内存中如果是LAZY，那么取出这条数据时，
	它关联的数据并不取出来，在同一个session中，什么时候要用，
	就什么时候取(再次访问数据库)。(延迟加载?)*/
	/*CascadeType.MERGE级联更新：若items属性修改了那么order对象
	 * 保存时同时修改items里的对象。对应EntityManager的merge方法 */
	/*CascadeType.PERSIST级联刷新：获取order对象里也同时也重新获
	 * 取最新的items时的对象。对应EntityManager的refresh(object)方
	 * 法有效。即会重新查询数据库里的最新数据  */
	
    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            targetEntity = Unit.class,
            fetch = FetchType.EAGER
        )
    @JoinTable(
            name="cas_user_unit",
            joinColumns=@JoinColumn(name="userId"),
            inverseJoinColumns=@JoinColumn(name="unitId")
        )
	public Set<Unit> getUnits() {
		return units;
	}

	public void setUnits(Set<Unit> units) {
		this.units = units;
	}
	
	
//	@Transient
//	public Set<Unit> getSchools() {
//		
//		Set<Unit> schools = new HashSet<Unit>();
//		
//		for(Unit unit : this.units) {
//			Unit school = getSchool(unit);
//			if (null != school)
//				schools.add(school);
//		}
//		
//		return schools;
//	}
//	
//	private Unit getSchool(Unit unit) {
//		Unit parent = unit.getParent();
//		
//		if (null == parent)
//			return null;
//		
//		if (parent.getCategory().equals("School"))
//			return parent;
//		else {
//			return getSchool(unit.getParent());
//		}
//	}
	
	@JsonIgnore
    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            targetEntity = Role.class,
            fetch = FetchType.EAGER
        )
    @JoinTable(
            name="cas_user_role",
            joinColumns=@JoinColumn(name="userId"),
            inverseJoinColumns=@JoinColumn(name="roleId")
        )
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@ManyToOne
	@JoinColumn(name = "managerId")
	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}

	@JsonIgnore
	@OneToMany(targetEntity=Device.class, cascade=CascadeType.ALL, mappedBy="user")
	public Set<Device> getDevices() {
		return devices;
	}

	public void setDevices(Set<Device> devices) {
		this.devices = devices;
	}
	/**加密处理*/
	public static String encode(String plainPassword) {
        MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
	        md.update(plainPassword.getBytes());
	        
	        byte byteData[] = md.digest();
	 
	        //convert the byte to hex format method 1
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < byteData.length; i++) {
	         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }
	 
	        System.out.println("Digest(in hex format):: " + sb.toString());
	 
	        //convert the byte to hex format method 2
	        StringBuffer hexString = new StringBuffer();
	    	for (int i=0;i<byteData.length;i++) {
	    		String hex=Integer.toHexString(0xff & byteData[i]);
	   	     	if(hex.length()==1) hexString.append('0');
	   	     	hexString.append(hex);
	    	}
	    	System.out.println("Digest(in hex format):: " + hexString.toString());
	    	
	    	return hexString.toString();
		} catch (NoSuchAlgorithmException e) {

		}
		
		return "";
	}
}
