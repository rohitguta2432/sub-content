package com.vedanta.vpmt.mapper;

import java.util.List;

import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.model.FormGroupField;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by manishsanger on 22/09/17.
 */
@Getter
@Setter
public class FormGroupFieldMapper {
    private Long groupId;
    private String groupName;
    private List<FormGroupField> groupFields;

    public boolean containsFieldId(Long fieldId){
    	
    	if(!ObjectUtils.isEmpty(this.groupFields) && this.groupFields.size() > 0 && !ObjectUtils.isEmpty(fieldId) && fieldId > 0){
    		for(FormGroupField groupField : this.groupFields){
    			if((long)groupField.getFormFieldId() == fieldId){
    				return true;
    			}
    		}
    	}
    	return false;
    }
}
