package com.vedanta.vpmt.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vedanta.vpmt.model.Category;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by manishsanger on 23/09/17.
 */
@Controller
@RequestMapping(value = "category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "{categoryId}", method = RequestMethod.GET)
    public @ResponseBody
    Category getCategoryDetails(@PathVariable("categoryId") Long categoryId) {
        Category categoryDetail = null;
        try{
            categoryDetail = categoryService.getCategoryDetail(categoryId);
            return categoryDetail;
        } catch (VedantaWebException e) {
        	
            log.error("Error fetching category details");
            throw new VedantaWebException("Error fetching category details",e.code);
        }
    }
    
    @RequestMapping(value = "business-unit-id/{businessUnitId}", method = RequestMethod.GET)
    public @ResponseBody
    List<Category> getCategoriesByBusinessUnitId(@PathVariable("businessUnitId") Long businessUnitId) {
        
        try{
            
            return categoryService.getCategoriesByBusinessUnitId(businessUnitId);
        } catch (VedantaWebException e) {
        	
            log.error("Error fetching category details");
            throw new VedantaWebException("Error fetching category details",e.code);
        }
    }
}
