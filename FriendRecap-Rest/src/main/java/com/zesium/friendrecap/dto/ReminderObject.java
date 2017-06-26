package com.zesium.friendrecap.dto;

import java.util.ArrayList;
import java.util.List;

import com.zesium.friendrecap.model.Reminder;


/**
* The ReminderObject is class which represents json object with text, date, color and id of reminders and
* facebook ids and names of tagged friends
*
* @author  Katarina Zorbic
* @version 1.0
* @since   2017-02-01
* @see Reminder
*/ 
public class ReminderObject {
	
	private String reminderText;
	private String reminderDate;
	private List<String> friendNameList = new ArrayList<>();
	private List<String> facebookIdList = new ArrayList<>();
	private String friendColor;
	private int reminderId;
	
	public String getReminderText() {
		return reminderText;
	}
	public void setReminderText(String reminderText) {
		this.reminderText = reminderText;
	}
	public String getReminderDate() {
		return reminderDate;
	}
	public void setReminderDate(String reminderDate) {
		this.reminderDate = reminderDate;
	}
	public List<String> getFriendNameList() {
		return friendNameList;
	}
	public void setFriendNameList(List<String> friendNameList) {
		this.friendNameList = friendNameList;
	}
	public int getReminderId() {
		return reminderId;
	}
	public void setReminderId(int reminderId) {
		this.reminderId = reminderId;
	}
	public String getFriendColor() {
		return friendColor;
	}
	public void setFriendColor(String friendColor) {
		this.friendColor = friendColor;
	}
	public List<String> getFacebookIdList() {
		return facebookIdList;
	}
	public void setFacebookIdList(List<String> facebookIdList) {
		this.facebookIdList = facebookIdList;
	}
	

}
