package com.vedanta.vpmt.web.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vedanta.vpmt.model.Contract;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.util.RestServiceUtil;
import com.vedanta.vpmt.web.util.URLConstants;
import com.vedanta.vpmt.web.util.VedantaConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * <h3>This Class Implements the Contracts interface</h3>
 * 
 * @author SAUD AHMAD
 * 
 * @since 16-sept-2017
 * @version v.0.0
 */
@Service("contractService")
@Slf4j
public class ContractServiceImpl implements ContractService {

	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	@Autowired
	private RestServiceUtil restServiceUtil;

	@Override
	public List<Contract> getContracts() {

		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_CONTRACT_DETAILS, null, null,
					HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching all contracts details.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<Contract>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching all contracts details");
			throw new VedantaWebException("Error while fetching all contracts details");
		}
	}

	@Override
	public Contract get(long id) {
		int status;
		String url = String.format(URLConstants.GET_CONTRAT_DETAIL_BY_CONTRACT_ID, id);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching contract details");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Contract>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching contract details");
			throw new VedantaWebException("Error while fetching contract details");
		}
	}

	@Override
	public List<Contract> getContractByVendor(long id) {
		int status;
		String url = String.format(URLConstants.GET_CONTRAT_DETAIL_BY_VENDOR_ID, id);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching contract details");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<Contract>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching contract details");
			throw new VedantaWebException("Error while fetching contract details");
		}
	}

	@Override
	public Contract save(Contract entity) {

		if (entity == null) {
			throw new VedantaWebException("Contract cannot be null");
		}
		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.SAVE_CONTRACT_DETAIL, entity, null,
					HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while save contract.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Contract>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while saving contract ");
			throw new VedantaWebException("Error while saving contract");
		}
	}

	@Override
	public Contract update(long id, Contract entity) {
		if (entity == null && id <= 0) {
			throw new VedantaWebException("Contract cannot be null");
		}
		entity.setId(id);
		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.SAVE_CONTRACT_DETAIL, entity, null,
					HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while save contract.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Contract>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while saving contract ");
			throw new VedantaWebException("Error while saving contract");
		}
	}
}
