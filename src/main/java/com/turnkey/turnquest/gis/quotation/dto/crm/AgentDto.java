package com.turnkey.turnquest.gis.quotation.dto.crm;

import lombok.Data;

import java.io.Serializable;

@Data
public class AgentDto implements Serializable {

    private static final long serialVersionUID = 6978037065862772265L;

    private Long id;

    private Long panelId;

    private Long insurerId;

    private Long organizationId;

    private String panelName;

    private String panelType;

    private OrganizationDto organization;

}
