package com.turnkey.turnquest.gis.quotation.dto.billing;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransmittalDto {
    private Long id;

    private String policyNumber;

    private String claimNumber;

    private Long policyBatchNumber;

    private String underwritingClaimTransaction;

    private String policyRenewalBatch;

    private String transactionRefNo;

    private BigDecimal receiptAmount;

    private BigDecimal amount;

    private String ipayReferenceNo;

    private Long quotationId;

    private Long claimId;

    private Long csdId;

    private String iPayStatus;

    private Long statusDate;

    private String iPayReferenceType;

    private Long quotationProductId;

    private String quotationNo;

    private String oldTransactionRefNo;

    private Long organizationId;

    private String account;

    private String accountName;

}
