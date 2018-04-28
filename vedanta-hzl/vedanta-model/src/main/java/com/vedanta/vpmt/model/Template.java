package com.vedanta.vpmt.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by manishsanger on 07/09/17.
 */
@Entity
@Table(name = "templates")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class Template implements Serializable {
    private static final long serialVersionUID = -5648753304765476477L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "slug")
    private String slug;

    @Column(name = "description")
    private String description;

    @Column(name = "title")
    private String title;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "sub_category_id")
    private Long subCategoryId;

    @Column(name = "target_score", columnDefinition = "int(5) DEFAULT 0")
    private int targetScore;

    @Column(name = "last_target_score", columnDefinition = "int(5) DEFAULT 0")
    private int lastTargetScore;

    @Column(name = "total_score", columnDefinition = "int(5) DEFAULT 100")
    private int totalScore;

    @Transient
    private Category category;

    @Transient
    private SubCategory subCategory;

    @Column(name = "frequency")
    private String frequency;

    @Column(name = "day_of_frequency")
    private int dayOfFrequency;

    @Column(name = "due_day_of_frequency")
    private int dueDayOfFrequency;

    @Column(name = "plant_id", columnDefinition = "bigint(20) default 0")
    private Long plantId = 0L;

    @Column(name = "plant_code")
    private String plantCode;

    @Column(name = "department_id", columnDefinition = "bigint(20) default 0")
    private Long departmentId = 0L;

    @Column(name = "business_unit_id", columnDefinition = "bigint(20) default 0")
    private Long businessUnitId = 0L;

    @Column(name = "department_code")
    private String departmentCode;

    @Column(name = "status", columnDefinition = "int(1) DEFAULT 1")
    private int status;

    @Column(name = "last_updated", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "template_id", referencedColumnName = "id")
    @Fetch(FetchMode.SELECT)
    private List<TemplateForm> templateForms;

    
    @Transient
    private List<Long> forms;
    
    @Transient
    private String userName;
    
    @Transient
    private Boolean isAllGroupAccess;

    public Boolean isContainsFormId(Long formId){
    
    	if(!ObjectUtils.isEmpty(this.templateForms) && this.templateForms.size() > 0){
    		return this.templateForms.stream().anyMatch(templateForm -> templateForm.getFormId() == formId);
    	}
    	
    	return false;
    }

}
