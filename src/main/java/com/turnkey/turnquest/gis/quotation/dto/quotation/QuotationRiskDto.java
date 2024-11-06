package com.turnkey.turnquest.gis.quotation.dto.quotation;

import com.turnkey.turnquest.gis.quotation.enums.EndorsementType;
import com.turnkey.turnquest.gis.quotation.enums.ValuationStatus;
import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class QuotationRiskDto {
    private Long id;

    private String code;

    private Long foreignId;

    private Long policyRiskId;

    private Long quotationProductId;

    private Long binderId;

    private String riskId;

    private String itemDescription;

    private String certificateNo;

    private BigDecimal quotationRevisionNumber;

    private String clientType = "C";

    private Long quantity;

    private Long withEffectFromDate;

    private Long withEffectToDate;

    private Long coverTypeId;

    private String coverTypeCode;

    private Long subClassId;

    private Long productSubClassId;

    private boolean akiVerified;

    private Long clientId;

    private BigDecimal totalPremium;

    private BigDecimal basicPremium;

    private BigDecimal annualPremium;

    private BigDecimal futureAnnualPremium;

    private BigDecimal value;

    private String minimumPremiumUsed;

    private Long coverDays;

    private Long ncdLevel;

    private BigDecimal commissionRate = BigDecimal.ZERO;

    private BigDecimal commissionAmount = BigDecimal.ZERO;

    private BigDecimal longTermAgreementCommissionRate = BigDecimal.ZERO;

    private BigDecimal longTermAgreementCommissionAmount = BigDecimal.ZERO;

    private BigDecimal subAgentCommissionRate;

    private BigDecimal subAgentCommissionAmount;

    private String enforceCoverTypeMinimumPremium;

    private BigDecimal marketerCommissionAmount;

    private BigDecimal marketerCommissionRate;

    private BigDecimal withHoldingTax = BigDecimal.ZERO;

    private ValuationStatus valuationStatus = ValuationStatus.NONE;

    private EndorsementType endorsementType = EndorsementType.NONE;

    private YesNo installmentAllowed = YesNo.N;

    private String frequencyCalculation;

    private String paymentFrequency;

    private Long productInstallmentId;

    private Long paidInstallmentNo;

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

    private BigDecimal butCharge;

    private Long installmentPlan;

    private Long valuationNumber = 0L;

    private MotorSchedulesDto motorSchedules;

    private QuotationValuationInfoDto quotationValuationInfo;

    private List<QuoteDocumentDto> quoteDocument;

    private List<QuotationRiskSectionDto> quotationRiskSections;

    private List<QuotationRiskTaxDto> quotationRiskTaxes;

    private Long lastValuationDate;
}
