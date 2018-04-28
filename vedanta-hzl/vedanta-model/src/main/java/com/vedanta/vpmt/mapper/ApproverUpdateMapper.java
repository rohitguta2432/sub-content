package com.vedanta.vpmt.mapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApproverUpdateMapper {

	private Long businessUnitId;
	private Integer monthId;
	private String contractNumber;
	private String contractManagerId;
	private String ApproverById;
	private String ApproverByName;
	private int status = 0;
	private String desc = "Checked";
}
