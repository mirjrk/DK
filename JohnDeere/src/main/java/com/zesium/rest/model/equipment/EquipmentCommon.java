package com.zesium.rest.model.equipment;

import java.io.Serializable;

public class EquipmentCommon implements Serializable{
	private int id;
	private EquipmentType equipmentType;
	private SwitchingEquipmentType switchingEquipmentType;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public EquipmentType getEquipmentType() {
		return equipmentType;
	}
	public void setEquipmentType(EquipmentType equipmentType) {
		this.equipmentType = equipmentType;
	}	
	public SwitchingEquipmentType getSwitchingEquipmentType() {
		return switchingEquipmentType;
	}

	public void setSwitchingEquipmentType(SwitchingEquipmentType switchingEquipmentType) {
		this.switchingEquipmentType = switchingEquipmentType;
	}
}
