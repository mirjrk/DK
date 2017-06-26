package com.zesium.rest.service;

import java.util.List;

import com.zesium.rest.model.spare_parts.SparePartsOrder;

public interface SparePartsOrderService {

	public void createSparePartsOrder(SparePartsOrder sparePartsOrder);
	
	public void deleteSparePartsOrder(int id);

	public List<SparePartsOrder> getAllSparePartsOrders();

	public SparePartsOrder getSparePartsOrder(int id);
	
	public SparePartsOrder updateSparePartsOrder(SparePartsOrder sparePartsOrder);
	
	public List<SparePartsOrder> getSparePartsOrdersForLocalService(String registeredDistrict);
	
	public List<SparePartsOrder> getSparePartsOrdersForUser(int userId);
}
