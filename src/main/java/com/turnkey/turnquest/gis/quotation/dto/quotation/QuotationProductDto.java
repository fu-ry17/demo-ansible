package com.turnkey.turnquest.gis.quotation.dto.quotation;

import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class QuotationProductDto {
    private Long id;

    private String code;

    private Long foreignId;

    private Long productId;

    private String productShortDescription;

    private Long policyCoverFrom;

    private Long policyCoverTo;

    private Long withEffectFromDate;

    private Long withEffectToDate;

    private Long quotationId;

    private String quotationNo;

    private String currencySymbol;

    private String quotationRevisionNumber;

    private BigDecimal totalSumInsured = BigDecimal.ZERO;

    private BigDecimal totalPremium = BigDecimal.ZERO;

    private BigDecimal basicPremium = BigDecimal.ZERO;

    private String remarks;

    private BigDecimal futureAnnualPremium = BigDecimal.ZERO;

    private String status;

    private String fromBinder;

    private Long binderId;

    private Long agentId;

    private Long panelId;

    private String agentCode;

    private String converted = "Y";

    private BigDecimal marketerCommissionAmount;

    private BigDecimal commissionAmount = BigDecimal.ZERO;

    private BigDecimal withHoldingTax = BigDecimal.ZERO;

    private BigDecimal subAgentCommissionAmount;

    private BigDecimal longTermArrangementCommissionAmount;

    private String isQuickQuote;

    private YesNo installmentAllowed = YesNo.N;

    ///Partial Premiums
    private String frequencyCalculation;

    private String paymentFrequency;

    private Long productInstallmentId;

    private Long paidInstallmentNo = 0L;

    private BigDecimal paidInstallmentAmount = BigDecimal.ZERO;

    private BigDecimal installmentAmount = BigDecimal.ZERO;

    private BigDecimal installmentPremium = BigDecimal.ZERO;

    private BigDecimal outstandingCommission = BigDecimal.ZERO;

    private BigDecimal paidInstallmentComm = BigDecimal.ZERO;

    private BigDecimal commInstallmentAmount = BigDecimal.ZERO;

    private BigDecimal commInstallmentPremium = BigDecimal.ZERO;

    private BigDecimal outstandingInstallmentAmount = BigDecimal.ZERO;

    private Long nextInstallmentNo = 0L;

    private Long paidToDate;

    private Integer totalNoOfInstallments = 0;

    private Long maturityDate;

    private Long installmentPlan;

    private Date renewalNotificationDate;

    private List<QuotationRiskDto> quotationRisks;

    private List<QuotationProductTaxDto> quotationProductTaxes;

    private Long lastValuationDate;
}
