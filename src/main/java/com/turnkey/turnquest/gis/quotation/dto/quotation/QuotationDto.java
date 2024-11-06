package com.turnkey.turnquest.gis.quotation.dto.quotation;

import com.turnkey.turnquest.gis.quotation.enums.PartialType;
import com.turnkey.turnquest.gis.quotation.enums.PolicyInterfaceType;
import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class QuotationDto {
    private Long id;

    private Long renewalBatchNo;

    private String quotationNo;

    private Long revisionNo;

    private Long clientId;

    private Long agencyId;

    private Long insurerOrgId;

    private Long panelId;

    private Long branchId;

    private Long currencyId;

    private Long coverToDate;

    private Long coverFromDate;

    private BigDecimal totalSumInsured;

    private String comments;

    private Long date;

    private String currentStatus = "D";

    private String status = "NB";

    private BigDecimal premium;

    private BigDecimal installmentPremium;

    private BigDecimal installmentCommission;

    private BigDecimal commissionAmount;

    private String authorizedBy;

    private Long dateAuthorized;

    private YesNo confirmed;

    private String confirmedBy;

    private Long dateConfirmed;

    private Long parentRevision;

    private Long subAgentId;

    private String subAgentShortDescription;

    private BigDecimal subAgntCommission;

    private String clientType;

    private String originalQuotNo;

    private Long webClientId;

    private Long datePrepared;

    private String paymentFrequency;

    private BigDecimal currencyRate;

    private String currencySymbol;

    private Long webPolicyId;

    private String policyNo;

    private Long policyId;

    private String quotationSourceId;

    private BigDecimal ltaCommission;

    private Long pipCode;

    private Long organizationId;

    private BigDecimal withholdingTax;

    private String paymentRef;

    private Long receiptId;

    private String sourceRef;

    private boolean readStatus = false;

    private boolean renewal = false;

    private boolean valued = false;

    private String paymentMode;

    private PolicyInterfaceType interfaceType;

    private PartialType partialType = PartialType.ORIGINAL;

    private Boolean isProposalConsented = false;

    private List<QuotationProductDto> quotationProducts;

    private BigDecimal basicPremium;

    private BigDecimal grossPremium;

    private Long lastValuationDate;
}
