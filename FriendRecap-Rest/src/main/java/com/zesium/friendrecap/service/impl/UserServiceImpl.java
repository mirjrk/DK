package com.zesium.friendrecap.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.zesium.friendrecap.model.User;
import com.zesium.friendrecap.repository.UserRepository;
import com.zesium.friendrecap.service.UserService;

/**
 * Implementation of User service interface.  
 * 
 * @author Katarina Zorbic
 */
@Service
@Validated
public class UserServiceImpl implements UserService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	
	private final UserRepository repository;
	
	@Inject
    public UserServiceImpl(final UserRepository repository) {
        this.repository = repository;
    }
	
	@Transactional
	@Override
    public User save(@NotNull @Valid final User user) {
        LOGGER.debug("Creating {}", user);
        return repository.save(user);
    }
	
	@Transactional()
	@Override
    public List<User> findAll() {
        LOGGER.debug("Retrieving the list of all users");
        return repository.findAll();
    }
	
	@Transactional
	@Override
    public User findById(@NotNull @Valid final int userId) {
        LOGGER.debug("Find by id {}", userId);
        return repository.findOne(userId);
    }
	
	@Transactional
	@Override
    public User findByFacebookID(@NotNull @Valid final String facebookID) {
        LOGGER.debug("Find by facebookID {}", facebookID);
        return repository.findByFacebookID(facebookID);
    }

}
