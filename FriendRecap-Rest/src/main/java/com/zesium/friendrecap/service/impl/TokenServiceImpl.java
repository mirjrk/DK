package com.zesium.friendrecap.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.zesium.friendrecap.model.Token;
import com.zesium.friendrecap.model.User;
import com.zesium.friendrecap.repository.TokenRepository;

import com.zesium.friendrecap.service.TokenService;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Implementation of Token service interface.  
 * 
 * @author Katarina Zorbic
 */
@Service
@Validated
public class TokenServiceImpl implements TokenService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TokenServiceImpl.class);
	
	private final TokenRepository repository;
	
	@Inject
    public TokenServiceImpl(final TokenRepository repository) {
        this.repository = repository;
    }
	
	@Transactional
	@Override
    public Token save(@NotNull @Valid final Token token) {
        LOGGER.debug("Creating {}", token);
        return repository.save(token);
    }
	
	@Override
	public void delete(@NotNull @Valid final Token token) {
		repository.delete(token);
	}
	
	@Transactional
	@Override
    public List<Token> findTokensList(@NotNull @Valid final User user) {
        LOGGER.debug("Find token list {}", user);
        return repository.findTokensList(user);
    }
	
	@Transactional
	@Override
    public Token findToken(@NotNull @Valid final User user, @NotNull @Valid final String token) {
        LOGGER.debug("Find token {}", user, token);
        return repository.findToken(user, token);
    }
	
	@Transactional
	@Override
    public Token findDeviceUID(@NotNull @Valid final User user, @NotNull @Valid final String deviceUID) {
        LOGGER.debug("Find token {}", user, deviceUID);
        return repository.findDeviceUID(user, deviceUID);
    }

}
