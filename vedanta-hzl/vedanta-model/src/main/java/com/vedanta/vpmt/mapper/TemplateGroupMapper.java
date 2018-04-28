package com.vedanta.vpmt.mapper;

import com.vedanta.vpmt.model.TemplateGroup;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import org.springframework.util.ObjectUtils;

/**
 * Created by manishsanger on 22/09/17.
 */
@Getter
@Setter
public class TemplateGroupMapper {
    private Long templateId;
    private String templateName;
    private List<TemplateGroup> templateGroups;

    public boolean containsGroupId(Long groupId){
    	
    	if(!ObjectUtils.isEmpty(this.templateGroups) && this.templateGroups.size() > 0 && !ObjectUtils.isEmpty(groupId) && groupId > 0){
    		for(TemplateGroup templateGroup : this.templateGroups){
    			if((long)templateGroup.getGroupId() == groupId){
    				return true;
    			}
    		}
    	}
    	return false;
    }
}
