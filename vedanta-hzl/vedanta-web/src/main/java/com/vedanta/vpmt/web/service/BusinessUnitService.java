package com.vedanta.vpmt.web.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vedanta.vpmt.model.BusinessUnit;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.util.RestServiceUtil;
import com.vedanta.vpmt.web.util.URLConstants;
import com.vedanta.vpmt.web.util.VedantaConstant;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BusinessUnitService {

	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Autowired
    private RestServiceUtil restServiceUtil;
    
    public List<BusinessUnit> getAllBusinessUnits() {

        int status;
        try {
            JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_BUSINESS_UNITS, null, null, HttpMethod.GET);
            status = response.get(VedantaConstant.STATUS_CODE).intValue();
            if (status != 200) {
                throw new VedantaWebException("API not responded while fetching BusinessUnit listing.");
            }
            String data = response.get(VedantaConstant.DATA).toString();
            return OBJECT_MAPPER.readValue(data, new TypeReference<List<BusinessUnit>>() {
            });
        } catch (VedantaWebException | IOException e) {
        	
        	String errorCode = e.getMessage();
			if(errorCode.equals("401")){
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching BusinessUnit listing",401);
			}
        	
            log.error("Error while fetching BusinessUnit listing");
            throw new VedantaWebException("Error while fetching BusinessUnit listing");
        }
    }
}
