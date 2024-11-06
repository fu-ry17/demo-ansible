package com.turnkey.turnquest.gis.quotation.dto.crm;

import lombok.Data;

@Data
public class InsurerCurrencyDto {
    private Long id;

    private String code;

    private String symbol;

    private Long organizationId;

}