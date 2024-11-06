package com.turnkey.turnquest.gis.quotation.dto.crm;

import lombok.Data;

import java.util.Date;

@Data
public class IntroducerDto {
    private Long id;

    private String surname;

    private String otherNames;

    private Long staffNumber;

    private String groupCo;

    private String postalAddress;

    private String town;

    private String pin;

    private String idRegistrationNo;

    private Long mobileTelephone;

    private Long telephone;

    private Date dateOfBirth;

    private String remarks;

    private Long organizationId;

    private String email;
}
