package com.turnkey.turnquest.gis.quotation.dto.underwriting;

import lombok.Data;

@Data
public class PolicyScheduleDto {

    private Long ipuCode;

    private String riskId;

    private String make;

    private Long cubicCapacity;

    private Long yearOfManufacture;

    private Long yearOfRegistration;

    private Long carryCapacity;

    private Double value;

    private String bodyType;

    private String chasisNo;

    private String engineNo;

    private String color;

    private String logbook;

    private int noPrint;

    private Long certificateNo;

    private String certificateType;

    private String model;

    private Long organizationId;

}
