package com.vedanta.vpmt.web.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vedanta.vpmt.model.Category;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.util.RestServiceUtil;
import com.vedanta.vpmt.web.util.URLConstants;
import com.vedanta.vpmt.web.util.VedantaConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by manishsanger on 23/09/17.
 */
@Component
@Slf4j
public class CategoryService {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Autowired
    private RestServiceUtil restServiceUtil;

    public List<Category> getAllCategories() {

        int status;
        try {
            JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_CATEGORIES, null, null, HttpMethod.GET);
            status = response.get(VedantaConstant.STATUS_CODE).intValue();
            if (status != 200) {
                throw new VedantaWebException("API not responded while fetching template listing.");
            }
            String data = response.get(VedantaConstant.DATA).toString();
            return OBJECT_MAPPER.readValue(data, new TypeReference<List<Category>>() {
            });
        } catch (VedantaWebException | IOException e) {
        	
        	String errorCode = e.getMessage();
			if(errorCode.equals("401")){
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing",401);
			}
        	
            log.error("Error while fetching template listing");
            throw new VedantaWebException("Error while fetching template listing");
        }
    }

    public Category getCategoryDetail(long categoryId) {

        int status;
        try {
            String url = String.format(URLConstants.GET_CATEGORY_DETAIL, categoryId);
            JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
            status = response.get(VedantaConstant.STATUS_CODE).intValue();
            if (status != 200) {
                throw new VedantaWebException("API not responded while fetching template listing.");
            }
            String data = response.get(VedantaConstant.DATA).toString();
            return OBJECT_MAPPER.readValue(data, new TypeReference<Category>() {
            });
        } catch (VedantaWebException | IOException e) {
        	
        	String errorCode = e.getMessage();
			if(errorCode.equals("401")){
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing",401);
			}
        	
            log.error("Error while fetching template listing");
            throw new VedantaWebException("Error while fetching template listing");
        }
    }
    
    public List<Category> getCategoriesByBusinessUnitId(long businessUnitId) {

        int status;
        try {
            String url = String.format(URLConstants.GET_CATEGORIES_BY_BUSINESS_UNIT_ID, businessUnitId);
            JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
            status = response.get(VedantaConstant.STATUS_CODE).intValue();
            if (status != 200) {
                throw new VedantaWebException("API not responded while fetching template listing.");
            }
            String data = response.get(VedantaConstant.DATA).toString();
            return OBJECT_MAPPER.readValue(data, new TypeReference<List<Category>>() {
            });
        } catch (VedantaWebException | IOException e) {
        	
        	String errorCode = e.getMessage();
			if(errorCode.equals("401")){
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing",401);
			}
        	log.error("Error while fetching template listing");
            throw new VedantaWebException("Error while fetching template listing");
        }
    }

}
