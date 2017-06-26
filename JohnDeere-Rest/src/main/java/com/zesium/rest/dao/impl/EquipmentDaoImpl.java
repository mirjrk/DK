package com.zesium.rest.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.zesium.rest.dao.EquipmentDao;
import com.zesium.rest.model.equipment.Equipment;
import com.zesium.rest.model.user.User;
import com.zesium.rest.utils.Utils;

@Repository
public class EquipmentDaoImpl implements EquipmentDao{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	public void createEquipment(Equipment equipment) {
		getCurrentSession().saveOrUpdate(equipment);
	}

	public Equipment updateEquipment(Equipment equipment) {
		getCurrentSession().update(equipment);
		return equipment;
	}

	public void deleteEquipment(int id) {
		Equipment equipment = getEquipment(id);
		getCurrentSession().delete(equipment);
	}

	public List<Equipment> getAllEquipments() {
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT e FROM Equipment e");
		return query.list();
	}

	public Equipment getEquipment(int id) {
		return (Equipment) getCurrentSession().get(Equipment.class, id);
	}
	
	public List<Equipment> getAllEquipmentsForUser(int userId, Map<String, String> parameters) {
		String queryString = "SELECT e FROM Equipment e join e.user eu WHERE eu.id=" + userId;
		if (parameters.size() > 0) {
		
			if (parameters.containsKey(Utils.FILTER_EQUIPMENT_GUARANTEE_DATE_FROM) && parameters.containsKey(Utils.FILTER_EQUIPMENT_GUARANTEE_DATE_TO)) {
				queryString += " and e.endOfGuaranteeDate between '" + parameters.get(Utils.FILTER_EQUIPMENT_GUARANTEE_DATE_FROM) + "' and '" + parameters.get(Utils.FILTER_EQUIPMENT_GUARANTEE_DATE_TO) + "'";
			} else if (parameters.containsKey(Utils.FILTER_EQUIPMENT_GUARANTEE_DATE_FROM)) {
				queryString += " and e.endOfGuaranteeDate >= '" + parameters.get(Utils.FILTER_EQUIPMENT_GUARANTEE_DATE_FROM) + "'";
			} else if (parameters.containsKey(Utils.FILTER_EQUIPMENT_GUARANTEE_DATE_TO)) {
				queryString += " and e.endOfGuaranteeDate <= '" + parameters.get(Utils.FILTER_EQUIPMENT_GUARANTEE_DATE_TO) + "'";
			}
			
			for (Map.Entry<String, String> entry : parameters.entrySet()){
				if (entry.getKey().equals(Utils.FILTER_EQUIPMENT_SERIAL_NUMBER) || entry.getKey().equals(Utils.FILTER_EQUIPMENT_TYPE)) {
					queryString += " and" + " e." + entry.getKey() + " like '%" + entry.getValue() + "%'";
				} 
			}
		}
		Query query = getCurrentSession().createQuery(queryString);
		return query.list();
	}

	public User getEquipmentOwner(int id) {
		Query query = getCurrentSession().createQuery("SELECT u FROM User u INNER JOIN u.equipments ue WHERE ue.id=:equipmentId");
		query.setParameter("equipmentId", id);
		User user = (User) query.uniqueResult();
		return user;
	}
	
	public List<Equipment> getEquipmentsForUser(int userId) {
		Query query = getCurrentSession().createQuery("SELECT e FROM Equipment e INNER JOIN e.user eu WHERE eu.id=:userId and e.serialNumber is not null and e.name is not null");
		query.setParameter("userId", userId);
		return query.list();
	}
}
