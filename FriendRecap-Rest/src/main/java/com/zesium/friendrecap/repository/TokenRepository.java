package com.zesium.friendrecap.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zesium.friendrecap.model.Token;
import com.zesium.friendrecap.model.User;

/**
 * Jpa repository for Token model.
 * 
 * @author Katarina Zorbic
 */
public interface TokenRepository extends JpaRepository<Token, Integer>{
	
	@Query(value = "SELECT t FROM Token t WHERE t.user = :user")
	public List<Token> findTokensList (@Param("user") User user);
	
	@Query(value = "SELECT t FROM Token t WHERE t.user = :user AND t.token = :token")
	public Token findToken (@Param("user") User user, @Param("token") String token);
	
	@Query(value = "SELECT t FROM Token t WHERE t.user = :user AND t.deviceUID = :deviceUID")
	public Token findDeviceUID (@Param("user") User user, @Param("deviceUID") String deviceUID);
	
}
