package com.turnkey.turnquest.gis.quotation.dto.Reports;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuotePrdctTax {

    private BigDecimal taxAmount;

    private String taxAmountString;

    private String transactionTypeCode;

    private String taxRateType;

    private String taxType;

    private String taxRateDescription;

    private String taxWording;

}
