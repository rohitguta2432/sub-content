package com.vedanta.vpmt.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vedanta.vpmt.model.HumanResource;
import com.vedanta.vpmt.web.service.HumanResourceService;
import com.vedanta.vpmt.web.service.PlantService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "humanResource")
@Slf4j
public class HumanResourceController {

	@Autowired
	private PlantService plantService;

	@Autowired
	private HumanResourceService humanResourceService;

	private final static String HR_CREATE = "web/hr/hr_create";
	private final static String HR_LIST = "web/hr/hr_view";

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public ModelAndView save() {
		final ModelAndView mav = new ModelAndView(HR_CREATE);
		mav.addObject("businessUnits", plantService.getAllBusinessUnitsByRole());
		mav.addObject("humanResource", new HumanResource());
		return mav;
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ModelAndView saveHumanResource(@ModelAttribute("humanResource") HumanResource humanResource,
			RedirectAttributes redirectAttributes) {
		HumanResource humanResourceDetails = humanResourceService.checkExistingHumanResource(humanResource);
		if (ObjectUtils.isEmpty(humanResourceDetails)) {
			humanResource.setStatus(1);
			humanResourceService.save(humanResource);
			redirectAttributes.addFlashAttribute("success", "Human resource saved successfully");
		} else {
			redirectAttributes.addFlashAttribute("error", "Human resource already exist for given plant");
		}

		return new ModelAndView("redirect:" + "/humanResource/create");
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView plantHeadList() {
		final ModelAndView mav = new ModelAndView(HR_LIST);
		log.info("fetching all human resource of plant");
		mav.addObject("humanResource", humanResourceService.getAllHumanResource());
		mav.addObject("businessUnits", plantService.getAllBusinessUnits());
		return mav;

	}

	@RequestMapping(value = "update/{humanResourceId}", method = RequestMethod.GET)
	public ModelAndView getHumanResourceById(@PathVariable("humanResourceId") final Long humanResourceId) {
		final ModelAndView mav = new ModelAndView(HR_CREATE);
		mav.addObject("businessUnits", plantService.getAllBusinessUnitsByRole());
		mav.addObject("humanResource", humanResourceService.getHumanResourceById(humanResourceId));
		return mav;

	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public ModelAndView deleteHumanResource(@PathVariable("id") final Long humanResourceId) {
		humanResourceService.deleteHumanResource(humanResourceId);
		return new ModelAndView("redirect:" + "/humanResource");
	}

}
