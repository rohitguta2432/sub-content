package com.vedanta.vpmt.mapper;

import java.util.List;

import com.vedanta.vpmt.model.Contract;
import com.vedanta.vpmt.model.Form;
import com.vedanta.vpmt.model.FormGroupDetail;
import com.vedanta.vpmt.model.User;
import com.vedanta.vpmt.model.Vendor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFormGroupDetailsMapper {

	private User user;
	private Vendor vendor;
	private Contract contract;
	private Form form;
	private List<FormGroupDetail> formGroupDetails;
	private int groupcount;
}
