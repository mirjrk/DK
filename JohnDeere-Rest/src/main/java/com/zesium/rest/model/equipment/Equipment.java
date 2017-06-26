package com.zesium.rest.model.equipment;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Created by Ivan Panic on 14.8.2015.
 */
@Entity
@Table(name = "equipment")
public class Equipment implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	@Column(name = "name")
	private String name;
	@Column(name = "serialNumber")
	private String serialNumber;
	@Column(name = "end_of_guarantee_date")
	private Date endOfGuaranteeDate;
	@Column(name = "date_of_registration")
	private Date dateOfRegistration;
	@Column(name = "equipment_type")
	@Enumerated(EnumType.STRING)
	private EquipmentType equipmentType;
	@Column(name = "switching_equipment_type")
	@Enumerated(EnumType.STRING)
	private SwitchingEquipmentType switchingEquipmentType;
	@Column(name = "under_guarantee", nullable = true)
	private Boolean underGuarantee;
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "equipment")
	@JsonIgnore
	private Set<Event> events;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Date getEndOfGuaranteeDate() {
		return endOfGuaranteeDate;
	}

	public void setEndOfGuaranteeDate(Date endOfGuaranteeDate) {
		this.endOfGuaranteeDate = endOfGuaranteeDate;
	}

	public Date getDateOfRegistration() {
		return dateOfRegistration;
	}

	public void setDateOfRegistration(Date dateOfRegistration) {
		this.dateOfRegistration = dateOfRegistration;
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

	public void setSwitchingEquipmentType(
			SwitchingEquipmentType switchingEquipmentType) {
		this.switchingEquipmentType = switchingEquipmentType;
	}

	public Boolean getUnderGuarantee() {
		return underGuarantee;
	}

	public void setUnderGuarantee(Boolean underGuarantee) {
		this.underGuarantee = underGuarantee;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Event> getEvents() {
		return events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}

	@JsonIgnore
	@Override
	public String toString() {
		return "Equipment [id=" + id + ", name=" + name + ", serialNumber="
				+ serialNumber + ", endOfGuaranteeDate=" + endOfGuaranteeDate
				+ ", dateOfRegistration=" + dateOfRegistration
				+ ", equipmentType=" + equipmentType
				+ ", switchingEquipmentType=" + switchingEquipmentType
				+ ", underGuarantee=" + underGuarantee + ", user="
				+ user.getFirstName() + ", events=" + events.size() + "]";
	}
}
