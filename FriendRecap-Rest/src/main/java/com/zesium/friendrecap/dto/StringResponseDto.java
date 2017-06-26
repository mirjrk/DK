package com.zesium.friendrecap.dto;

/**
* The StringResponseDto is class which represents json object with string message for success or error
*
* @author  Katarina Zorbic
* @version 1.0
* @since   2017-02-01
*/ 
public class StringResponseDto {
	
	private String response;
	private String error;

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	
}
