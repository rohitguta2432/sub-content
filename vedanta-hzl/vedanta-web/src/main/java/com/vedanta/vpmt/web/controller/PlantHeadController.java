package com.vedanta.vpmt.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vedanta.vpmt.model.BusinessUnit;
import com.vedanta.vpmt.model.Plant;
import com.vedanta.vpmt.model.PlantHead;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.service.PlantHeadService;
import com.vedanta.vpmt.web.service.PlantService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "plantHead")
@Slf4j
public class PlantHeadController {

	@Autowired
	private PlantService plantService;

	@Autowired
	private PlantHeadService plantHeadService;

	private final static String PLANTHEAD_CREATE = "web/plantHead/plantHead_create";
	private final static String PLANTHEAD_LIST = "web/plantHead/plantHead_view";

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView plantHeadList() {
		ModelAndView mav = new ModelAndView(PLANTHEAD_LIST);
		List<PlantHead> plantHeadList = null;
		List<BusinessUnit> businessUnitList = null;
		try {
			plantHeadList = plantHeadService.getAllPlantHead();
			businessUnitList = plantService.getAllBusinessUnits();
			mav.addObject("plantHead", plantHeadList);
			mav.addObject("businessUnits", businessUnitList);
			return mav;
		} catch (VedantaWebException e) {
			log.error("Error listing plant heads data");
			throw new VedantaWebException("Error listing plant heads data.", e.code);
		}
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public ModelAndView save() {
		ModelAndView mav = new ModelAndView(PLANTHEAD_CREATE);
		List<BusinessUnit> businessUnits = new ArrayList<>();
		businessUnits = plantService.getAllBusinessUnitsByRole();
		mav.addObject("businessUnits", businessUnits);
		mav.addObject("plantHead", new PlantHead());
		return mav;
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ModelAndView savePlantHead(@ModelAttribute("plantHead") PlantHead plantHead,
			RedirectAttributes redirectAttributes) {
		PlantHead plantHeadDetails = null;
		try {
			plantHeadDetails = plantHeadService.checkExistingPlantHead(plantHead);
			if (ObjectUtils.isEmpty(plantHeadDetails)) {
				plantHead.setStatus(1);
				plantHeadService.save(plantHead);
				redirectAttributes.addFlashAttribute("success", "Plant Head saved successfully");
			} else {
				redirectAttributes.addFlashAttribute("error", "User already exist for given plant");
			}

		} catch (VedantaWebException e) {
			log.error("Error saving plant head information");
			throw new VedantaWebException("Error saving plant head information", e.code);
		}
		return new ModelAndView("redirect:" + "/plantHead/create");
	}

	@RequestMapping(value = "update/{plantHeadId}", method = RequestMethod.GET)
	public ModelAndView getPlantHeadById(@PathVariable("plantHeadId") Long plantHeadId) {

		ModelAndView mav = new ModelAndView(PLANTHEAD_CREATE);
		PlantHead plantHead;
		List<BusinessUnit> businessUnits = new ArrayList<>();

		try {
			plantHead = plantHeadService.getPlantHeadById(plantHeadId);
			businessUnits = plantService.getAllBusinessUnitsByRole();
			mav.addObject("businessUnits", businessUnits);
			mav.addObject("plantHead", plantHead);
			return mav;
		} catch (VedantaWebException e) {
			log.error("Error getting plant head information");
			throw new VedantaWebException("Error getting plant head information", e.code);
		}
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public ModelAndView deletePlantHead(@PathVariable("id") Long id) {

		try {
			plantHeadService.deletePlantHead(id);
		} catch (VedantaWebException e) {
			log.error("Error while deleting plant head information");
			throw new VedantaWebException("Error getting deleting plant head  information", e.code);
		}
		return new ModelAndView("redirect:" + "/plantHead");
	}

	@RequestMapping(value = "business-unit-id/{businessUnitId}", method = RequestMethod.GET)
	public @ResponseBody List<Plant> getPlantsByBusinessUnitId(@PathVariable("businessUnitId") Long businessUnitId) {

		try {

			return plantService.getPlantByBusinessId(businessUnitId);
		} catch (VedantaWebException e) {
			log.error("Error fetching category details");
			throw new VedantaWebException("Error fetching category details", e.code);
		}
	}

	@RequestMapping(value = "plant-name/{plantName}", method = RequestMethod.GET)
	public @ResponseBody List<Plant> getPlantsByPlantName(@PathVariable("plantName") String plantName) {

		try {
			return plantService.getPlantByPlantName(plantName);
		} catch (VedantaWebException e) {
			log.error("Error fetching category details");
			throw new VedantaWebException("Error fetching category details", e.code);
		}
	}

}
