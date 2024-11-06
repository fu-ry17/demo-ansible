package com.turnkey.turnquest.gis.quotation.dto.endorsement;

import com.turnkey.turnquest.gis.quotation.enums.ValuationStatus;
import com.turnkey.turnquest.gis.quotation.enums.YesNo;

import java.math.BigDecimal;
import java.util.List;

public record PolicyActiveRiskDto(


        Long organizationId,
        Long productId,
        Long binderId,
        Long agentId,
        Long insurerOrgId,
        String quotationNo,
        BigDecimal annualPremium,
        String productShortDescription,
        String remarks,
        String agentCode,
        Long maturityDate,
        Long productGroupId,
        String riskId,
        String certificateNumber,
        Long withEffectFromDate,
        Long withEffectToDate,
        Long coverTypeId,
        String coverTypeCode,
        Long subClassId,
        Long productSubClassId,
        Long clientId,
        BigDecimal totalPremium,
        BigDecimal outstandingTotalPremium,
        BigDecimal basicPremium,
        BigDecimal outstandingBasicPremium,
        BigDecimal futureAnnualPremium,
        BigDecimal value,
        BigDecimal commissionAmount,
        BigDecimal outstandingCommissionAmount,
        BigDecimal commissionRate,
        BigDecimal withHoldingTax,
        BigDecimal outstandingWithHoldingTax,
        String installmentFrequency,
        Long underwritingYear,
        ValuationStatus valuationStatus,
        String policyStatus,
        String riskStatus,
        String riskInstallmentStatus,
        String policyNumber,
        Long policyId,
        String quoteNumber,
        Long quoteId,
        Long currencyId,
        Long panelId,
        Long agencyId,
        Long insurerId,
        YesNo isInstallmentBased,
        YesNo isAnnualRisk,
        Long installmentNumber,
        Long totalInstallment,
        Long valuerOrgId,
        Long valuerBranchId,
        String paymentRef,
        YesNo ncdDocumentUploaded,
        Long productionDate,
        List<DocumentDto> documents,
        String schedules,
        YesNo lapsedStatus,
        List<PolicyActiveRiskTaxDto> taxes,
        List<PolicyActiveRiskSectionDto> sections,
        Boolean isRenewal
) {
}
