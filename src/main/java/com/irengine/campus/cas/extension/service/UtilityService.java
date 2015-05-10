package com.irengine.campus.cas.extension.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.irengine.campus.cas.extension.domain.UploadedFile;
import com.irengine.campus.cas.extension.repository.UploadedFileRepository;
import com.irengine.commons.ResizeImage;

@Service
@Transactional
public class UtilityService {

	@Autowired
	UploadedFileRepository ufr;

	/** 创建UploadedFile */
	public UploadedFile createFile(String tName, String entityType,
			Long entityId, String uploadFileName, long size,
			String photoDescription) {
		UploadedFile uploadedFile = new UploadedFile(entityType, entityId,
				uploadFileName, tName, size, photoDescription);
		ufr.save(uploadedFile);
		return uploadedFile;
	}

	/** 通过entityType和entityId查找UploadedFile */
	public List<UploadedFile> listFiles(String type, String entityType,
			long entityId) {
		List<UploadedFile> list = ufr.findByEntityTypeAndEntityId(entityType,
				entityId);
		return list;
	}

	/** 通过fileId查找UploadedFile */
	public UploadedFile getFile(long fileId) {
		UploadedFile uploadedFile = ufr.findByFileId(fileId);
		return uploadedFile;
	}

	/** 删除UploadedFile */
	public void removeFile(long fileId) {
		ufr.delete(fileId);
	}

	public List<UploadedFile> findByEntityId(long id) {
		return ufr.findByEntityId(id);
	}

	public List<UploadedFile> findAll() {
		return (List<UploadedFile>) ufr.findAll();
	}

	public List<UploadedFile> findByEntityTypeAndEntityId(String entityType,
			long entityId) {
		return ufr.findByEntityTypeAndEntityId(entityType, entityId);
	}

	public List<UploadedFile> listFiles(String entityType, Long entityId) {
		return ufr.findByEntityTypeAndEntityId(entityType, entityId);
	}

	public void deleteByEntityTypeAndEntityId(String entityType, Long entityId) {
		ufr.deleteByTypeAndEntityTypeAndEntityId(entityType, entityId);
	}

	private static String DIRECTORY_UPLOAD = "/uploaded/";

	private static String getWebDirectory(HttpServletRequest request) {
		return request.getSession().getServletContext()
				.getRealPath(DIRECTORY_UPLOAD);
	}

	public List<UploadedFile> uploadFiles(String entityType, long entityId,
			List<MultipartFile> files, HttpServletRequest request,String photoDescription)
			throws IOException {
		List<UploadedFile> uploadedFiles = new ArrayList<UploadedFile>();
		String uploadDirectoryName = getWebDirectory(request);
		/* 删除该目录下的文件 */
		File deleteFile = new File(StringUtils.join(new Object[] {
				uploadDirectoryName, entityType, entityId }, "/"));
		if (deleteFile.isDirectory()) {
			File[] deleteFiles = deleteFile.listFiles();
			if (deleteFiles.length > 0) {
				for (int i = 0; i < deleteFiles.length; i++) {
					deleteFiles[i].delete();
				}
			}
		}
		for (MultipartFile file : files) {
			if (file == null || file.isEmpty()) {
			} else {
				File uploadDirectory = new File(uploadDirectoryName);
				FileUtils.forceMkdir(uploadDirectory);
				String uploadFileName = String
						.format("%d%s",
								System.currentTimeMillis(),
								file.getOriginalFilename().substring(
										file.getOriginalFilename().lastIndexOf(
												".")));
				File uploadFile = new File(StringUtils.join(new Object[] {
						uploadDirectoryName, entityType, entityId,
						uploadFileName }, "/"));
				FileUtils.writeByteArrayToFile(uploadFile, file.getBytes());
				/* 生成缩略图并保存在UploadedFile对应的表中 */
				String uploadFileUrl = StringUtils.join(new Object[] {
						uploadDirectoryName, entityType, entityId,
						uploadFileName }, "/");
				String fileUrl = uploadFileUrl;
				String outputFolder = uploadFileUrl.substring(0,
						uploadFileUrl.lastIndexOf("/"));
				String thumbnailName = ResizeImage.createThumbnail(fileUrl,
						outputFolder);
				UploadedFile uploadedFile = createFile(thumbnailName,entityType, entityId,
						uploadFileName, file.getSize(),photoDescription);
				uploadedFiles.add(uploadedFile);
			}
		}
		return uploadedFiles;
	}
}
