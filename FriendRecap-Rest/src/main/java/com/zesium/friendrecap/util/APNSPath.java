package com.zesium.friendrecap.util;

import com.zesium.friendrecap.model.InAppPost;

/**
* The APNSPath class contains string path with enviroment variable for .p12 file
*
* @author  Katarina Zorbic
* @version 1.0
* @since   2017-02-01
* @see InAppPost
*/ 
public class APNSPath {

	// Environment variables
	String path = System.getenv("FR_APNS_PATH");
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}	

}
