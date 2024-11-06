package com.turnkey.turnquest.gis.quotation.dto.crm;

import lombok.Data;


@Data
public class AgencyDto {
    private Long id;

    private Long panelId;

    private Long insurerId;

    private Long organizationId;

    private String code;

    private String agentLicenceNo;

    private Long withHoldingTax;

    private OrganizationDto organization;
}
