package com.zesium.rest.service;

import java.util.List;
import java.util.Map;

import com.zesium.rest.model.equipment.Equipment;
import com.zesium.rest.model.user.User;

public interface EquipmentService {
	
	public void createEquipment(Equipment equipment);

	public Equipment updateEquipment(Equipment equipment);

	public void deleteEquipment(int id);

	public List<Equipment> getAllEquipments();

	public Equipment getEquipment(int id);
	
	public List<Equipment> getAllEquipmentsForUser(int userId, Map<String, String> parameters);
	
	public User getEquipmentOwner(int id);
	
	public List<Equipment> getEquipmentsForUser(int userId);
}
