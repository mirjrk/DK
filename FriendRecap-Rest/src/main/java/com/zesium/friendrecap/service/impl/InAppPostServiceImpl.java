package com.zesium.friendrecap.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.zesium.friendrecap.model.InAppPost;

import com.zesium.friendrecap.model.User;
import com.zesium.friendrecap.repository.InAppPostRepository;

import com.zesium.friendrecap.service.InAppPostService;



import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Implementation of InAppPost service interface.  
 * 
 * @author Katarina Zorbic
 */
@Service
@Validated
public class InAppPostServiceImpl implements InAppPostService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InAppPostServiceImpl.class);
	
	private final InAppPostRepository repository;
	
	@Inject
    public InAppPostServiceImpl(final InAppPostRepository repository) {
        this.repository = repository;
    }
	
	@Transactional
	@Override
    public InAppPost save(@NotNull @Valid final InAppPost post) {
        LOGGER.debug("Creating {}", post);
        return repository.save(post);
    }
	
	@Override
	public void delete(@NotNull @Valid final InAppPost post) {
		repository.delete(post);
	}
	
	@Transactional
	@Override
    public List<InAppPost> findPostsList(@NotNull @Valid final User userIdPost) {
        LOGGER.debug("Find post list {}", userIdPost);
        return repository.findPostsList(userIdPost);
    }
	
	@Transactional()
	@Override
    public List<InAppPost> findAll() {
        LOGGER.debug("Retrieving the list of all posts");
        return repository.findAll();
    }
	
	@Transactional
	@Override
    public InAppPost findById(@NotNull @Valid final int postId) {
        LOGGER.debug("Find by id {}", postId);
        return repository.findOne(postId);
    }
	
	@Transactional
	@Override
    public InAppPost findPostForRedirection(@NotNull @Valid final User userIdPost, @NotNull @Valid final String timestamp) {
        LOGGER.debug("Find post {}", userIdPost, timestamp);
        return repository.findPostForRedirection(userIdPost, timestamp);
    }

}
