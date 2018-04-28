package com.vedanta.vpmt.service;

import java.util.List;
import com.vedanta.vpmt.model.FormGroupDetail;

public interface FormGroupDetailService extends VedantaService<FormGroupDetail> {

	List<FormGroupDetail> getAllFormGroupDetails();

}
