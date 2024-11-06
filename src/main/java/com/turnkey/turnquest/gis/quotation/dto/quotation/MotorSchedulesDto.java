package com.turnkey.turnquest.gis.quotation.dto.quotation;

import lombok.Data;

@Data
public class MotorSchedulesDto {
    private Long id;

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

    private String tonnage;

    private int noPrint;

    private Long certificateNo;

    private String certificateType;

    private String model;

    private Long organizationId;
}
