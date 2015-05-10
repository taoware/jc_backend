package com.irengine.campus.cas.extension.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.irengine.campus.cas.extension.domain.Store;
import com.irengine.campus.cas.extension.repository.StoreRepository;
import com.irengine.campus.cas.extension.repository.UploadedFileRepository;
import com.irengine.commons.DateProvider;

@Service
@Transactional
public class StoreService {
	@Autowired
	StoreRepository storeRepository;
	@Autowired
	UploadedFileRepository ufr;
	/**添加*/
	public void create(Store store) {
		store.setCreateTime(DateProvider.DEFAULT.getDate());
		store.setUpdateTime(DateProvider.DEFAULT.getDate());
		storeRepository.save(store);
	}
	/**根据id删除*/
	public void delete(Long id) {
		storeRepository.delete(id);
	}
	
	public void update(Store store) {
		store.setUpdateTime(DateProvider.DEFAULT.getDate());
		storeRepository.save(store);
	}

	public List<Store> findAll() {
		List<Store> stores=(List<Store>) storeRepository.findAll();
		return stores;
	}
	
	public List<Store> findByType(String type) {
		List<Store> stores=storeRepository.findByType(type);
		return stores;
	}
	
	public Store findById(long id) {
		Store store=storeRepository.findOne(id);
		return store;
	}
	/**根据province查找门店信息*/
	public List<Store> findByProvince(String province) {
		List<Store> stores=storeRepository.findByProvince(province);
		return stores;
	}
	
	public List<Store> findByProvinceAndType(String province, String type) {
		List<Store> stores=storeRepository.findByProvinceAndType(province,type);
		return stores;
	}
	public long findMaxId() {
		return storeRepository.findMaxId();
	}

}
