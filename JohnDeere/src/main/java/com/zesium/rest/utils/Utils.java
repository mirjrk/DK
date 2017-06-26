package com.zesium.rest.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zesium.rest.cotroller.SparePartsOrderController;

public class Utils {
	private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

	// List types
	public static final String USERS = "users";
	public static final String WORKERS = "workers";
	public static final String PENDING_USERS = "pending_users";

	public static final String CONFIG_FILE_PATH = "config.xml";
	public static final String SMS_SERVICE_KEY = "smsServiceUrl";
	public static final String SMS_PORT_KEY = "port";
	public static String SMS_SERVICE_URL_VALUE = "http://127.0.0.1:8080/JohnDeereSmsRest";
	public static String SMS_SERVICE_MULTIPPLE_SMS_URL_VALUE = "http://127.0.0.1:8080/JohnDeereSmsRest";

	public static final String SMS_SERVICE_PATH_TO_SMS_METHOD = "/JohnDeereSmsRest/sms/sendMesage";
	public static final String SMS_SERVICE_PATH_TO_MULTIPLE_SMS_METHOD = "/JohnDeereSmsRest/sms/sendMultipleMesages";
	public static final String SMS_SIGN_OUT = " Odjava: STOP na +381628862292.";
	public static final int MAX_CHARS_IN_SMS = 160;
	public static final int MAX_CHARS_WITHOUT_SMS_SIGN_OUT = MAX_CHARS_IN_SMS
			- SMS_SIGN_OUT.length();

	// Equipments filter values
	public static final String FILTER_EQUIPMENT_TYPE = "equipmentType";
	public static final String FILTER_EQUIPMENT_SERIAL_NUMBER = "serialNumber";
	public static final String FILTER_EQUIPMENT_GUARANTEE_DATE_FROM = "registrationDateFrom";
	public static final String FILTER_EQUIPMENT_GUARANTEE_DATE_TO = "registrationDateTo";

	// Equipments filter values
	public static final String FILTER_EVENT_REGISTRATION_TYPE = "registrationType";
	public static final String FILTER_EVENT_STATE = "state";
	public static final String FILTER_EVENT_SUBSTATE = "subState";
	public static final String FILTER_EVENT_RATE = "rate";
	public static final String FILTER_EVENT_DATE_CREATION_FROM = "creationDateFrom";
	public static final String FILTER_EVENT_DATE_CREATION_TO = "creationDateTo";
	public static final String FILTER_EVENT_OWNER_FIRST_NAME = "ownerFirstName";
	public static final String FILTER_EVENT_OWNER_LAST_NAME = "ownerLastName";
	public static final String FILTER_EVENT_WORKER_FIRST_NAME = "workerFirstName";
	public static final String FILTER_EVENT_WORKER_LAST_NAME = "workerLastName";
	public static final String FILTER_EVENT_SERVICE = "title";

	public static String getEventSubStateStringValue(EventSubState eventSubState) {
		String eventSubStateStringValue = "";
		switch (eventSubState) {
		case DEFAULT:
			eventSubStateStringValue = Utils.EVENT_SUB_STATE_DEDFAULT;
			break;
		case SPARE_PARTS_ORDERED:
			eventSubStateStringValue = Utils.EVENT_SUB_STATE_SPARE_PARTS_ORDERED;
			break;
		case SERVICE_WITHOUT_SPARE_PARTS:
			eventSubStateStringValue = Utils.EVENT_SUB_STATE_SERVICE_WITHOUT_SPARE_PARTS;
			break;
		case BLOCKED:
			eventSubStateStringValue = Utils.EVENT_SUB_STATE_BLOCKED;
			break;
		}
		return eventSubStateStringValue;
	}

	/**
	 * Get string value for forwarded enumeration.
	 * 
	 * @param eventState
	 *            enumeration value,
	 * @return String value of enumeration.
	 */
	public static String getEventStateStringValue(EventState eventState) {
		String eventStateStringValue = "";
		switch (eventState) {
		case NEW:
			eventStateStringValue = Utils.EVENT_STATE_NEW;
			break;
		case BLOCKED:
			eventStateStringValue = Utils.EVENT_STATE_BLOCKED;
			break;
		case REJECTED:
			eventStateStringValue = Utils.EVENT_STATE_REJECTED;
			break;
		case RUNNING:
			eventStateStringValue = Utils.EVENT_STATE_RUNNING;
			break;
		case DONE:
			eventStateStringValue = Utils.EVENT_STATE_DONE;
			break;
		case PAYED:
			eventStateStringValue = Utils.EVENT_STATE_PAYED;
			break;
		case ARCHIVED:
			eventStateStringValue = Utils.EVENT_STATE_ARCHIVED;
			break;
		}
		return eventStateStringValue;
	}

	/**
	 * Read some value form configuration given by key.
	 * 
	 * @param key
	 *            string value of key,
	 * @return read string value by key.
	 */
	public static String loadDataFromConfig(String key) {
		String returningValue = "";
		try {

			ClassLoader classLoader = Utils.class.getClassLoader();
			File file = new File(classLoader.getResource(CONFIG_FILE_PATH)
					.getFile());
			FileInputStream fileInput;

			fileInput = new FileInputStream(file);

			Properties properties = new Properties();
			properties.loadFromXML(fileInput);
			fileInput.close();

			Enumeration<Object> enuKeys = properties.keys();
			while (enuKeys.hasMoreElements()) {
				String readedKey = (String) enuKeys.nextElement();
				if (readedKey.equals(key)) {
					returningValue = properties.getProperty(readedKey);
				}
			}
		} catch (FileNotFoundException e) {
			LOGGER.error("File not found exception occurred with message: "
					+ e.getMessage());
		} catch (InvalidPropertiesFormatException e) {
			LOGGER.error("Invalid properties format exception occurred with message: "
					+ e.getMessage());
		} catch (IOException e) {
			LOGGER.error("IO exception occurred with message: "
					+ e.getMessage());
		}
		return returningValue;
	}

	/**
	 * Set null parts for user response so that circular references can be
	 * avoided. Also not needed data is removed.
	 * 
	 * @param user
	 *            object that contains all user data.
	 */
	public static void setNullPartsForUser(User user) {
		user.setEvents(null);
		user.setEquipments(null);
		user.setAssignedEvents(null);
		user.setService(null);
		user.getLocation().setServices(null);
		user.setSparePartsOrder(null);
	}

	/**
	 * Set null parts for user response so that circular references can be
	 * avoided. Also not needed data is removed.
	 * 
	 * @param users
	 *            list of objects that contains all user data.
	 */
	public static void setNullPartsForListUser(List<User> users) {
		for (User user : users) {
			user.setEvents(null);
			user.setEquipments(null);
			user.setAssignedEvents(null);
			if (user.getService() != null) {
				user.getService().setEvents(null);
				user.getService().setUsers(null);
				if (user.getService().getLocation() != null) {
					user.getService().getLocation().setServices(null);
				}
			}
			if (user.getLocation() != null) {
				user.getLocation().setServices(null);
			}
			user.setSparePartsOrder(null);
		}
	}
}
