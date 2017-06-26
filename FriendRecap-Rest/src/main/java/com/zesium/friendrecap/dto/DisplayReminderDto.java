package com.zesium.friendrecap.dto;

import java.util.ArrayList;
import java.util.List;

import com.zesium.friendrecap.model.Reminder;


/**
* The DisplayReminderDto is class which represents json object with list of reminder objects and message about error
*
* @author  Katarina Zorbic
* @version 1.0
* @since   2017-02-01
* @see Reminder
*/ 
public class DisplayReminderDto {
	
	private List<ReminderObject> reminderObject = new ArrayList<>();
	private String error;
	
	public List<ReminderObject> getReminderObject() {
		return reminderObject;
	}
	public void setReminderObject(List<ReminderObject> reminderObject) {
		this.reminderObject = reminderObject;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}

}
