package com.zesium.friendrecap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zesium.friendrecap.model.Friend;
import com.zesium.friendrecap.model.User;

/**
 * Jpa repository for Friend model.
 * 
 * @author Katarina Zorbic
 */
public interface FriendsRepository extends JpaRepository<Friend, Integer>{
	
	@Query(value = "SELECT f FROM Friend f WHERE f.userId = :userId AND f.friendId = :friendId")
	public Friend findFriendMatch (@Param("userId") User userId, @Param("friendId") User friendId);
	
	@Query(value = "SELECT f FROM Friend f WHERE f.userId = :userId")
	public List<Friend> findFriendsList (@Param("userId") User userId);
	
	@Query(value = "SELECT f FROM Friend f WHERE f.friendId = :friendId")
	public List<Friend> findFriendsOwner (@Param("friendId") User friendId);

}
