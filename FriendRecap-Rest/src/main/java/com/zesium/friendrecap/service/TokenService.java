package com.zesium.friendrecap.service;

import java.util.List;



import com.zesium.friendrecap.model.Token;
import com.zesium.friendrecap.model.User;

/**
 * Service interface for Token model.  
 *
 * @author Katarina Zorbic
 */
public interface TokenService {

	Token save (Token token);
	void delete (Token token);
	List<Token> findTokensList (User user);
	Token findToken (User user, String token);
	Token findDeviceUID (User user, String deviceUID);
}
