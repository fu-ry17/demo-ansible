package com.turnkey.turnquest.gis.quotation.dto.crm;

import lombok.Data;

@Data
public class BranchDto {
    private Long id;

    private String code;

    private Long registration;

    private String name;

    private String physicalAddress;

    private String postalAddress;

    private String telephoneNumber;

    private Long organizationId;

}
