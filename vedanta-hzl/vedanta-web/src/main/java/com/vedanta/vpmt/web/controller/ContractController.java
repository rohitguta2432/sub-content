package com.vedanta.vpmt.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.vedanta.vpmt.model.Contract;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.service.ContractService;

import lombok.extern.slf4j.Slf4j;

/**
 * <h3>This Controller manage the all Contracts activity</h3>
 * 
 * @author SAUD AHMAD
 * 
 * @since 16-sept-2017
 * @version v.0.0
 */
@Controller
@RequestMapping(value = "contract")
@Slf4j
public class ContractController {

	final static private String VIEWS = "web/contracts/";

	@Autowired
	private ContractService contractService;

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public ModelAndView getNewContract() {

		return new ModelAndView(VIEWS + "new_form_fields");
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public ModelAndView getUpdateContract(@PathVariable("id") Long id) {

		return new ModelAndView(VIEWS + "update_form_fields", "contract", contractService.get(id));
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public ModelAndView saveUpdateContract(@ModelAttribute("myContract") Contract myContract, Model model) {

		try {
			contractService.update(myContract.getId(), myContract);
			return new ModelAndView("redirect:" + "/contract");
		} catch (VedantaWebException e) {
			log.error("Error while updating contract data.");
			throw new VedantaWebException("Error while updating contract data.", e.code);
		}
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public ModelAndView getViewContract(@PathVariable("id") Long id) {

		return new ModelAndView(VIEWS + "view_form_fields", "contract", contractService.get(id));
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView getAllContract() {

		return new ModelAndView(VIEWS + "ContractListing", "contracts", contractService.getContracts());
	}

	@RequestMapping(value = "{vendorId}", method = RequestMethod.GET)
	public @ResponseBody List<Contract> getContractByVendorId(@PathVariable("vendorId") Long vendorId) {
		 List<Contract> list = null;
		try{
			list = contractService.getContractByVendor(vendorId);
			
			return list;
		}catch (VedantaWebException e) {
			log.error("Error while getting contract data by vendorId.");
			throw new VedantaWebException("Error while getting contract data by vendorId.", e.code);
		}
	}
	
	 /*@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	    public ModelAndView deleteField(@PathVariable("id") Long id) {

	        try {
	            contractService.deleteContract(id);
	        } catch (VedantaWebException e) {
	            logger.error("Error deleting field information");
	            throw new VedantaWebException("Error deleting field information");
	        }
	        return new ModelAndView("redirect:" + "/admin/template/field");
	    }*/
}
