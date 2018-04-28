package com.vedanta.vpmt.mapper;


import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by manishsanger on 22/09/17.
 */
@Getter
@Setter
@AllArgsConstructor
public class DashboardDataMapper {
    private long contractId;

    private String contractNumber;

    private Long vendorId;

    private String vendorCode;

    private Long categoryId;

    private String categoryName;

    private Long subCategoryId;

    private String subCategoryName;

    private Long templateId;

    private String templateName;

    private Long plantId = 0L;

    private String plantCode;

    private Long departmentId = 0L;

    private String departmentCode;

    private int weight;

    private double totalScore;

    private int targetScore;

    private double actualScore;

    private int status;

    private int monthId;

    private int yearId;

    private Date dueDate;


    private double avgActualScore;

    private long countPOItem;

    private double sumActualScore;

    private double sumTotalScore;

    private double weightScore; 

}
