//package com.turnkey.turnquest.gis.quotation.service;
//
//
//import com.turnkey.turnquest.gis.quotation.client.DocsService.DocsServiceClient;
//import com.turnkey.turnquest.gis.quotation.client.underwriting.impl.PolicyClient;
//import com.turnkey.turnquest.gis.quotation.client.billing.ReceiptClient;
//import com.turnkey.turnquest.gis.quotation.client.crm.*;
//import com.turnkey.turnquest.gis.quotation.client.gis.CoverTypeClient;
//import com.turnkey.turnquest.gis.quotation.client.gis.GisTransactionClient;
//import com.turnkey.turnquest.gis.quotation.client.gis.ProductClient;
//import com.turnkey.turnquest.gis.quotation.client.gis.ProductDocumentClient;
//import com.turnkey.turnquest.gis.quotation.client.notification.NotificationClient;
//import com.turnkey.turnquest.gis.quotation.dto.crm.*;
//import com.turnkey.turnquest.gis.quotation.dto.document.AttachmentDto;
//import com.turnkey.turnquest.gis.quotation.dto.gis.CoverTypeDto;
//import com.turnkey.turnquest.gis.quotation.dto.gis.ProductDocumentDto;
//import com.turnkey.turnquest.gis.quotation.dto.gis.ProductDto;
//import com.turnkey.turnquest.gis.quotation.dto.gis.TransactionTypeDto;
//import com.turnkey.turnquest.gis.quotation.dto.notification.SendNotificationDto;
//import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyDto;
//import com.turnkey.turnquest.gis.quotation.event.producer.OnFinishReportGeneration;
//import com.turnkey.turnquest.gis.quotation.model.*;
//import com.turnkey.turnquest.gis.quotation.projections.QuotationProjection;
//import com.turnkey.turnquest.gis.quotation.repository.QuotationReportsRepository;
//import com.turnkey.turnquest.gis.quotation.repository.QuotationRepository;
//import com.turnkey.turnquest.gis.quotation.service.impl.QuotationReportServiceImpl;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.*;
//import org.modelmapper.ModelMapper;
//import org.thymeleaf.spring5.SpringTemplateEngine;
//import org.thymeleaf.templatemode.TemplateMode;
//import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
//
//import java.math.BigDecimal;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//public class QuotationReportServiceTest {
//
//    @Mock
//    private  QuotationRepository quotationRepository;
//
//    @Mock
//    private  ProductClient productClient;
//
//    @Mock
//    private  GisTransactionClient gisTransactionClient;
//
//    @Mock
//    private  CoverTypeClient coverTypeClient;
//
//    @Mock
//    private  QuotationReportsRepository quotationReportsRepository;
//
//    @Mock
//    private  DocsServiceClient docsServiceClient;
//
//    @Mock
//    private  OrganizationClient organizationClient;
//
//    @Mock
//    private  EntitiesClient entitiesClient;
//
//    @Mock
//    private  NotificationClient notificationClient;
//
//    @Mock
//    private  ClientDataClient clientDataClient;
//
//    @Mock
//    private  AgencyClient agencyClient;
//
//    @Mock
//    private  SPNotesClient spNotesClient;
//
//    @Mock
//    private ProductDocumentClient productDocumentClient;
//
//    @Mock
//    private CurrencyClient currencyClient;
//
//    @Mock
//    private PolicyClient policyClient;
//
//    @Mock
//    private ReceiptClient receiptClient;
//
//    @Mock
//    private OnFinishReportGeneration onFinishReportGeneration;
//
//
//    private QuotationReportServiceImpl quotationReportService;
//
//    @Before
//    public void setUp(){
//        MockitoAnnotations.initMocks(this);
//
//        //The template engine used to generate the pdf
//        ClassLoaderTemplateResolver classLoaderTemplateResolver = new ClassLoaderTemplateResolver();
//        classLoaderTemplateResolver.setPrefix("/templates/");
//        classLoaderTemplateResolver.setSuffix(".html");
//        classLoaderTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
//        classLoaderTemplateResolver.setTemplateMode(TemplateMode.HTML);
//
//        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//        templateEngine.setTemplateResolver(classLoaderTemplateResolver);
//
//        quotationReportService = new QuotationReportServiceImpl(templateEngine,quotationRepository,new ModelMapper(),productClient,gisTransactionClient,coverTypeClient,quotationReportsRepository,
//                docsServiceClient,organizationClient,entitiesClient,notificationClient,clientDataClient,agencyClient,spNotesClient,productDocumentClient,currencyClient,policyClient,receiptClient,
//                onFinishReportGeneration);
//
//        //To prevent null pointer. Can be any port number not occupied
//        quotationReportService.servicePort = "8080";
//    }
//
//
//    @Test
//    public void generateQuoteSummaryReportTest(){
//
//        Mockito.when(quotationRepository.findByQuotationNo(ArgumentMatchers.anyString(),ArgumentMatchers.any())).thenReturn(new QuoteProjection());
//
//        Mockito.when(quotationRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(generateQuotation()));
//
//        Mockito.when(quotationReportsRepository.findByQuotationIdAndFileCategory(ArgumentMatchers.anyLong(),ArgumentMatchers.anyString()))
//                .thenReturn(null);
//
//        Mockito.when(clientDataClient.findById(ArgumentMatchers.anyLong())).thenReturn(generateClientDto());
//
//        Mockito.when(organizationClient.findById(ArgumentMatchers.anyLong())).thenReturn(generateOrganization());
//
//        Mockito.when(entitiesClient.find(ArgumentMatchers.anyLong())).thenReturn(generateEntity());
//
//        Mockito.when(agencyClient.findByPanelIdAndOrganizationId(ArgumentMatchers.anyLong(),ArgumentMatchers.anyLong())).thenReturn(generateAgencyDto());
//
//        Mockito.when(productClient.findById(ArgumentMatchers.anyLong())).thenReturn(generateProductDto());
//
//        Mockito.when(gisTransactionClient.findByCode(ArgumentMatchers.anyString())).thenReturn(generateTransactionTypeDto());
//
//        Mockito.when(coverTypeClient.findById(ArgumentMatchers.anyLong())).thenReturn(generateCoverTypeDto());
//
//        Mockito.when(docsServiceClient.saveAttachment(ArgumentMatchers.any(AttachmentDto.class))).thenReturn(generateAttachmentDto());
//
//        Mockito.when(quotationReportsRepository.save(ArgumentMatchers.any(QuotationReports.class))).thenReturn(null);
//
//        Mockito.when(policyClient.getPolicyForReportGeneration(ArgumentMatchers.anyString())).thenReturn(generatePolicyDto());
//
//
//        //True if quotation is found
//        assert(quotationReportService.generateQuoteSummaryReport(938L).length > 0);
//
//        Mockito.when(quotationRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());
//
//        //False if quotation is not found
//        assert(quotationReportService.generateQuoteSummaryReport(667L).length == 0);
//
//    }
//
//
//    @Test
//    public void generateQuoteValuationReportTest(){
//
//        Mockito.when(quotationRepository.findByQuotationNo(ArgumentMatchers.anyString(),ArgumentMatchers.any())).thenReturn(new QuoteProjection());
//
//        Mockito.when(quotationReportsRepository.findByQuotationIdAndFileCategory(ArgumentMatchers.anyLong(),ArgumentMatchers.anyString()))
//                .thenReturn(null);
//
//        Mockito.when(quotationRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(generateQuotation()));
//
//        Mockito.when(clientDataClient.findById(ArgumentMatchers.anyLong())).thenReturn(generateClientDto());
//
//        Mockito.when(agencyClient.findByPanelIdAndOrganizationId(ArgumentMatchers.anyLong(),ArgumentMatchers.anyLong())).thenReturn(generateAgencyDto());
//
//        Mockito.when(organizationClient.findById(ArgumentMatchers.anyLong())).thenReturn(generateOrganization());
//
//        Mockito.when(entitiesClient.find(ArgumentMatchers.anyLong())).thenReturn(generateEntity());
//
//        List<ServiceProviderNotesDto> notes = new ArrayList<>();
//        notes.add(generateNotes());
//        Mockito.when(spNotesClient.getByServiceProvider(ArgumentMatchers.anyLong())).thenReturn(notes);
//
//        Mockito.when(policyClient.getPolicyForReportGeneration(ArgumentMatchers.anyString())).thenReturn(generatePolicyDto());
//
//        Mockito.when(docsServiceClient.saveAttachment(ArgumentMatchers.any(AttachmentDto.class))).thenReturn(generateAttachmentDto());
//
//        Mockito.when(quotationReportsRepository.save(ArgumentMatchers.any(QuotationReports.class))).thenReturn(null);
//
//        //If Quotation is present
//        assert(quotationReportService.generateValuationReport(9383L).length > 0);
//
//        Mockito.when(quotationRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());
//
//        //If quotation does not exist
//        assert(quotationReportService.generateValuationReport(9383L).length == 0);
//
//    }
//
//    @Test
//    public void sendQuoteDocumentsTest(){
//
//        List<QuotationReports> reports = new ArrayList<>();
//        reports.add(generateQuotationReport());
//        Mockito.when(quotationReportsRepository.findByQuotationId(ArgumentMatchers.anyLong())).thenReturn(reports);
//
//        Mockito.when(quotationRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(generateQuotation()));
//
//        Mockito.when(clientDataClient.findById(ArgumentMatchers.anyLong())).thenReturn(generateClientDto());
//
//        Mockito.when(organizationClient.findById(ArgumentMatchers.anyLong())).thenReturn(generateOrganization());
//
//        Mockito.when(agencyClient.findByPanelIdAndOrganizationId(ArgumentMatchers.anyLong(),ArgumentMatchers.anyLong())).thenReturn(generateAgencyDto());
//
//        Mockito.when(notificationClient.initiate(ArgumentMatchers.any(SendNotificationDto.class))).thenReturn(null);
//
//        Mockito.doNothing().when(onFinishReportGeneration).queueReportsForMailing(ArgumentMatchers.anyLong());
//
//        //When quote is present
//        Assert.assertTrue(quotationReportService.sendQuotationDocuments(94L));
//
//        Mockito.when(quotationRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());
//
//        //When quote does not exist
//        Assert.assertFalse(quotationReportService.sendQuotationDocuments(908L));
//
//    }
//
//
//    @Test
//    public void getQuotationReportsTest(){
//        List<QuotationReports> reports = new ArrayList<>();
//        reports.add(generateQuotationReport());
//        reports.add(generateQuotationReport());
//        reports.add(generateQuotationReport());
//
//        Mockito.when(quotationReportsRepository.findByPolicyIdOrderByIdDesc(ArgumentMatchers.anyLong())).thenReturn(reports);
//
//        Assert.assertEquals(3,quotationReportService.getQuotationReports(ArgumentMatchers.anyLong()).size());
//    }
//
//
//    @Test
//    public void saveQuotationReportTest(){
//        QuotationReports quotationReport = generateQuotationReport();
//
//        Mockito.when(quotationReportsRepository.save(ArgumentMatchers.any(QuotationReports.class))).thenReturn(quotationReport);
//
//        Assert.assertEquals(quotationReport,quotationReportService.saveQuotationReport(quotationReport));
//    }
//
//   //<editor-fold defaultstate="collapsed" desc="Test Data">
//   private Quotation generateQuotation(){
//        Quotation quotation = new Quotation();
//
//       quotation.setOrganizationId(4L);
//       quotation.setPanelId(23L);
//       quotation.setClientId(854L);
//       quotation.setQuotationNo("FAKE939QUOTATION3NO");
//       quotation.setPolicyNo("FAKE948POLICY9NO");
//
//       QuotationProduct quotationProduct = new QuotationProduct();
//       quotationProduct.setProductId(445L);
//
//       QuotationProductTax quotationProductTax = new QuotationProductTax();
//       quotationProductTax.setTransactionTypeCode("SD");
//       quotationProductTax.setTaxAmount(BigDecimal.TEN);
//
//       QuotationProductTax quotationProductTax2 = new QuotationProductTax();
//       quotationProductTax2.setTransactionTypeCode("TL");
//       quotationProductTax2.setTaxAmount(BigDecimal.TEN);
//
//       QuotationProductTax quotationProductTax3 = new QuotationProductTax();
//       quotationProductTax3.setTransactionTypeCode("PHF");
//       quotationProductTax3.setTaxAmount(BigDecimal.TEN);
//
//       quotationProduct.getQuotationProductTaxes().add(quotationProductTax);
//       quotationProduct.getQuotationProductTaxes().add(quotationProductTax2);
//       quotationProduct.getQuotationProductTaxes().add(quotationProductTax3);
//
//       QuotationRisk quotationRisk = new QuotationRisk();
//       quotationRisk.setRiskId("KAA 001A");
//       quotationRisk.setCoverTypeId(567L);
//       quotationRisk.setWithEffectToDate(1603266879000L);
//
//       List<QuoteDocument> documents = new ArrayList<>();
//       documents.add(generateQuoteDocument());
//       quotationRisk.setQuoteDocument(documents);
//
//       QuotationValuationInfo quotationValuationInfo = new QuotationValuationInfo();
//       quotationValuationInfo.setValuerOrganizationId(67L);
//
//       quotationRisk.setQuotationValuationInfo(quotationValuationInfo);
//
//       quotationProduct.getQuotationRisks().add(quotationRisk);
//
//       quotation.setQuotationProducts(new ArrayList<>());
//       quotation.getQuotationProducts().add(quotationProduct);
//
//       return quotation;
//   }
//
//   private OrganizationDto generateOrganization(){
//        OrganizationDto organizationDto = new OrganizationDto();
//        organizationDto.setEntityId(4L);
//        organizationDto.setEntities(generateEntity());
//        return organizationDto;
//   }
//
//   private EntitiesDto generateEntity(){
//        EntitiesDto entitiesDto = new EntitiesDto();
//        entitiesDto.setEmailAddress("johndoe@mail.com");
//        entitiesDto.setFirstName("John");
//        entitiesDto.setLastName("Doe");
//        entitiesDto.setOrganizationName("FakeOrganization");
//        entitiesDto.setPhoneNumber("1234567890");
//        return entitiesDto;
//   }
//
//
//   private AgencyDto generateAgencyDto(){
//        AgencyDto agencyDto = new AgencyDto();
//        agencyDto.setInsurerId(9983L);
//        return agencyDto;
//   }
//
//   private ProductDto generateProductDto(){
//        ProductDto productDto = new ProductDto();
//        productDto.setDescription("FAKE3PRODUCT");
//        return productDto;
//   }
//
//   private TransactionTypeDto generateTransactionTypeDto(){
//        TransactionTypeDto transactionTypeDto = new TransactionTypeDto();
//        transactionTypeDto.setDescription("FAKE5DESCRIPTION");
//        return transactionTypeDto;
//   }
//
//   private CoverTypeDto generateCoverTypeDto(){
//        CoverTypeDto coverTypeDto = new CoverTypeDto();
//        coverTypeDto.setDescription("FAKE90DESCRIPTION");
//        return coverTypeDto;
//   }
//
//   private AttachmentDto generateAttachmentDto(){
//        AttachmentDto attachmentDto = new AttachmentDto();
//        attachmentDto.setOrganizationId(5L);
//        attachmentDto.setMimeType("pdf");
//        attachmentDto.setFileName("test123.pdf");
//        attachmentDto.setFileUrl("http://fakeurl/test123.pdf");
//        return attachmentDto;
//   }
//
//   private ClientDto generateClientDto(){
//        ClientDto clientDto = new ClientDto();
//        clientDto.setFirstName("John");
//        clientDto.setLastName("Doe");
//        clientDto.setEmailAddress("johndoe@mail.com");
//
//        return clientDto;
//   }
//
//   private ServiceProviderNotesDto generateNotes(){
//        ServiceProviderNotesDto serviceProviderNotesDto = new ServiceProviderNotesDto();
//        serviceProviderNotesDto.setNotes("TESTING 1 2 3");
//        return serviceProviderNotesDto;
//   }
//
//   private QuotationReports generateQuotationReport(){
//        QuotationReports quotationReports = new QuotationReports();
//        quotationReports.setFileUrl("http://fakeurl1/test.pdf,http://fakeurl1/test2.pdf");
//        quotationReports.setFileCategory("VALUATION");
//        return quotationReports;
//   }
//
//   private ProductDocumentDto generateDocuments(){
//        ProductDocumentDto productDocumentDto = new ProductDocumentDto();
//        productDocumentDto.setFileUrl("http://fakurl3/test44.pdf");
//        return productDocumentDto;
//   }
//
//   private PolicyDto generatePolicyDto(){
//        PolicyDto policyDto = new PolicyDto();
//        policyDto.setBatchNumber(99L);
//        return policyDto;
//   }
//
//   private QuoteDocument generateQuoteDocument(){
//        QuoteDocument quoteDocument = new QuoteDocument();
//        quoteDocument.setDocument("http://fake3document/url");
//        return quoteDocument;
//   }
//
//
//   private class QuoteProjection implements QuotationProjection{
//
//        Quotation quotation = generateQuotation();
//
//       @Override
//       public String getQuotationNo() {
//           return quotation.getQuotationNo();
//       }
//
//       @Override
//       public String getPaymentRef() {
//           return quotation.getPaymentRef();
//       }
//
//       @Override
//       public Long getOrganizationId() {
//           return quotation.getOrganizationId();
//       }
//
//       @Override
//       public Long getCurrencyId() {
//           return quotation.getCurrencyId();
//       }
//
//       @Override
//       public Long getPanelId() {
//           return quotation.getPanelId();
//       }
//
//       @Override
//       public Long getClientId() {
//           return quotation.getPanelId();
//       }
//
//       @Override
//       public String getPolicyNo() {
//           return quotation.getPolicyNo();
//       }
//
//       @Override
//       public Long getPolicyId() {
//           return quotation.getPolicyId();
//       }
//
//       @Override
//       public List<QuotationProductView> getQuotationProducts() {
//           List<QuotationProductView> quoteProductViews = new ArrayList<>();
//           quoteProductViews.add(new QuoteProductView());
//           return quoteProductViews;
//       }
//   }
//
//   private static class QuoteProductView implements QuotationProjection.QuotationProductView{
//
//       @Override
//       public Long getProductId() {
//           return 22L;
//       }
//
//       @Override
//       public BigDecimal getTotalSumInsured() {
//           return new BigDecimal(209999);
//       }
//
//       @Override
//       public BigDecimal getPremiumAmount() {
//           return new BigDecimal(399999);
//       }
//
//       @Override
//       public BigDecimal getFutureAnnualPremium() {
//           return new BigDecimal(790000);
//       }
//
//       @Override
//       public List<QuotationProjection.QuotationRiskView> getQuotationRisks() {
//           List<QuotationProjection.QuotationRiskView> riskViews = new ArrayList<>();
//           riskViews.add(new QuoteRiskView());
//           return riskViews;
//       }
//
//       @Override
//       public List<QuotationProjection.QuotationProductTaxView> getQuotationProductTaxes() {
//           List<QuotationProjection.QuotationProductTaxView> taxViews = new ArrayList<>();
//           taxViews.add(new QuoteProductTax());
//           return taxViews;
//       }
//
//       private static class QuoteRiskView implements QuotationProjection.QuotationRiskView{
//
//           @Override
//           public String getRiskId() {
//               return "KAA 001A";
//           }
//
//           @Override
//           public String getQuotationNumber() {
//               return "FAKE/QUOTE/001";
//           }
//
//           @Override
//           public Long getWithEffectToDate() {
//               return 16088797897L;
//           }
//
//           @Override
//           public Long getCoverTypeId() {
//               return 2L;
//           }
//
//           @Override
//           public String getCoverTypeCode() {
//               return "FAKE_CODE";
//           }
//
//           @Override
//           public Long getSubClassId() {
//               return 6L;
//           }
//
//           @Override
//           public BigDecimal getPremium() {
//               return new BigDecimal(90000);
//           }
//
//           @Override
//           public BigDecimal getAnnualPremium() {
//               return new BigDecimal(67000);
//           }
//
//           @Override
//           public BigDecimal getFuturePremium() {
//               return new BigDecimal(100000);
//           }
//
//           @Override
//           public BigDecimal getValue() {
//               return new BigDecimal(83900);
//           }
//
//           @Override
//           public QuotationProjection.QuotationValuationInfoView getQuotationValuationInfo() {
//               return new QuoteValuerInfo();
//           }
//       }
//
//       private static class QuoteProductTax implements QuotationProjection.QuotationProductTaxView{
//
//           @Override
//           public BigDecimal getTaxAmount() {
//               return new BigDecimal(10000);
//           }
//
//           @Override
//           public String getTransactionTypeCode() {
//               return "null";
//           }
//
//           @Override
//           public String getTaxRateType() {
//               return "null";
//           }
//
//           @Override
//           public String getTaxType() {
//               return "null";
//           }
//
//           @Override
//           public String getTaxRateDescription() {
//               return "null";
//           }
//       }
//
//       private static class QuoteValuerInfo implements  QuotationProjection.QuotationValuationInfoView{
//
//           @Override
//           public String getCode() {
//               return "null";
//           }
//
//           @Override
//           public Long getRequestValuationDate() {
//               return 9L;
//           }
//
//           @Override
//           public Long getValuerOrganizationId() {
//               return 9L;
//           }
//
//           @Override
//           public String getRpt() {
//               return "null";
//           }
//
//           @Override
//           public Long getClientId() {
//               return 88L;
//           }
//
//           @Override
//           public String getRiskId() {
//               return "null";
//           }
//
//           @Override
//           public Long getQuotationId() {
//               return 5L;
//           }
//
//           @Override
//           public String getQuotationNo() {
//               return "null";
//           }
//
//           @Override
//           public Long getEntryDate() {
//               return 6736356L;
//           }
//
//           @Override
//           public Long getOrganizationId() {
//               return 9L;
//           }
//       }
//   }
//
//   //</editor-fold>
//
//}
