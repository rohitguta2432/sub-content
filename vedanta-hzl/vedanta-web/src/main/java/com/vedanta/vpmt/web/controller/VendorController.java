package com.vedanta.vpmt.web.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.vedanta.vpmt.model.BusinessUnit;
import com.vedanta.vpmt.model.Plant;
import com.vedanta.vpmt.model.Vendor;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.service.PlantService;
import com.vedanta.vpmt.web.service.VendorService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "vendor")
@Slf4j
public class VendorController {
	private final static String USER_VIEW_NAME = "web/vendor_listing";
	private final static String USER_UPDATE_NAME = "web/vendor_update";
	public final static String IS_SAVE = "issave";

	private final static String USER_NEW_USER = "web/vendor_new";

	@Autowired
	private VendorService vendorService;

	@Autowired
	private PlantService plantService;

	@RequestMapping(value = "get", method = RequestMethod.GET)
	public ModelAndView getClientView(@RequestParam(value = "success", required = false) boolean success,
			@RequestParam(value = "action", required = false) String action,
			@RequestParam(value = "fieldName", required = false) String fieldName,
			@RequestParam(value = "buId", required = false) Long buId) {
		ModelAndView mav = new ModelAndView(USER_VIEW_NAME);
		List<Vendor> vendorList = null;
		List<BusinessUnit> businessUnitList = null;
		List<Plant> plants = null;

		try {
			/* vendorList = vendorService.getAllVendors(); */

			businessUnitList = plantService.getAllBusinessUnits();

			if (buId != null)
				vendorList = vendorService.getAllVendorsByBusinessUnitId(buId);
			else {
				if (businessUnitList.size() > 0)
					vendorList = vendorService.getAllVendorsByBusinessUnitId(businessUnitList.get(0).getId());
				else
					vendorList = new ArrayList<Vendor>();
			}
			plants = plantService.getAllPlants();

		} catch (VedantaWebException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		mav.addObject(IS_SAVE, success);
		mav.addObject("action", action);
		mav.addObject("vendors", vendorList);
		mav.addObject("fieldName", fieldName);
		mav.addObject("businessUnits", businessUnitList);
		mav.addObject("plants", plants);
		mav.addObject("searchBussinessId", buId);

		success = false;

		return mav;
	}

	private final static String DASHBOARD_VIEW_NAME = "web/create_vendor";

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView login(Principal principal) {

		if (principal == null) {
			log.info("Cannot provide access to unauthenticated vendor");
			throw new AccessDeniedException("Cannot provide access to unauthenticated vendor");
		}

		ModelAndView mav = new ModelAndView(DASHBOARD_VIEW_NAME);
		// mav.addObject("history", null);
		return mav;

		// return new ModelAndView(DASHBOARD_VIEW_NAME);
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public ModelAndView getVendorById(@PathVariable("id") long id) {

		ModelAndView mav = new ModelAndView(USER_UPDATE_NAME);
		Vendor vendor = null;
		try {
			vendor = vendorService.getVendor(id);
		} catch (VedantaWebException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		mav.addObject("updateVendor", new Vendor());
		mav.addObject("vendor", vendor);
		return mav;
	}

	@RequestMapping(value = "newVendor", method = RequestMethod.GET)
	public ModelAndView addNewVendor(@RequestParam(required = false) boolean success) {
		ModelAndView mav = new ModelAndView(USER_NEW_USER);
		Vendor vendor = null;
		mav.addObject("createVendor", new Vendor());
		mav.addObject("vendor", vendor);
		return mav;
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public ModelAndView save(@ModelAttribute("updateVendor") Vendor vendor, Model model) {
		ModelAndView mav = new ModelAndView(USER_UPDATE_NAME);
		Boolean bool = false;
		try {
			Map<String, String> map = vendorService.vendorValidation(vendor);
			String message = map.get("fieldName");
			if (message.equals("validationSuccessfully")) {
				Vendor saveVendor = vendorService.Update(vendor);
				if (saveVendor != null) {
					bool = true;
				}
			} else {
				vendor = vendorService.getVendor(vendor.getId());
				mav.addObject("updateVendor", new Vendor());
				mav.addObject("vendor", vendor);
				mav.addObject("fieldName", message);
				return mav;
			}
		} catch (VedantaWebException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		return new ModelAndView("redirect:" + "/logoutsuccessfully?success=" + true);
	}

	@RequestMapping(value = "saveNewVendor", method = RequestMethod.POST)
	public ModelAndView saveNewVendor(@ModelAttribute("createVendor") Vendor vendor, Model model) {
		ModelAndView mav = new ModelAndView(USER_NEW_USER);
		Boolean bool = false;
		try {

			Vendor saveVendor = vendorService.Update(vendor);
		} catch (VedantaWebException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		return new ModelAndView("redirect:" + "get?success=" + bool + "&action=" + "created");
	}

	/*
	 * @RequestMapping(value = "delete/{id}", method = RequestMethod.GET) public
	 * ModelAndView deleteTemplate(@PathVariable("id") Long id) {
	 * 
	 * try { vendorService.deleteVendor(id); } catch (VedantaWebException e) {
	 * logger.error("Error getting vendor information"); throw new
	 * VedantaWebException("Error getting vendor information"); } return new
	 * ModelAndView("redirect:" + "/vendor/get"); }
	 */

}
