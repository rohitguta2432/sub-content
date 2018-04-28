package com.vedanta.vpmt.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.model.GroupField;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by manishsanger on 22/09/17.
 */
@Getter
@Setter
public class GroupFieldMapper {
	private Long groupId;
	private String groupName;
	private List<GroupField> groupFields;

	public GroupFieldMapper() {
		groupFields = new ArrayList<GroupField>(1000);
	}

	public boolean containsFielId(Long fieldId) {

		if (!ObjectUtils.isEmpty(this.groupFields) && this.groupFields.size() > 0 && !ObjectUtils.isEmpty(fieldId)
				&& fieldId > 0) {
			for (GroupField groupField : this.groupFields) {
				if ((long) groupField.getFieldId() == 	fieldId ){
					return true;
				}
			}
		}
		return false;
	}
}
