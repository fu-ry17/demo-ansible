package com.turnkey.turnquest.gis.quotation.dto.Reports;

import com.turnkey.turnquest.gis.quotation.dto.crm.OrganizationDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class Quote {

    private String reportDate;

    private String quotationNo;

    private String paymentRef;

    private Long organizationId;

    private Long currencyId;

    private String currencyDescription;

    private Long panelId;

    private Long expiryDate;

    private Long clientId;

    private String policyNo;

    private String status;

    private Long policyId;

    private Long coverFromDate;

    private List<QuotePrdct> quotationProducts = new ArrayList<>();

    private OrganizationDto organization;

    private String generationTime;

    private String clientName;

    private String clientAddress;

    private String clientPhoneNumber;

    private String agentName;

    private String insurerImageUrl;

    private String expiryDateString;

}
