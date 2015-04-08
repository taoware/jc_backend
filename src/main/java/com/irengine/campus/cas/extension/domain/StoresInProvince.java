package com.irengine.campus.cas.extension.domain;

import java.util.ArrayList;
import java.util.List;

public class StoresInProvince {
	private String province;//省份信息
	private List<Store> stores=new ArrayList<Store>();//对应的门店信息
	public StoresInProvince(){
		super();
	}
	public StoresInProvince(String province, List<Store> stores) {
		super();
		this.province = province;
		this.stores = stores;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public List<Store> getStores() {
		return stores;
	}
	public void setStores(List<Store> stores) {
		this.stores = stores;
	}
	
}
