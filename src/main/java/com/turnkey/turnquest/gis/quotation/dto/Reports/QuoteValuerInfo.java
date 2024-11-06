package com.turnkey.turnquest.gis.quotation.dto.Reports;

import lombok.Data;

import java.util.List;


@Data
public class QuoteValuerInfo {

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

    private Long organizationId;

    private String dateOfIssue;

    private String valuerEmail;

    private String valuerName;

    private String valuerPhysicalAddress;

    private String valuerTelephone;

    private List<String> valuerNote;

}
