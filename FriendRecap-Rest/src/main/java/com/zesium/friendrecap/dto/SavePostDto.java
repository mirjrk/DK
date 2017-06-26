package com.zesium.friendrecap.dto;

import com.zesium.friendrecap.model.InAppPost;

/**
* The SavePostDto is class which represents json object with facebook id of user which posted in application
*
* @author  Katarina Zorbic
* @version 1.0
* @since   2017-02-01
* @see InAppPost
*/ 
public class SavePostDto {
	

	private String facebookIdPost;

	public String getFacebookIdPost() {
		return facebookIdPost;
	}

	public void setFacebookIdPost(String facebookIdPost) {
		this.facebookIdPost = facebookIdPost;
	}

}
