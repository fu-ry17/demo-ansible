package com.turnkey.turnquest.gis.quotation.dto.crm;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrganizationDto implements Serializable {
    private static final long serialVersionUID = -8331635382989319531L;
    private Long id;

    private Long entityId;

    private Long accountTypeId;

    private AccountTypeDto accountType;

    private EntitiesDto entities;
}
