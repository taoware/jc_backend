package com.irengine.campus.cas.extension.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.irengine.campus.cas.extension.domain.Square;
import com.irengine.campus.cas.extension.domain.Store;
import com.irengine.campus.cas.extension.domain.UploadedFile;
import com.irengine.campus.cas.extension.domain.User;
import com.irengine.campus.cas.extension.repository.SquareRepository;
import com.irengine.commons.DateProvider;

@Service
@Transactional
public class SquareService {
	@Autowired
	SquareRepository squareRepository;
	@Autowired
	UserService userService;
	@Autowired
	UtilityService us;
	
	public void create(Square square) {
		square.setCreateTime(DateProvider.DEFAULT.getDate());
		square.setUpdateTime(DateProvider.DEFAULT.getDate());
		User user=userService.findById(square.getUserId());
		if(user!=null){
			square.setType(user.getPosition());
		}
		squareRepository.save(square);
	}

	public List<Square> findAll() {
		List<Square> squares=(List<Square>) squareRepository.findAll();
		addPhotos(squares);
//		for(Square square:squares){
//			User user=userService.findById(square.getUserId());
//			square.setUser(user);
//		}
		return squares;
	}

	public void delete(Long id) {
		squareRepository.delete(id);
	}

	public void update(Square square) {
		square.setUpdateTime(DateProvider.DEFAULT.getDate());
		squareRepository.save(square);
	}
	/**关联查询照片*/
	private void addPhotos(List<Square> squares) {
		for(Square square:squares){
			List<UploadedFile> largeFiles=us.findByEntityTypeAndEntityId("large","square", square.getId());
			if(largeFiles.size()>0){
				for(UploadedFile file:largeFiles){
					List<UploadedFile> smallFiles=us.findByEntityTypeAndEntityId("small","square", square.getId());
					if(smallFiles.size()>0){
						file.setThumbnailUrl(smallFiles.get(0).getUrl());
					}
				}
				Set<UploadedFile> setFiles=new HashSet<UploadedFile>();
				for(UploadedFile file:largeFiles){
					setFiles.add(file);
				}
				square.setPhotos(setFiles);
			}
		}
	}

	public List<Square> findById(long id) {
		List<Square> squares=squareRepository.findById(id);
		addPhotos(squares);
		return squares;
	}

	public List<Square> findByUserId(long userId) {
		List<Square> squares=squareRepository.findByUserId(userId);
		addPhotos(squares);
		return squares;
	}

}
