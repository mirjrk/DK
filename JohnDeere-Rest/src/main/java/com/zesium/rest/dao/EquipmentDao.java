package com.zesium.rest.dao;

import java.util.List;
import java.util.Map;

import com.zesium.rest.model.equipment.Equipment;

/**
 * Interface for equipment data access object methods.
 * 
 * @author Ivan Panic_2
 *
 */
public interface EquipmentDao {

	public void createEquipment(Equipment equipment);

	public Equipment updateEquipment(Equipment equipment);

	public void deleteEquipment(int id);

	public List<Equipment> getAllEquipments();

	public Equipment getEquipment(int id);

	public List<Equipment> getAllEquipmentsForUser(int userId,
			Map<String, String> parameters);

	public User getEquipmentOwner(int id);

	public List<Equipment> getEquipmentsForUser(int userId);
}
