package com.turnkey.turnquest.gis.quotation.dto.underwriting;

import lombok.Data;

@Data
public class PolicyValuationDto {

    private Long id;

    private String code;

    private Long requestValuationDate;

    private Long valuerOrganizationId;

    private String rpt;

    private Long clientId;

    private String riskId;

    private Long policyId;

    private String surveyValue;

    private Long entryDate;

    private String approved;

    private String approvedDate;

    private String approvedByOrganizationId;

    private Long organizationId;
}
