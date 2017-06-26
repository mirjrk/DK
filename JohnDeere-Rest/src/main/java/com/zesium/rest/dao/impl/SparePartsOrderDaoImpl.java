package com.zesium.rest.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.zesium.rest.dao.SparePartsOrderDao;
import com.zesium.rest.model.spare_parts.SparePartsOrder;

@Repository
public class SparePartsOrderDaoImpl implements SparePartsOrderDao {

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
	 * Create new spare parts order in database.
	 * 
	 * @param sparePartsOrder
	 *            object with all spare parts order data that will be stored in
	 *            database
	 */
	public void createSparePartsOrder(SparePartsOrder sparePartsOrder) {
		getCurrentSession().saveOrUpdate(sparePartsOrder);
	}

	/**
	 * Delete spare parts order from database by given ID.
	 * 
	 * @param id
	 *            integer value of spare parts order ID
	 */
	public void deleteSparePartsOrder(int id) {
		SparePartsOrder sparePartsOrder = getSparePartsOrder(id);
		getCurrentSession().delete(sparePartsOrder);
	}

	/**
	 * Get all spare parts orders that exists in system.
	 * 
	 * @return list of all spare parts order objects.
	 */
	public List<SparePartsOrder> getAllSparePartsOrders() {
		Session session = getCurrentSession();
		Query query = session
				.createQuery("SELECT spo FROM SparePartsOrder spo");
		return query.list();
	}

	/**
	 * Get spare parts order object by given spare parts order ID.
	 * 
	 * @param id
	 *            value of spare parts order ID
	 * @return spare parts order object that contains all spare parts order data
	 */
	public SparePartsOrder getSparePartsOrder(int id) {
		return (SparePartsOrder) getCurrentSession().get(SparePartsOrder.class,
				id);
	}

	/**
	 * Update existing spare parts order.
	 * 
	 * @param sparePartsOrder
	 *            updated object that will be stored in database
	 * @return updated spare parts order object
	 */
	public SparePartsOrder updateSparePartsOrder(SparePartsOrder sparePartsOrder) {
		getCurrentSession().update(sparePartsOrder);
		return sparePartsOrder;
	}

	/**
	 * Get all spare parts orders for given district. This method will return
	 * only spare parts order that corresponds users from specific registered
	 * district (service).
	 * 
	 * @param registeredDistrict
	 *            value of registered district
	 * @return list of spare parts orders
	 */
	public List<SparePartsOrder> getSparePartsOrdersForLocalService(
			String registeredDistrict) {
		Query query = getCurrentSession()
				.createQuery(
						"SELECT spo FROM SparePartsOrder spo INNER JOIN spo.sparePartsUser spou "
								+ "INNER JOIN spou.location spoul WHERE spoul.registeredDistrict=:registeredDistrict"
								+ " ORDER BY date ASC");
		query.setParameter("registeredDistrict", registeredDistrict);
		return query.list();
	}

	/**
	 * Get all spare parts orders for given user.
	 * 
	 * @param userId
	 *            users ID
	 * @return list of all users spare parts orders
	 */
	public List<SparePartsOrder> getSparePartsOrdersForUser(int userId) {
		Session session = getCurrentSession();
		Query query = session
				.createQuery("SELECT spo FROM SparePartsOrder spo INNER JOIN spo.sparePartsUser spou "
						+ "WHERE spou.id=:userId ORDER BY date DESC");
		query.setParameter("userId", userId);
		return query.list();
	}
}
