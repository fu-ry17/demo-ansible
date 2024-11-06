package com.turnkey.turnquest.gis.quotation.dto.crm;

import lombok.Data;


@Data
public class EntitiesDto {
    private Long id;

    private String imageUrl;

    private String iraRegistrationNumber;

    private String firstName;

    private String lastName;

    private String organizationName;

    private String emailAddress;

    private String phoneNumber;

    private String physicalAddress;

    private Long akiMemberCompanyId;

    private Boolean akiIntegrated;
}
