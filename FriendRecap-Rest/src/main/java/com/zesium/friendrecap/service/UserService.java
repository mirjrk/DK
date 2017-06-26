package com.zesium.friendrecap.service;

import java.util.List;

import com.zesium.friendrecap.model.User;

/**
 * Service interface for User model.  
 *
 * @author Katarina Zorbic
 */
public interface UserService {
	
	User save (User user);
	List<User> findAll();
	User findById (int userId);
	User findByFacebookID (String facebookID);
	

}
