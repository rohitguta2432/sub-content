package com.vedanta.vpmt.web.controller;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.vedanta.vpmt.model.BusinessUnit;
import com.vedanta.vpmt.model.Support;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.service.PlantService;
import com.vedanta.vpmt.web.service.SupportService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "support")
@Slf4j
public class SupportController {

	private final static String SUPPORT_ADD_FORM = "web/support/support";
	private final static String SUPPORT_VIEW = "web/support/support_list";

	@Value("${file.location.support}")
	private String directory;

	@Autowired
	private SupportService supportService;

	@Autowired
	private PlantService plantService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView support() {
		ModelAndView mav = new ModelAndView(SUPPORT_ADD_FORM);
		return mav;
	}

	@RequestMapping(value = "support-list", method = RequestMethod.GET)
	public ModelAndView supportList() {
		ModelAndView mav = new ModelAndView(SUPPORT_VIEW);
		List<Support> supports;
		List<BusinessUnit> businessUnits;

		try {
			supports = supportService.getSupportList();
			businessUnits = plantService.getAllBusinessUnitsByRole();
			mav.addObject("supports", supports);
			mav.addObject("businessUnits", businessUnits);
			return mav;
		} catch (VedantaWebException e) {
			log.error("Error while fetching support information");
			throw new VedantaWebException("Error while fetching support information", e.code);
		}

	}

	@InitBinder
	public void init(WebDataBinder binder) {
		binder.setBindEmptyMultipartFiles(false);
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ModelAndView save(@ModelAttribute("support") Support support, RedirectAttributes attributes) {

		List<MultipartFile> filesName = support.getFiles();

		String folder = "supportFiles";
		String path = directory + folder;

		if (!new File(path).exists()) {
			new File(path).mkdir();
		}

		int count = 0;

		if (!ObjectUtils.isEmpty(filesName)) {

			List<String> fileNames = new ArrayList<String>();
			for (MultipartFile multipartFile : filesName) {
				try {

					DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
					String ext = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

					String fileName = df.format(new Date()) + "_" + count + "." + ext;
					String filePath = path + "/" + fileName;
					fileNames.add(fileName);

					multipartFile.transferTo(new File(filePath));
				} catch (IllegalStateException | IOException e) {

					log.error("Error while uploading support files ");
				}
				count++;

			}
			support.setFileList(fileNames);
			supportService.saveSupport(support);

		} else {
			supportService.saveSupport(support);
		}

		attributes.addFlashAttribute("success", "Support request has been submitted successfully.");

		return new ModelAndView("redirect:" + "/support");
	}

	@RequestMapping(value = "update", method = RequestMethod.PUT)
	public @ResponseBody String update(@RequestBody Support support) {
		ModelAndView mav = new ModelAndView(SUPPORT_VIEW);

		Support resultSupport;
		try {
			resultSupport = supportService.updateSupport(support);
			String result = new Gson().toJson(support);
			return result;
		} catch (VedantaWebException e) {
			log.error("Error while updating support information");
			throw new VedantaWebException("Error while updating support information", e.code);
		}

	}

}
