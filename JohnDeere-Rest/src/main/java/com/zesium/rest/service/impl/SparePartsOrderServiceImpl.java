package com.zesium.rest.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zesium.rest.dao.SparePartsOrderDao;
import com.zesium.rest.model.spare_parts.SparePartsOrder;
import com.zesium.rest.service.SparePartsOrderService;

@Service
@Transactional
public class SparePartsOrderServiceImpl implements SparePartsOrderService{

	@Autowired
	private SparePartsOrderDao sparePartsOrderDao;

	@Override
	public void createSparePartsOrder(SparePartsOrder sparePartsOrder) {
		sparePartsOrderDao.createSparePartsOrder(sparePartsOrder);
	}

	@Override
	public void deleteSparePartsOrder(int id) {
		sparePartsOrderDao.deleteSparePartsOrder(id);
	}

	@Override
	public List<SparePartsOrder> getAllSparePartsOrders() {
		return sparePartsOrderDao.getAllSparePartsOrders();
	}

	@Override
	public SparePartsOrder getSparePartsOrder(int id) {
		return sparePartsOrderDao.getSparePartsOrder(id);
	}

	@Override
	public SparePartsOrder updateSparePartsOrder(SparePartsOrder sparePartsOrder) {
		return sparePartsOrderDao.updateSparePartsOrder(sparePartsOrder);
	}

	@Override
	public List<SparePartsOrder> getSparePartsOrdersForLocalService(
			String registeredDistrict) {
		return sparePartsOrderDao.getSparePartsOrdersForLocalService(registeredDistrict);
	}

	@Override
	public List<SparePartsOrder> getSparePartsOrdersForUser(int userId) {
		return sparePartsOrderDao.getSparePartsOrdersForUser(userId);
	}
}
