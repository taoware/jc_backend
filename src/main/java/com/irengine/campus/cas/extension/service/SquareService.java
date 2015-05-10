package com.irengine.campus.cas.extension.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.irengine.campus.cas.extension.domain.Square;
import com.irengine.campus.cas.extension.domain.Unit;
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
	@Autowired
	UnitService unitService;

	public void create(Square square) {
		square.setCreateTime(DateProvider.DEFAULT.getDate());
		square.setUpdateTime(DateProvider.DEFAULT.getDate());
		squareRepository.save(square);
	}

	public List<Square> findAll() {
		List<Square> squares = (List<Square>) squareRepository.findAll();
		return squares;
	}

	public void delete(Long id) {
		squareRepository.delete(id);
	}

	public void update(Square square) {
		square.setUpdateTime(DateProvider.DEFAULT.getDate());
		squareRepository.save(square);
	}

	public Square findById(long id) {
		Square square = squareRepository.findById(id);
		return square;
	}

	public List<Square> findByUserId(long userId) {
		List<Square> squares = squareRepository.findByUserId(userId);
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

	public List<Square> findByUserIdAndPermissions(Long userId,List<Long> permissionIds){
		List<Square> squares=new ArrayList<Square>();
		User user=userService.findById(userId);
		Set<Unit> units=user.getUnits();
		//units1:最终返回的unit集合
		Set<Unit> units1=new HashSet<Unit>();
		for(Unit unit:units){
			//查询该unit子集
			List<Unit> units2=unitService.listChildren(unit.getId());
			//把该unit也加入集合
			units2.add(unit);
			for(Unit unit1:units2){
				units1.add(unit1);
			}
		}
		for(Unit unit:units1){
			squares.addAll(findByUnitIdAndPermissionIds(unit.getId(),permissionIds));
		}
		return squares;
	}

	private List<Square> findByUnitIdAndPermissionIds(Long unitId,
			List<Long> permissionIds) {
		List<Square> squares=new ArrayList<Square>();
		if (permissionIds.indexOf(14L) >= 0) {
			List<Square> squares1 = findByType("联采");
			squares.addAll(squares1);
		}
		if (permissionIds.indexOf(16L) >= 0) {
			List<Square> squares1 = findByUnitIdAndType(unitId,"员工");
			squares.addAll(squares1);
		}
		if (permissionIds.indexOf(18L) >= 0) {
			List<Square> squares1 = findByUnitIdAndType(unitId,"供应商");
			squares.addAll(squares1);
		}
		return squares;
	}

	private List<Square> findByUnitIdAndType(Long unitId, String type) {
		List<Square> squares=squareRepository.findByUnitIdAndType(unitId, type);
		return squares;
	}
}
