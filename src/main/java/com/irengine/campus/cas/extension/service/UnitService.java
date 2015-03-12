package com.irengine.campus.cas.extension.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.irengine.campus.cas.extension.domain.Unit;
import com.irengine.campus.cas.extension.domain.User;
import com.irengine.campus.cas.extension.repository.UnitRepository;
import com.irengine.campus.cas.extension.repository.UserRepository;

@Service
@Transactional
public class UnitService {

	@Autowired
	UnitRepository unitRepository;

	@Autowired
	UserRepository userRepository;

	public Unit getRoot() {
		
		return unitRepository.findRoot();
	}
	
	public void setRoot(Unit root) {
		
		Unit oldRoot = getRoot();
		if (null == oldRoot) {
			unitRepository.save(root);
			updateLRValue(root);
		}
		else {
			oldRoot.setName(root.getName());
			unitRepository.save(oldRoot);
		}
	}
	
	public List<Unit> list() {

		return unitRepository.findAll();
	}
	
	public List<Unit> listChildren(Long id) {
		
		Unit parentUnit = unitRepository.findOne(id);

		return unitRepository.findAllChildren(parentUnit.getLeft(), parentUnit.getRight());
	}

	public List<Unit> listAncestor(Long id) {
		
		Unit leafUnit = unitRepository.findOne(id);

		return unitRepository.findAllAncestor(leafUnit.getLeft(), leafUnit.getRight());
	}

	private void updateLRValue(Unit unit) {
		
		Long parentLeft = null == unit.getParent() ? 0 : unit.getParent().getLeft();
		
		unitRepository.updateLRValue1(parentLeft);
		unitRepository.updateLRValue2(parentLeft + 1);
		unitRepository.updateLRValue3(parentLeft);
		unitRepository.updateLRValue4(parentLeft + 2);
	}
	
	public void addChild(Long id, Unit unit) {
		
		Unit parentUnit = unitRepository.findOne(id);
		parentUnit.addChild(unit);
		unitRepository.save(parentUnit);
		
		updateLRValue(unit);
	}

	// TODO: not implemented
	public void removeChild(Long id, Unit unit) {
	}
	
	public void addUser(Long unitId, Long userId) {
		
		Unit unit = unitRepository.findOne(unitId);
		User user = userRepository.findOne(userId);
		
		unit.getUsers().add(user);
		user.getUnits().add(unit);

		unitRepository.save(unit);
		userRepository.save(user);
	}
	
	// TODO: not implemented
	public void removeUser(Long unitId, Long userId) {
	}
}
