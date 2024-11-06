package com.turnkey.turnquest.gis.quotation.dto.crm;

import com.turnkey.turnquest.gis.quotation.enums.ClientTypes;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class ClientDto {
    private Long id;

    private String imageUrl;

    private String firstName;

    private String lastName;

    private String emailAddress;

    private String phoneNumber;

    private String idNumber;

    private String physicalAddress;

    private String gender;

    private String kraPin;

    private Long entityId;

    private Long organizationId;

    private String companyName;

    private ClientTypes clientType;

}