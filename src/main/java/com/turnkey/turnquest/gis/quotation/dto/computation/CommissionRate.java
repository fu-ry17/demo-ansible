package com.turnkey.turnquest.gis.quotation.dto.computation;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CommissionRate {
    private BigDecimal id;
    private String code;
    private String transactionTypeCode;
    private String panelId;
    private BigDecimal rate;
}
