package com.zesium.friendrecap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zesium.friendrecap.model.Reminder;
import com.zesium.friendrecap.model.User;
	
/**
 * Jpa repository for Reminder model.
 * 
 * @author Katarina Zorbic
 */
public interface ReminderRepository extends JpaRepository<Reminder, Integer> {
	
	@Query(value = "SELECT r FROM Reminder r WHERE r.userIdPost = :userIdPost")
	public List<Reminder> findRemindersList (@Param("userIdPost") User userIdPost);

}
