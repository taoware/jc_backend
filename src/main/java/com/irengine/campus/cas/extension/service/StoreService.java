package com.irengine.campus.cas.extension.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.irengine.campus.cas.extension.domain.Store;
import com.irengine.campus.cas.extension.domain.UploadedFile;
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
	/*查询的同事注入UploadFile对象*/
	public List<Store> findAll() {
		List<Store> stores=(List<Store>) storeRepository.findAll();
		for(Store store:stores){
			List<UploadedFile> largeFiles=ufr.findByEntityTypeAndEntityId("large","store", store.getId());
			if(largeFiles.size()>0){
				List<UploadedFile> smallFiles=ufr.findByEntityTypeAndEntityId("small","store", store.getId());
				largeFiles.get(0).setThumbnailUrl(smallFiles.get(0).getUrl());
				store.setPhoto(largeFiles.get(0));
			}
		}
		return stores;
	}
	public List<Store> findByType(String type) {
		List<Store> stores=storeRepository.findByType(type);
		for(Store store:stores){
			List<UploadedFile> largeFiles=ufr.findByEntityTypeAndEntityId("large","store", store.getId());
			if(largeFiles.size()>0){
				List<UploadedFile> smallFiles=ufr.findByEntityTypeAndEntityId("small","store", store.getId());
				largeFiles.get(0).setThumbnailUrl(smallFiles.get(0).getUrl());
				store.setPhoto(largeFiles.get(0));
			}
		}
		return stores;
	}
	
	public Store findById(long id) {
		Store store=storeRepository.findOne(id);
		List<UploadedFile> largeFiles=ufr.findByEntityTypeAndEntityId("large","store", store.getId());
		if(largeFiles.size()>0){
			List<UploadedFile> smallFiles=ufr.findByEntityTypeAndEntityId("small","store", store.getId());
			largeFiles.get(0).setThumbnailUrl(smallFiles.get(0).getUrl());
			store.setPhoto(largeFiles.get(0));
		}
		return store;
	}

}
