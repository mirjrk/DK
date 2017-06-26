package com.zesium.friendrecap.service;

import java.util.List;

import com.zesium.friendrecap.model.InAppPost;
import com.zesium.friendrecap.model.User;

/**
 * Service interface for InAppPost model.  
 *
 * @author Katarina Zorbic
 */
public interface InAppPostService {
	
	InAppPost save (InAppPost post);
	void delete (InAppPost post);
	List<InAppPost> findPostsList (User userIdPost);
	List<InAppPost> findAll();
	InAppPost findById (int postId);
	InAppPost findPostForRedirection (User userIdPost, String timestamp);
}
