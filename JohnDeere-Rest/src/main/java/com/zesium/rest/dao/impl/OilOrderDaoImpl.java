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

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public void createOilOrder(com.zesium.rest.model.oil_order.OilOrder oilOrder) {
		getCurrentSession().saveOrUpdate(oilOrder);
	}

	@Override
	public void deleteOilOrder(int id) {
		OilOrder oilOrder = getOilOrder(id);
		getCurrentSession().delete(oilOrder);
	}

	@Override
	public List<com.zesium.rest.model.oil_order.OilOrder> getAllOilOrders() {
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT oo FROM OilOrder oo");
		return query.list();
	}

	@Override
	public com.zesium.rest.model.oil_order.OilOrder getOilOrder(int id) {
		return (OilOrder) getCurrentSession().get(OilOrder.class, id);
	}

	@Override
	public com.zesium.rest.model.oil_order.OilOrder updateOilOrder(
			com.zesium.rest.model.oil_order.OilOrder oilOrder) {
		getCurrentSession().update(oilOrder);
		return oilOrder;
	}

	@Override
	public List<com.zesium.rest.model.oil_order.OilOrder> getOilOrdersForLocalService(
			String registeredDistrict) {
		Session session = getCurrentSession();
		Query query = session.createQuery("select oo from OilOrder oo inner join oo.oilOrderUser oou "
				+ "inner join oou.location ooul where ooul.registeredDistrict=:registeredDistrict ORDER BY date ASC");
		query.setParameter("registeredDistrict", registeredDistrict);
		return query.list();
	}

	@Override
	public List<com.zesium.rest.model.oil_order.OilOrder> getOilOrdersForUser(int userId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("select oo from OilOrder oo inner join oo.oilOrderUser oou "
				+ "where oou.id=:userId ORDER BY date DESC");
		query.setParameter("userId", userId);
		return query.list();
	}
}
