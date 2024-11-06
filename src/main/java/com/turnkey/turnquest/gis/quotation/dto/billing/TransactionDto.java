package com.turnkey.turnquest.gis.quotation.dto.billing;

import lombok.Data;

@Data
public class TransactionDto {

    private String documentRef;

    private String quotationNumber;

    private String policyNo;

    private String claimNo;

    private Long productId;

    private Long policyBatchNo;

    private String productCode;

    private String btrTransactionCode;

    private String clientPolicyNo;

    private String underwritingOrClaimTrans;

    private Long transactionDate;

    private String authorized;

    private String authorizedBy;

    private Long authorizedDate;

    private Long oldTransactionNo;

    private Long effectiveDate;

    private Long riskGroupId;

    private Long mailId;

    private String scheduleStatus;

    private String scheduleAuthorizedBy;

    private Long scheduleStatusDate;

    private String edpBatchNo;

    private String edpChecked;

    private String edpCheckedBy;

    private String edpCheckedDate;

    private String status;

    private Long incsId;

    private String policyCancledBy;

    private String rrcReceiptNo;

    private String claimReferenceNo;

    private Long xasCode;

    private Long organizationId;
}
