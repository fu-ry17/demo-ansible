package com.turnkey.turnquest.gis.quotation.aki.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CertificateRequestDto {

    private String type;

    private Long memberCompanyId;

    private String typeOfCertificate;

    private String intermediaryIRANumber;

    private String typeOfCover;

    private String policyHolder;

    private String policyNumber;

    private String commencingDate;

    private String expiringDate;

    private String registrationNumber;

    private String chassisNumber;

    private String phoneNumber;

    private String bodyType;

    private Integer licensedToCarry;

    private String vehicleMake;

    private String vehicleModel;

    private Integer yearOfRegistration;

    private String engineNumber;

    private String email;

    private BigDecimal sumInsured;

    private String insuredPin;

    private Integer yearOfManufacture;

    private String hudumaNumber;

    private String vehicleType;

    private Integer tonnageCarryingCapacity;

    private Integer tonnage;

    private Long policyBatchNo;

    private Long quotationId;

    private Long insurerOrganizationId;
}
