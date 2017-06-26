package com.zesium.friendrecap.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.zesium.friendrecap.model.Reminder;
import com.zesium.friendrecap.model.User;
import com.zesium.friendrecap.repository.ReminderRepository;
import com.zesium.friendrecap.service.ReminderService;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Implementation of Reminder service interface.  
 * 
 * @author Katarina Zorbic
 */
@Service
@Validated
public class ReminderServiceImpl implements ReminderService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReminderServiceImpl.class);
	
	private final ReminderRepository repository;
	
	@Inject
    public ReminderServiceImpl(final ReminderRepository repository) {
        this.repository = repository;
    }
	
	@Transactional
	@Override
    public Reminder save(@NotNull @Valid final Reminder reminder) {
        LOGGER.debug("Creating {}", reminder);
        return repository.save(reminder);
    }
	
	@Transactional()
	@Override
    public List<Reminder> findRemindersList(User user) {
        LOGGER.debug("Retrieving the list of all reminders for user");
        return repository.findRemindersList(user);
    }
	
	@Override
	public void delete(@NotNull @Valid final Reminder reminder) {
		repository.delete(reminder);
	}
	
	@Transactional
	@Override
    public Reminder findById(@NotNull @Valid final int reminderId) {
        LOGGER.debug("Find {}", reminderId);
        return repository.findOne(reminderId);
    }
	
	@Transactional()
	@Override
	public List<Reminder> findAll() {
        LOGGER.debug("Retrieving the list of all reminders");
        return repository.findAll();
    }

}
