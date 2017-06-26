package com.zesium.friendrecap.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.zesium.friendrecap.model.User;

/**
 * Jpa repository for User model.
 * 
 * @author Katarina Zorbic
 */
public interface UserRepository extends JpaRepository<User, Integer>{
	
	public User findByEmail(String email);
	
	@Query(value = "SELECT u FROM User u WHERE u.facebookID = :facebookID")
	public User findByFacebookID (@Param("facebookID") String facebookID);
	
}
