package com.zesium.rest.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zesium.rest.dao.OilOrderDao;
import com.zesium.rest.model.oil_order.OilOrder;
import com.zesium.rest.service.OilOrderService;

@Service
@Transactional
public class OilOrderServiceImpl implements OilOrderService{

	@Autowired
	private OilOrderDao oilOrderDao;
	
	@Override
	public void createOilOrder(OilOrder oilOrder) {
		oilOrderDao.createOilOrder(oilOrder);
	}

	@Override
	public void deleteOilOrder(int id) {
		oilOrderDao.deleteOilOrder(id);
	}

	@Override
	public List<OilOrder> getAllOilOrders() {
		return oilOrderDao.getAllOilOrders();
	}

	@Override
	public OilOrder getOilOrder(int id) {
		return oilOrderDao.getOilOrder(id);
	}

	@Override
	public OilOrder updateOilOrder(OilOrder oilOrder) {
		return oilOrderDao.updateOilOrder(oilOrder);
	}

	@Override
	public List<OilOrder> getOilOrdersForLocalService(String registeredDistrict) {
		return oilOrderDao.getOilOrdersForLocalService(registeredDistrict);
	}

	@Override
	public List<OilOrder> getOilOrdersForUser(int userId) {
		return oilOrderDao.getOilOrdersForUser(userId);
	}

}
