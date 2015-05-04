package com.irengine.campus.cas.extension.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.irengine.campus.cas.extension.domain.Square;
import com.irengine.campus.cas.extension.domain.UploadedFile;
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
		squareRepository.save(square);
	}

	public List<Square> findAll() {
		List<Square> squares = (List<Square>) squareRepository.findAll();
		addPhotos(squares);
		// for(Square square:squares){
		// User user=userService.findById(square.getUserId());
		// square.setUser(user);
		// }
		return squares;
	}

	public void delete(Long id) {
		squareRepository.delete(id);
	}

	public void update(Square square) {
		square.setUpdateTime(DateProvider.DEFAULT.getDate());
		squareRepository.save(square);
	}

	/** 关联查询照片 */
	private void addPhotos(List<Square> squares) {
		for (Square square : squares) {
			List<UploadedFile> largeFiles = us.findByEntityTypeAndEntityId(
					"large", "square", square.getId());
			Set<UploadedFile> setFiles = new HashSet<UploadedFile>();
			if (largeFiles.size() > 0) {
				for(int i=0;i<(largeFiles.size()>9?9:largeFiles.size());i++){
					largeFiles.get(i).setThumbnailUrl(largeFiles.get(i).getUrl());
					setFiles.add(largeFiles.get(i));
				}
			}
			square.setPhotos(setFiles);
		}
	}

	public Square findById(long id) {
		Square square = squareRepository.findById(id);
		addPhotos(square);
		return square;
	}

	private void addPhotos(Square square) {
		List<UploadedFile> largeFiles = us.findByEntityTypeAndEntityId(
				"large", "square", square.getId());
		Set<UploadedFile> setFiles = new HashSet<UploadedFile>();
		if (largeFiles.size() > 0) {
			for(int i=0;i<(largeFiles.size()>9?9:largeFiles.size());i++){
				largeFiles.get(i).setThumbnailUrl(largeFiles.get(i).getUrl());
				setFiles.add(largeFiles.get(i));
			}
		}
		square.setPhotos(setFiles);
	}

	public List<Square> findByUserId(long userId) {
		List<Square> squares = squareRepository.findByUserId(userId);
		addPhotos(squares);
		return squares;
	}

	public long getMaxId() {
		Long maxId = squareRepository.getMaxId();
		if (maxId == null || "".equals(maxId)) {
			maxId = (long) 0;
		}
		return maxId;
	}

	public List<Square> findByType(String type) {
		List<Square> squares = squareRepository.findByType(type);
		addPhotos(squares);
		return squares;
	}

	public List<Square> findByTypeAndLocation(String type, String location) {
		List<Square> squares = squareRepository.findByType(type);
		if (squares.size() > 0) {
			for (int i = 0; i < squares.size(); i++) {
				if (squares.get(i).getUser().getLocation().indexOf(location) == -1) {
					squares.remove(i);
					--i;
				}
			}
		}
		return squares;
	}

	public List<Square> findByUserIdAndUnitId(long userId, long unitId) {
		return squareRepository.findByUserIdAndUnitId(userId, unitId);
	}

}
