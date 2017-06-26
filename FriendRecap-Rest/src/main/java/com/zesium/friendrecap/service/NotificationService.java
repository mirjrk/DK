package com.zesium.friendrecap.service;

import java.util.List;

import com.zesium.friendrecap.model.Notification;
import com.zesium.friendrecap.model.User;

/**
 * Service interface for Notification model.  
 *
 * @author Katarina Zorbic
 */
public interface NotificationService {
	
	Notification save (Notification notification);
	void delete (Notification notification);
	List<Notification> findByUser (User user);
	Notification findNotification (User user, String type, String friendFacebookId);
	List<Notification> findNotificationList (User user, String type, String friendFacebookId);
	List<Notification> findAll();
	Notification findNotificationForRedirection (User user, String type, String timestamp);
	List<Notification> findNotificationListByTimestamp (User user, String timestamp);
}
