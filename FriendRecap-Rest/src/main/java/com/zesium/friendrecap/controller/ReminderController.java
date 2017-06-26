package com.zesium.friendrecap.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import com.zesium.friendrecap.dto.DisplayReminderDto;
import com.zesium.friendrecap.dto.ReminderObject;
import com.zesium.friendrecap.dto.SaveReminderDto;
import com.zesium.friendrecap.dto.StringResponseDto;
import com.zesium.friendrecap.model.Friend;
import com.zesium.friendrecap.model.Reminder;

import com.zesium.friendrecap.model.User;
import com.zesium.friendrecap.service.FriendsService;
import com.zesium.friendrecap.service.ReminderService;
import com.zesium.friendrecap.service.UserService;

/**
* The ReminderController implements a three methods - 
* for save or edit reminders, show list of reminders and delete reminders.
*
* @author  Katarina Zorbic
* @version 1.0
* @since   2017-02-01
* @see Reminder
*/ 
@Controller
public class ReminderController {
	
	private static final Logger logger = LoggerFactory.getLogger(ReminderController.class);
	
	@Autowired 
    private UserService userService;
	
	@Autowired 
    private FriendsService friendsService;
	
	@Autowired 
    private ReminderService reminderService;
	
	/**
	* This method is used to save or edit reminders.
	* 
	* @param saveReminderDto This parameter is json object with text, date, timezone of reminders,
	* facebook id of user which posted reminder and list of tagged friends
	* @return response This returns message about success or error.
	*/
	@RequestMapping(value = "/api/saveOrEditReminder", method = RequestMethod.POST)
	@ResponseBody
	public StringResponseDto saveReminder(@RequestBody SaveReminderDto saveReminderDto){
		
		logger.debug("saveReminder() : {}", saveReminderDto);
		
		String facebookIdPost = saveReminderDto.getFacebookIdPost();
		User userPost = userService.findByFacebookID(facebookIdPost);
		
		List<String> facebookIdTagList = saveReminderDto.getFacebookIdTagList();
		
		int reminderId = saveReminderDto.getReminderId();
		Reminder reminder = reminderService.findById(reminderId);
		
		StringResponseDto response = new StringResponseDto();
		
		List<User> userTagList = new ArrayList<>();
		for (String facebookIdTag : facebookIdTagList){
			
			User userTag = userService.findByFacebookID(facebookIdTag);
			userTagList.add(userTag);
		}
		
		// create new reminder
	
		reminder.setReminderText(saveReminderDto.getReminderText());
		reminder.setUserIdPost(userPost);
		reminder.setUserIdTagList(userTagList);
			
		String dateString = saveReminderDto.getReminderDate();
		String timezoneDevice = saveReminderDto.getReminderTimezone();
			
		if (!dateString.isEmpty()){
			// parse date and time from reminder to local date and time
			LocalDateTime deviceDate = LocalDateTime.parse(dateString);
			ZonedDateTime fromDateTime = deviceDate.atZone(ZoneId.of(timezoneDevice));
				
			String dateS = fromDateTime.toString();
			LocalDateTime dateL = LocalDateTime.parse(dateS, DateTimeFormatter.ISO_ZONED_DATE_TIME);
				
			Date date = Date.from(dateL.atZone(ZoneId.systemDefault()).toInstant());
				
			reminder.setReminderDate(date);
				
			reminder.setReminderTimezone(timezoneDevice);
				
			Timestamp timestampReminder = new Timestamp(System.currentTimeMillis());
			long tReminder = timestampReminder.getTime();
			String tsReminder = String.valueOf(tReminder);
			reminder.setTimestamp(tsReminder);
					
			reminderService.save(reminder); 
					
			response.setResponse("Successfuly saved reminder");
		}
		
		if (response.getResponse() == null){
			response.getError();
		}else{
			response.setError(null);
		}
		
		return response;
		
	}
	
	/**
	* This method is used to show list of reminders.
	* 
	* @param saveReminderDto This parameter is json object with facebook id of user which posted reminder
	* @return displayListDto This returns json object with list of reminders and message about errors
	*/
	@RequestMapping(value = "/api/showListOfReminders", method = RequestMethod.POST)
	@ResponseBody
	public DisplayReminderDto showListOfReminders(@RequestBody SaveReminderDto saveReminderDto){
		
		logger.debug("showListOfReminders() : {}", saveReminderDto);
		
		String facebookIdPost = saveReminderDto.getFacebookIdPost();
		User userPost = userService.findByFacebookID(facebookIdPost);
		
		List<Reminder> remindersList = reminderService.findRemindersList(userPost);
		
		DisplayReminderDto displayListDto = new DisplayReminderDto();
		List<ReminderObject> reminderObject = new ArrayList<>();
		
		// sorting a reminders from the latest
		Collections.sort(remindersList, Comparator.comparing(Reminder::getTimestamp).reversed());
		
		for (Reminder r : remindersList){
			
			ReminderObject ro = new ReminderObject();
			
			List<String> friendNameList =  new ArrayList<>();
			List<String> facebookIdList =  new ArrayList<>();
			List<String> friendColorList =  new ArrayList<>();
			List<User> userIdTagList = r.getUserIdTagList();
			
			for (User userTag : userIdTagList){
				String friendName = userTag.getFirstName();
				friendNameList.add(friendName);
				
				String facebookId = userTag.getFacebookID();
				facebookIdList.add(facebookId);
				
				Friend friend = friendsService.findFriendMatch(userPost, userTag);
				String friendColor = friend.getFriendColor();
				friendColorList.add(friendColor);
			}
			
			for (String frColor : friendColorList){
				ro.setFriendColor(frColor);
				break;
			}
			
			ro.setFriendNameList(friendNameList);
			ro.setFacebookIdList(facebookIdList);
			ro.setReminderText(r.getReminderText());
			
			Date date = r.getReminderDate();
			String dateString = date.toString();
			ro.setReminderDate(dateString);
			
			ro.setReminderId(r.getReminderId());
			
			
			reminderObject.add(ro);
		}
		
		displayListDto.setReminderObject(reminderObject);
		
		
		if (displayListDto.getReminderObject() == null){
			displayListDto.setError("Bad data");
		}else{
			displayListDto.setError(null);
		}
		
		return displayListDto;
	
	}
	
	/**
	* This method is used to delete reminders.
	* 
	* @param saveReminderDto This parameter is json object with reminder id which will be deleted
	* @return response This returns message about success or error.
	*/
	@RequestMapping(value = "/api/deleteReminder", method = RequestMethod.POST)
	@ResponseBody
	public StringResponseDto deleteReminder(@RequestBody SaveReminderDto saveReminderDto){
		
		logger.debug("deleteReminder() : {}", saveReminderDto);
		
		int reminderId = saveReminderDto.getReminderId();
		Reminder reminder = reminderService.findById(reminderId);
		
		StringResponseDto response = new StringResponseDto();
		
		if (reminder != null){
		
			reminderService.delete(reminder);
			
			response.setResponse("Successfully deleted reminder");
			
		}else{
			
			response.setError("Reminder don't exists");
		}
		
		if (response.getResponse() == null){
			response.getError();
		}else{
			response.setError(null);
		}
		
		return response;
	}

}
