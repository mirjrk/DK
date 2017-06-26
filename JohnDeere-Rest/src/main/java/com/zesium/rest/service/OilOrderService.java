package com.zesium.rest.service;

import java.util.List;

import com.zesium.rest.model.oil_order.OilOrder;

public interface OilOrderService {

public void createOilOrder(OilOrder oilOrder);
	
	public void deleteOilOrder(int id);

	public List<OilOrder> getAllOilOrders();

	public OilOrder getOilOrder(int id);
	
	public OilOrder updateOilOrder(OilOrder oilOrder);
	
	public List<OilOrder> getOilOrdersForLocalService(String registeredDistrict);
	
	public List<OilOrder> getOilOrdersForUser(int userId);
}
