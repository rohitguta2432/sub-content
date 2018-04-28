package com.vedanta.vpmt.web.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vedanta.vpmt.model.BusinessUnit;
import com.vedanta.vpmt.model.Plant;
import com.vedanta.vpmt.model.User;
import com.vedanta.vpmt.model.UserDepartment;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.service.EmailTemplateService;
import com.vedanta.vpmt.web.service.PlantService;
import com.vedanta.vpmt.web.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "user")
@Slf4j
public class UserController {
	private final static String USER_VIEW_NAME = "web/user_view";
	private final static String USER_UPDATE_NAME = "web/user_update";
	public final static String IS_SAVE = "issave";

	private final static String USER_NEW_USER = "web/user_new";

	@Autowired
	private UserService userService;

	@Autowired
	private EmailTemplateService emailTemplateServices;

	@Autowired
	private PlantService plantService;

	@RequestMapping(value = "get", method = RequestMethod.GET)
	public ModelAndView getClientView(@RequestParam(value = "success", required = false) boolean success,
			@RequestParam(value = "action", required = false) String action,
			@RequestParam(value = "fieldName", required = false) String fieldName,
			@RequestParam(value = "buId", required = false) Long buId)

	{
		ModelAndView mav = new ModelAndView(USER_VIEW_NAME);
		List<User> userList = null;
		try {
			List<BusinessUnit> AllbussinessUnit = plantService.getAllBusinessUnitsByRole();

			if (buId == null) {
				userList = userService.userByBusinessUnitId(AllbussinessUnit.get(0).getId());

			} else {
				userList = userService.userByBusinessUnitId(buId);
			}

			mav.addObject("AllbussinessUnit", AllbussinessUnit);

		} catch (VedantaWebException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		mav.addObject(IS_SAVE, success);
		mav.addObject("action", action);
		mav.addObject("users", userList);
		mav.addObject("fieldName", fieldName);
		mav.addObject("searchBussinessId", buId);

		success = false;

		return mav;
	}

	@RequestMapping(value = "get-all-users", method = RequestMethod.GET)
	public @ResponseBody List<User> getAllUsers() {
		List<User> userList = null;
		try {
			userList = userService.getAllUsers();
			return userList;
		} catch (VedantaWebException e) {
			log.error("Error fetching all users");
			throw new VedantaWebException("Error fetching all users", e.code);
		}
	}

	@RequestMapping(value = "getAllUsersByBusinessUnit", method = RequestMethod.GET)
	public @ResponseBody List<User> getAllUsersByBusinessUnit(@PathVariable("buId") Long buId) {
		List<User> userList = null;
		try {

			userList = userService.userByBusinessUnitId(buId);

			return userList;
		} catch (VedantaWebException e) {
			log.error("Error fetching all users by business unit");
			throw new VedantaWebException("Error fetching all users by business unit", e.code);
		}
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public ModelAndView getUserById(@PathVariable("id") long id) {

		ModelAndView mav = new ModelAndView(USER_UPDATE_NAME);
		User user = null;

		try {

			user = userService.getUser(id);

		} catch (VedantaWebException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		mav.addObject("updateUser", new User());
		/*
		 * mav.addObject("userDepartment", userDepartment); mav.addObject("userOffice",
		 * userOffice);
		 */
		mav.addObject("user", user);
		return mav;
	}

	@RequestMapping(value = "newUser", method = RequestMethod.GET)
	public ModelAndView addNewUser(@RequestParam(required = false) boolean success) {
		ModelAndView mav = new ModelAndView(USER_NEW_USER);
		List<BusinessUnit> businessUnits = new ArrayList<>();
		businessUnits = plantService.getAllBusinessUnitsByRole();
		User user = null;
		mav.addObject("createUser", new User());
		mav.addObject("businessUnits", businessUnits);
		mav.addObject("user", user);
		return mav;
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public ModelAndView save(@ModelAttribute("updateUser") User user, Model model) {
		ModelAndView mav = new ModelAndView(USER_UPDATE_NAME);
		Boolean bool = false;
		try {
			Map<String, String> map = userService.userValidation(user);
			String message = map.get("fieldName");
			if (message.equals("validationSuccessfully")) {
				User saveUser = userService.Update(user);
				if (saveUser != null) {
					bool = true;
				}
			} else {
				user = userService.getUser(user.getId());
				mav.addObject("updateUser", new User());
				mav.addObject("user", user);
				mav.addObject("fieldName", message);
				return mav;
			}
		} catch (VedantaWebException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		if (!user.getAuthorities().equals("ROLE_USER_MANAGER"))
			return new ModelAndView("redirect:" + "get?success=" + bool + "&action=" + "updated");
		else
			return new ModelAndView("redirect:" + "/logoutsuccessfully?success=" + true);
	}

	@RequestMapping(value = "saveNewUser", method = RequestMethod.POST)

	public ModelAndView saveNewUser(@ModelAttribute("createUser") User user, Model model,
			RedirectAttributes attributes) {

		ModelAndView mav = new ModelAndView(USER_NEW_USER);
		Boolean bool = false;
		try {

			Map<String, String> map = userService.userValidation(user);
			String message = map.get("fieldName");
			if (message.equals("validationSuccessfully")) {
				if (user.getParentId().isEmpty() || user.getParentId() == "" || user.getParentId() == null)
					user.setParentId(null);
				if (user.getStatus() != 1)
					user.setStatus(0);
				User saveUser = userService.Update(user);
				if (saveUser != null) {
					bool = true;
				}
			} else {
				mav.addObject("createUser", new User());
				mav.addObject("user", user);
				mav.addObject("fieldName", message);
				return mav;
			}
		} catch (VedantaWebException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		attributes.addFlashAttribute("success", "User has been Added successfully.");
		return new ModelAndView("redirect:" + "/user/newUser");
	}

	@RequestMapping(value = "validateUserByloginId/{loginId}", method = RequestMethod.GET)
	@ResponseBody
	public String checkUserExistsOrNotByLoginId(@PathVariable("loginId") String loginId) {
		String message = "";
		User userDetails = userService.getUserByloginId(loginId);
		if (!ObjectUtils.isEmpty(userDetails)) {
			String userloginId = userDetails.getLoginId();

			if (loginId.equalsIgnoreCase(userloginId)) {
				message = "ExitsUser";
			}

		} else {
			message = "UserNotExists";
		}

		return message;
	}

	@RequestMapping(value = "validateUserByEmployeeId/{employeeId}", method = RequestMethod.GET)
	@ResponseBody
	public String checkUserExistsOrNotByEmployeeId(@PathVariable("employeeId") String employeeId) {
		String message = "";
		User userDetails = emailTemplateServices.getUserDetailByEmployeeId(employeeId);
		if (!ObjectUtils.isEmpty(userDetails)) {
			String userlEmployeeId = userDetails.getEmployeeId();

			if (employeeId.equalsIgnoreCase(userlEmployeeId)) {
				message = "ExitsUser";
			}

		} else {
			message = "UserNotExists";
		}
		return message;
	}

	@RequestMapping(value = "businessUnit/{businessId}", method = RequestMethod.GET)
	public @ResponseBody List<Plant> getPlantByBusinessId(@PathVariable("businessId") Long businessId) {
		List<Plant> plantDetail = null;
		try {
			plantDetail = plantService.getPlantByBusinessId(businessId);
			return plantDetail;
		} catch (VedantaWebException e) {
			log.error("Error fetching plant details");
			throw new VedantaWebException("Error fetching plant details", e.code);
		}
	}

	@RequestMapping(value = "userDepartmentByBuId/{businessId}", method = RequestMethod.GET)
	public @ResponseBody HashSet<UserDepartment> getUserDepartmentByBuId(
			@PathVariable("businessId") Long businessUnitId) {
		HashSet<UserDepartment> userDepartment = null;
		try {
			userDepartment = userService.userDepartmentByBuId(businessUnitId);
			return userDepartment;
		} catch (VedantaWebException e) {
			log.error("Error fetching plant details");
			throw new VedantaWebException("Error fetching plant details", e.code);
		}
	}

	@RequestMapping(value = "userByPlantHeadAndBusinessUnitId/{businessId}", method = RequestMethod.GET)
	public @ResponseBody List<User> userByPlantHeadAndBusinessUnitId(@PathVariable("businessId") Long businessId) {
		List<User> userDetail = null;
		try {
			userDetail = userService.userByPlantHeadAndBusinessUnitId(businessId);
			return userDetail;
		} catch (VedantaWebException e) {
			log.error("Error fetching user details by business id");
			throw new VedantaWebException("Error fetching  user details by business id", e.code);
		}
	}

	@RequestMapping(value = "userByHumanResourceAndBusinessUnitId/{businessId}", method = RequestMethod.GET)
	public @ResponseBody List<User> userByHumanResourceAndBusinessUnitId(
			@PathVariable("businessId") final Long businessId) {
		return userService.userByHumanResourceAndBusinessUnitId(businessId);
	}

	@RequestMapping(value = "getUserDepartmentByLimit/{businessUnitId}", method = RequestMethod.GET)
	public @ResponseBody List<UserDepartment> getUserDepartmentByLimit(@PathVariable("businessUnitId") Long businessUnitId) {
		return userService.getUserDepartmentByLimit(businessUnitId);
	}
	
	@RequestMapping(value = "getUserDepartmentByName/{name}/{buId}", method = RequestMethod.GET)
	public @ResponseBody List<UserDepartment> getUserDepartmentByName(@PathVariable("name") String name,
			@PathVariable("buId") Long buId) {
		return userService.getUserDepartmentByName(name, buId);
	}

}
