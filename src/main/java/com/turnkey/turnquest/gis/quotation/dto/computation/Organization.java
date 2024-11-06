package com.turnkey.turnquest.gis.quotation.dto.computation;

import lombok.Data;

@Data
public class Organization {
    private Long id;
    private Long entityId;
    private Long accountTypeId;
    private AccountType accountType;
    private Entities entities;

}
