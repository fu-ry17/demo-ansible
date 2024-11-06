package com.turnkey.turnquest.gis.quotation.service.impl;

import com.turnkey.turnquest.gis.quotation.client.gis.SectionClient;
import com.turnkey.turnquest.gis.quotation.dto.gis.SectionDto;
import com.turnkey.turnquest.gis.quotation.enums.BenefitType;
import com.turnkey.turnquest.gis.quotation.enums.PremiumRateType;
import com.turnkey.turnquest.gis.quotation.model.QuotationRisk;
import com.turnkey.turnquest.gis.quotation.model.QuotationRiskSection;
import com.turnkey.turnquest.gis.quotation.repository.QuotationRiskSectionRepository;
import com.turnkey.turnquest.gis.quotation.service.QuotationRiskSectionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
class QuotationRiskSectionServiceImplTest {

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @InjectMocks
    QuotationRiskSectionServiceImpl quotationRiskSectionService;
    @Mock
    QuotationRiskSectionRepository quotationRiskSectionRepository;

    @Mock
    SectionClient sectionClient;

    @Test
    void create() {
        Mockito.when(quotationRiskSectionRepository.findByForeignId(anyLong())).thenReturn(Optional.of(quotationRiskSection()));
        var result = quotationRiskSectionService.create(quotationRiskSection());
        System.out.println(result);
    }

    @Test
    void update() {
        Mockito.when(quotationRiskSectionRepository.save(quotationRiskSection())).thenReturn(quotationRiskSection());
        var result = quotationRiskSectionService.update(quotationRiskSection(), quotationRiskSection().getId());
        Assertions.assertEquals(123, result.getId());
    }

    @Test
    void createOrUpdate() {
        Mockito.when(quotationRiskSectionRepository.save(quotationRiskSection())).thenReturn(quotationRiskSection());
        var results = quotationRiskSectionService.createOrUpdate(quotationRiskSection());
        Assertions.assertEquals(123, results.getId());
    }

    @Test
    void find() {
        Mockito.when(quotationRiskSectionRepository.findById(anyLong())).thenReturn(Optional.of(quotationRiskSection()));
        var results = quotationRiskSectionService.find(quotationRiskSection().getId());
        Assertions.assertEquals(123, results.get().getId());
    }


    @Test
    void createMultiple() {
        Mockito.when(quotationRiskSectionRepository.saveAll(any())).thenReturn(List.of(quotationRiskSection()));
        var results = quotationRiskSectionService.createMultiple(List.of(quotationRiskSection()));
        Assertions.assertEquals(123, results.get(0).getId());
    }

    @Test
    void findByQuotationRiskIdAndSectionId() {
        Mockito.when(quotationRiskSectionRepository.findAllByQuotationRiskIdAndSectionId(anyLong(), anyLong())).thenReturn(List.of(quotationRiskSection()));
        var results = quotationRiskSectionService.findByQuotationRiskIdAndSectionId(quotationRiskSection().getQuotationRiskId(), quotationRiskSection().getSectionId());
        Assertions.assertEquals(123, results.get().getId());
    }


    @Test
    void convertToPolicyRiskSections() {
        var results = quotationRiskSectionService.convertToPolicyRiskSections(List.of(quotationRiskSection()));
        Assertions.assertEquals(123, results.get(0).getSectionId());
    }

    @Test
    void convertToPolicyRiskSection() {
        QuotationRiskSection quotationRiskSection = quotationRiskSection();
        quotationRiskSection.setRowNumber(null);
        var results = quotationRiskSectionService.convertToPolicyRiskSections(List.of(quotationRiskSection()));
        Assertions.assertEquals(123, results.get(0).getSectionId());
    }

    @Test
    void convertToPolicyRiskSectionCalculationGroup() {
        QuotationRiskSection quotationRiskSection = quotationRiskSection();
        quotationRiskSection.setCalculationGroup(null);
        var results = quotationRiskSectionService.convertToPolicyRiskSections(List.of(quotationRiskSection()));
        Assertions.assertEquals(123, results.get(0).getSectionId());
    }

    @Test
    void findByQuotationRiskId_ShouldReturnQuotationRiskSections_WhenQuotationRiskIdExists() {
        // Given
        Long quotationRiskId = 123L;
        QuotationRiskSection quotationRiskSection = quotationRiskSection();
        List<QuotationRiskSection> expectedQuotationRiskSections = List.of(quotationRiskSection);

        Mockito.when(quotationRiskSectionRepository.findAllByQuotationRiskId(quotationRiskId)).thenReturn(expectedQuotationRiskSections);

        // When
        List<QuotationRiskSection> actualQuotationRiskSections = quotationRiskSectionService.findByQuotationRiskId(quotationRiskId);

        // Then
        Assertions.assertEquals(expectedQuotationRiskSections, actualQuotationRiskSections);
    }

    @Test
    void findByQuotationRiskId_ShouldReturnEmptyList_WhenQuotationRiskIdDoesNotExist() {
        // Given
        Long quotationRiskId = 123L;
        List<QuotationRiskSection> expectedQuotationRiskSections = new ArrayList<>();

        Mockito.when(quotationRiskSectionRepository.findAllByQuotationRiskId(quotationRiskId)).thenReturn(expectedQuotationRiskSections);

        // When
        List<QuotationRiskSection> actualQuotationRiskSections = quotationRiskSectionService.findByQuotationRiskId(quotationRiskId);

        // Then
        Assertions.assertEquals(expectedQuotationRiskSections, actualQuotationRiskSections);
    }

    @Test
    void delete_ShouldCallDeleteById_WhenIdIsProvided() {
        // Given
        Long id = 123L;

        // When
        quotationRiskSectionService.delete(id);

        // Then
        verify(quotationRiskSectionRepository, times(1)).deleteById(id);
    }

    @Test
    void delete_ShouldReturnTrue_WhenIdIsProvided() {
        // Given
        Long id = 123L;

        // When
        boolean result = quotationRiskSectionService.delete(id);

        // Then
        assertTrue(result);
    }


    private QuotationRiskSection quotationRiskSection() {
        QuotationRiskSection quotationRiskSection = new QuotationRiskSection();
        quotationRiskSection.setId(123L);
        quotationRiskSection.setQuotationRiskId(123L);
        quotationRiskSection.setQuotationRisk(new QuotationRisk());
        quotationRiskSection.setDescription("description");
        quotationRiskSection.setForeignId(123L);
        quotationRiskSection.setSectionId(123L);
        quotationRiskSection.setSectionCode("sectionCode");
        quotationRiskSection.setSubClassSectionId(123L);
        quotationRiskSection.setSubClassSectionDesc("sectionDescription");
        quotationRiskSection.setSectionType("sectionType");
        quotationRiskSection.setQuotationRiskId(123L);
        quotationRiskSection.setLimitAmount(BigDecimal.TEN);
        quotationRiskSection.setAnnualPremiumAmount(BigDecimal.TEN);
        quotationRiskSection.setPremiumAmount(BigDecimal.TEN);
        quotationRiskSection.setMinimumPremiumAmount(BigDecimal.TEN);
        quotationRiskSection.setUsedLimitAmount(BigDecimal.TEN);
        quotationRiskSection.setFreeLimitAmount(BigDecimal.ONE);
        quotationRiskSection.setPremiumRate(BigDecimal.ONE);
        quotationRiskSection.setMultiplierRate(BigDecimal.ONE);
        quotationRiskSection.setMultiplierDivisionFactor(BigDecimal.ONE);
        quotationRiskSection.setRateDivisionFactor(BigDecimal.ONE);
        quotationRiskSection.setRateType(PremiumRateType.FXD);
        quotationRiskSection.setProrated("Propated");
        quotationRiskSection.setPremiumRateDescription("premiumdescription");
        quotationRiskSection.setCalculationGroup(BigDecimal.TEN);
        quotationRiskSection.setRowNumber(BigDecimal.ONE);
        quotationRiskSection.setCompute("compute");
        quotationRiskSection.setBenefitType(BenefitType.AMOUNT);
        quotationRiskSection.setDualBasis("dualBasis");
        quotationRiskSection.setSumInsuredRate(BigDecimal.ONE);
        quotationRiskSection.setSumInsuredLimitType("sumInsuredLimitType");
        quotationRiskSection.setSectionMandatory("setSectionMandatory");
        quotationRiskSection.setSection(sectionDto());
        return quotationRiskSection;
    }

    SectionDto sectionDto() {
        return new SectionDto(123L, "code", "description", "Type", new ArrayList<>());
    }

}
