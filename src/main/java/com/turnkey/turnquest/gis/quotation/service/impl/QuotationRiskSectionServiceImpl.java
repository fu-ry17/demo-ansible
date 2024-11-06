package com.turnkey.turnquest.gis.quotation.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turnkey.turnquest.gis.quotation.client.gis.PremiumRateClient;
import com.turnkey.turnquest.gis.quotation.client.gis.SectionClient;
import com.turnkey.turnquest.gis.quotation.dto.gis.PremiumRateDto;
import com.turnkey.turnquest.gis.quotation.dto.gis.PremiumRateFilterDto;
import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyRiskSectionDto;
import com.turnkey.turnquest.gis.quotation.enums.PremiumRateType;
import com.turnkey.turnquest.gis.quotation.model.QuotationRisk;
import com.turnkey.turnquest.gis.quotation.model.QuotationRiskSection;
import com.turnkey.turnquest.gis.quotation.repository.QuotationRiskSectionRepository;
import com.turnkey.turnquest.gis.quotation.service.QuotationRiskSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 */
@RequiredArgsConstructor
@Service("quotationRiskSectionService")
public class QuotationRiskSectionServiceImpl implements QuotationRiskSectionService {

    /**
     *
     */
    private final QuotationRiskSectionRepository quotationRiskSectionRepository;
    private final SectionClient sectionClient;
    private final PremiumRateClient premiumRateClient;

    /**
     * @param quotationRiskSection
     * @return
     */
    @Override
    public QuotationRiskSection create(QuotationRiskSection quotationRiskSection) {
        if (quotationRiskSection.getForeignId() != null) {
            quotationRiskSectionRepository.findByForeignId(quotationRiskSection.getForeignId()).ifPresent(qrs -> quotationRiskSection.setId(qrs.getId()));
        }
        return this.createOrUpdate(quotationRiskSection);
    }

    /**
     * @param quotationRiskSection
     * @param id
     * @return
     */
    @Override
    public QuotationRiskSection update(QuotationRiskSection quotationRiskSection, Long id) {
        quotationRiskSection.setId(id);
        return quotationRiskSectionRepository.save(quotationRiskSection);
    }

    /**
     * @param quotationRiskSection
     * @return
     */
    @Override
    public QuotationRiskSection createOrUpdate(QuotationRiskSection quotationRiskSection) {
        return quotationRiskSectionRepository.save(quotationRiskSection);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public boolean delete(Long id) {
        quotationRiskSectionRepository.deleteById(id);
        return true;
    }

    @Override
    public Optional<QuotationRiskSection> find(Long id) {
        return quotationRiskSectionRepository.findById(id);
    }

    /**
     * Find all quotation risk sections by quotation risk id and attach
     * respective sections
     *
     * @param quotationRiskId
     * @return
     */
    @Override
    public List<QuotationRiskSection> findByQuotationRiskId(Long quotationRiskId) {
        return this.composeQuotationRiskSections(quotationRiskSectionRepository.findAllByQuotationRiskId(quotationRiskId));
    }

    @Override
    public List<QuotationRiskSection> createMultiple(List<QuotationRiskSection> quotationRiskSections) {
        List<QuotationRiskSection> saveQuotationRiskSections = new ArrayList<>();
        for (QuotationRiskSection quotationRiskSection : quotationRiskSections) {
            saveQuotationRiskSections.add(this.createOrUpdate(quotationRiskSection));
        }
        return quotationRiskSectionRepository.saveAll(quotationRiskSections);
    }

    @Override
    public Optional<QuotationRiskSection> findByQuotationRiskIdAndSectionId(Long quotationRiskId, Long sectionId) {
        List<QuotationRiskSection> quotationRiskSection = this.quotationRiskSectionRepository.findAllByQuotationRiskIdAndSectionId(
                quotationRiskId,
                sectionId
        );
        return quotationRiskSection.stream().findFirst();
    }


    /**
     * @param quotationRiskSections
     * @return
     */
    @Override
    public List<PolicyRiskSectionDto> convertToPolicyRiskSections(List<QuotationRiskSection> quotationRiskSections) {
        List<PolicyRiskSectionDto> policyRiskSections = new ArrayList<>();
        for (QuotationRiskSection quotationRiskSection : quotationRiskSections) {
            policyRiskSections.add(this.convertToPolicyRiskSection(quotationRiskSection));
        }
        return policyRiskSections;
    }


    /**
     * @param quotationRiskSection
     * @return
     */
    @Override
    public PolicyRiskSectionDto convertToPolicyRiskSection(QuotationRiskSection quotationRiskSection) {
        PolicyRiskSectionDto policyRiskSectionDto = new PolicyRiskSectionDto();

        policyRiskSectionDto.setSectionId(quotationRiskSection.getSectionId());
        policyRiskSectionDto.setDescription(quotationRiskSection.getSectionCode());
        if (quotationRiskSection.getRowNumber() == null) {
            quotationRiskSection.setRowNumber(BigDecimal.ONE);
        }
        policyRiskSectionDto.setRowNumber(quotationRiskSection.getRowNumber().intValue());
        if (quotationRiskSection.getCalculationGroup() == null) {
            quotationRiskSection.setCalculationGroup(BigDecimal.ONE);
        }
        policyRiskSectionDto.setCode(quotationRiskSection.getSectionCode());
        policyRiskSectionDto.setDescription(quotationRiskSection.getDescription());
        policyRiskSectionDto.setCalculationGroup(quotationRiskSection.getCalculationGroup());
        policyRiskSectionDto.setLimitAmount(quotationRiskSection.getLimitAmount());
        policyRiskSectionDto.setPremiumRate(quotationRiskSection.getPremiumRate());
        policyRiskSectionDto.setPremiumAmount(quotationRiskSection.getPremiumAmount());
        policyRiskSectionDto.setPremiumRateType(quotationRiskSection.getRateType().toString());
        policyRiskSectionDto.setPremiumRateDescription(quotationRiskSection.getPremiumRateDescription());
        policyRiskSectionDto.setSectionType(quotationRiskSection.getSectionType());
        policyRiskSectionDto.setMinimumPremiumAmount(quotationRiskSection.getMinimumPremiumAmount());
        policyRiskSectionDto.setMultiplierRate(quotationRiskSection.getMultiplierRate());
        policyRiskSectionDto.setMultiplierDivisionFactor(quotationRiskSection.getMultiplierDivisionFactor());
        policyRiskSectionDto.setAnnualPremiumAmount(quotationRiskSection.getAnnualPremiumAmount());
        policyRiskSectionDto.setPremiumRateDivisionFactor(quotationRiskSection.getRateDivisionFactor());
        policyRiskSectionDto.setDescription(quotationRiskSection.getDescription());
        policyRiskSectionDto.setCompute(quotationRiskSection.getCompute());
        policyRiskSectionDto.setUsedLimitAmount(quotationRiskSection.getUsedLimitAmount());
        policyRiskSectionDto.setFreeLimitAmount(quotationRiskSection.getFreeLimitAmount());
        policyRiskSectionDto.setSubClassSectionId(quotationRiskSection.getSubClassSectionId());
        policyRiskSectionDto.setSubClassSectionDesc(quotationRiskSection.getSubClassSectionDesc());
        policyRiskSectionDto.setProrated(quotationRiskSection.getProrated());
        policyRiskSectionDto.setBenefitType(quotationRiskSection.getBenefitType());
        policyRiskSectionDto.setSectionMandatory(quotationRiskSection.getSectionMandatory());

        return policyRiskSectionDto;
    }

    List<QuotationRiskSection> composeQuotationRiskSections(List<QuotationRiskSection> quotationRiskSections) {
        return quotationRiskSections.stream()
                .peek(quotationRiskSection -> quotationRiskSection.setSection(sectionClient.findById(quotationRiskSection.getSectionId()))).collect(Collectors.toList());
    }

    @Override
    public void saveQuickQuotationRiskSection(QuotationRisk quoteRisk, QuotationRiskSection quotationRiskSection) {
        quotationRiskSection.setQuotationRiskId(quoteRisk.getId());
        this.create(quotationRiskSection);
    }

}
