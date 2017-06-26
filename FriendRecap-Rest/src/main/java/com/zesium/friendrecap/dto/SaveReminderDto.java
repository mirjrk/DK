package com.zesium.friendrecap.dto;

import java.util.ArrayList;
import java.util.List;

import com.zesium.friendrecap.model.Reminder;

/**
* The SaveReminderDto is class which represents json object with text, date, timezone of reminders,
* facebook id of user which posted reminder and list of tagged friends
*
* @author  Katarina Zorbic
* @version 1.0
* @since   2017-02-01
* @see Reminder
*/ 
public class SaveReminderDto {
	
	private String reminderText;
	private String reminderDate;
	private String reminderTimezone;
	private String facebookIdPost;
	private List<String> facebookIdTagList = new ArrayList<>();
	private int reminderId;

	
	public String getReminderDate() {
		return reminderDate;
	}

	public void setReminderDate(String reminderDate) {
		this.reminderDate = reminderDate;
	}

	public String getReminderText() {
		return reminderText;
	}

	public void setReminderText(String reminderText) {
		this.reminderText = reminderText;
	}

	public String getFacebookIdPost() {
		return facebookIdPost;
	}

	public void setFacebookIdPost(String facebookIdPost) {
		this.facebookIdPost = facebookIdPost;
	}

	public int getReminderId() {
		return reminderId;
	}

	public void setReminderId(int reminderId) {
		this.reminderId = reminderId;
	}

	public List<String> getFacebookIdTagList() {
		return facebookIdTagList;
	}

	public void setFacebookIdTagList(List<String> facebookIdTagList) {
		this.facebookIdTagList = facebookIdTagList;
	}

	public String getReminderTimezone() {
		return reminderTimezone;
	}

	public void setReminderTimezone(String reminderTimezone) {
		this.reminderTimezone = reminderTimezone;
	}
	
}
