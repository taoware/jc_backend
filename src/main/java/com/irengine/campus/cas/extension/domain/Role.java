package com.irengine.campus.cas.extension.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**角色权限表*/
@Entity
@Table(name = "cas_role")
public class Role {
	private Long id;//id
	private String role;//角色名
	private boolean im;//IM
	private boolean cloudOffice;//云办公
	private boolean contaces;//联系人列表
	private boolean groupAvail;//组
	private boolean newsCreate;//新建资讯
	private boolean newsAvail;//资讯
	private boolean storeCreate;//新建门店
	private boolean storeAvail;//门店
	private boolean crowdCreate;//新建群
	private boolean crowdAvail;//群
	private boolean infoCreate;//新建通知
	private boolean infoAvail;//收通知
	private boolean purchasingCreate;//新建联采
	private boolean purchasingAvail;//收联采
	private boolean staffSquareCreate;//新建员工广场
	private boolean staffSquareAvail;//收员工广场
	private boolean supplierSquareCreate;//新建供应商广场
	private boolean supplierSquareAvail;//收供应商广场
	private boolean saleInfoCreate;//新建宝盒信息
	private boolean saleInfoAvail;//收供应商宝盒信息
	private boolean staffTrainCreate;//新建员工培训
	private boolean staffTrainAvail;//收员工培训
	private boolean applyForSupplier;//供应商申请
	private boolean audit;//审批
	private boolean configurationAudit;//审核配置
	
	public boolean isConfigurationAudit() {
		return configurationAudit;
	}
	public void setConfigurationAudit(boolean configurationAudit) {
		this.configurationAudit = configurationAudit;
	}
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(nullable=false,unique=true)
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean isIm() {
		return im;
	}
	public void setIm(boolean im) {
		this.im = im;
	}
	public boolean isCloudOffice() {
		return cloudOffice;
	}
	public void setCloudOffice(boolean cloudOffice) {
		this.cloudOffice = cloudOffice;
	}
	public boolean isContaces() {
		return contaces;
	}
	public void setContaces(boolean contaces) {
		this.contaces = contaces;
	}
	public boolean isGroupAvail() {
		return groupAvail;
	}
	public void setGroupAvail(boolean groupAvail) {
		this.groupAvail = groupAvail;
	}
	public boolean isNewsCreate() {
		return newsCreate;
	}
	public void setNewsCreate(boolean newsCreate) {
		this.newsCreate = newsCreate;
	}
	public boolean isNewsAvail() {
		return newsAvail;
	}
	public void setNewsAvail(boolean newsAvail) {
		this.newsAvail = newsAvail;
	}
	public boolean isStoreCreate() {
		return storeCreate;
	}
	public void setStoreCreate(boolean storeCreate) {
		this.storeCreate = storeCreate;
	}
	public boolean isStoreAvail() {
		return storeAvail;
	}
	public void setStoreAvail(boolean storeAvail) {
		this.storeAvail = storeAvail;
	}
	public boolean isCrowdCreate() {
		return crowdCreate;
	}
	public void setCrowdCreate(boolean crowdCreate) {
		this.crowdCreate = crowdCreate;
	}
	public boolean isCrowdAvail() {
		return crowdAvail;
	}
	public void setCrowdAvail(boolean crowdAvail) {
		this.crowdAvail = crowdAvail;
	}
	public boolean isInfoCreate() {
		return infoCreate;
	}
	public void setInfoCreate(boolean infoCreate) {
		this.infoCreate = infoCreate;
	}
	public boolean isInfoAvail() {
		return infoAvail;
	}
	public void setInfoAvail(boolean infoAvail) {
		this.infoAvail = infoAvail;
	}
	public boolean isPurchasingCreate() {
		return purchasingCreate;
	}
	public void setPurchasingCreate(boolean purchasingCreate) {
		this.purchasingCreate = purchasingCreate;
	}
	public boolean isPurchasingAvail() {
		return purchasingAvail;
	}
	public void setPurchasingAvail(boolean purchasingAvail) {
		this.purchasingAvail = purchasingAvail;
	}
	public boolean isStaffSquareCreate() {
		return staffSquareCreate;
	}
	public void setStaffSquareCreate(boolean staffSquareCreate) {
		this.staffSquareCreate = staffSquareCreate;
	}
	public boolean isStaffSquareAvail() {
		return staffSquareAvail;
	}
	public void setStaffSquareAvail(boolean staffSquareAvail) {
		this.staffSquareAvail = staffSquareAvail;
	}
	public boolean isSupplierSquareCreate() {
		return supplierSquareCreate;
	}
	public void setSupplierSquareCreate(boolean supplierSquareCreate) {
		this.supplierSquareCreate = supplierSquareCreate;
	}
	public boolean isSupplierSquareAvail() {
		return supplierSquareAvail;
	}
	public void setSupplierSquareAvail(boolean supplierSquareAvail) {
		this.supplierSquareAvail = supplierSquareAvail;
	}
	public boolean isSaleInfoCreate() {
		return saleInfoCreate;
	}
	public void setSaleInfoCreate(boolean saleInfoCreate) {
		this.saleInfoCreate = saleInfoCreate;
	}
	public boolean isSaleInfoAvail() {
		return saleInfoAvail;
	}
	public void setSaleInfoAvail(boolean saleInfoAvail) {
		this.saleInfoAvail = saleInfoAvail;
	}
	public boolean isStaffTrainCreate() {
		return staffTrainCreate;
	}
	public void setStaffTrainCreate(boolean staffTrainCreate) {
		this.staffTrainCreate = staffTrainCreate;
	}
	public boolean isStaffTrainAvail() {
		return staffTrainAvail;
	}
	public void setStaffTrainAvail(boolean staffTrainAvail) {
		this.staffTrainAvail = staffTrainAvail;
	}
	public boolean isApplyForSupplier() {
		return applyForSupplier;
	}
	public void setApplyForSupplier(boolean applyForSupplier) {
		this.applyForSupplier = applyForSupplier;
	}
	public boolean isAudit() {
		return audit;
	}
	public void setAudit(boolean audit) {
		this.audit = audit;
	}
	
}







