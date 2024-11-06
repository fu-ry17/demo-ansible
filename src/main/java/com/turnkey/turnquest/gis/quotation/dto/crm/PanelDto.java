package com.turnkey.turnquest.gis.quotation.dto.crm;

import lombok.Data;

@Data
public class PanelDto {

    private Long id;

    private String code;

    private String description;

    private String panelType;

    private Long organizationId;

}
