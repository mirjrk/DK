package com.zesium.friendrecap.util;

import com.zesium.friendrecap.model.InAppPost;

/**
* The TimeParserForDelete implements a method to parsing current time in millis in number of days
* 
* @author  Katarina Zorbic
* @version 1.0
* @since   2017-02-01
* @see InAppPost
*/ 
public class TimeParserForDelete {
	
	private long timeRightNow;
    private long timeCreated;

    public TimeParserForDelete(String timeCreatedNotification) {
        timeRightNow = System.currentTimeMillis();
        this.timeCreated = Long.valueOf(timeCreatedNotification);
    }

    public long getTimeBeforeNotificationWasCreated() {
        return timeRightNow-timeCreated;
    }

    public String getParsedTime() {
        long time = (timeRightNow-timeCreated)/1000;

        String result="";
        
        if(time > 3600 && time<86400) {
           
        	result = String.valueOf(0);
            
        } else if(time < 3600 && time > 60) {
        	
            result = String.valueOf(0);
           
        } else if(time > 86400) {
            
        	result = String.valueOf(time / (3600*24));
        }
        	
        return result;
    }

}
