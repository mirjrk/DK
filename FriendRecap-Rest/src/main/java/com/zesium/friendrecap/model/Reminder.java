package com.zesium.friendrecap.model;

import java.io.Serializable;
import javax.persistence.*;


import java.util.Date;
import java.util.List;


/**
 * The persistent class for the reminder database table.
 * 
 * @author Katarina Zorbic
 */
@Entity
@NamedQuery(name="Reminder.findAll", query="SELECT r FROM Reminder r")
public class Reminder implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="reminder_id")
	private int reminderId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="reminder_date")
	private Date reminderDate;

	@Column(name="reminder_text")
	private String reminderText;
	
	@Column(name="reminder_timezone")
	private String reminderTimezone;
	
	@Column(name="timestamp")
	private String timestamp;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id_post")
	private User userIdPost;

	//bi-directional many-to-many association to User
	@ManyToMany (fetch=FetchType.EAGER)
	@JoinColumn(name="user_id_tag")
	private List<User> userIdTagList;
	
	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getReminderTimezone() {
		return reminderTimezone;
	}

	public void setReminderTimezone(String reminderTimezone) {
		this.reminderTimezone = reminderTimezone;
	}

	public int getReminderId() {
		return this.reminderId;
	}

	public void setReminderId(int reminderId) {
		this.reminderId = reminderId;
	}

	public Date getReminderDate() {
		return this.reminderDate;
	}

	public void setReminderDate(Date reminderDate) {
		this.reminderDate = reminderDate;
	}

	public String getReminderText() {
		return this.reminderText;
	}

	public void setReminderText(String reminderText) {
		this.reminderText = reminderText;
	}

	public User getUserIdPost() {
		return userIdPost;
	}

	public void setUserIdPost(User userIdPost) {
		this.userIdPost = userIdPost;
	}

	public List<User> getUserIdTagList() {
		return userIdTagList;
	}

	public void setUserIdTagList(List<User> userIdTagList) {
		this.userIdTagList = userIdTagList;
	}


}