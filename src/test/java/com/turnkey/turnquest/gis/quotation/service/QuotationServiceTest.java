//
//package com.turnkey.turnquest.gis.quotation.service;
//
//
//import com.turnkey.turnquest.gis.quotation.client.computation.ComputationClient;
//import com.turnkey.turnquest.gis.quotation.client.aki.AkiClient;
//import com.turnkey.turnquest.gis.quotation.client.billing.GisTransactionClient;
//import com.turnkey.turnquest.gis.quotation.client.billing.TransmittalClient;
//import com.turnkey.turnquest.gis.quotation.client.crm.AgencyClient;
//import com.turnkey.turnquest.gis.quotation.client.crm.ClientDataClient;
//import com.turnkey.turnquest.gis.quotation.client.crm.OrganizationClient;
//import com.turnkey.turnquest.gis.quotation.client.gis.*;
//import com.turnkey.turnquest.gis.quotation.enums.YesNo;
//import com.turnkey.turnquest.gis.quotation.model.Quotation;
//import com.turnkey.turnquest.gis.quotation.model.QuotationProductTax;
//import com.turnkey.turnquest.gis.quotation.repository.QuotationRepository;
//import com.turnkey.turnquest.gis.quotation.service.impl.QuotationServiceImpl;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import static org.mockito.Mockito.when;
//
//public class QuotationServiceTest {
//
//    QuotationServiceImpl quotationService;
//
//    @Mock
//    private QuotationRepository repository;
//
//    @Mock
//    private GisSequenceClient gisSequenceClient;
//
//    @Mock
//    private GisParameterClient gisParameterClient;
//
//    @Mock
//    private QuotationRevisionService quotationRevisionService;
//
//    @Mock
//    private QuotationRiskService quotationRiskService;
//
//    @Mock
//    private QuotationProductService quotationProductService;
//
//    @Mock
//    private QuotationRiskSectionService quotationRiskSectionService;
//
//    @Mock
//    private PremiumRateClient premiumRateClient;
//
//    @Mock
//    private ProductClient productClient;
//
//    @Mock
//    private QuotationClauseService quotationClauseService;
//
//    @Mock
//    private ClauseClient clauseClient;
//
//    @Mock
//    private OrganizationClient organizationClient;
//
//    @Mock
//    private ClientDataClient clientDataClient;
//
//    @Mock
//    private ComputationClient computationClient;
//
//    @Mock
//    private TransmittalClient transmittalClient;
//
//    @Mock
//    private AgencyClient agencyClient;
//
//    @Mock
//    private GisTransactionClient gisTransactionClient;
//
//    @Mock
//    private QuotationProductTaxService quotationProductTaxService;
//
//    @Mock
//    private QuotationReportService quotationReportService;
//
//    @Mock
//    private AkiClient akiClient;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        quotationService = new QuotationServiceImpl(repository, gisSequenceClient, gisParameterClient, quotationRevisionService, quotationRiskService, quotationProductService, quotationRiskSectionService, quotationProductTaxService, premiumRateClient, productClient, quotationClauseService, clauseClient, organizationClient, clientDataClient, computationClient, transmittalClient, agencyClient, gisTransactionClient, quotationReportService);
//    }
//
//
//    @Test
//    public void save_shouldReturnQuotation() {
//        Quotation quotation = generateQuotation(22L, "QT/FAKE/NO", 2L, 44L, 1L, 56L, 22L,
//                "FAKESTATUS", YesNo.N);
//
//        when(repository.save(quotation)).thenReturn(quotation);
//    }
//
//
//    /**
//     * This block generates test data
//     */
//
//    public Quotation generateQuotation(Long id, String quotationNo, Long clientId, Long agencyId, Long orgId, Long marketerId, Long currencyId,
//                                       String status, YesNo isQuotationOk) {
//        Quotation quotation = new Quotation();
//        quotation.setId(id);
//        quotation.setAgencyId(agencyId);
//        quotation.setQuotationNo(quotationNo);
//        quotation.setClientId(clientId);
//        quotation.setOrganizationId(orgId);
//        quotation.setMarketerId(marketerId);
//        quotation.setCurrencyId(currencyId);
//        quotation.setStatus(status);
//        quotation.setIsQuotationOk(isQuotationOk);
//
//        return quotation;
//    }
//
//}
//
