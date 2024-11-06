package com.turnkey.turnquest.gis.quotation.projections;

import com.turnkey.turnquest.gis.quotation.enums.ValuationStatus;
import com.turnkey.turnquest.gis.quotation.enums.YesNo;

import java.math.BigDecimal;
import java.util.List;

public interface QuotationProjection {

    String getQuotationNo();

    String getStatus();

    String getPaymentRef();

    Long getOrganizationId();

    Long getCurrencyId();

    Long getPanelId();

    Long getCoverFromDate();

    Long getClientId();

    String getPolicyNo();

    Long getPolicyId();

    List<QuotationProductView> getQuotationProducts();

    interface QuotationProductView {
        Long getProductId();

        BigDecimal getTotalSumInsured();

        BigDecimal getTotalPremium();

        BigDecimal getFutureAnnualPremium();

        BigDecimal getBasicPremium();

        BigDecimal getInstallmentAmount();

        BigDecimal getInstallmentPremium();

        YesNo getInstallmentAllowed();

        List<QuotationRiskView> getQuotationRisks();

        List<QuotationProductTaxView> getQuotationProductTaxes();
    }

    interface QuotationRiskView {
        String getRiskId();

        String getQuotationNumber();

        Long getWithEffectFromDate();

        Long getWithEffectToDate();

        Long getCoverTypeId();

        String getCoverTypeCode();

        Long getSubClassId();

        BigDecimal getTotalPremium();

        BigDecimal getAnnualPremium();

        BigDecimal getFutureAnnualPremium();

        BigDecimal getValue();

        Long getValuerOrgId();

        ValuationStatus getValuationStatus();

        Long getProductSubClassId();

    }

    interface QuotationProductTaxView {
        BigDecimal getTaxAmount();

        String getTransactionTypeCode();

        String getTaxRateType();

        String getTaxType();

        String getTaxRateDescription();
    }

}
