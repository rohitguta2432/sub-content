package com.vedanta.vpmt.service;

import com.vedanta.vpmt.model.FieldOption;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FieldOptionService extends VedantaService<FieldOption> {
	@Transactional
	Boolean saveOptionsList(List<FieldOption> fieldOptions);

	List<FieldOption> getAllFieldOptionsByFieldId(long fieldId);

	Boolean delete(List<FieldOption> fieldOptions);
}
