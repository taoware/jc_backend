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
	/**查询组织最上级的Unit*/
	public Unit getRoot() {
		
		return unitRepository.findRoot();
	}
	
	public void setRoot(Unit root) {
		
		Unit oldRoot = getRoot();
		if (null == oldRoot) {
			unitRepository.save(root);
			//设置子组织的leftId,rightId
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
	/**列出子组织*/
	public List<Unit> listChildren(Long id) {
		
		Unit parentUnit = unitRepository.findOne(id);

		return unitRepository.findAllChildren(parentUnit.getLeft(), parentUnit.getRight());
	}
	/**列出上级组织*/
	public List<Unit> listAncestor(Long id) {
		
		Unit leafUnit = unitRepository.findOne(id);

		return unitRepository.findAllAncestor(leafUnit.getLeft(), leafUnit.getRight());
	}
	/**修改leftId和rightId*/
	private void updateLRValue(Unit unit) {
		//取得该unit的上级组织的leftId
		Long parentLeft = null == unit.getParent() ? 0 : unit.getParent().getLeft();
		/*ok*/
		//添加
//		unit.setLeft(parentLeft+1);
//		unit.setRight(parentLeft+2);
		//将同级组织leftId加2
		unitRepository.updateLRValue1(parentLeft);
		//设置该unit的leftId为起上级的leftId+1,
		unitRepository.updateLRValue2(parentLeft + 1);
		//将同级组织rightId加2
		unitRepository.updateLRValue3(parentLeft);
		//设置该unit的rightId为起上级的rightId+1,
		unitRepository.updateLRValue4(parentLeft + 2);
	}
	/**添加子组织*/
	public void addChild(Long id, Unit unit) {
		
		Unit parentUnit = unitRepository.findOne(id);
		parentUnit.addChild(unit);
		unitRepository.save(parentUnit);
		//修改left,rightid
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
	
	public Unit findById(Long id){
		Unit unit=unitRepository.findOne(id);
		return unit;
	}
	
}
