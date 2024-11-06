package com.turnkey.turnquest.gis.quotation.dto.quotation;

import lombok.Data;

@Data
public class QuotationValuationInfoDto {
    private Long id;

    private String code;

    private Long requestValuationDate;

    private Long valuerOrganizationId;

    private String rpt;

    private Long clientId;

    private String riskId;

    private Long quotationId;

    private String quotationNo;

    private String surveyValue;

    private Long entryDate;

    private String approved;

    private String approvedDate;

    private String approvedByOrganizationId;

    private Long organizationId;
}
