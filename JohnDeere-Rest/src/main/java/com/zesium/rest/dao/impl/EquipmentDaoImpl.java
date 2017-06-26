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
public class EquipmentDaoImpl implements EquipmentDao {

	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * This method returns session which represents main interface between
	 * server and database.
	 * 
	 * @return session object
	 */
	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * Create new equipment in database.
	 * 
	 * @param equipment
	 *            object with all equipment data that will be stored in database
	 */
	public void createEquipment(Equipment equipment) {
		getCurrentSession().saveOrUpdate(equipment);
	}

	/**
	 * Update existing equipment.
	 * 
	 * @param equipment
	 *            updated object that will be stored in database
	 * @return updated equipment object
	 */
	public Equipment updateEquipment(Equipment equipment) {
		getCurrentSession().update(equipment);
		return equipment;
	}

	/**
	 * Delete equipment form database by given equipment id.
	 * 
	 * @param id
	 *            integer value of equipment ID
	 */
	public void deleteEquipment(int id) {
		Equipment equipment = getEquipment(id);
		getCurrentSession().delete(equipment);
	}

	/**
	 * Get all equipments that exist in database.
	 * 
	 * @return list of all equipments
	 */
	public List<Equipment> getAllEquipments() {
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT e FROM Equipment e");
		return query.list();
	}

	/**
	 * This method returns equipment by given equipment ID.
	 * 
	 * @param id
	 *            integer value of equipment ID.
	 * @return equipment object that is found in database.
	 */
	public Equipment getEquipment(int id) {
		return (Equipment) getCurrentSession().get(Equipment.class, id);
	}

	/**
	 * This method returns all equipments for the given user by his ID.
	 * 
	 * @param userId
	 *            integer value of users ID
	 * @param parameters
	 *            search parameters
	 * @return list of users equipments
	 */
	public List<Equipment> getAllEquipmentsForUser(int userId,
			Map<String, String> parameters) {
		String queryString = "SELECT e FROM Equipment e JOIN e.user eu WHERE eu.id="
				+ userId;
		if (parameters.size() > 0) {

			// Add date search parameters
			if (parameters
					.containsKey(Utils.FILTER_EQUIPMENT_GUARANTEE_DATE_FROM)
					&& parameters
							.containsKey(Utils.FILTER_EQUIPMENT_GUARANTEE_DATE_TO)) {
				queryString += " AND e.endOfGuaranteeDate BETWEEN '"
						+ parameters
								.get(Utils.FILTER_EQUIPMENT_GUARANTEE_DATE_FROM)
						+ "' AND '"
						+ parameters
								.get(Utils.FILTER_EQUIPMENT_GUARANTEE_DATE_TO)
						+ "'";
			} else if (parameters
					.containsKey(Utils.FILTER_EQUIPMENT_GUARANTEE_DATE_FROM)) {
				queryString += " AND e.endOfGuaranteeDate >= '"
						+ parameters
								.get(Utils.FILTER_EQUIPMENT_GUARANTEE_DATE_FROM)
						+ "'";
			} else if (parameters
					.containsKey(Utils.FILTER_EQUIPMENT_GUARANTEE_DATE_TO)) {
				queryString += " AND e.endOfGuaranteeDate <= '"
						+ parameters
								.get(Utils.FILTER_EQUIPMENT_GUARANTEE_DATE_TO)
						+ "'";
			}

			// Add additional search parameters if exists
			for (Map.Entry<String, String> entry : parameters.entrySet()) {
				if (entry.getKey().equals(Utils.FILTER_EQUIPMENT_SERIAL_NUMBER)
						|| entry.getKey().equals(Utils.FILTER_EQUIPMENT_TYPE)) {
					queryString += " AND" + " e." + entry.getKey() + " LIKE '%"
							+ entry.getValue() + "%'";
				}
			}
		}
		Query query = getCurrentSession().createQuery(queryString);
		return query.list();
	}

	/**
	 * Get owner of equipment by given user ID.
	 * 
	 * @param id
	 *            integer value of users ID
	 * @return user object that contains all users data
	 */
	public User getEquipmentOwner(int id) {
		Query query = getCurrentSession()
				.createQuery(
						"SELECT u FROM User u INNER JOIN u.equipments ue WHERE ue.id=:equipmentId");
		query.setParameter("equipmentId", id);
		User user = (User) query.uniqueResult();
		return user;
	}

	/**
	 * This method returns list of users equipments that have serial number
	 * which indicates that they are manually inserted or registered in official
	 * store.
	 * 
	 * @param userId
	 *            integer value of users ID
	 * @return list of users equipment
	 */
	public List<Equipment> getEquipmentsForUser(int userId) {
		Query query = getCurrentSession()
				.createQuery(
						"SELECT e FROM Equipment e INNER JOIN e.user eu WHERE eu.id=:userId "
								+ "and e.serialNumber IS NOT NULL and e.name IS NOT NULL");
		query.setParameter("userId", userId);
		return query.list();
	}
}
