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

	private String code;
	
	private String name;

	private String email;
	
	private String mobile;

	private String gender;
	
	private String position;
	
	private String password;
	
	private String plainPassword;
	
	private boolean enable;
	
	private Date createdTime;
	
	private Date updatedTime;
	
	private Date deletedTime;
	
	private Set<Unit> units = new HashSet<Unit>();
	
//	private Set<Unit> schools = new HashSet<Unit>();
	
	private User manager;

	private Set<Device> devices = new HashSet<Device>();

	public User() {
		this.enable = true;
	}

	@Column(unique=true, nullable=false)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

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

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getDeletedTime() {
		return deletedTime;
	}

	public void setDeletedTime(Date deletedTime) {
		this.deletedTime = deletedTime;
	}

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

	@Transient
	public Set<Unit> getSchools() {
		
		Set<Unit> schools = new HashSet<Unit>();
		
		for(Unit unit : this.units) {
			Unit school = getSchool(unit);
			if (null != school)
				schools.add(school);
		}
		
		return schools;
	}
	
	private Unit getSchool(Unit unit) {
		Unit parent = unit.getParent();
		
		if (null == parent)
			return null;
		
		if (parent.getCategory().equals("School"))
			return parent;
		else {
			return getSchool(unit.getParent());
		}
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
