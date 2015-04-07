package com.irengine.campus.cas.extension.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.irengine.campus.cas.extension.domain.UploadedFile;
import com.irengine.campus.cas.extension.repository.UploadedFileRepository;
@Service
@Transactional
public class UtilityService {
	
	@Autowired
	UploadedFileRepository ufr;
	/**创建UploadedFile*/
	public UploadedFile createFile(String type,String entityType, long entityId,
			String uploadFileName, long size) {
		UploadedFile uploadedFile=new UploadedFile();
		uploadedFile.setType(type);
		uploadedFile.setEntityId(entityId);
		uploadedFile.setEntityType(entityType);
		uploadedFile.setName(uploadFileName);
		uploadedFile.setSize(size);
		ufr.save(uploadedFile);
		return uploadedFile;
	}
	/**通过entityType和entityId查找UploadedFile*/
	public List<UploadedFile> listFiles(String type,String entityType, long entityId) {
		List<UploadedFile> list=ufr.findByEntityTypeAndEntityId(type,entityType,entityId);
		return list;
	}
	/**通过fileId查找UploadedFile*/
	public UploadedFile getFile(long fileId) {
		UploadedFile uploadedFile=ufr.findByFileId(fileId);
		return uploadedFile;
	}
	/**删除UploadedFile*/
	public void removeFile(long fileId) {
		ufr.delete(fileId);
	}
	public List<UploadedFile> findByEntityId(long id) {
		return ufr.findByEntityId(id);
	}
	public List<UploadedFile> findAll() {
		return (List<UploadedFile>) ufr.findAll();
	}
	public List<UploadedFile> findByEntityTypeAndEntityId(String type,String entityType, long entityId) {
		return ufr.findByEntityTypeAndEntityId(type,entityType, entityId);
	}
	public List<UploadedFile> listFiles(String entityType, Long entityId) {
		return ufr.findByEntityTypeAndEntityId(entityType, entityId);
	}
	
}
