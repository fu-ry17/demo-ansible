package com.turnkey.turnquest.gis.quotation.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
@Data
public class ScheduleDetailsDto {
    private String policyNo;
    private String clientName;
    private String clientEmail;
    private String clientPIN;
    private String clientPhoneNumber;
    private String chassisNumber;
    private String engineNumber;
    private String vehicleMake;
    private String vehicleModel;
    private String bodyType;
    private Long vehicleManufactureYear;
    private Long vehicleRegistrationYear;
    private String vehicleRegistrationNumber;
    private String commencingDate;
    private String expiryDate;
    private Long coverTypeId;
    private String coverTypeCode;
    private BigDecimal sumInsured;
    private Long insurerOrgId;
}
