package com.vedanta.vpmt.mapper;

import java.util.List;

import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.model.FormGroup;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by manishsanger on 22/09/17.
 */
@Getter
@Setter
@NoArgsConstructor
public class FormGroupMapper {
	private Long formId;
    private String formName;
    private List<FormGroup> formGroups;

    public boolean containsGroupId(Long groupId){
    	
    	if(!ObjectUtils.isEmpty(this.formGroups) && this.formGroups.size() > 0 && !ObjectUtils.isEmpty(groupId) && groupId > 0){
    		for(FormGroup formGroup : this.formGroups){
    			if((long)formGroup.getFormGroupDetailId() == groupId){
    				return true;
    			}
    		}
    	}
    	return false;
    }
}
