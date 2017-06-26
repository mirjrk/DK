package com.zesium.rest.cotroller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zesium.rest.model.spare_parts.SparePartsOrder;
import com.zesium.rest.service.SparePartsOrderService;

@Controller
public class SparePartsOrderController {

	@Autowired
	private UserService userService;
	@Autowired
	private SparePartsOrderService sparePartsOrderService;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SparePartsOrderController.class);

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				new SimpleDateFormat("MM/dd/yyyy"), false));
	}

	@RequestMapping(value = "/web/sparePartsOrder/addSparePartsOrder", method = RequestMethod.GET)
	public ModelAndView addSparePartsOrder(ModelAndView model,
			HttpServletRequest request) throws IOException {
		
		// Get all farmer users from database
		List<User> users = userService.getAllFarmers();

		SparePartsOrder sparePartsOrder = new SparePartsOrder();
		model.addObject("sparePartsOrder", sparePartsOrder);
		model.addObject("users", users);
		model.setViewName("addSparePartsOrder");

		return model;
	}

	@RequestMapping(value = "/web/sparePartsOrder/saveSparePartsOrder", method = RequestMethod.POST)
	public ModelAndView saveSparePartsOrder(
			@ModelAttribute("sparePartsOrder") SparePartsOrder sparePartsOrder,
			HttpServletRequest request, BindingResult result)
			throws IOException {
		
		if (result.hasErrors()) {
			return new ModelAndView();
		}
		if (sparePartsOrder.getId() == 0) {
			// Create new spare part order order if id is 0
			sparePartsOrderService.createSparePartsOrder(sparePartsOrder);
		} else {
			// Otherwise update spare part order
			sparePartsOrderService.updateSparePartsOrder(sparePartsOrder);
		}
		return new ModelAndView("redirect:listSparePartsOrders");
	}

	@RequestMapping(value = "/web/sparePartsOrder/listSparePartsOrders", method = RequestMethod.GET)
	public ModelAndView listSparePartsOrders(ModelAndView model,
			HttpServletRequest request) throws IOException {

		// Get all oil orders from database
		List<SparePartsOrder> sparePartsOrders = sparePartsOrderService
				.getAllSparePartsOrders();

		// Create response with spare parts object
		model.addObject("sparePartsOrders", sparePartsOrders);
		model.setViewName("listSparePartsOrders");

		return model;
	}

	@RequestMapping(value = "/web/sparePartsOrder/editSparePartsOrder", method = RequestMethod.GET)
	public ModelAndView editSparePartsOrder(@RequestParam("id") Integer id,
			HttpServletRequest request) throws IOException {

		// Get spare parts order from database by given id
		SparePartsOrder sparePartsOrder = sparePartsOrderService
				.getSparePartsOrder(id);

		// Get all farmer users from database
		List<User> users = userService.getAllFarmers();

		// Return model with all farmers and spare parts order
		ModelAndView model = new ModelAndView("addSparePartsOrder");
		model.addObject("users", users);
		model.addObject("sparePartsOrder", sparePartsOrder);

		return model;
	}

	@RequestMapping(value = "/mobile/sparePartsOrder/addSparePartsOrder", method = RequestMethod.POST)
	public @ResponseBody HttpStatus addSparePartsOrderForMobileUser(
			@RequestBody SparePartsOrder sparePartsOrder) throws IOException {
		LOGGER.info("Start adding order for spare parts for user with id "
				+ sparePartsOrder.getUser().getId());

		// Get all user spare parts orders from database
		List<SparePartsOrder> sparePartsOrders = sparePartsOrderService
				.getSparePartsOrdersForUser(sparePartsOrder.getUser().getId());

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);

		// Check if user has ordered spare parts in last 1 hour
		if (sparePartsOrders.size() > 0) {
			if (calendar.getTime().before(sparePartsOrders.get(0).getDate())) {
				return HttpStatus.ALREADY_REPORTED;
			}
		}

		// Otherwise create new spare parts order with current date
		sparePartsOrder.setDate(new Date());
		sparePartsOrderService.createSparePartsOrder(sparePartsOrder);

		return HttpStatus.OK;
	}
}
