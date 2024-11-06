package com.turnkey.turnquest.gis.quotation.dto.billing;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReceiptDto {
    private String iPayRefNo;
    private String referenceNo;
    private BigDecimal amount;
    private String mpesaCode;
    private Long organizationId;
    private String direct;
    private String accountType;
    private Long currencyId;
    private String cancellation;
    private Long transactionDate;
    private Long quotationId;
    private String clientName;
    private String quotationNo;
    private String policyNo;
    private Long clientId;
}
