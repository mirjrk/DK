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

import com.zesium.friendrecap.model.Friend;
import com.zesium.friendrecap.model.User;
import com.zesium.friendrecap.repository.FriendsRepository;
import com.zesium.friendrecap.service.FriendsService;

/**
 * Implementation of Friend service interface.  
 * 
 * @author Katarina Zorbic
 */
@Service
@Validated
public class FriendsServiceImpl implements FriendsService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	
	private final FriendsRepository repository;
	
	@Inject
    public FriendsServiceImpl(final FriendsRepository repository) {
        this.repository = repository;
    }
	
	@Transactional
	@Override
    public Friend save(@NotNull @Valid final Friend friend) {
        LOGGER.debug("Creating {}", friend);
        return repository.save(friend);
    }
	
	@Transactional()
	@Override
    public List<Friend> findAll() {
        LOGGER.debug("Retrieving the list of all friends");
        return repository.findAll();
    }
	
	@Transactional
	@Override
    public Friend findFriendMatch(@NotNull @Valid final User user, @NotNull @Valid final User friend) {
        LOGGER.debug("Find friend match {}", user, friend);
        return repository.findFriendMatch(user, friend);
    }
	
	@Transactional
	@Override
    public List<Friend> findFriendsList(@NotNull @Valid final User user) {
        LOGGER.debug("Find friend list {}", user);
        return repository.findFriendsList(user);
    }
	
	@Transactional
	@Override
    public List<Friend> findFriendsOwner(@NotNull @Valid final User friend) {
        LOGGER.debug("Find friends owner {}", friend);
        return repository.findFriendsOwner(friend);
    }
	
	@Override
	public void delete(@NotNull @Valid final Friend friend) {
		repository.delete(friend);
	}

}
