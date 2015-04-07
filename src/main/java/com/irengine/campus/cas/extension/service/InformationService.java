package com.irengine.campus.cas.extension.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.irengine.campus.cas.extension.domain.Information;
import com.irengine.campus.cas.extension.domain.UploadedFile;
import com.irengine.campus.cas.extension.repository.InformationRepository;
import com.irengine.campus.cas.extension.repository.UploadedFileRepository;
import com.irengine.commons.DateProvider;

@Service
@Transactional
public class InformationService {
	
	@Autowired
	private InformationRepository informationRepository;
	@Autowired
	UploadedFileRepository ufr;
	/**根据id查找资讯*/
	public Information findById(Long id){
		Information information=informationRepository.findOne(id);
		List<UploadedFile> largeFiles=ufr.findByEntityTypeAndEntityId("large","news", information.getId());
		if(largeFiles.size()>0){
			List<UploadedFile> smallFiles=ufr.findByEntityTypeAndEntityId("small","news", information.getId());
			largeFiles.get(0).setThumbnailUrl(smallFiles.get(0).getUrl());
			information.setPhoto(largeFiles.get(0));
		}
		return information;
	}
	/**根据userId查找资讯*/
	public List<Information> findByUserId(Long userId){
		List<Information> list=informationRepository.findByUserId(userId);
		for(Information information:list){
			List<UploadedFile> largeFiles=ufr.findByEntityTypeAndEntityId("large","news", information.getId());
			if(largeFiles.size()>0){
				List<UploadedFile> smallFiles=ufr.findByEntityTypeAndEntityId("small","news", information.getId());
				largeFiles.get(0).setThumbnailUrl(smallFiles.get(0).getUrl());
				information.setPhoto(largeFiles.get(0));
			}
		}
		return list;
	}
	
	/**添加资讯*/
	public void create(Information information){
		information.setCreateTime(DateProvider.DEFAULT.getDate());
		information.setUpdateTime(DateProvider.DEFAULT.getDate());
		informationRepository.save(information);
	}
	/**查找所有资讯*/
	public List<Information> list(){
		List<Information>list=(List<Information>)informationRepository.findAll();
		for(Information information:list){
			List<UploadedFile> largeFiles=ufr.findByEntityTypeAndEntityId("large","news", information.getId());
			if(largeFiles.size()>0){
				List<UploadedFile> smallFiles=ufr.findByEntityTypeAndEntityId("small","news", information.getId());
				largeFiles.get(0).setThumbnailUrl(smallFiles.get(0).getUrl());
				information.setPhoto(largeFiles.get(0));
			}
		}
		return list;
	}
	/**找到指定时间后的资讯*/
	public List<Information> getLastUpdated(Date queryTime) {
		List<Information> list=informationRepository.findLastUpdated(queryTime);
		for(Information information:list){
			List<UploadedFile> largeFiles=ufr.findByEntityTypeAndEntityId("large","news", information.getId());
			if(largeFiles.size()>0){
				List<UploadedFile> smallFiles=ufr.findByEntityTypeAndEntityId("small","news", information.getId());
				largeFiles.get(0).setThumbnailUrl(smallFiles.get(0).getUrl());
				information.setPhoto(largeFiles.get(0));
			}
		}
		return list;
	}
	/**删除资讯*/
	public void delete(Long id) {
		informationRepository.delete(id);
	}
	/**修改资讯*/
	public void update(Information information){
		information.setUpdateTime(DateProvider.DEFAULT.getDate());
		informationRepository.save(information);
	}
	/**根据type查找资讯*/
	public List<Information> findByType(String type) {
		List<Information> list=informationRepository.findByType(type);
		for(Information information:list){
			List<UploadedFile> largeFiles=ufr.findByEntityTypeAndEntityId("large","news", information.getId());
			if(largeFiles.size()>0){
				List<UploadedFile> smallFiles=ufr.findByEntityTypeAndEntityId("small","news", information.getId());
				largeFiles.get(0).setThumbnailUrl(smallFiles.get(0).getUrl());
				information.setPhoto(largeFiles.get(0));
			}
		}
		return list;
	}
	
}












