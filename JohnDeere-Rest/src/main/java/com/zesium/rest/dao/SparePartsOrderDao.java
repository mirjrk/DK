package com.zesium.rest.dao;

import java.util.List;

import com.zesium.rest.model.spare_parts.SparePartsOrder;

/**
 * Interface for spare parts order data access object methods.
 * 
 * @author Ivan Panic_2
 *
 */
public interface SparePartsOrderDao {

	public void createSparePartsOrder(SparePartsOrder sparePartsOrder);

	public void deleteSparePartsOrder(int id);

	public List<SparePartsOrder> getAllSparePartsOrders();

	public SparePartsOrder getSparePartsOrder(int id);

	public SparePartsOrder updateSparePartsOrder(SparePartsOrder sparePartsOrder);

	public List<SparePartsOrder> getSparePartsOrdersForLocalService(
			String registeredDistrict);

	public List<SparePartsOrder> getSparePartsOrdersForUser(int userId);
}
