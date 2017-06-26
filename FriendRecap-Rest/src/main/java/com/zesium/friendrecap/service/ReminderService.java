package com.zesium.friendrecap.service;

import java.util.List;

import com.zesium.friendrecap.model.Reminder;
import com.zesium.friendrecap.model.User;

/**
 * Service interface for Reminder model.  
 *
 * @author Katarina Zorbic
 */
public interface ReminderService {
	
	Reminder save (Reminder reminder);
	void delete (Reminder reminder);
	List<Reminder> findRemindersList (User user);
	Reminder findById(int reminderId);
	List<Reminder> findAll();

}
