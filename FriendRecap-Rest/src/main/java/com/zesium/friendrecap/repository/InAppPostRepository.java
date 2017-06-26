package com.zesium.friendrecap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zesium.friendrecap.model.InAppPost;
import com.zesium.friendrecap.model.User;


/**
 * Jpa repository for InAppPost model.
 * 
 * @author Katarina Zorbic
 */
public interface InAppPostRepository extends JpaRepository<InAppPost, Integer>{
	
	@Query(value = "SELECT i FROM InAppPost i WHERE i.userIdPost = :userIdPost")
	public List<InAppPost> findPostsList (@Param("userIdPost") User userIdPost);
	
	@Query(value = "SELECT i FROM InAppPost i WHERE i.userIdPost = :userIdPost AND i.timestamp = :timestamp")
	public InAppPost findPostForRedirection (@Param("userIdPost") User userIdPost, @Param("timestamp") String timestamp);

}
