package com.zesium.rest.service.impl;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zesium.rest.dao.EquipmentDao;
import com.zesium.rest.model.equipment.Equipment;
import com.zesium.rest.model.user.User;
import com.zesium.rest.service.EquipmentService;

@Service
@Transactional
public class EquipmentServiceImpl implements EquipmentService{

	@Autowired
	private EquipmentDao equipmentDao;
	
	public void createEquipment(Equipment equipment) {
		equipmentDao.createEquipment(equipment);
	}

	public Equipment updateEquipment(Equipment equipment) {
		return equipmentDao.updateEquipment(equipment);
	}

	public void deleteEquipment(int id) {
		equipmentDao.deleteEquipment(id);
	}

	public List<Equipment> getAllEquipments() {
		return equipmentDao.getAllEquipments();
	}

	public Equipment getEquipment(int id) {
		return equipmentDao.getEquipment(id);
	}
	
	public List<Equipment> getAllEquipmentsForUser(int userId,Map<String, String> parameters) {
		return equipmentDao.getAllEquipmentsForUser(userId, parameters);
	}

	public User getEquipmentOwner(int id) {
		return equipmentDao.getEquipmentOwner(id);
	}
	
	public List<Equipment> getEquipmentsForUser(int userId) {
		return equipmentDao.getEquipmentsForUser(userId);
	}
}
