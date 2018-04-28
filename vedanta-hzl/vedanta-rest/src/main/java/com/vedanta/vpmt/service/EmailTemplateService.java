package com.vedanta.vpmt.service;

import java.util.List;

import com.vedanta.vpmt.model.EmailTemplate;
import com.vedanta.vpmt.model.User;

public interface EmailTemplateService extends VedantaService<EmailTemplate> {

	public List<EmailTemplate> getAllEmailTemplate();

	public User getUserByEmployeeId(String employeeId);

}
