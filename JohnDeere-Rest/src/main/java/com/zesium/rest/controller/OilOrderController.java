package com.zesium.rest.controller;

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

import com.zesium.rest.model.oil_order.OilOrder;
import com.zesium.rest.service.OilOrderService;

@Controller
public class OilOrderController {
	
	@Autowired
	private OilOrderService oilOrderService;
	@Autowired
	private UserService userService;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(OilOrderController.class);
	
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				new SimpleDateFormat("MM/dd/yyyy"), false));
	}
	
	@RequestMapping(value = "/web/oilOrder/addOilOrder", method = RequestMethod.GET)
	public ModelAndView addOilOrder(ModelAndView model,
			HttpServletRequest request) throws IOException {
	
		// Get all farmer users from database
		List<User> users = userService.getAllFarmers();
		
		// Create response object
		OilOrder oilOrder = new OilOrder();
		model.addObject("oilOrder", oilOrder);
		model.addObject("users", users);
		model.setViewName("addOilOrder");

		return model;
	}
	
	@RequestMapping(value = "/web/oilOrder/saveOilOrder", method = RequestMethod.POST)
	public ModelAndView saveOilOrder(@ModelAttribute("oilOrder") OilOrder oilOrder,
			HttpServletRequest request, BindingResult result)
			throws IOException {
		if (result.hasErrors()) {
			return new ModelAndView();
		}
		if (oilOrder.getId() == 0) {
			// Create new oil order if id is 0
			oilOrderService.createOilOrder(oilOrder);
		} else {
			// Otherwise update oil order
			oilOrderService.updateOilOrder(oilOrder);
		}
		return new ModelAndView("redirect:listOilOrders");
	}
	
	@RequestMapping(value = "/web/oilOrder/listOilOrders", method = RequestMethod.GET)
	public ModelAndView listOilOrders(ModelAndView model,
			HttpServletRequest request) throws IOException {
	
		// Get all oil orders from database
		List<OilOrder> oilOrders = oilOrderService.getAllOilOrders();
		
		model.addObject("oilOrders", oilOrders);
		model.setViewName("listOilOrders");

		return model;
	}
	
	@RequestMapping(value = "/web/oilOrder/editOilOrder", method = RequestMethod.GET)
	public ModelAndView editOilOrder(@RequestParam("id") Integer id,
			HttpServletRequest request) throws IOException {
		
		// Get oil order from database by given id
		OilOrder oilOrder = oilOrderService.getOilOrder(id);
		
		// Get all farmer users from database
		List<User> users = userService.getAllFarmers();
		
		// Return model with all farmers and oil order
		ModelAndView model = new ModelAndView("addOilOrder");
		model.addObject("users", users);
		model.addObject("oilOrder", oilOrder);
		
		return model;
	}
	
	@RequestMapping(value = "/mobile/oilOrder/addOilOrder", method = RequestMethod.POST)
	public @ResponseBody HttpStatus addOilOrderForMobileUser(
			@RequestBody OilOrder oilOrder) throws IOException {
		LOGGER.info("Start adding order for oil for user with id " + oilOrder.getUser().getId());

		// Get all user oil orders from database 
		List<OilOrder> oilOrders = oilOrderService.getOilOrdersForUser(oilOrder.getUser().getId());
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);
		
		// Check if user has ordered oil in last 1 hour
		if (oilOrders.size() > 0) {
			if (calendar.getTime().before(oilOrders.get(0).getDate())) {
				return HttpStatus.ALREADY_REPORTED;
			}
		}
		
		// Otherwise create new oil order with current date 
		oilOrder.setDate(new Date());
		oilOrderService.createOilOrder(oilOrder);
			
		return HttpStatus.OK;
	}
}
