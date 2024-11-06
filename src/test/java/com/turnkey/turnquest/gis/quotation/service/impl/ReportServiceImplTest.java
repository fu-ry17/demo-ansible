package com.turnkey.turnquest.gis.quotation.service.impl;

import com.turnkey.turnquest.gis.quotation.client.DocsService.DocsServiceClient;
import com.turnkey.turnquest.gis.quotation.client.billing.ReceiptClient;
import com.turnkey.turnquest.gis.quotation.client.crm.ClientDataClient;
import com.turnkey.turnquest.gis.quotation.client.crm.OrganizationClient;
import com.turnkey.turnquest.gis.quotation.client.gis.CoverTypeClient;
import com.turnkey.turnquest.gis.quotation.client.gis.ProductClient;
import com.turnkey.turnquest.gis.quotation.client.gis.ProductDocumentClient;
import com.turnkey.turnquest.gis.quotation.client.gis.SubClassCoverTypeClient;
import com.turnkey.turnquest.gis.quotation.client.notification.NotificationClient;
import com.turnkey.turnquest.gis.quotation.client.underwriting.PolicyClientMock;
import com.turnkey.turnquest.gis.quotation.dto.Reports.BooleanResult;
import com.turnkey.turnquest.gis.quotation.dto.Reports.DebitReportDto;
import com.turnkey.turnquest.gis.quotation.dto.Reports.QuotationReportDto;
import com.turnkey.turnquest.gis.quotation.dto.Reports.Quote;
import com.turnkey.turnquest.gis.quotation.dto.ScheduleDetailsDto;
import com.turnkey.turnquest.gis.quotation.dto.crm.*;
import com.turnkey.turnquest.gis.quotation.dto.document.S3MetaData;
import com.turnkey.turnquest.gis.quotation.dto.document.S3Object;
import com.turnkey.turnquest.gis.quotation.dto.gis.BinderDto;
import com.turnkey.turnquest.gis.quotation.dto.gis.CoverTypeDto;
import com.turnkey.turnquest.gis.quotation.dto.gis.ProductDto;
import com.turnkey.turnquest.gis.quotation.dto.gis.TaxRateDto;
import com.turnkey.turnquest.gis.quotation.dto.notification.NotificationFeedBack;
import com.turnkey.turnquest.gis.quotation.dto.notification.NotificationRecipientDto;
import com.turnkey.turnquest.gis.quotation.dto.notification.SendNotificationDto;
import com.turnkey.turnquest.gis.quotation.dto.quotation.PremiumCardDto;
import com.turnkey.turnquest.gis.quotation.dto.quotation.ResendReportRequestDto;
import com.turnkey.turnquest.gis.quotation.dto.quotation.ResendReportResponseDto;
import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyDto;
import com.turnkey.turnquest.gis.quotation.enums.ClientTypes;
import com.turnkey.turnquest.gis.quotation.enums.QuotationReportType;
import com.turnkey.turnquest.gis.quotation.enums.TaxRateInstallmentType;
import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import com.turnkey.turnquest.gis.quotation.model.*;
import com.turnkey.turnquest.gis.quotation.projections.QuotationProjection;
import com.turnkey.turnquest.gis.quotation.repository.QuotationReportsRepository;
import com.turnkey.turnquest.gis.quotation.repository.QuotationRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ReportServiceImplTest {

    @Mock
    private QuotationRepository quotationRepository;

    @Mock
    private OrganizationClient organizationClient;

    @Mock
    private ReceiptClient receiptClient;

    @Mock
    private ProductDocumentClient productDocumentClient;


    @Mock
    private QuotationReportsRepository quotationReportsRepository;

    @Mock
    private NotificationClient notificationClient;

    @Mock
    private TemplateContent templateContent;

    @Mock
    private Converter converter;


    @Mock
    private SubClassCoverTypeClient subClassCoverTypeClient;

    @Mock
    private ProductClient productClient;

    @Mock
    private CoverTypeClient coverTypeClient;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    ClientDataClient clientDataClient;

    @Mock
    TemplateEngine templateEngine;

    @Mock
    DocsServiceClient docsServiceClient;

    @Mock
    PolicyClientMock policyClient;

    @InjectMocks
    private ReportServiceImpl reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateQuoteSummaryReportIfReportIsAvailable() {
        QuotationReportDto quotationReportDto = getQuotationReportDto();
        Quotation quotation = testQuotation();
        ProductDto productDto = getProductDto();
        QuotationReports quotationReports = new QuotationReports();
        List<QuotationReports> quotationReportsList = new ArrayList<>();
        quotationReportsList.add(quotationReports);

        Quote quote = new Quote();
        quote.setQuotationNo("Q/TEST/001/2023");

        ProjectionFactory pf = new SpelAwareProxyProjectionFactory();
        QuotationProjection qp = pf.createProjection(QuotationProjection.class, quotation);

        when(quotationRepository.getQuoteById(anyLong(), any())).thenReturn(qp);
        when(quotationRepository.findById(anyLong())).thenReturn(Optional.of(quotation));
        when(quotationReportsRepository.findByQuotationIdAndFileCategory(
                anyLong(), any(QuotationReportType.class))).thenReturn(quotationReportsList);
        when(templateContent.getTemplateContent(any(Quote.class))).thenReturn("Test Template");
        when(converter.convert(anyString())).thenReturn(new ByteArrayOutputStream());
        when(productClient.findById(anyLong())).thenReturn(productDto);
        when(coverTypeClient.findById(anyLong())).thenReturn(new CoverTypeDto());

        byte[] result = reportService.generateQuoteSummaryReport(quotationReportDto);

        assertNotNull(result);
    }

    @Test
    void generateQuoteSummaryReportIfReportIsUnAvailable() {
        QuotationReportDto quotationReportDto = getQuotationReportDto();
        ClientDto clientDto = testClientData();
        Quotation quotation = testQuotation();
        ProductDto productDto = getProductDto();

        Quote quote = mock(Quote.class); // Mocking the Quote class

        QuotationProjection quotationProjection = mock(QuotationProjection.class); // Mocking the interface
        when(quotationRepository.getQuoteById(anyLong(), eq(QuotationProjection.class))).thenReturn(quotationProjection);
        when(modelMapper.map(any(QuotationProjection.class), eq(Quote.class))).thenReturn(quote);
        when(quote.getQuotationNo()).thenReturn("Q/TEST/001/2023");

        when(modelMapper.getConfiguration()).thenReturn(mock(Configuration.class));
        when(quotationRepository.findById(anyLong())).thenReturn(Optional.of(quotation));
        when(quotationReportsRepository.findByQuotationIdAndFileCategory(
                anyLong(), any(QuotationReportType.class))).thenReturn(null);
        when(templateContent.getTemplateContent(any(Quote.class))).thenReturn("Test Template");
        when(converter.convert(anyString())).thenReturn(new ByteArrayOutputStream());
        when(productClient.findById(anyLong())).thenReturn(productDto);
        when(coverTypeClient.findById(anyLong())).thenReturn(new CoverTypeDto());
        when(clientDataClient.findById(anyLong())).thenReturn(clientDto);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("template");
        when(docsServiceClient.saveAttachment(any(), any(), anyString(), anyLong(), anyString()))
                .thenReturn(getListOfS3Object());
        when(policyClient.findPolicyByBatchNumber(any())).thenReturn(getPolicyDto());

        byte[] result = reportService.generateQuoteSummaryReport(quotationReportDto);

        assertNotNull(result);
    }

    @Test
    void generateValuationReportDoesNotRequireValuation() {
        QuotationReportDto quotationReportDto = getQuotationReportDto();
        Quotation quotation = testQuotation();

        ProjectionFactory pf = new SpelAwareProxyProjectionFactory();
        QuotationProjection qp = pf.createProjection(QuotationProjection.class, quotation);

        when(quotationRepository.findById(any(Long.class))).thenReturn(Optional.of(quotation));
        when(quotationRepository.getQuoteById(anyLong(), any())).thenReturn(qp);
        when(quotationReportsRepository.findByQuotationIdAndFileCategory(
                any(Long.class), any(QuotationReportType.class))).thenReturn(new ArrayList<>());
        when(subClassCoverTypeClient.requiresValuation(anyLong(), anyLong())).thenReturn(YesNo.N);

        List<byte[]> result = reportService.generateValuationReport(quotationReportDto);

        assertNotNull(result);
    }

    @Test
    void generateValuationReportRequireValuation() {
        QuotationReportDto quotationReportDto = getQuotationReportDto();
        ClientDto clientDto = testClientData();
        Quotation quotation = testQuotation();
        Quote quote = new Quote();
        quote.setQuotationNo("Q/TEST/001/2023");

        QuotationProjection quotationProjection = mock(QuotationProjection.class); // Mocking the interface
        QuotationProjection.QuotationProductView quotationProductMock = mock(QuotationProjection.QuotationProductView.class);
        QuotationProjection.QuotationRiskView quotationRiskMock = mock(QuotationProjection.QuotationRiskView.class);
        when(quotationProjection.getQuotationProducts()).thenReturn(Collections.singletonList(quotationProductMock));
        when(quotationProductMock.getQuotationRisks()).thenReturn(Collections.singletonList(quotationRiskMock));
        when(quotationRiskMock.getCoverTypeId()).thenReturn(1L);
        when(quotationRiskMock.getProductSubClassId()).thenReturn(1L);
        when(quotationRepository.getQuoteById(anyLong(), eq(QuotationProjection.class))).thenReturn(quotationProjection);
        when(modelMapper.map(eq(quotationProjection), any())).thenReturn(quote);

        when(modelMapper.getConfiguration()).thenReturn(mock(Configuration.class));

        when(quotationRepository.findById(any(Long.class))).thenReturn(Optional.of(quotation));
//        when(quotationRepository.getQuoteById(anyLong(), any())).thenReturn(qp);
        when(quotationReportsRepository.findByQuotationIdAndFileCategory(
                any(Long.class), any(QuotationReportType.class))).thenReturn(new ArrayList<>());
        when(subClassCoverTypeClient.requiresValuation(any(), any())).thenReturn(YesNo.Y);
        when(clientDataClient.findById(anyLong())).thenReturn(clientDto);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("template");

        List<byte[]> result = reportService.generateValuationReport(quotationReportDto);

        assertNotNull(result);
    }

    @Test
    void generateRenewalNoticeForNonRenewalTransactions() {
        when(quotationRepository.findById(any(Long.class))).thenReturn(Optional.of(new Quotation()));

        byte[] result = reportService.generateRenewalNotice(1L);

        assertNotNull(result);
    }

    @Test
    void generateRenewalNoticeForRenewalTransactions() {
        ClientDto clientDto = testClientData();
        Quotation quotation = testQuotation();
        quotation.setStatus("RN");

        when(quotationRepository.findById(any(Long.class))).thenReturn(Optional.of(new Quotation()));
        ProjectionFactory pf = new SpelAwareProxyProjectionFactory();
        QuotationProjection qp = pf.createProjection(QuotationProjection.class, quotation);

        when(modelMapper.getConfiguration()).thenReturn(mock(Configuration.class));

        when(quotationRepository.findById(any(Long.class))).thenReturn(Optional.of(quotation));
        when(quotationRepository.getQuoteById(anyLong(), any())).thenReturn(qp);
        when(quotationReportsRepository.findByQuotationIdAndFileCategory(
                any(Long.class), any(QuotationReportType.class))).thenReturn(new ArrayList<>());
        when(subClassCoverTypeClient.requiresValuation(anyLong(), anyLong())).thenReturn(YesNo.N);
        when(clientDataClient.findById(anyLong())).thenReturn(clientDto);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("template");
        when(docsServiceClient.saveAttachment(any(), any(), anyString(), anyLong(), anyString()))
                .thenReturn(getListOfS3Object());
        byte[] result = reportService.generateRenewalNotice(1L);

        assertNotNull(result);
    }


    @Test
    void shouldReturnTrueWhenReportExists() {
        Long quotationId = 1L;
        QuotationReportType reportType = QuotationReportType.RECEIPT;

        QuotationReports report = new QuotationReports();
        report.setQuotationId(quotationId);
        report.setFileCategory(reportType);

        when(quotationReportsRepository.findByQuotationIdAndFileCategory(quotationId, reportType))
                .thenReturn(Collections.singletonList(report));

        BooleanResult result = reportService.reportExists(quotationId, reportType);

        assertTrue(result.isExists());
    }

    @Test
    void shouldSaveQuotationReportWhenReportIsValid() {
        // Given
        QuotationReports quotationReports = new QuotationReports();
        quotationReports.setQuotationId(1L);
        quotationReports.setFileCategory(QuotationReportType.RECEIPT);

        when(quotationReportsRepository.save(any(QuotationReports.class))).thenReturn(quotationReports);

        // When
        QuotationReports result = reportService.saveQuotationReport(quotationReports);

        // Then
        assertNotNull(result);
        assertEquals(quotationReports.getQuotationId(), result.getQuotationId());
        assertEquals(quotationReports.getFileCategory(), result.getFileCategory());
        verify(quotationReportsRepository, times(1)).save(quotationReports);
    }

    @Test
    void shouldReturnPolicyReportsWhenPolicyBatchNoExists() {
        Long policyBatchNo = 1L;

        QuotationReports report = new QuotationReports();
        report.setPolicyId(policyBatchNo);
        report.setFileCategory(QuotationReportType.RECEIPT);

        when(quotationReportsRepository.findByPolicyIdAndFileCategoryOrderByCreatedDateDesc(policyBatchNo, QuotationReportType.RECEIPT))
                .thenReturn(Collections.singletonList(report));

        List<QuotationReports> result = reportService.getPolicyReports(policyBatchNo);

        assertFalse(result.isEmpty());
        assertEquals(policyBatchNo, result.get(0).getPolicyId());
    }


    @Test
    void shouldNotSendQuotationDocumentsWhenReportsAreNotAvailable() {
        // Given
        when(quotationReportsRepository.findByQuotationId(anyLong())).thenReturn(Collections.emptyList());

        // When
        boolean result = reportService.sendQuotationDocuments(1L);

        // Then
        assertFalse(result);
        verify(notificationClient, never()).initiate(any());
    }

    @Test
    void shouldNotSendQuotationDocumentsWhenQuotationIsNotPresent() {
        // Given
        QuotationReports report = new QuotationReports();
        report.setMailable(true);
        report.setSent(false);
        report.setEmailRetryCount(0);
        report.setMaxRetryCount(3);
        List<QuotationReports> reports = List.of(report);
        when(quotationReportsRepository.findByQuotationId(anyLong())).thenReturn(reports);

        when(quotationRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When
        boolean result = reportService.sendQuotationDocuments(1L);

        // Then
        assertFalse(result);
        verify(notificationClient, never()).initiate(any());
    }

    @Test
    void shouldResendReportsToClientSuccessfully() {
        // Given
        ResendReportRequestDto request = new ResendReportRequestDto();
        request.setClientEmail("test@test.com");
        request.setClientName("Test User");
        request.setInsurerOrgId(1L);
        request.setQuotationId(1L);
        request.setRemarks("Test Remarks");

        QuotationReports report = new QuotationReports();
        report.setFileUrl("http://test.com/report.pdf");
        List<QuotationReports> reports = Collections.singletonList(report);

        OrganizationDto organization = new OrganizationDto();
        EntitiesDto entities = new EntitiesDto();
        entities.setOrganizationName("Test Organization");
        entities.setPhoneNumber("1234567890");
        entities.setEmailAddress("test@test.com");
        organization.setEntities(entities);

        when(quotationReportsRepository.findByQuotationId(anyLong())).thenReturn(reports);
        when(organizationClient.findById(anyLong())).thenReturn(organization);

        // When
        ResendReportResponseDto response = reportService.resendReportsToClient(request);

        // Then
        assertEquals("Reports resent successfully to test@test.com", response.getMessage());
        verify(notificationClient).initiate(any());
    }

    @Test
    void shouldHandleErrorWhenResendingReportsToClient() {
        // Given
        ResendReportRequestDto request = new ResendReportRequestDto();
        request.setClientEmail("test@test.com");
        request.setClientName("Test User");
        request.setInsurerOrgId(1L);
        request.setQuotationId(1L);
        request.setRemarks("Test Remarks");

        QuotationReports report = new QuotationReports();
        report.setFileUrl("http://test.com/report.pdf");
        List<QuotationReports> reports = Collections.singletonList(report);

        OrganizationDto organization = new OrganizationDto();
        EntitiesDto entities = new EntitiesDto();
        entities.setOrganizationName("Test Organization");
        entities.setPhoneNumber("1234567890");
        entities.setEmailAddress("test@test.com");
        organization.setEntities(entities);

        when(quotationReportsRepository.findByQuotationId(anyLong())).thenReturn(reports);
        when(organizationClient.findById(anyLong())).thenReturn(organization);
        doThrow(new RuntimeException("Test Exception")).when(notificationClient).initiate(any());

        // When
        ResendReportResponseDto response = reportService.resendReportsToClient(request);

        // Then
        assertEquals("Failed. Check your details and retry", response.getMessage());
    }

    @Test
    void shouldReturnSuccessNotificationFeedbackWhenNotificationIsSent() {
        // Given
        SendNotificationDto sendNotificationDto = new SendNotificationDto();
        when(notificationClient.initiate(any(SendNotificationDto.class))).thenReturn(null);

        // When
        NotificationFeedBack result = reportService.sendNotification(sendNotificationDto);

        // Then
        assertTrue(result.isSuccess());
        assertNull(result.getCause());
    }

    @Test
    void shouldReturnFailureNotificationFeedbackWhenNotificationFails() {
        // Given
        SendNotificationDto sendNotificationDto = new SendNotificationDto();
        Exception exception = new RuntimeException("Test Exception");
        doThrow(exception).when(notificationClient).initiate(any(SendNotificationDto.class));

        // When
        NotificationFeedBack result = reportService.sendNotification(sendNotificationDto);

        // Then
        assertFalse(result.isSuccess());
        assertEquals(exception, result.getCause());
    }

    @Test
    void shouldReturnEmptyListWhenPolicyBatchNoIsNull() {
        List<QuotationReports> reports = reportService.getQuotationReport(null);
        assertTrue(reports.isEmpty());
    }

    @Test
    void shouldReturnReportsWhenQuotationIdIsFound() throws Exception {
        Long policyBatchNo = 1L;
        Long quotationId = 2L;

        PolicyDto policyDto = new PolicyDto();
        policyDto.setQuotationId(quotationId);
        when(policyClient.findPolicyByBatchNumber(policyBatchNo)).thenReturn(policyDto);

        QuotationReports report = new QuotationReports();
        when(quotationReportsRepository.findByQuotationIdAndFileCategoryOrderByCreatedDateDesc(quotationId, QuotationReportType.POLICY_DOCUMENT))
                .thenReturn(Collections.singletonList(report));

        List<QuotationReports> reports = reportService.getQuotationReport(policyBatchNo);

        assertFalse(reports.isEmpty());
        assertEquals(report, reports.getFirst());
    }


    @Test
    void shouldReturnReportsWhenQuotationIdIsNotFoundButPolicyBatchNoIsFound() throws Exception {
        Long policyBatchNo = 1L;

        PolicyDto policyDto = new PolicyDto();
        policyDto.setQuotationId(-1L);
        when(policyClient.findPolicyByBatchNumber(policyBatchNo)).thenReturn(policyDto);

        QuotationReports report = new QuotationReports();
        when(quotationReportsRepository.findByPolicyIdAndFileCategoryOrderByCreatedDateDesc(policyBatchNo, QuotationReportType.POLICY_DOCUMENT))
                .thenReturn(Collections.singletonList(report));

        List<QuotationReports> reports = reportService.getQuotationReport(policyBatchNo);

        assertFalse(reports.isEmpty());
        assertEquals(report, reports.getFirst());
    }

    @Test
    void shouldReturnEmptyListWhenNeitherQuotationIdNorPolicyBatchNoIsFound() throws Exception {
        Long policyBatchNo = 1L;

        PolicyDto policyDto = new PolicyDto();
        policyDto.setQuotationId(-1L);
        when(policyClient.findPolicyByBatchNumber(policyBatchNo)).thenReturn(policyDto);

        when(quotationReportsRepository.findByPolicyIdAndFileCategoryOrderByCreatedDateDesc(policyBatchNo, QuotationReportType.POLICY_DOCUMENT))
                .thenReturn(Collections.emptyList());

        List<QuotationReports> reports = reportService.getQuotationReport(policyBatchNo);

        assertTrue(reports.isEmpty());
    }

    @Test
    void shouldGenerateDebitReportSuccessfully() {
        // Given
        DebitReportDto debitReportDto = new DebitReportDto();
        debitReportDto.setIPayRef("ipayRef");
        debitReportDto.setBatchNo(1L);
        debitReportDto.setQuotationId(1L);

        doNothing().when(receiptClient).generateDebitReport(anyString(), anyLong(), anyLong());

        // When
        reportService.generateDebitReport(debitReportDto);

        // Then
        verify(receiptClient, times(1)).generateDebitReport(anyString(), anyLong(), anyLong());
    }

    @Test
    void shouldHandleExceptionWhenGenerateDebitReportFails() {
        // Given
        DebitReportDto debitReportDto = new DebitReportDto();
        debitReportDto.setIPayRef("ipayRef");
        debitReportDto.setBatchNo(1L);
        debitReportDto.setQuotationId(1L);

        doThrow(new RuntimeException("Test Exception")).when(receiptClient).generateDebitReport(anyString(), anyLong(), anyLong());

        // When
        reportService.generateDebitReport(debitReportDto);

        // Then
        verify(receiptClient, times(1)).generateDebitReport(anyString(), anyLong(), anyLong());
    }

    @Test
    void shouldSendToInsurerWhenInsurerEmailIsNotEmptyAndQuotationExists() {
        // Given
        String insurerEmail = "insurer@test.com";
        Long quotationId = 1L;
        SendNotificationDto sendNotificationDto = new SendNotificationDto();
        NotificationRecipientDto recipientDto = new NotificationRecipientDto();
        sendNotificationDto.setNotificationRecipient(recipientDto);

        Quotation quotation = new Quotation();
        QuotationProduct quotationProduct = new QuotationProduct();
        QuotationRisk quotationRisk = new QuotationRisk();
        QuoteDocument quoteDocument = new QuoteDocument();
        quoteDocument.setDocument("document");
        quotationRisk.setQuoteDocument(Collections.singletonList(quoteDocument));
        quotationProduct.setQuotationRisks(Collections.singletonList(quotationRisk));
        quotation.setQuotationProducts(Collections.singletonList(quotationProduct));

        when(quotationRepository.findById(quotationId)).thenReturn(Optional.of(quotation));

        // When
        reportService.sendToInsurer(insurerEmail, quotationId, sendNotificationDto);

        // Then
        verify(quotationRepository, times(1)).findById(quotationId);
        verify(notificationClient, times(1)).initiate(any(SendNotificationDto.class));
    }

    @Test
    void shouldNotSendToInsurerWhenInsurerEmailIsEmpty() {
        // Given
        String insurerEmail = "";
        Long quotationId = 1L;
        SendNotificationDto sendNotificationDto = new SendNotificationDto();

        // When
        reportService.sendToInsurer(insurerEmail, quotationId, sendNotificationDto);

        // Then
        verify(quotationRepository, times(0)).findById(quotationId);
        verify(notificationClient, times(0)).initiate(any(SendNotificationDto.class));
    }

    @Test
    void shouldNotSendToInsurerWhenQuotationDoesNotExist() {
        // Given
        String insurerEmail = "insurer@test.com";
        Long quotationId = 1L;
        SendNotificationDto sendNotificationDto = new SendNotificationDto();

        when(quotationRepository.findById(quotationId)).thenReturn(Optional.empty());

        // When
        reportService.sendToInsurer(insurerEmail, quotationId, sendNotificationDto);

        // Then
        verify(quotationRepository, times(1)).findById(quotationId);
        verify(notificationClient, times(0)).initiate(any(SendNotificationDto.class));
    }

    private QuotationReportDto getQuotationReportDto() {
        QuotationReportDto quotationReportDto = new QuotationReportDto();
        quotationReportDto.setQuotationId(1L);
        quotationReportDto.setPolicyBatchNo(1L);
        return quotationReportDto;
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
        quotationRisk.setProductSubClassId(1L);
        quotationRisk.setRiskId("AGN 001N");
        quotationRisk.setCoverTypeId(1L);


        QuotationRisk quotationRisk2 = new QuotationRisk();
        quotationRisk2.setId(2L);
        quotationRisk2.setQuotationProductId(1L);
        quotationRisk.setProductSubClassId(1L);
        quotationRisk2.setRiskId("AGN 002N");
        quotationRisk2.setCoverTypeId(3L);

        QuotationRisk quotationRisk3 = new QuotationRisk();
        quotationRisk3.setId(2L);
        quotationRisk3.setQuotationProductId(12L);
        quotationRisk.setProductSubClassId(1L);
        quotationRisk3.setRiskId("AGN 0021N");
        quotationRisk3.setCoverTypeId(13L);


        quotationRiskList.add(quotationRisk);
        quotationRiskList.add(quotationRisk2);
        quotationRiskList.add(quotationRisk3);
        return quotationRiskList;
    }

    private ProductDto getProductDto() {
        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setCode("Test Product");
        productDto.setAccomodation("Test Accomodation");
        productDto.setBinder(getBinder());

        return productDto;
    }

    private BinderDto getBinder() {
        BinderDto binder = new BinderDto();
        binder.setId(1L);
        binder.setName("TP");
        binder.setRemarks("Test Product");
        binder.setCommissionType("Test Accomodation");
        binder.setType("Type");

        return binder;
    }

    private S3Object getS3Object() {
        S3Object s3Object = new S3Object();
        s3Object.setKey("key");
        s3Object.setUrl("url");
        s3Object.setMetaData(getS3MetaData());
        return s3Object;
    }

    private S3MetaData getS3MetaData() {
        S3MetaData s3MetaData = new S3MetaData();
        s3MetaData.setMimeType("key");
        s3MetaData.setSize(1024L);
        s3MetaData.setCreatedAt(83948348L);

        return s3MetaData;
    }

    private List<S3Object> getListOfS3Object() {
        List<S3Object> s3ObjectList = new ArrayList<>();
        s3ObjectList.add(getS3Object());
        return s3ObjectList;
    }

    private PolicyDto getPolicyDto() {
        PolicyDto policyDto = new PolicyDto();
        policyDto.setCurrentStatus("Active");
        policyDto.setBranchCode("Branch Code");

        return policyDto;
    }
}
