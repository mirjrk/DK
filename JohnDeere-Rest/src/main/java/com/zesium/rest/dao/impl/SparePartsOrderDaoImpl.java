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
public class SparePartsOrderDaoImpl implements SparePartsOrderDao{

	@Autowired
	private SessionFactory sessionFactory;

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public void createSparePartsOrder(SparePartsOrder sparePartsOrder) {
		getCurrentSession().saveOrUpdate(sparePartsOrder);
	}

	@Override
	public void deleteSparePartsOrder(int id) {
		SparePartsOrder sparePartsOrder = getSparePartsOrder(id);
		getCurrentSession().delete(sparePartsOrder);
	}

	@Override
	public List<SparePartsOrder> getAllSparePartsOrders() {
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT spo FROM SparePartsOrder spo");
		return query.list();
	}

	@Override
	public SparePartsOrder getSparePartsOrder(int id) {
		return (SparePartsOrder) getCurrentSession().get(SparePartsOrder.class, id);
	}

	@Override
	public SparePartsOrder updateSparePartsOrder(SparePartsOrder sparePartsOrder) {
		getCurrentSession().update(sparePartsOrder);
		return sparePartsOrder;
	}

	@Override
	public List<SparePartsOrder> getSparePartsOrdersForLocalService(
			String registeredDistrict) {
		Session session = getCurrentSession();
		Query query = session.createQuery("select spo from SparePartsOrder spo inner join spo.sparePartsUser spou "
				+ "inner join spou.location spoul where spoul.registeredDistrict=:registeredDistrict ORDER BY date ASC");
		query.setParameter("registeredDistrict", registeredDistrict);
		return query.list();
	}

	@Override
	public List<SparePartsOrder> getSparePartsOrdersForUser(int userId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("select spo from SparePartsOrder spo inner join spo.sparePartsUser spou "
				+ "where spou.id=:userId ORDER BY date DESC");
		query.setParameter("userId", userId);
		return query.list();
	}
}
