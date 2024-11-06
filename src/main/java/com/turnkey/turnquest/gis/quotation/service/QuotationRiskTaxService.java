package com.turnkey.turnquest.gis.quotation.service;

import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyRiskTaxDto;
import com.turnkey.turnquest.gis.quotation.model.QuotationRisk;
import com.turnkey.turnquest.gis.quotation.model.QuotationRiskTax;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface QuotationRiskTaxService {

    Optional<QuotationRiskTax> find(Long id);

    QuotationRiskTax create(QuotationRiskTax quotationRiskTax);

    QuotationRiskTax update(QuotationRiskTax quotationRiskTax, Long id);

    List<QuotationRiskTax> findByQuotationRiskId(Long quotationRiskId);

    List<QuotationRiskTax> createMultiple(List<QuotationRiskTax> addedQuotationRiskTaxes);

    QuotationRiskTax updateTaxAmount(BigDecimal totalTaxAmount, Long id);

    List<PolicyRiskTaxDto> convertToPolicyRiskTaxes(List<QuotationRiskTax> quotationRiskTaxes);

    PolicyRiskTaxDto convertToPolicyRiskTax(QuotationRiskTax quotationRiskTax);

    QuotationRiskTax saveQuickQuotationRiskTax(QuotationRisk quoteRisk, QuotationRiskTax quotationRiskTax);
}
