package com.turnkey.turnquest.gis.quotation.dto.computation;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountType {
    private Long id;
    private String code;
    private String accountType;
    private String typeId;
    private BigDecimal withHoldingTaxRate;
    private String receiptsIncludeCommission;
}
