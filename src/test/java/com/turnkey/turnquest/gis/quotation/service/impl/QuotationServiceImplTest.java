package com.turnkey.turnquest.gis.quotation.service.impl;

import com.turnkey.turnquest.gis.quotation.aki.ValidateRiskService;
import com.turnkey.turnquest.gis.quotation.aki.dto.ErrorDto;
import com.turnkey.turnquest.gis.quotation.aki.dto.ValidationResponseDto;
import com.turnkey.turnquest.gis.quotation.aki.error.AKIValidationException;
import com.turnkey.turnquest.gis.quotation.client.billing.PremiumCardClient;
import com.turnkey.turnquest.gis.quotation.client.crm.AgencyClient;
import com.turnkey.turnquest.gis.quotation.client.crm.ClientDataClient;
import com.turnkey.turnquest.gis.quotation.client.crm.InsurerCurrencyClient;
import com.turnkey.turnquest.gis.quotation.client.crm.OrganizationClient;
import com.turnkey.turnquest.gis.quotation.client.gis.*;
import com.turnkey.turnquest.gis.quotation.client.partial.PartialQuotationClient;
import com.turnkey.turnquest.gis.quotation.client.underwriting.PolicyClientMock;
import com.turnkey.turnquest.gis.quotation.dto.ScheduleDetailsDto;
import com.turnkey.turnquest.gis.quotation.dto.crm.*;
import com.turnkey.turnquest.gis.quotation.dto.gis.ProductDto;
import com.turnkey.turnquest.gis.quotation.dto.gis.TaxRateDto;
import com.turnkey.turnquest.gis.quotation.dto.quotation.PaymentPlan;
import com.turnkey.turnquest.gis.quotation.dto.quotation.PremiumCardDto;
import com.turnkey.turnquest.gis.quotation.dto.quotation.QuotationDto;
import com.turnkey.turnquest.gis.quotation.enums.ClientTypes;
import com.turnkey.turnquest.gis.quotation.enums.SortType;
import com.turnkey.turnquest.gis.quotation.enums.TaxRateInstallmentType;
import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import com.turnkey.turnquest.gis.quotation.event.producer.AllocatePaymentsEvent;
import com.turnkey.turnquest.gis.quotation.event.producer.CreateQuotationEvent;
import com.turnkey.turnquest.gis.quotation.event.producer.NotificationProducer;
import com.turnkey.turnquest.gis.quotation.exception.error.ItemCannotBeNullException;
import com.turnkey.turnquest.gis.quotation.exception.error.QuoteCreationException;
import com.turnkey.turnquest.gis.quotation.exception.error.QuoteRiskDeletionException;
import com.turnkey.turnquest.gis.quotation.exception.error.ResourceNotFoundException;
import com.turnkey.turnquest.gis.quotation.model.*;
import com.turnkey.turnquest.gis.quotation.repository.AkiResponseRepository;
import com.turnkey.turnquest.gis.quotation.repository.QuotationRepository;
import com.turnkey.turnquest.gis.quotation.service.EndorsementService;
import com.turnkey.turnquest.gis.quotation.service.QuotationProductService;
import com.turnkey.turnquest.gis.quotation.service.QuotationProductTaxService;
import com.turnkey.turnquest.gis.quotation.service.QuotationRiskService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class QuotationServiceImplTest {
    @Mock
    QuotationRepository quotationRepository;

    @Mock
    QuotationRiskService quotationRiskService;

    @Mock
    QuotationProductService quotationProductService;

    @Mock
    QuotationProductTaxService quotationProductTaxService;

    @Mock
    ProductClient productClient;


    @Mock
    ValidateRiskService validateRiskService;

    @Mock
    ClauseClient clauseClient;

    @Mock
    OrganizationClient organizationClient;

    @Mock
    ClientDataClient clientDataClient;

    @Mock
    AgencyClient agencyClient;

    @Mock
    AkiResponseRepository akiResponseRepository;

    @Mock
    InsurerCurrencyClient insurerCurrencyClient;

    @Mock
    CreateQuotationEvent createQuotationEvent;

    @Mock
    ProductInstallmentClient productInstallmentClient;

    @Mock
    BinderClient binderClient;

    @Mock
    PremiumCardClient premiumCardClient;

    @Mock
    PolicyClientMock policyClient;

    @Mock
    PartialQuotationClient partialQuotationClient;

    @Mock
    PaymentPlan paymentPlan;

    @Mock
    SequenceGeneratorClient sequenceGeneratorClient;

    @Mock
    AllocatePaymentsEvent allocatePaymentsEvent;

    @Mock
    NotificationProducer notificationProducer;

    @Mock
    EndorsementService endorsementService;

    @InjectMocks
    QuotationServiceImpl quotationService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById() throws Exception {


        Optional<Quotation> quotationOptional = Optional.of(testQuotation());

        Mockito.when(quotationRepository.findByIdAndAgencyId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong())).thenReturn(quotationOptional);

        Quotation fetchedQuotation = quotationService.findById(1L, 1L).orElseThrow(() -> new Exception("quotation not found"));

        assertEquals(1L, fetchedQuotation.getId());
        assertEquals(1L, fetchedQuotation.getOrganizationId());
        Mockito.verify(quotationRepository).findByIdAndAgencyId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());

    }

    @Test
    void testFindById() throws Exception {


        Optional<Quotation> quotationOptional = Optional.of(testQuotation());

        Mockito.when(quotationRepository.findById(ArgumentMatchers.anyLong())).thenReturn(quotationOptional);

        Quotation fetchedQuotation = quotationService.findById(1L).orElseThrow(() -> new Exception("quotation not found"));

        assertEquals(1L, fetchedQuotation.getId());
        Mockito.verify(quotationRepository).findById(ArgumentMatchers.anyLong());


    }

    @Test
    void findQuotationById() {

        Optional<Quotation> optional = Optional.of(testReturnedQuotation());

        Mockito.when(quotationRepository.findById(ArgumentMatchers.anyLong())).thenReturn(optional);

        Mockito.when(clientDataClient.findById(ArgumentMatchers.anyLong())).thenReturn(testClientDto());

        Mockito.when(agencyClient.findByPanelIdAndOrganizationId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong())).thenReturn(testAgencyDto());

        Mockito.when(organizationClient.findById(ArgumentMatchers.anyLong())).thenReturn(testOrganizationDto());


        Quotation fetched = quotationService.findById(1L).orElse(null);

        assertNotNull(fetched);
        assertEquals(testReturnedQuotation().getClientId(), testClientDto().getId());

    }

    @Test
    void deleteById() {

        quotationService.deleteById(testReturnedQuotation().getId());

        Mockito.verify(quotationRepository).deleteById(ArgumentMatchers.anyLong());


    }

    @Test
    void findAll() {

        List<Quotation> quotationList = testReturnedQuotationList();

        Page<Quotation> quotationPage = new PageImpl<>(quotationList);

        Mockito.when(quotationRepository.findAllByOrganizationIdOrderByQuotationNoAscCreatedDateDesc(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(quotationPage);

        List<Quotation> quotations = quotationService.findAll(69L, Pageable.unpaged());

        quotations.forEach(quotation -> {
            System.out.println(quotation.getQuotationNo());
            System.out.println(quotation.getId());
        });
        assertNotNull(quotations);
        assertEquals(1, quotations.size());
        assertEquals(quotations.getFirst(), quotationList.getFirst());

    }

    @Test
    void findByQuotationNo() throws Exception {

        List<Quotation> quotationOptional = List.of(testReturnedQuotation());

        Mockito.when(quotationRepository.findByQuotationNoAndOrganizationId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong())).thenReturn(quotationOptional);

        List<Quotation> fetchedQuotation = quotationService.findByQuotationNo("QUOTATION_NO", 69L);


        assertEquals(69L, fetchedQuotation.getFirst().getOrganizationId());
        assertEquals("QUOTATION_NO", fetchedQuotation.getFirst().getQuotationNo());
        Mockito.verify(quotationRepository).findByQuotationNoAndOrganizationId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong());

    }

    @Test
    void findByClientIds() {

        List<Quotation> returnedQuotationList = testReturnedQuotationList();

        Page<Quotation> quotationPage = new PageImpl<>(returnedQuotationList);

        List<Long> clientIds = returnedQuotationList.stream().map(Quotation::getClientId).collect(Collectors.toList());

        Mockito.when(quotationRepository.findAllByClientIdInAndOrganizationId(ArgumentMatchers.anyList(), ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(quotationPage);

        List<Quotation> quotations = quotationService.findByClientIds(clientIds, 69L, Pageable.unpaged());

        assertNotNull(quotations);
        assertEquals(3, quotations.size());
        assertEquals(returnedQuotationList, quotations);

    }

    @Test
    void findClientRenewals() {
        List<Quotation> returnedQuotationList = testReturnedQuotationList();

        Page<Quotation> quotationPage = new PageImpl<>(returnedQuotationList);

        List<Long> clientIds = returnedQuotationList.stream().map(Quotation::getClientId).collect(Collectors.toList());

        Mockito.when(quotationRepository.findAllByClientIdInAndOrganizationIdAndStatus(ArgumentMatchers.anyList(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyString(), ArgumentMatchers.any())).thenReturn(quotationPage);

        List<Quotation> clientRenewals = quotationService.findClientRenewals(clientIds, 69L, Pageable.unpaged());

        assertNotNull(clientRenewals);
        assertEquals("EN", clientRenewals.get(1).getStatus());
        assertEquals(returnedQuotationList, clientRenewals);
    }

    @Test
    void update() {
        Quotation quote = testQuotationData(YesNo.Y).get(0);
        QuotationDto quotationDto = new QuotationDto();
        quotationDto.setQuotationNo(quote.getQuotationNo());
        quotationDto.setClientId(quote.getClientId());
        quotationDto.setOrganizationId(quote.getOrganizationId());
        quotationDto.setId(1L);

        when(quotationRepository.findByIdAndOrganizationId(anyLong(), anyLong())).thenReturn(Optional.of(quote));
        when(quotationRepository.save(any(Quotation.class))).thenReturn(quote);

        var result = quotationService.update(quote.getId(), quotationDto, 200L);
        assertNotNull(result);
        assertEquals(quote.getId(), result.getId());
    }

    @Test
    void updateWhereQuoteIsNotPresent() {
        Quotation quote = testQuotationData(YesNo.Y).get(0);
        QuotationDto quotationDto = new QuotationDto();
        quotationDto.setQuotationNo(quote.getQuotationNo());
        quotationDto.setClientId(quote.getClientId());
        quotationDto.setOrganizationId(quote.getOrganizationId());
        quotationDto.setId(1L);

        when(quotationRepository.findByIdAndOrganizationId(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(quotationRepository.save(any(Quotation.class))).thenReturn(quote);

        assertThrows(ResourceNotFoundException.class, () -> quotationService.update(quote.getId(), quotationDto, 200L));
    }

    @Test
    void count() {
        Quotation one = new Quotation();
        one.setId(1L);
        one.setOrganizationId(54L);

        Quotation two = new Quotation();
        two.setId(2L);
        two.setOrganizationId(54L);

        Quotation three = new Quotation();
        three.setId(3L);
        three.setOrganizationId(54L);

        List<Quotation> quotations = new LinkedList<>();
        quotations.add(one);
        quotations.add(two);
        quotations.add(three);

        Long total = (long) quotations.size();
        Mockito.when(quotationRepository.countByOrganizationId(ArgumentMatchers.anyLong())).thenReturn(total);

        Long count = quotationService.count(54L);

        assertEquals(count, total);


    }

    @Test
    void generateQuotationNumber() {

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setCode("AGENCIFY_PRODUCT");


        String value = "STRING";

        Mockito.when(productClient.findById(ArgumentMatchers.anyLong())).thenReturn(productDto);

        Mockito.when(sequenceGeneratorClient.generateQuotationNumber(ArgumentMatchers.anyMap())).thenReturn(value);

        String generatedValue = quotationService.generateQuotationNumber(69L, productDto.getId());

        assertNotNull(generatedValue);
        assertEquals(value, generatedValue);

    }


    @Test
    void findAllQuotations() {


        List<Quotation> returnedQuotationList = testReturnedQuotationList();

        Page<Quotation> quotationPage = new PageImpl<>(returnedQuotationList);

        Mockito.when(quotationRepository.findByOrganizationIdAndStatus(ArgumentMatchers.anyLong(), ArgumentMatchers.anyString(), ArgumentMatchers.any())).thenReturn(quotationPage);

        List<Quotation> quotations = quotationService.findAllQuotations(69L, Pageable.unpaged());

        assertNotNull(quotations);
        assertEquals(returnedQuotationList, quotations);
        assertEquals("NB", quotations.get(0).getStatus());
        assertEquals(69L, quotations.get(1).getOrganizationId());
    }

    @Test
    void findAllByCurrentStatusAndStatus() {


        List<Quotation> returnedQuotationList = testReturnedQuotationList();


        Page<Quotation> quotationPage = new PageImpl<>(returnedQuotationList);

        Mockito.when(quotationRepository.findAllByCurrentStatusAndStatusAndOrganizationId(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(quotationPage);

        List<Quotation> quotations = quotationService.findAllByCurrentStatusAndStatus("D", "NB", 69L, Pageable.unpaged());

        assertNotNull(quotations);
        assertEquals(returnedQuotationList, quotations);
    }

    @Test
    void cancel() {
        //method returns null
    }

    @Test
    void findQuotationClauses() {

    }

    @Test
    void findQuotationProducts() {
        QuotationProduct quotationProduct = new QuotationProduct();
        quotationProduct.setId(1L);
        quotationProduct.setQuotationId(1L);

        QuotationProduct secondQuotationProduct = new QuotationProduct();
        secondQuotationProduct.setId(2L);
        secondQuotationProduct.setQuotationId(1L);

        QuotationProduct thirdQuotationProduct = new QuotationProduct();
        thirdQuotationProduct.setId(3L);
        thirdQuotationProduct.setQuotationId(1L);

        List<QuotationProduct> quotationProducts = new LinkedList<>();
        quotationProducts.add(quotationProduct);
        quotationProducts.add(secondQuotationProduct);
        quotationProducts.add(thirdQuotationProduct);

        Mockito.when(quotationProductService.findByQuotationId(ArgumentMatchers.anyLong())).thenReturn(quotationProducts);

        testQuotation().setQuotationProducts(quotationProducts);

        List<QuotationProduct> fetchedList = quotationService.findQuotationProducts(testQuotation().getId(), testQuotation().getOrganizationId());

        assertEquals(3, fetchedList.size());
        assertEquals(fetchedList, quotationProducts);
    }

    @Test
    void convertQuotationToPolicies() throws ParseException {
        Quotation quote = testQuotationData(YesNo.Y).get(0);
        doNothing().when(quotationProductService).convertToPolicy(any(QuotationProduct.class));

        quotationService.convertQuotationToPolicies(quote, 69L);
        verify(quotationProductService, times(1)).convertToPolicy(any(QuotationProduct.class));

    }

    @Test
    void convertQuotationToPoliciesWhereQuotationisNull() throws ParseException {
        Quotation quote = null;
        doNothing().when(quotationProductService).convertToPolicy(any(QuotationProduct.class));

        assertThrows(ItemCannotBeNullException.class, () -> quotationService.convertQuotationToPolicies(quote, 69L));
    }

    @Test
    void saveQuickQuotation() throws QuoteCreationException {
        Quotation quotation = testQuotationData(YesNo.Y).getFirst();
        quotation.setStatus("NB");
        Optional<Quotation> optionalQuotation = Optional.of(quotation);
        InsurerCurrencyDto insurerCurrencyData = testInsurerCurrencyDto();

        when(quotationRepository.findByRenewalBatchNo(anyLong())).thenReturn(optionalQuotation);
        when(agencyClient.findByPanelIdAndOrganizationId(anyLong(), anyLong())).thenReturn(testAgencyData());
        when(insurerCurrencyClient.findInsurerCurrency(anyLong(), anyLong())).thenReturn(insurerCurrencyData);
        when(quotationProductService.saveQuickQuotationProduct(any(Quotation.class), any(QuotationProduct.class))).thenReturn(quotation.getQuotationProducts().getFirst());
        when(quotationRepository.save(any(Quotation.class))).thenReturn(quotation);
        when(clientDataClient.findById(anyLong())).thenReturn(testClientData());
        when(organizationClient.findById(anyLong())).thenReturn(testOrganizationData());
        when(premiumCardClient.initPayment(any(PremiumCardDto.class))).thenReturn(premiumCardDto());
        Quotation result = quotationService.saveQuickQuotation(quotation, 1L);
        System.out.println(result);
        assertNotNull(result);
        assertEquals(result, quotation);
    }


    @Test
    void getRenewals() {
        List<Quotation> quotations = testQuotationData(YesNo.Y);
        quotations.forEach(quote -> {
            quote.setStatus("RN");
        });
        when(quotationRepository.findByOrganizationIdAndStatus(anyLong(), anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(quotations));

        var result = quotationService.getRenewals(1L, Pageable.unpaged());
        assertEquals("RN", result.get(0).getStatus());
        assertNotNull(result);
    }

    @Test
    void composeQuotationWhenClientIdandPanelIdisPresent() {
        when(clientDataClient.findById(ArgumentMatchers.anyLong())).thenReturn(testClientData());
        when(agencyClient.findByPanelIdAndOrganizationId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong())).thenReturn(testAgencyData());
        when(organizationClient.findById(ArgumentMatchers.anyLong())).thenReturn(testOrganizationData());
        var result = quotationService.composeQuotation(testQuotationData(YesNo.Y).get(0));
        assertNotNull(result);
        assertEquals(result.getClient().getId(), testClientData().getId());
    }

    @Test
    void composeQuotationWhenClientIdisNotPresentandPanelIdisPresent() {
        var quotation = testQuotationData(YesNo.Y).get(0);
        quotation.setClient(null);
        when(clientDataClient.findById(ArgumentMatchers.anyLong())).thenReturn(quotation.getClient());
        when(agencyClient.findByPanelIdAndOrganizationId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong())).thenReturn(testAgencyData());
        when(organizationClient.findById(ArgumentMatchers.anyLong())).thenReturn(testOrganizationData());
        var result = quotationService.composeQuotation(quotation);
        assertNotNull(result);
        assertNull(result.getClient());
        assertEquals(result.getAgent().getId(), testAgencyData().getId());
    }


    @Test
    void loadAgentBusiness() {
        when(quotationRepository.findAllByAgencyId(anyLong())).thenReturn(testQuotationData(YesNo.Y));
        when(quotationRepository.saveAll(anyList())).thenReturn(testQuotationData(YesNo.Y));
    }

    @Test
    void sortFilterAndSearch() {
        when(quotationRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(new PageImpl<>(testQuotationData(YesNo.Y)));

        var result = quotationService.sortFilterAndSearch("policy", 1L, 200L, 1683062955000L, 1683062955000L, BigDecimal.valueOf(1000000), BigDecimal.valueOf(2000000), SortType.ASC, SortType.ASC, 0, 10, 200L, "NB");
        assertNotNull(result);
    }

    @Test
    void sortFilterAndSearchSortByNullSortPriceNotNull() {
        when(quotationRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(new PageImpl<>(testQuotationData(YesNo.Y)));

        var result = quotationService.sortFilterAndSearch("policy", 1L, 200L, 1683062955000L, 1683062955000L, BigDecimal.valueOf(1000000), BigDecimal.valueOf(2000000), null, SortType.ASC, 0, 10, 200L, "NB");
        assertNotNull(result);
    }

    @Test
    void sortFilterAndSearchSortByNotNull() {
        when(quotationRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(new PageImpl<>(testQuotationData(YesNo.Y)));

        var result = quotationService.sortFilterAndSearch("policy", 1L, 200L, 1683062955000L, 1683062955000L, BigDecimal.valueOf(1000000), BigDecimal.valueOf(2000000), SortType.ASC, null, 0, 10, 200L, "NB");
        assertNotNull(result);
    }

    @Test
    void sortFilterAndSearchTextIsNull() {
        when(quotationRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(new PageImpl<>(testQuotationData(YesNo.Y)));
        var result = quotationService.sortFilterAndSearch(null, 1L, 200L, 1683062955000L, 1683062955000L, BigDecimal.valueOf(1000000), BigDecimal.valueOf(2000000), SortType.ASC, SortType.ASC, 0, 10, 200L, "NB");
        assertNotNull(result);
    }

    @Test
    void getQuotationsByClientIdAndOrgId() {
        Quotation one = new Quotation();
        one.setReadStatus(false);
        one.setStatus("RN");
        one.setOrganizationId(1L);
        one.setCreatedDate(1637585163L);
        one.setCurrentStatus("R");
        one.setClientId(600L);

        Quotation two = new Quotation();
        two.setReadStatus(false);
        two.setStatus("RN");
        two.setOrganizationId(1L);
        two.setCreatedDate(1637585163L);
        two.setCurrentStatus("R");
        two.setClientId(600L);

        Quotation three = new Quotation();
        three.setReadStatus(false);
        three.setStatus("RN");
        three.setOrganizationId(1L);
        three.setCreatedDate(1637585163L);
        three.setCurrentStatus("R");
        three.setClientId(600L);


        List<Quotation> returnedList = new LinkedList<>();
        returnedList.add(one);
        returnedList.add(two);
        returnedList.add(three);

        Mockito.when(quotationRepository.findByClientIdAndOrganizationIdAndCurrentStatus(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyString())).thenReturn(returnedList);

        List<Quotation> quotations = quotationService.getQuotationsByClientIdAndOrgId(600L, 1L);

        assertEquals(3, quotations.size());
        assertEquals(returnedList, quotations);
    }

    @Test
    void verifyQuotation() {
        Quotation quotation = testQuotationData(YesNo.Y).get(0);
        quotation.setPanelId(1L);
        AgencyDto agencyDto = testAgencyData();
        ErrorDto errorDto = new ErrorDto();
        errorDto.setErrorCode("406");
        errorDto.setErrorText("Quotation is not valid");
        ArrayList<ErrorDto> errorDtos = new ArrayList<>();
        errorDtos.add(errorDto);
        ValidationResponseDto validationResponseDto = new ValidationResponseDto();
        validationResponseDto.setSuccess(true);
        validationResponseDto.setErrors(errorDtos);
        when(quotationRepository.findById(anyLong())).thenReturn(Optional.of(quotation));
        when(agencyClient.findByPanelIdAndOrganizationId(anyLong(), anyLong())).thenReturn(agencyDto);
        when(validateRiskService.validateRisk(anyLong())).thenReturn(validationResponseDto);

        var result = quotationService.verifyQuotation(1L);
        assertTrue(result);
    }

    @Test
    void verifyQuotationWhenSuccessIsFalse() {
        Quotation quotation = testQuotationData(YesNo.Y).get(0);
        AgencyDto agencyDto = testAgencyData();
        ErrorDto errorDto = new ErrorDto();
        errorDto.setErrorCode("406");
        errorDto.setErrorText("Quotation is not valid");
        ArrayList<ErrorDto> errorDtos = new ArrayList<>();
        errorDtos.add(errorDto);
        ValidationResponseDto validationResponseDto = new ValidationResponseDto();
        validationResponseDto.setSuccess(false);
        validationResponseDto.setErrors(errorDtos);
        when(quotationRepository.findById(anyLong())).thenReturn(Optional.of(quotation));
        when(agencyClient.findByPanelIdAndOrganizationId(anyLong(), anyLong())).thenReturn(agencyDto);
        when(validateRiskService.validateRisk(anyLong())).thenReturn(validationResponseDto);

        assertThrows(AKIValidationException.class, () -> quotationService.verifyQuotation(1L));
    }

    @Test
    void getNoRenewalsFromDateNullEndDate() {
        Quotation one = new Quotation();
        one.setReadStatus(false);
        one.setStatus("RN");
        one.setOrganizationId(1L);
        one.setCreatedDate(1637585163L);

        Quotation two = new Quotation();
        two.setReadStatus(false);
        two.setStatus("RN");
        two.setOrganizationId(1L);
        two.setCreatedDate(1637585163L);

        Quotation three = new Quotation();
        three.setReadStatus(false);
        three.setStatus("RN");
        three.setOrganizationId(1L);
        three.setCreatedDate(1637585163L);


        List<Quotation> returnedList = new LinkedList<>();
        returnedList.add(one);
        returnedList.add(two);
        returnedList.add(three);

        Mockito.when(quotationRepository.findAllByOrganizationIdAndCreatedDateIsGreaterThanAndStatus(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyString())).thenReturn(returnedList);

        int number = quotationService.getNoRenewalsFromDate(1637585163L, null, 1L);

        assertEquals(number, returnedList.size());
    }

    @Test
    void getNoRenewalsFromDate() {

        Quotation one = new Quotation();
        one.setReadStatus(false);
        one.setStatus("RN");
        one.setOrganizationId(1L);
        one.setCreatedDate(1637585163L);

        Quotation two = new Quotation();
        two.setReadStatus(false);
        two.setStatus("RN");
        two.setOrganizationId(1L);
        two.setCreatedDate(1637585163L);

        Quotation three = new Quotation();
        three.setReadStatus(false);
        three.setStatus("RN");
        three.setOrganizationId(1L);
        three.setCreatedDate(1637585163L);


        List<Quotation> returnedList = new LinkedList<>();
        returnedList.add(one);
        returnedList.add(two);
        returnedList.add(three);

        Mockito.when(quotationRepository.findAllByOrganizationIdAndCreatedDateIsGreaterThanAndCreatedDateIsLessThanAndStatus(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyString())).thenReturn(returnedList);
        int number = quotationService.getNoRenewalsFromDate(1637585163L, 1669121163L, 1L);

        assertEquals(number, returnedList.size());

    }

    @Test
    void findByRenewalBatchNo() {
        testReturnedQuotation().setRenewalBatchNo(13L);

        Mockito.when(quotationRepository.findByRenewalBatchNo(ArgumentMatchers.anyLong())).thenReturn(Optional.of(testReturnedQuotation()));

        Optional<Quotation> fetchedQuotation = quotationService.findByRenewalBatchNo(13L);

        assertNotNull(fetchedQuotation);
        assertEquals(Optional.of(testReturnedQuotation()), fetchedQuotation);
    }

    @Test
    void findUnreadQuotes() {
        Quotation one = new Quotation();
        one.setReadStatus(false);
        one.setStatus("NB");
        one.setOrganizationId(1L);

        Quotation two = new Quotation();
        two.setReadStatus(false);
        two.setStatus("NB");
        two.setOrganizationId(1L);

        Quotation three = new Quotation();
        three.setReadStatus(false);
        three.setStatus("NB");
        three.setOrganizationId(1L);


        List<Quotation> returnedList = new LinkedList<>();
        returnedList.add(one);
        returnedList.add(two);
        returnedList.add(three);

        Mockito.when(quotationRepository.findAllByReadStatusAndStatusAndOrganizationId(ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyString(), ArgumentMatchers.anyLong())).thenReturn(returnedList);

        Mono<Integer> unreadQuotes = quotationService.findUnreadQuotes(1L);

        assertNotNull(unreadQuotes);
        assertEquals(returnedList.size(), unreadQuotes.block());
    }

    @Test
    void findUnreadRenewals() {
        Quotation one = new Quotation();
        one.setReadStatus(false);
        one.setStatus("RN");
        one.setOrganizationId(1L);

        Quotation two = new Quotation();
        two.setReadStatus(false);
        two.setStatus("RN");
        two.setOrganizationId(1L);

        Quotation three = new Quotation();
        three.setReadStatus(false);
        three.setStatus("RN");
        three.setOrganizationId(1L);


        List<Quotation> returnedList = new LinkedList<>();
        returnedList.add(one);
        returnedList.add(two);
        returnedList.add(three);

        Mockito.when(quotationRepository.findAllByReadStatusAndStatusAndOrganizationId(ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyString(), ArgumentMatchers.anyLong())).thenReturn(returnedList);

        Mono<Integer> unreadRenewals = quotationService.findUnreadRenewals(1L);

        assertNotNull(unreadRenewals);
        assertEquals(returnedList.size(), unreadRenewals.block());

    }

    @Test
    void getByPolicyNo() {
        Quotation one = new Quotation();
        one.setPolicyNo("POLICY_NO");

        Quotation two = new Quotation();
        two.setPolicyNo("POLICY_NO");

        Quotation three = new Quotation();
        three.setPolicyNo("POLICY_NO");

        List<Quotation> returnedList = new LinkedList<>();
        returnedList.add(one);
        returnedList.add(two);
        returnedList.add(three);

        Mockito.when(quotationRepository.findByPolicyNo(anyString())).thenReturn(returnedList);

        List<Quotation> quotations = quotationService.getByPolicyNo("POLICY_NO");

        assertEquals(3, quotations.size());
        assertEquals(returnedList, quotations);
        assertEquals("POLICY_NO", quotations.get(0).getPolicyNo());
    }

    @Test
    void deleteByRightPolicyNo() {
        when(quotationRepository.findByPolicyNo("P/TEST/001/2023")).thenReturn(testQuotationData(YesNo.Y));
        doNothing().when(quotationRepository).deleteById(anyLong());

        var result = quotationService.deleteByPolicyNo("P/TEST/001/2023");

        verify(quotationRepository, times(1)).findByPolicyNo(anyString());
        verify(quotationRepository, times(1)).deleteById(anyLong());


    }

    @Test
    void deleteByWrongPolicyNo() {
        when(quotationRepository.findByPolicyNo("P/TEST/001/2023")).thenReturn(testQuotationData(YesNo.Y));
        doNothing().when(quotationRepository).deleteById(anyLong());

        var result = quotationService.deleteByPolicyNo("POLICY_NO");

        verify(quotationRepository, times(1)).findByPolicyNo(anyString());
        verify(quotationRepository, times(0)).deleteById(anyLong());


    }

    @Test
    void findByPolicyNo() {
        when(quotationRepository.findByPolicyNo(anyString())).thenReturn(testQuotationData(YesNo.Y));

        var result = quotationService.findByPolicyNo("P/TEST/001/2023");
        assertEquals(testQuotationData(YesNo.Y).get(0), result);
    }

    @Test
    void getByQuotationNo() {
        when(quotationRepository.findByQuotationNo(anyString())).thenReturn(testQuotationData(YesNo.Y));

        var result = quotationService.findByQuotationNo("Q/TEST/001/2023");
        assertEquals(testQuotationData(YesNo.Y), result);
    }

    @Test
    void getByQuotationNoOrderByIdDesc() {
        when(quotationRepository.findByQuotationNoOrderByIdDesc(anyString())).thenReturn(testQuotationData(YesNo.Y));

        var result = quotationService.getByQuotationNoOrderByIdDesc("Q/TEST/001/2023");
        assertEquals(testQuotationData(YesNo.Y), result);
    }

    @Test
    void findQuotationDraft() {
        when(quotationRepository.findTopByPolicyNoAndStatusAndCurrentStatusOrderByIdDesc(anyString(), anyString(), anyString())).thenReturn(Optional.of(testQuotationData(YesNo.Y).get(0)));

        var result = quotationService.findQuotationDraft("P/TEST/001/2023");

        assertEquals(Optional.of(testQuotationData(YesNo.Y).get(0)), result);
        assertNotNull(result);
    }

    @Test
    void deletePrimaryQuotationRisk() {
        Quotation noQuotationRisk = testQuotationData(YesNo.Y).get(0);
        noQuotationRisk.getQuotationProducts().get(0).setQuotationRisks(null);

        when(quotationRepository.findById(anyLong())).thenReturn(Optional.of(testQuotationData(YesNo.Y).get(0)));

        doThrow(new QuoteRiskDeletionException("Cannot delete primary risk!")).when(quotationProductService).deleteQuotationProductRisk(any(Quotation.class), anyLong());

        when(quotationProductService.saveQuickQuotationProduct(any(Quotation.class), any(QuotationProduct.class))).thenReturn(noQuotationRisk.getQuotationProducts().get(0));

        assertThrows(QuoteRiskDeletionException.class, () -> quotationService.deleteQuotationRisk(1L, 1L));
    }

    @Test
    void deleteSecondaryQuotationRisk() {
        Quotation quotation = testReturnedQuotationList().getFirst();


        when(quotationRepository.findById(anyLong())).thenReturn(Optional.of(quotation));

        when(quotationProductService.deleteQuotationProductRisk(any(Quotation.class), anyLong())).thenReturn(quotation);

        when(quotationProductService.saveQuickQuotationProduct(any(Quotation.class), any(QuotationProduct.class))).thenReturn(quotation.getQuotationProducts().getFirst());

        when(quotationRepository.save(any(Quotation.class))).thenReturn(quotation);

        when(premiumCardClient.initPayment(any(PremiumCardDto.class))).thenReturn(premiumCardDto());

        when(clientDataClient.findById(anyLong())).thenReturn(testClientData());

        when(organizationClient.findById(anyLong())).thenReturn(testOrganizationData());

        when(agencyClient.findByPanelIdAndOrganizationId(anyLong(), anyLong())).thenReturn(testAgencyData());
        var result = quotationService.deleteQuotationRisk(2L, 1L);
        System.out.println("Result:" + result);
        System.out.println("Quotation:" + quotation);
        assertEquals(quotation, result);

    }


    @Test
    void convertPartialQuoteToPolicies() throws ParseException {
        Optional<Quotation> optQuote = Optional.of(testQuotationData(YesNo.N).getFirst());
        Quotation partialQuote = testQuotationData(YesNo.Y).getFirst();
        when(quotationRepository.findById(anyLong())).thenReturn(optQuote);
        when(partialQuotationClient.computeQuotationInstallments(optQuote.get())).thenReturn(partialQuote);
        doNothing().when(quotationProductService).convertToPolicy(any(QuotationProduct.class));
        quotationService.convertQuoteToPolicies(1L, 322L, "ABRCPI32L");
        verify(quotationProductService, times(1)).convertToPolicy(any(QuotationProduct.class));
    }


    @Test
    void convertNonPartialQuoteToPolicies() throws ParseException {
        Optional<Quotation> optQuote = Optional.of(testQuotationData(YesNo.Y).getFirst());
        when(quotationRepository.findById(anyLong())).thenReturn(optQuote);
        when(endorsementService.saveActiveRisks(optQuote.get(), (YesNo.Y))).thenReturn(any());
        when(endorsementService.computeFirstInstallment(optQuote.get())).thenReturn(optQuote.get());
        doNothing().when(quotationProductService).convertToPolicy(any(QuotationProduct.class));
        quotationService.convertQuoteToPolicies(1L, 322L, "ABRCPI32L");
        verify(quotationProductService, times(1)).convertToPolicy(any(QuotationProduct.class));
    }


    @Test
    void convertQuoteToPoliciesIfInstallAllowedisFalse() throws ParseException {
        Optional<Quotation> optQuote = Optional.of(testQuotationData(YesNo.Y).getFirst());
        optQuote.get().getQuotationProducts().getFirst().setInstallmentAllowed(YesNo.N);
        when(quotationRepository.findById(anyLong())).thenReturn(optQuote);
        doNothing().when(quotationProductService).convertToPolicy(any(QuotationProduct.class));
        quotationService.convertQuoteToPolicies(1L, 322L, "ABRCPI32L");
        verify(quotationProductService, times(1)).convertToPolicy(any(QuotationProduct.class));
    }

    @Test
    void convertQuoteToPoliciesIfQuotationIsNull() {
        Optional<Quotation> optQuote = Optional.empty();
        when(quotationRepository.findById(anyLong())).thenReturn(optQuote);
        assertThrows(ResourceNotFoundException.class, () -> quotationService.convertQuoteToPolicies(1L, 322L, "ABRCPI32L"));
    }

    @Test
    void testFindScheduleDetails() {

        Quotation quotation = testQuotationData(YesNo.Y).getFirst();
        quotation.setId(1L);
        quotation.setPolicyNo("testPolicyNo");
        quotation.setInsurerOrgId(1L);

        when(clientDataClient.findById(1L)).thenReturn(testClientData());
        when(agencyClient.findByPanelIdAndOrganizationId(1L, 1L)).thenReturn(testAgencyDto());
        when(organizationClient.findById(1L)).thenReturn(testOrganizationDto());

        when(quotationRepository.findById(anyLong())).thenReturn(Optional.of(quotation));
        when(quotationRiskService.findRisksByRegistrationNo(anyString())).thenReturn(testQuotationRiskData());
        when(quotationProductService.find(anyLong())).thenReturn(Optional.of(testQuotationProductData(YesNo.Y).getFirst()));

        ScheduleDetailsDto result = quotationService.findScheduleDetails("AGN 001N");

        assertEquals(quotation.getPolicyNo(), result.getPolicyNo());
        assertEquals(quotation.getInsurerOrgId(), result.getInsurerOrgId());
    }

    @Test
    void shouldReturnQuotationWhenPaymentRefExists() {
        // Given
        String paymentRef = "paymentRef";
        Quotation expectedQuotation = new Quotation();
        when(quotationRepository.findByPaymentRef(paymentRef)).thenReturn(Optional.of(expectedQuotation));

        // When
        Optional<Quotation> actualQuotation = quotationService.findByPaymentRef(paymentRef);

        // Then
        assertTrue(actualQuotation.isPresent());
        assertEquals(expectedQuotation, actualQuotation.get());
        verify(quotationRepository, times(1)).findByPaymentRef(paymentRef);
    }

    @Test
    void shouldReturnEmptyWhenPaymentRefDoesNotExist() {
        // Given
        String paymentRef = "nonExistentPaymentRef";
        when(quotationRepository.findByPaymentRef(paymentRef)).thenReturn(Optional.empty());

        // When
        Optional<Quotation> actualQuotation = quotationService.findByPaymentRef(paymentRef);

        // Then
        assertFalse(actualQuotation.isPresent());
        verify(quotationRepository, times(1)).findByPaymentRef(paymentRef);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenQuotationDoesNotExist() {
        // Given
        Long id = 1L;
        Long organizationId = 1L;
        when(quotationRepository.findByIdAndAgencyId(id, organizationId)).thenReturn(Optional.empty());

        // Then
        assertThrows(ResourceNotFoundException.class, () -> {
            // When
            quotationService.convertQuotationToPolicies(id, organizationId);
        });

        verify(quotationRepository, times(1)).findByIdAndAgencyId(id, organizationId);
    }

    private List<Quotation> testQuotationData(YesNo installmentAllowed) {
        Quotation quotation = new Quotation();
        quotation.setId(1L);
        quotation.setQuotationNo("Q/TEST/001/2023");
        quotation.setPolicyNo("P/TEST/001/2023");
        quotation.setClient(testClientData());
        quotation.setOrganizationId(1L);
        quotation.setCurrentStatus("D");
        quotation.setOrganization(testOrganizationData());
        quotation.setAgent(testAgencyData());
        quotation.setQuotationProducts(testQuotationProductData(installmentAllowed));
        quotation.setClientId(1L);
        quotation.setBasicPremium(BigDecimal.valueOf(10000.00));
        quotation.setPremium(BigDecimal.valueOf(10000.00));

        var quotationList = new ArrayList<Quotation>();
        quotationList.add(quotation);

        return quotationList;
    }

    private List<QuotationProduct> testQuotationProductData(YesNo installmentAllowed) {
        QuotationProduct quotationProduct = new QuotationProduct();
        quotationProduct.setId(1L);
        quotationProduct.setQuotationId(1L);
        quotationProduct.setQuotationProductTaxes(testQuotationProductTaxData());
        quotationProduct.setQuotationRisks(testQuotationRiskData());
        quotationProduct.setInstallmentAllowed(installmentAllowed);

        var quotationProductList = new ArrayList<QuotationProduct>();
        quotationProductList.add(quotationProduct);
        return quotationProductList;
    }

    private List<QuotationProductTax> testQuotationProductTaxData() {
        QuotationProductTax quotationProductTax = new QuotationProductTax();
        quotationProductTax.setId(1L);
        quotationProductTax.setQuotationProductId(1L);
        quotationProductTax.setTaxRate(testTaxRateDtoData());
        quotationProductTax.setTaxAmount(BigDecimal.valueOf(160000.00));
        quotationProductTax.setApplicationArea("P");

        var quotationProductTaxList = new ArrayList<QuotationProductTax>();
        quotationProductTaxList.add(quotationProductTax);

        return quotationProductTaxList;
    }

    private TaxRateDto testTaxRateDtoData() {
        TaxRateDto taxRateDto = new TaxRateDto();
        taxRateDto.setRate(BigDecimal.valueOf(16.00));
        taxRateDto.setAmount(BigDecimal.valueOf(160000.00));
        taxRateDto.setTransactionTypeCode("PHREFUND");
        return taxRateDto;
    }

    private List<QuotationRisk> testQuotationRiskData() {
        QuotationRisk quotationRisk = new QuotationRisk();
        quotationRisk.setId(1L);
        quotationRisk.setQuotationProductId(1L);
        quotationRisk.setRiskId("AGN 001N");
        quotationRisk.setCoverTypeId(1L);
        quotationRisk.setCoverTypeCode("COMP");
        quotationRisk.setWithEffectFromDate(LocalDateTime.now().atZone(TimeZone.getTimeZone("Africa/Nairobi").toZoneId()).toInstant().toEpochMilli());
        quotationRisk.setWithEffectToDate(LocalDateTime.now().plusMonths(1).atZone(TimeZone.getTimeZone("Africa/Nairobi").toZoneId()).toInstant().toEpochMilli());
        quotationRisk.setMotorSchedules(testScheduleData());
        quotationRisk.setValue(BigDecimal.valueOf(1000000.00));
        quotationRisk.setInstallmentPremium(BigDecimal.valueOf(10000.00));
        quotationRisk.setInstallmentAmount(BigDecimal.valueOf(10000.00));
        quotationRisk.setCommissionAmount(BigDecimal.valueOf(1000.00));
        quotationRisk.setBasicPremium(BigDecimal.valueOf(10000.00));
        quotationRisk.setFutureAnnualPremium(BigDecimal.valueOf(10000.00));
        quotationRisk.setPaidInstallmentComm(BigDecimal.valueOf(1000.00));
        quotationRisk.setPaidInstallmentAmount(BigDecimal.valueOf(10000.00));
        quotationRisk.setOutstandingCommission(BigDecimal.valueOf(1000.00));
        quotationRisk.setWithHoldingTax(BigDecimal.valueOf(100.00));
        quotationRisk.setQuotationRiskTaxes(testRiskTaxesData());

        QuotationRisk quotationRisk2 = new QuotationRisk();
        quotationRisk2.setId(2L);
        quotationRisk2.setQuotationProductId(1L);
        quotationRisk2.setRiskId("AGN 001N");
        quotationRisk2.setCoverTypeId(1L);
        quotationRisk2.setCoverTypeCode("COMP");
        quotationRisk2.setWithEffectFromDate(LocalDateTime.now().atZone(TimeZone.getTimeZone("Africa/Nairobi").toZoneId()).toInstant().toEpochMilli());
        quotationRisk2.setWithEffectToDate(LocalDateTime.now().plusMonths(1).atZone(TimeZone.getTimeZone("Africa/Nairobi").toZoneId()).toInstant().toEpochMilli());
        quotationRisk2.setMotorSchedules(testScheduleData());
        quotationRisk.setValue(BigDecimal.valueOf(1000000.00));
        quotationRisk.setInstallmentPremium(BigDecimal.valueOf(10000.00));
        quotationRisk.setInstallmentAmount(BigDecimal.valueOf(10000.00));
        quotationRisk.setCommissionAmount(BigDecimal.valueOf(1000.00));
        quotationRisk.setBasicPremium(BigDecimal.valueOf(10000.00));
        quotationRisk.setTotalPremium(BigDecimal.valueOf(10000.00));
        quotationRisk.setFutureAnnualPremium(BigDecimal.valueOf(10000.00));
        quotationRisk.setPaidInstallmentComm(BigDecimal.valueOf(1000.00));
        quotationRisk.setPaidInstallmentAmount(BigDecimal.valueOf(10000.00));
        quotationRisk.setCommInstallmentPremium(BigDecimal.valueOf(10000.00));
        quotationRisk.setOutstandingInstallmentAmount(BigDecimal.valueOf(10000.00));
        quotationRisk.setOutstandingCommission(BigDecimal.valueOf(1000.00));
        quotationRisk.setWithHoldingTax(BigDecimal.valueOf(100.00));


        var quotationRiskList = new ArrayList<QuotationRisk>();
        quotationRiskList.add(quotationRisk);
        quotationRiskList.add(quotationRisk2);

        return quotationRiskList;
    }

    private List<QuotationRiskTax> testRiskTaxesData() {
        QuotationRiskTax quotationRiskTax = new QuotationRiskTax();
        quotationRiskTax.setQuotationRiskId(1L);
        quotationRiskTax.setTaxAmount(BigDecimal.valueOf(100.00));
        quotationRiskTax.setRate(BigDecimal.valueOf(16.00));
        quotationRiskTax.setTaxRateInstallmentType(TaxRateInstallmentType.FIRST);
        quotationRiskTax.setTransactionTypeCode("PHREFUND");

        var quotationRiskTaxList = new ArrayList<QuotationRiskTax>();
        quotationRiskTaxList.add(quotationRiskTax);


        return quotationRiskTaxList;

    }

    private ClientDto testClientData() {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);
        clientDto.setFirstName("John");
        clientDto.setLastName("Doe");
        clientDto.setClientType(ClientTypes.INDIVIDUAL);
        clientDto.setKraPin("A033244243Z");
        clientDto.setEmailAddress("agencify@agent.com");
        clientDto.setPhoneNumber("0712345678");

        return clientDto;

    }

    private MotorSchedules testScheduleData() {
        MotorSchedules schedules = new MotorSchedules();
        schedules.setId(1L);
        schedules.setMake("Toyota");
        schedules.setModel("Corolla");
        schedules.setYearOfManufacture(2023L);
        schedules.setYearOfRegistration(2023L);
        schedules.setChasisNo("AJFLLSDKKLESD");

        return schedules;
    }

    private ScheduleDetailsDto testScheduleDetailsData() {
        ScheduleDetailsDto scheduleDetailsDto = new ScheduleDetailsDto();
        var schedule = testScheduleData();
        var client = testClientData();
        var quoteRisk = testQuotationRiskData().get(0);
        scheduleDetailsDto.setVehicleMake(schedule.getMake());
        scheduleDetailsDto.setVehicleModel(schedule.getModel());
        scheduleDetailsDto.setVehicleManufactureYear(schedule.getYearOfManufacture());
        scheduleDetailsDto.setVehicleRegistrationYear(schedule.getYearOfRegistration());
        scheduleDetailsDto.setChassisNumber(schedule.getChasisNo());
        scheduleDetailsDto.setBodyType(schedule.getBodyType());
        scheduleDetailsDto.setClientEmail(client.getEmailAddress());
        scheduleDetailsDto.setClientPIN(client.getKraPin());
        scheduleDetailsDto.setCoverTypeId(quoteRisk.getCoverTypeId());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        scheduleDetailsDto.setCommencingDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(quoteRisk.getWithEffectFromDate()), TimeZone.getTimeZone("Africa/Nairobi").toZoneId()).format(dtf));
        scheduleDetailsDto.setExpiryDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(quoteRisk.getWithEffectToDate()), TimeZone.getTimeZone("Africa/Nairobi").toZoneId()).format(dtf));

        return scheduleDetailsDto;
    }

    private AgencyDto testAgencyData() {
        AgencyDto agency = new AgencyDto();
        agency.setId(1L);
        agency.setInsurerId(1000L);
        agency.setPanelId(1L);
        agency.setOrganization(testOrganizationData());
        agency.setPanelId(1L);

        return agency;
    }

    private OrganizationDto testOrganizationData() {
        OrganizationDto organization = new OrganizationDto();
        organization.setId(1000L);
        organization.setAccountTypeId(1L);
        organization.setEntities(testEntitiesData());
        organization.setAccountType(testAccountTypeData());

        return organization;
    }

    private AccountTypeDto testAccountTypeData() {
        AccountTypeDto accountType = new AccountTypeDto();
        accountType.setId(1L);
        accountType.setCode("AGENCY");
        accountType.setFormat("AGN");

        return accountType;
    }

    private EntitiesDto testEntitiesData() {
        EntitiesDto entities = new EntitiesDto();
        entities.setId(1L);
        entities.setFirstName("John");
        entities.setLastName("Doe");
        entities.setEmailAddress("agent@agencify.insure");
        entities.setPhoneNumber("0712345678");
        entities.setOrganizationName("Agencify");
        entities.setAkiIntegrated(true);

        return entities;
    }


    private PremiumCardDto premiumCardDto() {
        PremiumCardDto premiumCardDto = new PremiumCardDto();

        premiumCardDto.setQuotationNo("Q/TEST/001/2023");
        premiumCardDto.setQuotationId(1L);
        premiumCardDto.setFees(BigDecimal.valueOf(10000.00));
        premiumCardDto.setCommission(BigDecimal.valueOf(10000.00));
        premiumCardDto.setBasicPremium(BigDecimal.valueOf(10000.00));
        premiumCardDto.setIncentives(BigDecimal.valueOf(10000.00));
        premiumCardDto.setRiskTaxes(BigDecimal.valueOf(10000.00));
        premiumCardDto.setPolicyTaxes(BigDecimal.valueOf(10000.00));
        premiumCardDto.setClientId(1L);
        premiumCardDto.setPaymentRef("1234567890");
        premiumCardDto.setWithHoldingTax(BigDecimal.valueOf(10000.00));

        return premiumCardDto;

    }

    private InsurerCurrencyDto testInsurerCurrencyDto() {
        InsurerCurrencyDto insurerCurrencyDto = new InsurerCurrencyDto();
        insurerCurrencyDto.setCode("KSHS");
        insurerCurrencyDto.setId(1L);
        insurerCurrencyDto.setOrganizationId(200L);
        insurerCurrencyDto.setSymbol("KSHS");

        return insurerCurrencyDto;
    }


    private Quotation testQuotation() {
        Quotation quotation = new Quotation();
        quotation.setId(1L);
        quotation.setQuotationNo("Q/TEST/001/2023");
        quotation.setPolicyNo("P/TEST/001/2023");
        quotation.setClient(testClientData());
        quotation.setOrganizationId(1L);
        quotation.setCurrentStatus("D");
        quotation.setOrganization(testOrganizationData());
        quotation.setAgent(testAgencyData());
        quotation.setQuotationProducts(testQuotationProductData(YesNo.Y));
        quotation.setClientId(1L);
        quotation.setBasicPremium(BigDecimal.valueOf(10000.00));
        quotation.setPremium(BigDecimal.valueOf(10000.00));

        return quotation;
    }

    private ClientDto testClientDto() {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);
        clientDto.setPhoneNumber("PHONE_NO");
        clientDto.setFirstName("FIRST_NAME");
        clientDto.setLastName("LAST_NAME");

        return clientDto;
    }

    private AgencyDto testAgencyDto() {
        AgencyDto agencyDto = new AgencyDto();
        agencyDto.setId(700L);
        agencyDto.setPanelId(17L);
        agencyDto.setInsurerId(69L);
        agencyDto.setOrganizationId(69L);

        return agencyDto;
    }

    private OrganizationDto testOrganizationDto() {
        OrganizationDto organizationDto = new OrganizationDto();
        organizationDto.setId(69L);
        organizationDto.setAccountTypeId(1L);

        return organizationDto;
    }

    private Quotation testReturnedQuotation() {

        Quotation returnedQuotation = new Quotation();
        returnedQuotation.setId(1L);
        returnedQuotation.setClientId(1L);
        returnedQuotation.setOrganizationId(69L);
        returnedQuotation.setPanelId(17L);
        returnedQuotation.setStatus("NB");
        returnedQuotation.setClient(testClientData());
        returnedQuotation.setAgent(testAgencyDto());
        returnedQuotation.setOrganization(testOrganizationDto());
        returnedQuotation.setCurrentStatus("D");
        returnedQuotation.setPaymentRef("PAYMENT_REF");
        returnedQuotation.setQuotationNo("QUOTATION_NO");

        return returnedQuotation;

    }

    private List<Quotation> testReturnedQuotationList() {

        List<QuotationProduct> quotationProductList = new ArrayList<>();
        List<QuotationRisk> quotationRiskList = getQuotationRisks();

        QuotationProduct quotationProduct = new QuotationProduct();
        quotationProduct.setId(1L);
        quotationProduct.setQuotationId(1L);
        quotationProduct.setQuotationRisks(quotationRiskList);
        quotationProduct.setQuotationProductTaxes(testQuotationProductTaxData());
        quotationProductList.add(quotationProduct);

        QuotationProduct quotationProduct2 = new QuotationProduct();
        quotationProduct2.setId(2L);
        quotationProduct2.setQuotationId(1L);
        quotationProduct2.setQuotationProductTaxes(testQuotationProductTaxData());
        quotationProduct2.setQuotationRisks(quotationRiskList);
        quotationProductList.add(quotationProduct);
        quotationProductList.add(quotationProduct2);


        Quotation returnedQuotation = new Quotation();
        returnedQuotation.setId(1L);
        returnedQuotation.setClientId(600L);
        returnedQuotation.setOrganizationId(69L);
        returnedQuotation.setPanelId(17L);
        returnedQuotation.setStatus("NB");
        returnedQuotation.setClient(testClientData());
        returnedQuotation.setAgent(testAgencyDto());
        returnedQuotation.setOrganization(testOrganizationDto());
        returnedQuotation.setCurrentStatus("D");
        returnedQuotation.setQuotationProducts(quotationProductList);
        returnedQuotation.setQuotationNo("QUOTATION_NO");

        Quotation returnedQuotation2 = new Quotation();
        returnedQuotation2.setId(2L);
        returnedQuotation2.setClientId(60L);
        returnedQuotation2.setOrganizationId(60L);
        returnedQuotation2.setPanelId(7L);
        returnedQuotation2.setStatus("EN");
        returnedQuotation2.setQuotationProducts(quotationProductList);
        returnedQuotation2.setClient(testClientData());
        returnedQuotation2.setAgent(testAgencyDto());
        returnedQuotation2.setOrganization(testOrganizationDto());
        returnedQuotation2.setCurrentStatus("C");
        returnedQuotation2.setQuotationNo("QUOTATION_NO");

        Quotation returnedQuotation3 = new Quotation();
        returnedQuotation2.setId(2L);
        returnedQuotation2.setClientId(610L);
        returnedQuotation2.setOrganizationId(69L);
        returnedQuotation2.setPanelId(70L);
        returnedQuotation2.setStatus("EN");
        returnedQuotation2.setQuotationProducts(quotationProductList);
        returnedQuotation2.setClient(testClientData());
        returnedQuotation2.setAgent(testAgencyDto());
        returnedQuotation2.setOrganization(testOrganizationDto());
        returnedQuotation2.setCurrentStatus("C");
        returnedQuotation2.setQuotationNo("QUOTATION_NO");


        List<Quotation> returnedQuotationList = new ArrayList<>();
        returnedQuotationList.add(returnedQuotation);
        returnedQuotationList.add(returnedQuotation2);
        returnedQuotationList.add(returnedQuotation3);

        return returnedQuotationList;
    }

    @NotNull
    private static List<QuotationRisk> getQuotationRisks() {
        List<QuotationRisk> quotationRiskList = new ArrayList<>();

        QuotationRisk quotationRisk = new QuotationRisk();
        quotationRisk.setId(1L);
        quotationRisk.setQuotationProductId(1L);
        quotationRisk.setRiskId("AGN 001N");
        quotationRisk.setCoverTypeId(1L);


        QuotationRisk quotationRisk2 = new QuotationRisk();
        quotationRisk2.setId(2L);
        quotationRisk2.setQuotationProductId(1L);
        quotationRisk2.setRiskId("AGN 002N");
        quotationRisk2.setCoverTypeId(3L);

        QuotationRisk quotationRisk3 = new QuotationRisk();
        quotationRisk3.setId(2L);
        quotationRisk3.setQuotationProductId(12L);
        quotationRisk3.setRiskId("AGN 0021N");
        quotationRisk3.setCoverTypeId(13L);


        quotationRiskList.add(quotationRisk);
        quotationRiskList.add(quotationRisk2);
        quotationRiskList.add(quotationRisk3);
        return quotationRiskList;
    }


}
