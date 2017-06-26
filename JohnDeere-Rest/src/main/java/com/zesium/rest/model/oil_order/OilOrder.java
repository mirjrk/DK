package com.zesium.rest.model.oil_order;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.zesium.rest.model.user.User;

@Entity
@Table(name = "oil_order")
public class OilOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	@Column(name = "date")
	private Date date;
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User oilOrderUser;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public User getUser() {
		return oilOrderUser;
	}
	public void setUser(User oilOrderUser) {
		this.oilOrderUser = oilOrderUser;
	}
}
