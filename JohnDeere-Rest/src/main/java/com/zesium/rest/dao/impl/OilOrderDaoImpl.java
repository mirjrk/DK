package com.zesium.rest.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.zesium.rest.dao.OilOrderDao;
import com.zesium.rest.model.oil_order.OilOrder;

@Repository
public class OilOrderDaoImpl implements OilOrderDao {

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
	 * Create new oil order in database.
	 * 
	 * @param oilOrder
	 *            object with all oil order data that will be stored in database
	 */
	public void createOilOrder(com.zesium.rest.model.oil_order.OilOrder oilOrder) {
		getCurrentSession().saveOrUpdate(oilOrder);
	}

	/**
	 * Delete oil order from database by given ID.
	 * 
	 * @param id
	 *            integer value of oil order ID
	 */
	public void deleteOilOrder(int id) {
		OilOrder oilOrder = getOilOrder(id);
		getCurrentSession().delete(oilOrder);
	}

	/**
	 * Get all oil orders that exists in system.
	 * 
	 * @return list of all oil order objects.
	 */
	public List<com.zesium.rest.model.oil_order.OilOrder> getAllOilOrders() {
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT oo FROM OilOrder oo");
		return query.list();
	}

	/**
	 * Get oil order object by given oil order ID.
	 * 
	 * @param id
	 *            value of oil order ID
	 * @return oil order object that contains all oil order data
	 */
	public com.zesium.rest.model.oil_order.OilOrder getOilOrder(int id) {
		return (OilOrder) getCurrentSession().get(OilOrder.class, id);
	}

	/**
	 * Update existing oil order.
	 * 
	 * @param oilOrder
	 *            updated object that will be stored in database
	 * @return updated oil order object
	 */
	public com.zesium.rest.model.oil_order.OilOrder updateOilOrder(
			com.zesium.rest.model.oil_order.OilOrder oilOrder) {
		getCurrentSession().update(oilOrder);
		return oilOrder;
	}

	/**
	 * Get all oil orders for given district. This method will return only oil
	 * order that corresponds users from specific registered district (service).
	 * 
	 * @param registeredDistrict
	 *            value of registered district
	 * @return list of oil orders
	 */
	public List<com.zesium.rest.model.oil_order.OilOrder> getOilOrdersForLocalService(
			String registeredDistrict) {
		Session session = getCurrentSession();
		Query query = session
				.createQuery("SELECT oo FROM OilOrder oo INNER JOIN oo.oilOrderUser oou "
						+ "INNER JOIN oou.location ooul WHERE ooul.registeredDistrict=:registeredDistrict ORDER BY date ASC");
		query.setParameter("registeredDistrict", registeredDistrict);
		return query.list();
	}

	/**
	 * Get all oil orders for given user.
	 * 
	 * @param userId
	 *            users ID
	 * @return list of all users oil orders
	 */
	public List<com.zesium.rest.model.oil_order.OilOrder> getOilOrdersForUser(
			int userId) {
		Session session = getCurrentSession();
		Query query = session
				.createQuery("SELECT oo FROM OilOrder oo INNER JOIN oo.oilOrderUser oou "
						+ "WHERE oou.id=:userId ORDER BY date DESC");
		query.setParameter("userId", userId);
		return query.list();
	}
}
