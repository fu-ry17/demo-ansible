package com.turnkey.turnquest.gis.quotation.service;

import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyRiskSectionDto;
import com.turnkey.turnquest.gis.quotation.model.QuotationRisk;
import com.turnkey.turnquest.gis.quotation.model.QuotationRiskSection;

import java.util.List;
import java.util.Optional;

public interface QuotationRiskSectionService {
    QuotationRiskSection create(QuotationRiskSection quotationRiskSection);

    QuotationRiskSection update(QuotationRiskSection quotationRiskSection, Long id);

    QuotationRiskSection createOrUpdate(QuotationRiskSection quotationRiskSection);

    boolean delete(Long id);

    Optional<QuotationRiskSection> find(Long id);

    List<QuotationRiskSection> findByQuotationRiskId(Long quotationRiskId);

    List<QuotationRiskSection> createMultiple(List<QuotationRiskSection> quotationRiskSections);

    Optional<QuotationRiskSection> findByQuotationRiskIdAndSectionId(Long quotationRiskId, Long sectionId);

    List<PolicyRiskSectionDto> convertToPolicyRiskSections(List<QuotationRiskSection> quotationRiskSections);

    PolicyRiskSectionDto convertToPolicyRiskSection(QuotationRiskSection quotationRiskSection);

    void saveQuickQuotationRiskSection(QuotationRisk quoteRisk, QuotationRiskSection quotationRiskSection);
}
