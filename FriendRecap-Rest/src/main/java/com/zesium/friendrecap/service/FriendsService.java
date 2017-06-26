package com.zesium.friendrecap.service;

import java.util.List;

import com.zesium.friendrecap.model.Friend;
import com.zesium.friendrecap.model.User;

/**
 * Service interface for Friend model.  
 *
 * @author Katarina Zorbic
 */
public interface FriendsService {
	
	Friend save(Friend friend);
	List<Friend> findAll();
	Friend findFriendMatch (User user, User friend);
	List<Friend> findFriendsList (User user);
	List<Friend> findFriendsOwner (User friend);
	void delete(Friend friend);

}
