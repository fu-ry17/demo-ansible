package com.turnkey.turnquest.gis.quotation.service.impl;

import com.turnkey.turnquest.gis.quotation.client.gis.CoverTypeClient;
import com.turnkey.turnquest.gis.quotation.client.gis.SubClassCoverTypeSectionClient;
import com.turnkey.turnquest.gis.quotation.dto.gis.CoverTypeDto;
import com.turnkey.turnquest.gis.quotation.dto.quotation.UpdateQuotationDto;
import com.turnkey.turnquest.gis.quotation.dto.underwriting.*;
import com.turnkey.turnquest.gis.quotation.enums.PremiumRateType;
import com.turnkey.turnquest.gis.quotation.enums.ValuationStatus;
import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import com.turnkey.turnquest.gis.quotation.event.producer.UpdateQuotationProductPublisher;
import com.turnkey.turnquest.gis.quotation.model.*;
import com.turnkey.turnquest.gis.quotation.repository.QuotationProductRepository;
import com.turnkey.turnquest.gis.quotation.repository.QuotationRiskRepository;
import com.turnkey.turnquest.gis.quotation.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
class QuotationRiskServiceImplTest {

    @Mock
    QuotationRiskRepository quotationRiskRepository;

    @Mock
    QuotationProductRepository quotationProductRepository;

    @Mock
    QuotationRiskSectionService quotationRiskSectionService;

    @Mock
    SubClassCoverTypeSectionClient subClassCoverTypeSectionClient;


    @Mock
    QuotationRiskTaxService quotationRiskTaxService;

    @Mock
    QuoteDocumentService quoteDocumentService;

    @Mock
    CoverTypeClient coverTypeClient;

    @Mock
    private UpdateQuotationProductPublisher updateQuotationProductPublisher;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    QuotationRiskServiceImpl quotationRiskService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create() {

        var risk = testRisk();
        risk.setClientType("I");
        risk.setQuotationRevisionNumber(BigDecimal.ZERO);

        when(quotationRiskRepository.findByForeignId(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        when(quotationRiskRepository.save(ArgumentMatchers.any())).thenReturn(risk);

        QuotationRisk created = quotationRiskService.create(testRisk());

        assertNotNull(created);
        assertEquals(risk, created);
        verify(quotationRiskRepository, times(1)).save(risk);
    }

    @Test
    void testUpdate() {
        // Arrange
        Long id = 1L;
        QuotationRisk quotationRisk = new QuotationRisk();
        quotationRisk.setQuotationProductId(2L);
        quotationRisk.setWithEffectFromDate(System.currentTimeMillis());
        quotationRisk.setWithEffectToDate(System.currentTimeMillis());

        QuotationProduct quotationProduct = new QuotationProduct();
        quotationProduct.setQuotation(new Quotation());
        quotationProduct.getQuotation().setStatus("status");

        when(quotationProductRepository.findById(quotationRisk.getQuotationProductId()))
                .thenReturn(Optional.of(quotationProduct));

        // Act
        QuotationRisk result = quotationRiskService.update(quotationRisk, id);

        // Assert
        verify(quotationRiskRepository).save(quotationRisk);
        verify(updateQuotationProductPublisher).updateQuotationProduct(any(UpdateQuotationDto.class));
        assertEquals(id, quotationRisk.getId());
        // Add more assertions as needed
    }


    @Test
    void testUpdateItemDescription() {
        // Arrange
        QuotationRisk quotationRisk = new QuotationRisk();
        MotorSchedules schedules = new MotorSchedules();
        schedules.setMake("Toyota");
        schedules.setModel("Corolla");
        schedules.setBodyType("Sedan");
        quotationRisk.setMotorSchedules(schedules);

        quotationRiskService.updateItemDescription(quotationRisk);
        assertEquals("Toyota/Corolla/Sedan", quotationRisk.getItemDescription());
        schedules.setMake(null);
        schedules.setChasisNo("123ABC");
        quotationRiskService.updateItemDescription(quotationRisk);
        assertEquals("123ABC", quotationRisk.getItemDescription());
        schedules.setChasisNo(null);

        quotationRiskService.updateItemDescription(quotationRisk);
        assertEquals("item_description", quotationRisk.getItemDescription());
    }

    @Test
    void delete() {

        quotationRiskService.delete(1L);

        verify(quotationRiskRepository, times(1)).deleteById(ArgumentMatchers.anyLong());
    }

    @Test
    void find() {
        var risk = testRisk();
        Optional<QuotationRisk> optional = Optional.of(risk);

        when(quotationRiskRepository.findById(ArgumentMatchers.anyLong())).thenReturn(optional);

        Optional<QuotationRisk> fetchedQuotationRisk = quotationRiskService.find(1L);

        assert (fetchedQuotationRisk.isPresent());
        assertEquals(risk, fetchedQuotationRisk.get());
    }

    @Test
    void findByQuotationProductIds() {
        Mockito.when(quotationRiskRepository.findByQuotationProduct_Id(anyLong())).thenReturn(List.of(testRisk()));
        var results = quotationRiskService.findByQuotationProductId(testRisk().getQuotationProductId());
        Assertions.assertEquals(results.get(0).getRiskId(), "KDJ 777K");

    }

    @Test
    void findByQuotationId() {
        Mockito.when(quotationRiskRepository.findByQuotationId(anyLong())).thenReturn(List.of(testRisk()));
        var results = quotationRiskService.findByQuotationId(testQuote().getId());
        Assertions.assertEquals(results.get(0).getRiskId(), "KDJ 777K");

    }
//
//    @Test
//    void findByQuotationProductId() {
//        CoverTypeDto coverTypeDto = new CoverTypeDto();
//        coverTypeDto.setId(15L);
//
//        QuotationRisk one = new QuotationRisk();
//        one.setId(1L);
//        one.setQuotationProductId(13L);
//        one.setCoverTypeId(15L);
//
//        QuotationRisk two = new QuotationRisk();
//        two.setId(2L);
//        two.setQuotationProductId(13L);
//        two.setCoverTypeId(15L);
//
//        QuotationRisk three = new QuotationRisk();
//        three.setId(3L);
//        three.setQuotationProductId(13L);
//        three.setCoverTypeId(15L);
//
//        List<QuotationRisk> returnedList = new LinkedList<>();
//        returnedList.add(one);
//        returnedList.add(two);
//        returnedList.add(three);
//
//
//        when(coverTypeClient.findById(ArgumentMatchers.anyLong())).thenReturn(coverTypeDto);
//
//        List<QuotationRisk> fetchedList = quotationRiskService.findByQuotationProductId(13L);
//
//        assertNotNull(fetchedList);
//        assertEquals(3, fetchedList.size());
//
//        assertNotNull(fetchedList.get(0).getCoverType());
//        assertEquals(fetchedList.get(0).getCoverType().getId(), fetchedList.get(0).getCoverTypeId());
//
//        assertEquals(13L, fetchedList.get(0).getQuotationProductId());
//
//    }

    @Test
    void findByQuotatinId() {
        Quotation quotation = new Quotation();
        quotation.setId(300L);

        QuotationProduct quotationProduct = new QuotationProduct();
        quotationProduct.setId(824L);
        quotationProduct.setQuotation(quotation);

        QuotationRisk one = new QuotationRisk();
        one.setId(1L);
        one.setQuotationProduct(quotationProduct);

        QuotationRisk two = new QuotationRisk();
        two.setId(2L);
        two.setQuotationProduct(quotationProduct);

        QuotationRisk three = new QuotationRisk();
        three.setId(3L);
        three.setQuotationProduct(quotationProduct);

        List<QuotationRisk> quotationRisks = new LinkedList<>();
        quotationRisks.add(one);
        quotationRisks.add(two);
        quotationRisks.add(three);


        when(quotationRiskRepository.findByQuotationId(ArgumentMatchers.anyLong())).thenReturn(quotationRisks);

        List<QuotationRisk> fetchedList = quotationRiskService.findByQuotationId(300L);

        assertEquals(3, fetchedList.size());
        assertEquals(fetchedList, quotationRisks);
    }

    @Test
    void saveMultiple() {

        //populating default sections
        QuotationRiskSection quotationRiskSection = new QuotationRiskSection();
        quotationRiskSection.setId(29L);

        quotationRiskSection.setQuotationRiskId(1L);
        quotationRiskSection.setSectionId(48L);
        quotationRiskSection.setSectionCode("SOME_CODE");
        quotationRiskSection.setCompute("Y");
        quotationRiskSection.setRowNumber(BigDecimal.ONE);
        quotationRiskSection.setCalculationGroup(BigDecimal.ONE);
        quotationRiskSection.setDualBasis("N");

        quotationRiskSection.setRateType(PremiumRateType.FXD);
        quotationRiskSection.setPremiumRate(BigDecimal.valueOf(1));
        quotationRiskSection.setFreeLimitAmount(BigDecimal.valueOf(1));
        quotationRiskSection.setRateDivisionFactor(BigDecimal.valueOf(1));
        quotationRiskSection.setMultiplierDivisionFactor(BigDecimal.valueOf(1));
        quotationRiskSection.setMultiplierRate(BigDecimal.valueOf(1));

        quotationRiskSection.setPremiumAmount(BigDecimal.ZERO);
        quotationRiskSection.setLimitAmount(BigDecimal.ZERO);
        quotationRiskSection.setSectionType("SOME_TYPE");

        QuotationRisk one = new QuotationRisk();
        one.setId(1L);
        one.setSubClassId(21L);
        one.setCoverTypeId(15L);
        one.setBinderId(30L);
        one.setQuotationRiskSections(new LinkedList<>());
        one.getQuotationRiskSections().add(quotationRiskSection);

        QuotationRisk two = new QuotationRisk();
        two.setId(1L);
        two.setSubClassId(21L);
        two.setCoverTypeId(15L);
        two.setBinderId(30L);
        two.setQuotationRiskSections(new LinkedList<>());
        two.getQuotationRiskSections().add(quotationRiskSection);

        QuotationRisk three = new QuotationRisk();
        three.setId(1L);
        three.setSubClassId(21L);
        three.setCoverTypeId(15L);
        three.setBinderId(30L);
        three.setQuotationRiskSections(new LinkedList<>());
        three.getQuotationRiskSections().add(quotationRiskSection);

        List<QuotationRisk> returnedList = new LinkedList<>();
        returnedList.add(one);
        returnedList.add(two);
        returnedList.add(three);

        when(quotationRiskRepository.saveAll(ArgumentMatchers.anyList())).thenReturn(returnedList);

        List<QuotationRisk> savedList = quotationRiskService.saveMultiple(returnedList);

        assertEquals(3, savedList.size());
        assertEquals(returnedList, savedList);

    }

    @Test
    void getQuotationRiskSections() {
        QuotationRiskSection one = new QuotationRiskSection();
        one.setId(1L);

        QuotationRiskSection two = new QuotationRiskSection();
        two.setId(2L);

        QuotationRiskSection three = new QuotationRiskSection();
        three.setId(3L);

        List<QuotationRiskSection> returnedList = new LinkedList<>();
        returnedList.add(one);
        returnedList.add(two);
        returnedList.add(three);

//        risk.setQuotationRiskSections(returnedList);

        when(quotationRiskSectionService
                .findByQuotationRiskId(ArgumentMatchers.anyLong()))
                .thenReturn(returnedList);

        List<QuotationRiskSection> fetchedSections = quotationRiskService.getQuotationRiskSections(1L);

        assertEquals(3, fetchedSections.size());
        assertEquals(fetchedSections, returnedList);
    }

//    @Test
//    void updatePremiumComputations() {
//
//        SectionPremiumComputationResult sectionPremiumComputationResult = new SectionPremiumComputationResult();
//        sectionPremiumComputationResult.setProrataPremiumAmount(BigDecimal.valueOf(30000));
//
//        GroupPremiumComputationResult groupPremiumComputationResult = new GroupPremiumComputationResult();
//        groupPremiumComputationResult.setSectionPremiumComputationResults(new LinkedList<>());
//        groupPremiumComputationResult.setProrataPremiumAmount(BigDecimal.valueOf(30000));
//        groupPremiumComputationResult.getSectionPremiumComputationResults().add(sectionPremiumComputationResult);
//
//        RiskPremiumComputationResult riskPremiumComputationResult = new RiskPremiumComputationResult();
//        riskPremiumComputationResult.setQuotationRiskId(1L);
//        riskPremiumComputationResult.setMinimumPremiumUsed(true);
//        riskPremiumComputationResult.setFutureAnnualPremium(BigDecimal.valueOf(30000));
//        riskPremiumComputationResult.setSumInsuredAmount(BigDecimal.valueOf(300000));
//        riskPremiumComputationResult.setProrataPremiumAmount(BigDecimal.valueOf(30000));
//        riskPremiumComputationResult.setCoverDays(365L);
//        riskPremiumComputationResult.setGroupPremiumComputationResults(new LinkedList<>());
//        riskPremiumComputationResult.getGroupPremiumComputationResults().add(groupPremiumComputationResult);
//
//        QuotationRisk toUpdate = new QuotationRisk();
//        toUpdate.setId(1L);
//        toUpdate.setMinimumPremiumUsed(true ? "Y" : "N");
//        toUpdate.setFutureAnnualPremium(BigDecimal.valueOf(30000));
//        toUpdate.setValue(BigDecimal.valueOf(300000));
//        toUpdate.setAnnualPremium(BigDecimal.valueOf(30000));
//        toUpdate.setTotalPremium(BigDecimal.valueOf(30000));
//        toUpdate.setCoverDays(365L);
//
//        QuotationRiskSection quotationRiskSection = new QuotationRiskSection();
//        quotationRiskSection.setId(542L);
//        Optional<QuotationRisk> optional = Optional.of(risk);
//
//        when(quotationRiskRepository.findById(ArgumentMatchers.anyLong())).thenReturn(optional);
//
//        when(quotationRiskSectionService
//                        .updatePremiumComputations(ArgumentMatchers.any()))
//                .thenReturn(quotationRiskSection);
//
//        when(quotationRiskRepository.save(ArgumentMatchers.any())).thenReturn(toUpdate);
//
//
//    }
////
//    @Test
//    void populateDefaultSections() {
//
//        SectionDto sectionDto = new SectionDto();
//        sectionDto.setId(48L);
//        sectionDto.setCode("SOME_CODE");
//        sectionDto.setType("SOME_TYPE");
//
//        QuotationRiskSection quotationRiskSection = new QuotationRiskSection();
//        quotationRiskSection.setId(29L);
//
//        quotationRiskSection.setQuotationRiskId(1L);
//        quotationRiskSection.setSectionId(48L);
//        quotationRiskSection.setSectionCode("SOME_CODE");
//        quotationRiskSection.setCompute("Y");
//        quotationRiskSection.setRowNumber(BigDecimal.ONE);
//        quotationRiskSection.setCalculationGroup(BigDecimal.ONE);
//        quotationRiskSection.setDualBasis("N");
//
//        quotationRiskSection.setRateType(PremiumRateType.FXD);
//        quotationRiskSection.setPremiumRate(BigDecimal.valueOf(1));
//        quotationRiskSection.setFreeLimitAmount(BigDecimal.valueOf(1));
//        quotationRiskSection.setRateDivisionFactor(BigDecimal.valueOf(1));
//        quotationRiskSection.setMultiplierDivisionFactor(BigDecimal.valueOf(1));
//        quotationRiskSection.setMultiplierRate(BigDecimal.valueOf(1));
//
//        quotationRiskSection.setPremiumAmount(BigDecimal.ZERO);
//        quotationRiskSection.setLimitAmount(BigDecimal.ZERO);
//        quotationRiskSection.setSectionType("SOME_TYPE");
//        QuotationRisk risk = new QuotationRisk();
//        risk.setSubClassId(21L);
//        risk.setCoverTypeId(15L);
//        risk.setBinderId(30L);
//        risk.setQuotationRiskSections(new LinkedList<>());
//        risk.getQuotationRiskSections().add(quotationRiskSection);
//
//        PremiumRateDto premiumRateDto = new PremiumRateDto();
//        premiumRateDto.setId(305L);
//        premiumRateDto.setType("SOME_TYPE");
//        premiumRateDto.setRate(BigDecimal.valueOf(1));
//        premiumRateDto.setFreeLimit(BigDecimal.valueOf(1));
//        premiumRateDto.setDivisionFactor(BigDecimal.valueOf(1));
//        premiumRateDto.setMultiplierDivisionFactor(BigDecimal.valueOf(1));
//        premiumRateDto.setMultiplierRate(BigDecimal.valueOf(1));
//
//        SubClassCoverTypeSectionDto subClassCoverTypeSectionDto = new SubClassCoverTypeSectionDto();
//        subClassCoverTypeSectionDto.setId(83L);
//        subClassCoverTypeSectionDto.setSection(sectionDto);
//        subClassCoverTypeSectionDto.setPremiumRates(new LinkedList<>());
//        subClassCoverTypeSectionDto.getPremiumRates().add(premiumRateDto);
//
//
//        List<SubClassCoverTypeSectionDto> mandatorySubclassCoverTypeSections = new LinkedList<>();
//        mandatorySubclassCoverTypeSections.add(subClassCoverTypeSectionDto);
//
//        List<QuotationRiskSection> populatedQuotationRiskSections = new LinkedList<>();
//        populatedQuotationRiskSections.add(quotationRiskSection);
//
//        when(subClassCoverTypeSectionClient
//                        .findMandatorySectionsWithPremiumRates(
//                                ArgumentMatchers.anyLong(),
//                                ArgumentMatchers.anyLong(),
//                                ArgumentMatchers.anyLong()))
//                .thenReturn(mandatorySubclassCoverTypeSections);
//
//        when(quotationRiskSectionService
//                        .createMultiple(ArgumentMatchers.anyList()))
//                .thenReturn(populatedQuotationRiskSections);
//
//        List<QuotationRiskSection> defaultSectionsPopulated = quotationRiskService.populateDefaultSections(risk);
//
//        assertNotNull(defaultSectionsPopulated);
//        assertEquals(premiumRateDto.getType(), defaultSectionsPopulated.get(0).getRateType().getLiteral());
//        assertEquals(sectionDto.getCode(), defaultSectionsPopulated.get(0).getSectionCode());
//
//    }

    //
    @Test
    void createOrUpdateQuotationRiskSection() {
        QuotationRiskSection one = new QuotationRiskSection();
        one.setId(1L);
        one.setSectionId(52L);

        QuotationRiskSection two = new QuotationRiskSection();
        two.setId(2L);
        two.setSectionId(52L);

        QuotationRiskSection three = new QuotationRiskSection();
        three.setId(3L);
        three.setSectionId(52L);
        three.setSectionCode("TO_UPDATE");

        List<QuotationRiskSection> returnedList = new LinkedList<>();
        returnedList.add(one);
        returnedList.add(two);
        returnedList.add(three);
        QuotationRisk risk = new QuotationRisk();

        risk.setQuotationRiskSections(returnedList);

        QuotationRiskSection toSave = new QuotationRiskSection();
        toSave.setId(4L);
        toSave.setSectionId(52L);

        QuotationRiskSection toUpdate = new QuotationRiskSection();
        toUpdate.setId(3L);
        toUpdate.setSectionId(52L);
        toUpdate.setSectionCode("UPDATED");

        Optional<QuotationRisk> optional = Optional.of(risk);

        when(quotationRiskRepository.findById(ArgumentMatchers.anyLong())).thenReturn(optional);

        when(quotationRiskSectionService
                .findByQuotationRiskId(ArgumentMatchers.anyLong()))
                .thenReturn(returnedList);

        when(quotationRiskSectionService.create(ArgumentMatchers.any())).thenReturn(toSave);

        when(quotationRiskSectionService
                .update(
                        ArgumentMatchers.any(),
                        ArgumentMatchers.anyLong()))
                .thenReturn(toUpdate);

        QuotationRiskSection createdSection = quotationRiskService.createOrUpdateQuotationRiskSection(1L, toSave);

        QuotationRiskSection updatedSection = quotationRiskService.createOrUpdateQuotationRiskSection(1L, toUpdate);

        assertNotNull(createdSection);
        assertNotNull(updatedSection);

        assertEquals(3L, updatedSection.getId());

        assertEquals("UPDATED", updatedSection.getSectionCode());
    }

    @Test
    void shouldReturnRisksByRegistrationNo() {
        // Given
        String registrationNo = "ABC123";
        QuotationRisk risk1 = new QuotationRisk();
        risk1.setRiskId(registrationNo);
        QuotationRisk risk2 = new QuotationRisk();
        risk2.setRiskId(registrationNo);
        List<QuotationRisk> expectedRisks = Arrays.asList(risk1, risk2);

        when(quotationRiskRepository.findAllByRiskId(registrationNo)).thenReturn(expectedRisks);

        // When
        List<QuotationRisk> actualRisks = quotationRiskService.findRisksByRegistrationNo(registrationNo);

        // Then
        assertEquals(expectedRisks, actualRisks);
    }

    @Test
    void shouldReturnEmptyListWhenNoRisksFoundByRegistrationNo() {
        // Given
        String registrationNo = "XYZ789";

        when(quotationRiskRepository.findAllByRiskId(registrationNo)).thenReturn(Arrays.asList());

        // When
        List<QuotationRisk> actualRisks = quotationRiskService.findRisksByRegistrationNo(registrationNo);

        // Then
        assertEquals(0, actualRisks.size());
    }

    @Test
    void shouldConvertToPolicyRisksWhenQuotationRisksAreNotEmpty() {
        // Given
        QuotationRisk quotationRisk1 = new QuotationRisk();
        quotationRisk1.setId(1L);
        QuotationRisk quotationRisk2 = new QuotationRisk();
        quotationRisk2.setId(2L);
        List<QuotationRisk> quotationRisks = Arrays.asList(quotationRisk1, quotationRisk2);

        // When
        List<PolicyRiskDto> policyRiskDtos = quotationRiskService.convertToPolicyRisks(quotationRisks);

        // Then
        assertEquals(2, policyRiskDtos.size());
    }

    @Test
    void shouldReturnEmptyListWhenQuotationRisksAreEmpty() {
        // Given
        List<QuotationRisk> quotationRisks = new ArrayList<>();

        // When
        List<PolicyRiskDto> policyRiskDtos = quotationRiskService.convertToPolicyRisks(quotationRisks);

        // Then
        assertTrue(policyRiskDtos.isEmpty());
    }



    @Test
    void convertToPolicyRisk() {
        var mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        when(quotationRiskSectionService.convertToPolicyRiskSections(anyList()))
                .thenReturn(testPolicyRiskSectionData());

        when(quotationRiskTaxService.convertToPolicyRiskTaxes(anyList())).thenReturn(testPolicyRiskTaxData());

        when(modelMapper.getConfiguration()).thenReturn(mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT));
        doNothing().when(modelMapper).map(any(MotorSchedules.class), any(PolicyScheduleDto.class));
        when(quoteDocumentService.convertToPolicyDocuments(anyList())).thenReturn(testPolicyDocumentData());

        var result = quotationRiskService.convertToPolicyRisk(testRisk());
        assertNotNull(result);
    }

    @Test
    void updateCertificateNo() {
        QuotationRisk toUpdate = new QuotationRisk();
        toUpdate.setId(1L);
        toUpdate.setCertificateNo("CERT");
        QuotationRisk risk = new QuotationRisk();

        Optional<QuotationRisk> optional = Optional.of(risk);

        when(quotationRiskRepository.findById(ArgumentMatchers.anyLong())).thenReturn(optional);

        when(quotationRiskRepository.save(ArgumentMatchers.any())).thenReturn(toUpdate);

        QuotationRisk updatedCertificateNo = quotationRiskService.updateCertificateNo("CERT", risk.getId());

        assertEquals(null, updatedCertificateNo);


    }

    @Test
    void updateCertificateNoRiskPresent() {
        QuotationRisk toUpdate = new QuotationRisk();
        toUpdate.setId(1L);
        toUpdate.setCertificateNo("CERT");
        QuotationRisk risk = new QuotationRisk();
        risk.setRiskId("KBM 345H");
        risk.setId(123L);

        Optional<QuotationRisk> optional = Optional.of(risk);

        when(quotationRiskRepository.findById(ArgumentMatchers.anyLong())).thenReturn(optional);

        when(quotationRiskRepository.save(ArgumentMatchers.any())).thenReturn(toUpdate);

        QuotationRisk updatedCertificateNo = quotationRiskService.updateCertificateNo("CERT", risk.getId());


        assertEquals("CERT", updatedCertificateNo.getCertificateNo());

    }


    @Test
    void findTaxes() {
        QuotationRiskTax one = new QuotationRiskTax();
        one.setId(1L);

        QuotationRiskTax two = new QuotationRiskTax();
        two.setId(2L);

        QuotationRiskTax three = new QuotationRiskTax();
        three.setId(3L);


        List<QuotationRiskTax> returnedList = new LinkedList<>();
        returnedList.add(one);
        returnedList.add(two);
        returnedList.add(three);
        QuotationRisk risk = new QuotationRisk();

        risk.setQuotationRiskTaxes(returnedList);

        when(quotationRiskTaxService
                .findByQuotationRiskId(ArgumentMatchers.anyLong()))
                .thenReturn(returnedList);

        List<QuotationRiskTax> fetchedTaxes = quotationRiskService.findTaxes(1L);

        assertEquals(3, fetchedTaxes.size());
        assertEquals(fetchedTaxes, returnedList);

    }

    @Test
    void composeQuotationRisk() {
        CoverTypeDto coverTypeDto = new CoverTypeDto();
        coverTypeDto.setId(15L);
        QuotationRisk risk = new QuotationRisk();
        risk.setCoverTypeId(15L);

        List<QuotationRisk> listToCompose = new LinkedList<>();
        listToCompose.add(risk);

        when(coverTypeClient.findById(ArgumentMatchers.anyLong())).thenReturn(coverTypeDto);

    }


    private QuotationRisk testRisk() {
        var qr = new QuotationRisk();

        qr.setWithEffectFromDate(Timestamp.valueOf("2029-2-2 00:00:00").getTime());
        qr.setWithEffectToDate(Timestamp.valueOf("2029-3-1 00:00:00").getTime());
        qr.setRiskId("KDJ 777K");
        qr.setValuationStatus(ValuationStatus.OPEN);
        qr.setQuotationProductId(1L);
        qr.setMotorSchedules(testMotorSchedulesData());
        qr.setInstallmentAllowed(YesNo.Y);
        qr.setPolicyRiskId(1L);

        return qr;
    }

    private QuotationProduct testQuotationProduct() {
        var quotationProduct = new QuotationProduct();
        quotationProduct.setId(1L);
        quotationProduct.setProductId(33L);
        quotationProduct.setCode("NOT_UPDATED");
        quotationProduct.setQuotation(testQuote());

        return quotationProduct;
    }

    private Quotation testQuote() {
        var quotation = new Quotation();
        quotation.setId(706L);
        quotation.setStatus("NB");

        return quotation;
    }

    private MotorSchedules testMotorSchedulesData() {
        MotorSchedules motorSchedules = new MotorSchedules();
        motorSchedules.setId(1L);
        motorSchedules.setMake("TOYOTA");
        motorSchedules.setModel("HILUX");
        motorSchedules.setYearOfManufacture(2019L);
        motorSchedules.setBodyType("SALOON");

        return motorSchedules;
    }

    private List<QuoteDocument> testQuotationDocumentData() {
        QuoteDocument quoteDocument = new QuoteDocument();
        quoteDocument.setId(1L);
        quoteDocument.setQuotationId(1L);
        quoteDocument.setDocumentId(1L);
        quoteDocument.setOrganizationId(200L);
        quoteDocument.setQuotationRisk(testRisk());
        quoteDocument.setDocument("documentales");

        List<QuoteDocument> quoteDocuments = new LinkedList<>();
        quoteDocuments.add(quoteDocument);

        return quoteDocuments;
    }


    private List<PolicyRiskSectionDto> testPolicyRiskSectionData() {
        PolicyRiskSectionDto policyRiskSectionDto = new PolicyRiskSectionDto();
        policyRiskSectionDto.setId(1L);
        policyRiskSectionDto.setPolicyRiskId(1L);
        policyRiskSectionDto.setSectionId(1L);
        policyRiskSectionDto.setCode("CODE");
        policyRiskSectionDto.setLimitAmount(BigDecimal.valueOf(1000));

        List<PolicyRiskSectionDto> policyRiskSectionDtos = new LinkedList<>();
        policyRiskSectionDtos.add(policyRiskSectionDto);

        return policyRiskSectionDtos;
    }

    private List<PolicyRiskTaxDto> testPolicyRiskTaxData() {
        PolicyRiskTaxDto policyRiskTaxDto = new PolicyRiskTaxDto();
        policyRiskTaxDto.setId(1L);
        policyRiskTaxDto.setPolicyRiskId(1L);
        policyRiskTaxDto.setPolicyNumber("P/TEST/001/2021");
        policyRiskTaxDto.setRate(BigDecimal.valueOf(0.1));
        policyRiskTaxDto.setTaxRateId(1L);

        List<PolicyRiskTaxDto> policyRiskTaxDtos = new LinkedList<>();
        policyRiskTaxDtos.add(policyRiskTaxDto);

        return policyRiskTaxDtos;
    }

    private List<PolicyClauseDto> testPolicyClauseData() {
        PolicyClauseDto policyClauseDto = new PolicyClauseDto();
        policyClauseDto.setClauseId(1L);
        policyClauseDto.setPolicyRiskId(1L);
        policyClauseDto.setClauseId(1L);
        policyClauseDto.setCode("CODE");
        policyClauseDto.setSubClassId(1L);

        List<PolicyClauseDto> policyClauseDtos = new LinkedList<>();
        policyClauseDtos.add(policyClauseDto);

        return policyClauseDtos;
    }

    private PolicyValuationDto testPolicyValuationData() {
        PolicyValuationDto policyValuationDto = new PolicyValuationDto();
        policyValuationDto.setId(1L);
        policyValuationDto.setPolicyId(1L);
        policyValuationDto.setRequestValuationDate(Timestamp.valueOf("2021-01-01 00:00:00").getTime());
        policyValuationDto.setOrganizationId(1L);

        return policyValuationDto;
    }

    private List<PolicyDocumentsDto> testPolicyDocumentData() {
        PolicyDocumentsDto policyDocumentsDto = new PolicyDocumentsDto();
        policyDocumentsDto.setId(1L);
        policyDocumentsDto.setPolicyId(1L);
        policyDocumentsDto.setDocumentId(1L);
        policyDocumentsDto.setOrganizationId(1L);
        policyDocumentsDto.setDocumentRef("REF");

        List<PolicyDocumentsDto> policyDocumentsDtos = new LinkedList<>();
        policyDocumentsDtos.add(policyDocumentsDto);

        return policyDocumentsDtos;
    }
}
