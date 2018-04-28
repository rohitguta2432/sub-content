package com.vedanta.vpmt.web.service;

import java.util.List;

import com.vedanta.vpmt.model.TargetValue;

public interface TargetValueService extends Service<TargetValue> {

	List<TargetValue> getTargetValues();

	TargetValue deleteTargetValue(long targetvalueId);
}
