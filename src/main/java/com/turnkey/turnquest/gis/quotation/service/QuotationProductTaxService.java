package com.turnkey.turnquest.gis.quotation.service;

import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyTaxDto;
import com.turnkey.turnquest.gis.quotation.model.QuotationProduct;
import com.turnkey.turnquest.gis.quotation.model.QuotationProductTax;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface QuotationProductTaxService {

    Optional<QuotationProductTax> find(Long id);

    QuotationProductTax create(QuotationProductTax quotationProductTax);

    QuotationProductTax update(QuotationProductTax quotationProductTax, Long id);

    List<QuotationProductTax> findByQuotationProductId(Long quotationProductId);

    List<QuotationProductTax> createMultiple(List<QuotationProductTax> addedQuotationProductTaxes);

    QuotationProductTax updateTaxAmount(BigDecimal totalTaxAmount, Long id);

    List<PolicyTaxDto> convertToPolicyTaxes(List<QuotationProductTax> quotationProductTaxes);

    PolicyTaxDto convertToPolicyTax(QuotationProductTax quotationProductTax);

    QuotationProductTax saveQuickProductTax(QuotationProduct quoteProduct, QuotationProductTax quotationProductTax);

    List<QuotationProductTax> composeQuoteProductTaxes(QuotationProduct quoteProduct);

}