package com.vedanta.vpmt.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.vedanta.vpmt.web.service.BusinessUnitService;
import com.vedanta.vpmt.web.service.MultipartFileService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "form-file")
@Slf4j
public class MultipartFileController {

	private static String VIEW_NAME = "web/file/view";
	
	@Autowired
	private MultipartFileService multipartFileService;
	
	@Autowired
	private BusinessUnitService businessUnitService;
	
	@RequestMapping(value = "get", method = RequestMethod.GET)
	public ModelAndView getScoreCardForScorecardId() {

		ModelAndView mav = new ModelAndView(VIEW_NAME);
		Map<String, Object> viewData = new HashMap<>();
		viewData.put("businessUnits", businessUnitService.getAllBusinessUnits());
		mav.addObject("data", viewData);
		
		return mav;
	}
	
	
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public ModelAndView uploadFileHandler(
			@RequestParam("businessUnitId") Long businessUnitId,
			@RequestParam("monthId") Integer monthId,
			@RequestParam("file") MultipartFile file) {

		Map<String, Object> viewData = new HashMap<>();
		
		
		if (!file.isEmpty() && (file.getOriginalFilename().endsWith("xls") || file.getOriginalFilename().endsWith("xlsx"))) {
			multipartFileService.importFile(file,businessUnitId,monthId);
			viewData.put("code", "200");
		} else {
			viewData.put("code", "500");
			viewData.put("desc", "Your File ");
		}
		
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		viewData.put("businessUnits", businessUnitService.getAllBusinessUnits());
		mav.addObject("data", viewData);
		return mav;
	}
	
    @RequestMapping(value = "format-download",method = RequestMethod.GET)
    public void doDownload(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
    	
    	multipartFileService.getDownload(request, response);
    }
}
