package com.zesium.friendrecap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zesium.friendrecap.model.Notification;
import com.zesium.friendrecap.model.User;

/**
 * Jpa repository for Notification model.
 * 
 * @author Katarina Zorbic
 */
public interface NotificationsRepository extends JpaRepository<Notification, Integer>{
	
	@Query(value = "SELECT n FROM Notification n WHERE n.user = :user")
	public List<Notification> findByUser (@Param("user") User user);
	
	@Query(value = "SELECT n FROM Notification n WHERE n.user = :user AND n.type = :type AND n.friendFacebookId = :friendFacebookId")
	public Notification findNotification (@Param("user") User user, @Param("type") String type, @Param("friendFacebookId") String friendFacebookId);
	
	@Query(value = "SELECT n FROM Notification n WHERE n.user = :user AND n.type = :type AND n.friendFacebookId = :friendFacebookId")
	public List<Notification> findNotificationList (@Param("user") User user, @Param("type") String type, @Param("friendFacebookId") String friendFacebookId);
	
	@Query(value = "SELECT n FROM Notification n WHERE n.user = :user AND n.type = :type AND n.timestamp = :timestamp")
	public Notification findNotificationForRedirection (@Param("user") User user, @Param("type") String type, @Param("timestamp") String timestamp);
	
	@Query(value = "SELECT n FROM Notification n WHERE n.user = :user AND n.timestamp > :timestamp")
	public List<Notification> findNotificationListByTimestamp (@Param("user") User user, @Param("timestamp") String timestamp);

}
