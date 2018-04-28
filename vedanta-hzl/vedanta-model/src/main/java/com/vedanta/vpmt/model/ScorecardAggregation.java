package com.vedanta.vpmt.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="scorecard_aggregation")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class ScorecardAggregation implements Serializable {
	
	  private static final long serialVersionUID = -592510226348544877L;
	
	   @Id
	   @GeneratedValue(strategy = GenerationType.AUTO)
	   private Long id;
	   
	   @Column(name="contract_id")
	   private Long contractId;
	   
	   @Column(name="contract_number")
	   private String contractNumber;

	   @Column(name="vendor_id")
	    private Long vendorId;
        
	   @Column(name="vendor_code")
	    private String vendorCode;

	   @Column(name="category_id") 
	   private Long categoryId;
        
	   @Column(name="category_name")
	    private String categoryName;
	   
	   @Column(name="subcategory_id")
	    private Long subCategoryId;

	   @Column(name="subcategory_name")
	    private String subCategoryName;

	   @Column(name="template_id")
	    private Long templateId;

	   @Column(name="template_name")
	   private String templateName;

	   @Column(name="plant_id")
	    private Long plantId;

	   @Column(name="plant_code")
	    private String plantCode;

	   @Column(name="department_id")
	    private Long departmentId ;

	   @Column(name="department_code")
	   private String departmentCode;

	   @Column(name="weight")
	   private int weight;
	   
	   @Column(name="total_Score")
	    private double totalScore;

	   @Column(name="target_score")
	   private int targetScore;

	   @Column(name="actual_Score")
	    private double actualScore;
	   
	   @Column(name="status")
	    private int status;

	   @Column(name="month_id")
	   private int monthId;

	   @Column(name="year_id")
	    private int yearId;
	   
	   @Column(name="due_date")
	   @Temporal(TemporalType.DATE)
	    private Date dueDate;

	   @Column(name="average_actual_score")
	   private double avgActualScore;

	   @Column(name="count_poi_tem")
	   private long countPOItem;

	   @Column(name="sum_actual_score")
	   private double sumActualScore;

	   @Column(name="sum_total_score")
	    private double sumTotalScore;

	   @Column(name="weighscore")
	   private double weightScore; 
	   
	   @Column(name="business_unit_id")
	   private Long businessUnitId;
	   
	   @Column(name="vendor_name")
	   private String vendorName;

	   
}
