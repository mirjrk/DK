package com.zesium.friendrecap.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.zesium.friendrecap.model.Notification;
import com.zesium.friendrecap.model.User;
import com.zesium.friendrecap.repository.NotificationsRepository;
import com.zesium.friendrecap.service.NotificationService;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Implementation of Notification service interface.  
 * 
 * @author Katarina Zorbic
 */
@Service
@Validated
public class NotificationServiceImpl implements NotificationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);
	
	private final NotificationsRepository repository;
	
	@Inject
    public NotificationServiceImpl(final NotificationsRepository repository) {
        this.repository = repository;
    }
	
	@Transactional
	@Override
    public Notification save(@NotNull @Valid final Notification notification) {
        LOGGER.debug("Creating {}", notification);
        return repository.save(notification);
    }
	
	@Transactional
	@Override
    public Notification findNotification(@NotNull @Valid final User user, @NotNull @Valid final String type, @NotNull @Valid final String friendFacebookId ) {
        LOGGER.debug("Find notification {}", user, type, friendFacebookId);
        return repository.findNotification(user, type, friendFacebookId);
    }
	
	@Override
	public void delete(@NotNull @Valid final Notification notification) {
		repository.delete(notification);
	}
	
	@Transactional
	@Override
	public List<Notification> findNotificationList(@NotNull @Valid final User user, @NotNull @Valid final String type, @NotNull @Valid final String friendFacebookId ) {
        LOGGER.debug("Find notification list {}", user, type, friendFacebookId);
        return repository.findNotificationList(user, type, friendFacebookId);
    }
	
	@Transactional
	@Override
    public List<Notification> findNotificationListByTimestamp(@NotNull @Valid final User user, @NotNull @Valid final String timestamp) {
        LOGGER.debug("Find notification list by timestamp {}", user, timestamp);
        return repository.findNotificationListByTimestamp(user, timestamp);
    }
	
	@Transactional
	@Override
	public List<Notification> findByUser(@NotNull @Valid final User user) {
        LOGGER.debug("Find notification by user {}", user);
        return repository.findByUser(user);
    }
	
	@Transactional()
	@Override
	public List<Notification> findAll() {
        LOGGER.debug("Retrieving the list of all notifications");
        return repository.findAll();
    }
	
	@Transactional
	@Override
	public Notification findNotificationForRedirection(@NotNull @Valid final User user, @NotNull @Valid final String type, @NotNull @Valid final String timestamp ) {
        LOGGER.debug("Find notification {}", user, type, timestamp);
        return repository.findNotificationForRedirection(user, type, timestamp);
    }
	

}
