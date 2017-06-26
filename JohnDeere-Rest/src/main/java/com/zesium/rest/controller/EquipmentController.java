package com.zesium.rest.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zesium.rest.model.equipment.Equipment;
import com.zesium.rest.model.equipment.EquipmentType;
import com.zesium.rest.model.equipment.SwitchingEquipmentType;
import com.zesium.rest.service.EquipmentService;
import com.zesium.rest.utils.Utils;

@Controller
public class EquipmentController {

	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private UserService userService;
	@Autowired
	private EventService eventService;
	@Autowired
	private ServiceService serviceService;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EquipmentController.class);

	
	@RequestMapping(value = "mobile/user/equipment/getEquipmentsForUser/{userId}", method = RequestMethod.GET)
	public @ResponseBody List<Equipment> getEquipmentsForUser(
			@PathVariable("userId") int userId) throws IOException {
		LOGGER.debug("Start get events method for user with id " + userId);

		// Get user from database by userId
		User user = null;
		try {
			user = userService.getUser(userId);
		} catch (Exception e) {
			LOGGER.error("Error occurred during get user method for user with id: "
					+ userId + ", with message: " + e.getMessage());
		}

		// Get equipments for user
		List<Equipment> equipments = new ArrayList<Equipment>();
		if (user != null) {
			try {
				equipments = equipmentService.getEquipmentsForUser(userId);
			} catch (Exception e) {
				LOGGER.error("Error occurred during get equipments method for user with id: "
						+ userId + ", with message: " + e.getMessage());
			}
		}
		return equipments;
	}

	// ***************************************************************************//
	// **************************WebEquipmentMethods******************************//
	// ***************************************************************************//

	@RequestMapping(value = "/web/user/equipment/newEquipment", method = RequestMethod.GET)
	public ModelAndView newEquipment(@RequestParam("userId") Integer userId,
			ModelAndView model) throws IOException {
		Equipment equipment = new Equipment();
		User user = new User();
		user.setId(userId);
		equipment.setUser(user);

		model.addObject("equipment", equipment);
		model.addObject("equipment.user.id", userId);
		model.addObject("equipmentTypes", EquipmentType.values());
		model.addObject("switchingEquipmentTypes",
				SwitchingEquipmentType.values());
		model.setViewName("CreateEquipment");

		return model;
	}

	@RequestMapping(value = "/web/user/equipment/editEquipment", method = RequestMethod.GET)
	public ModelAndView editEquipment(@RequestParam("id") Integer equipmentId,
			HttpServletRequest request) throws IOException {

		// Get equipment from database by equipmentId
		Equipment equipment = equipmentService.getEquipment(equipmentId);

		// Create response object by equipment
		ModelAndView model = new ModelAndView("CreateEquipment");
		model.addObject("equipment", equipment);
		model.addObject("equipment.user.id", equipment.getUser().getId());
		model.addObject("equipmentTypes", EquipmentType.values());
		model.addObject("switchingEquipmentTypes",
				SwitchingEquipmentType.values());

		return model;
	}

	@RequestMapping(value = "/web/user/equipment/deleteEquipment", method = RequestMethod.GET)
	public ModelAndView deleteCEquipment(
			@RequestParam("id") Integer equipmentId, HttpServletRequest request)
			throws IOException {

		// Get user from database by equipmentId
		User user = equipmentService.getEquipmentOwner(equipmentId);

		// Get equipment from database by equipmentId
		Equipment equipment = equipmentService.getEquipment(equipmentId);

		// Remove equipment from user
		for (Iterator<Equipment> iterator = user.getEquipments().iterator(); iterator
				.hasNext();) {
			Equipment e = iterator.next();
			if (e.getId() == equipmentId) {
				iterator.remove();
				break;
			}
		}
		// Update user
		userService.updateUser(user);

		return new ModelAndView("redirect:/web/user/showEquipments?id="
				+ equipment.getUser().getId());
	}

	@RequestMapping(value = "/web/user/equipment/changeOwner", method = RequestMethod.GET)
	public ModelAndView changeOwner(@RequestParam("id") Integer equipmentId,
			HttpServletRequest request) throws IOException {

		// Get equipment from database by equipmentId
		Equipment equipment = equipmentService.getEquipment(equipmentId);

		// Get all users from database
		List<User> users = userService.getAllUsers();

		// Create response object by users and equipment
		ModelAndView model = new ModelAndView("ChangeOwner");
		model.addObject("equipment", equipment);
		model.addObject("equipment.user.id", equipment.getUser().getId());
		model.addObject("users", users);

		return model;
	}

	@RequestMapping(value = "/web/user/equipment/saveEquipmentOwner", method = RequestMethod.POST)
	public ModelAndView saveEquipmentOwner(@ModelAttribute Equipment equipment,
			BindingResult result) throws IOException {
		if (result.hasErrors()) {
			return new ModelAndView();
		}
		if (equipment.getId() == 0) {
			// Create new equipment if id is 0
			equipmentService.createEquipment(equipment);
		} else {
			// Otherwise update equipment
			equipmentService.updateEquipment(equipment);
		}

		// Get all events for given equpment
		List<Event> events = eventService.getEventsForEquipment(equipment
				.getId());

		// Update events with new user
		for (Event e : events) {
			e.setUser(equipment.getUser());
			e.setModificationDate(new Date());

			eventService.updateEvent(e);
		}

		return new ModelAndView("redirect:/web/user/showEquipments?id="
				+ equipment.getUser().getId());
	}

	// ***************************************************************************//
	// *************************JSON Equipment
	// methods****************************//
	// ***************************************************************************//

	@RequestMapping(value = "/web/user/equipment/editEquipmentJson", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> editEquipmentJson(
			@RequestParam("id") Integer equipmentId) throws IOException {

		// Get equipment from database by equipmentId
		Equipment equipment = equipmentService.getEquipment(equipmentId);

		// Set returning user data
		User user = new User();
		user.setId(equipment.getUser().getId());
		equipment.setUser(user);

		// Create response object by user and equipment
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("equipment", equipment);
		model.put("equipment.user.id", equipment.getUser().getId());
		model.put("equipmentTypes", EquipmentType.values());
		model.put("switchingEquipmentTypes", SwitchingEquipmentType.values());

		return model;
	}

	@RequestMapping(value = "/web/user/equipment/addEquipmentJson", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> addEquipmentJson(
			@RequestParam("userId") Integer userId) throws IOException {

		Equipment equipment = new Equipment();
		User user = new User();
		user.setId(userId);
		equipment.setUser(user);

		Map<String, Object> model = new HashMap<String, Object>();

		model.put("equipment", equipment);
		model.put("equipment.user.id", userId);
		model.put("equipmentTypes", EquipmentType.values());
		model.put("switchingEquipmentTypes", SwitchingEquipmentType.values());

		return model;
	}

	@RequestMapping(value = "/web/user/equipment/saveEquipmentJson", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveEquipmentJson(
			@RequestBody Equipment equipment) throws IOException {

		Map<String, Object> model = new HashMap<String, Object>();
		if (equipment.getId() == 0) {
			// Create new equipment if id is 0
			equipmentService.createEquipment(equipment);
		} else {
			// Otherwise update equipment
			equipmentService.updateEquipment(equipment);
		}
		model.put("result", "OK");

		return model;
	}

	@RequestMapping(value = "/web/user/equipment/changeOwnerJson", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> changeOwnerJson(
			@RequestParam("id") Integer equipmentId) throws IOException {

		// Get equipment from database by equipmentId
		Equipment equipment = equipmentService.getEquipment(equipmentId);

		// Get all farmer users from database
		List<User> users = userService.getAllFarmers();

		// Create user with basic (needed) data
		User user = new User();
		user.setId(equipment.getUser().getId());
		user.setFirstName(equipment.getUser().getFirstName());
		user.setLastName(equipment.getUser().getLastName());
		equipment.setUser(user);

		// Create response object by users and equipment
		HashMap<String, Object> model = new HashMap<String, Object>();
		model.put("equipment", equipment);

		Utils.setNullPartsForListUser(users);
		model.put("users", users);

		return model;
	}

	@RequestMapping(value = "/web/user/equipment/searchOwners", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> searchOwners(
			@RequestParam("id") Integer equipmentId,
			@RequestParam(value = "firstName", required = false) String firstName,
			@RequestParam(value = "lastName", required = false) String lastName)
			throws IOException {

		// Get equipment from database by equipmentId
		Equipment equipment = equipmentService.getEquipment(equipmentId);
		Utils.setNullPartsForUser(equipment.getUser());

		// Set filter parameters
		Map<String, String> parameters = new HashMap<String, String>();
		if (firstName != null && !firstName.equals("")) {
			parameters.put(Utils.FILTER_USER_FIRST_NAME, firstName);
		}
		if (lastName != null && !lastName.equals("")) {
			parameters.put(Utils.FILTER_USER_LAST_NAME, lastName);
		}

		// Get users from database for filter parameters
		List<User> users = userService.getAllActivatedUsersWithParameters(
				parameters, Utils.USERS);
		Utils.setNullPartsForListUser(users);

		// Create response object by users and equipment
		HashMap<String, Object> model = new HashMap<String, Object>();
		model.put("users", users);
		model.put("equipment", equipment);

		return model;
	}

	@RequestMapping(value = "/web/user/equipment/saveEquipmentOwnerJson", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveEquipmentOwnerJson(
			@RequestBody Equipment equipment) throws IOException {

		if (equipment.getId() == 0) {
			// Create new equipment if id is 0
			equipmentService.createEquipment(equipment);
		} else {
			// Otherwise update equipment
			equipmentService.updateEquipment(equipment);
		}

		// Get event for equipment by equipment ID
		List<Event> events = eventService.getEventsForEquipment(equipment
				.getId());

		// Get user from database by user ID
		User user = userService.getUser(equipment.getUser().getId());

		// Update all equipment events and add comments
		for (Event e : events) {
			e.setUser(equipment.getUser());
			e.setModificationDate(new Date());

			try {
				List<Service> services = serviceService.getCentralServices();

				// Create comment for change equipment owner
				String commentMessage = CommentEnumWords.CHANGE_EQUIPMENT_OWNER
						+ user.getFirstName() + " " + user.getLastName();
				Comment comment = new Comment();
				comment.setUserFirstName(user.getFirstName());
				comment.setUserLastName(user.getLastName());
				comment.setCommentDate(new Date());
				comment.setContent(commentMessage);
				comment.setServiceName(services.get(0).getTitle());
				comment.setCommentType(CommentType.COMMENT);

				if (!e.getState().equals(EventState.ARCHIVED)) {
					comment.setCommentDate(new Date());

					Set<Comment> comments = new HashSet<Comment>();
					if (e.getComments() != null) {
						comments = e.getComments();
					}
					comments.add(comment);
					comment.setEvent(e);
					e.setComments(comments);
				}
			} catch (Exception ex) {
				LOGGER.error("Error occurred during creating new comment for change quipment owner for equipment with id: "
						+ equipment.getId()
						+ ", with message: "
						+ ex.getMessage());
			}

			eventService.updateEvent(e);
		}

		Map<String, Object> model = new HashMap<String, Object>();

		model.put("result", "OK");

		return model;
	}
}
