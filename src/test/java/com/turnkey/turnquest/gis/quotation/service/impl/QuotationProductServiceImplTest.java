package com.turnkey.turnquest.gis.quotation.service.impl;

import com.turnkey.turnquest.gis.quotation.client.billing.ReceiptClient;
import com.turnkey.turnquest.gis.quotation.client.crm.AgencyClient;
import com.turnkey.turnquest.gis.quotation.client.valuation.ValuationClient;
import com.turnkey.turnquest.gis.quotation.dto.crm.AccountTypeDto;
import com.turnkey.turnquest.gis.quotation.dto.crm.OrganizationDto;
import com.turnkey.turnquest.gis.quotation.dto.quotation.UpdateInstallmentDto;
import com.turnkey.turnquest.gis.quotation.dto.quotation.UpdateQuotationDto;
import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyLevelClauseDto;
import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyRiskDto;
import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyTaxDto;
import com.turnkey.turnquest.gis.quotation.enums.ValuationStatus;
import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import com.turnkey.turnquest.gis.quotation.event.producer.ConvertQuoteEvent;
import com.turnkey.turnquest.gis.quotation.event.producer.UpdateQuotationPublisher;
import com.turnkey.turnquest.gis.quotation.exception.error.QuoteConversionException;
import com.turnkey.turnquest.gis.quotation.model.*;
import com.turnkey.turnquest.gis.quotation.repository.QuotationProductRepository;
import com.turnkey.turnquest.gis.quotation.service.QuotationProductTaxService;
import com.turnkey.turnquest.gis.quotation.service.QuotationRiskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class QuotationProductServiceImplTest {

    @InjectMocks
    private QuotationProductServiceImpl quotationProductService;
    @Mock
    private QuotationProductRepository quotationProductRepository;

    @Mock
    private UpdateQuotationPublisher updateQuotationPublisher;
    @Mock
    private QuotationRiskService quotationRiskService;
    @Mock
    private QuotationProductTaxService quotationProductTaxService;
    @Mock
    private AgencyClient agencyClient;
    @Mock
    private ConvertQuoteEvent convertQuoteEvent;
    @Mock
    private ReceiptClient receiptClient;
    @Mock
    private ValuationClient valuationClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void find() {

        Optional<QuotationProduct> optionalQp = Optional.of(testQuotationProduct());

        when(quotationProductRepository.findById(ArgumentMatchers.anyLong())).thenReturn(optionalQp);

        var result = quotationProductService.find(1L);

        assert (result.isPresent());
        assertEquals(optionalQp.get(), result.get());
    }

    @Test
    void create() {
        var qp = testQuotationProduct();
        when(quotationProductRepository.findByForeignId(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        when(quotationProductRepository.save(ArgumentMatchers.any())).thenReturn(qp);

        var result = quotationProductService.create(qp);

        assertNotNull(result);
        assertEquals(qp, result);
    }

    @Test
    void update() {
        QuotationProduct toUpdate = testQuotationProduct();
        toUpdate.setCode("UPDATED");

        when(quotationProductRepository.save(ArgumentMatchers.any())).thenReturn(toUpdate);

        var result = quotationProductService.update(toUpdate, toUpdate.getId());

        assertNotNull(result);
        assertEquals(toUpdate.getId(), result.getId());
        assertEquals(toUpdate.getCode(), result.getCode());

    }

    @Test
    void findByQuotationId() {
        QuotationProduct qp_1 = testQuotationProduct();
        qp_1.setId(1L);
        qp_1.setQuotationId(77L);

        QuotationProduct qp_2 = testQuotationProduct();
        qp_2.setId(2L);
        qp_2.setQuotationId(77L);

        List<QuotationProduct> returnedList = new ArrayList<>();
        returnedList.add(qp_1);
        returnedList.add(qp_2);


        when(quotationProductRepository.findByQuotationId(ArgumentMatchers.anyLong())).thenReturn(returnedList);

        List<QuotationProduct> fetchedList = quotationProductService.findByQuotationId(77L);

        assertNotNull(fetchedList);
        assertEquals(2, fetchedList.size());
        assertEquals(returnedList, fetchedList);
    }

    @Test
    void getQuotationRisks() {
        QuotationRisk qp_1 = testRisk();
        qp_1.setId(1L);
        qp_1.setQuotationProductId(12L);

        QuotationRisk qp_2 = testRisk();
        qp_2.setId(2L);
        qp_2.setQuotationProductId(12L);

        List<QuotationRisk> returnedList = new ArrayList<>();
        returnedList.add(qp_1);
        returnedList.add(qp_2);

        when(quotationRiskService.findByQuotationProductId(ArgumentMatchers.anyLong())).thenReturn(returnedList);

        List<QuotationRisk> fetchedList = quotationProductService.getQuotationRisks(12L);

        assertNotNull(fetchedList);
        assertEquals(2, fetchedList.size());
        assertEquals(returnedList, fetchedList);
    }

    @Test
    void updateInstallmentAmounts() {
        UpdateInstallmentDto updateInstallmentDto = new UpdateInstallmentDto();
        updateInstallmentDto.setQuotationProductId(1L);
        updateInstallmentDto.setNewInstallmentAmount(BigDecimal.valueOf(1000));
        updateInstallmentDto.setNewCommissionInstallmentAmount(BigDecimal.valueOf(10));

        QuotationRisk quotationRisk = new QuotationRisk();
        quotationRisk.setId(6022L);
        quotationRisk.setOutstandingInstallmentAmount(BigDecimal.valueOf(3000));
        quotationRisk.setCommInstallmentAmount(BigDecimal.valueOf(250));
        quotationRisk.setNextInstallmentNo(12L);

        List<QuotationRisk> listOfRisks = new ArrayList<>();
        listOfRisks.add(quotationRisk);

        var quotationProduct = testQuotationProduct();
        Optional<QuotationProduct> optionalQp = Optional.of(quotationProduct);

        when(quotationProductRepository.findById(ArgumentMatchers.anyLong())).thenReturn(optionalQp);

        when(quotationRiskService.findByQuotationProductId(ArgumentMatchers.anyLong())).thenReturn(listOfRisks);

        when(quotationRiskService.update(ArgumentMatchers.any(), ArgumentMatchers.anyLong())).thenReturn(quotationRisk);

        when(quotationProductRepository.save(ArgumentMatchers.any())).thenReturn(quotationProduct);


        var result = quotationProductService.updateInstallmentAmounts(updateInstallmentDto);

        assertNotNull(result.getCommInstallmentPremium());
        assertNotNull(result.getInstallmentPremium());
        assertEquals(updateInstallmentDto.getNewInstallmentAmount(), result.getInstallmentPremium());
        assertEquals(updateInstallmentDto.getNewCommissionInstallmentAmount(), result.getCommInstallmentAmount());
    }


    @Test
    void convertToPolicy() {
        QuotationRisk quotationRisk = new QuotationRisk();
        quotationRisk.setId(76L);

        QuotationProductTax quotationProductTax = new QuotationProductTax();
        quotationProductTax.setId(39L);

        QuotationClause quotationClause = new QuotationClause();
        quotationClause.setClauseId(88L);

        var quotationProduct = testQuotationProduct();
        quotationProduct.setQuotation(testQuote());
        quotationProduct.getQuotationRisks().add(quotationRisk);
        quotationProduct.getQuotationProductTaxes().add(quotationProductTax);

        AccountTypeDto accountType = new AccountTypeDto();
        accountType.setWithHoldingTaxRate(BigDecimal.valueOf(0.1));

        OrganizationDto org = new OrganizationDto();
        org.setAccountType(accountType);

        var policyRisk = new PolicyRiskDto();
        var policyTax = new PolicyTaxDto();
        var policyClause = new PolicyLevelClauseDto();


        when(quotationRiskService.convertToPolicyRisks(ArgumentMatchers.anyList())).thenReturn(List.of(policyRisk));

        when(quotationProductTaxService.convertToPolicyTaxes(ArgumentMatchers.anyList())).thenReturn(List.of(policyTax));

        when(agencyClient.findByOrganizationId(ArgumentMatchers.anyLong())).thenReturn(org);

        quotationProductService.convertToPolicy(quotationProduct);

        verify(quotationRiskService, times(1)).convertToPolicyRisks(quotationProduct.getQuotationRisks());
        verify(agencyClient, times(1)).findByOrganizationId(quotationProduct.getQuotation().getOrganizationId());
        verify(convertQuoteEvent, times(1)).convertQuotationProductToPolicy(ArgumentMatchers.any());
    }

    @Test
    void saveQuickQuotationProduct() {
        Quotation quickQuote = testQuote();
        quickQuote.setStatus("RN");
        quickQuote.setOrganizationId(23L);

        QuotationRisk quotationRisk = testRisk();
        quotationRisk.setId(89L);

        QuotationProductTax quotationProductTax = new QuotationProductTax();
        quotationProductTax.setId(442L);

        var quotationProduct = testQuotationProduct();
        quotationProduct.setQuotationRisks(List.of(quotationRisk));
        quotationProduct.getQuotationProductTaxes().add(quotationProductTax);

        when(quotationProductRepository.findByForeignId(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        when(quotationProductRepository.save(ArgumentMatchers.any())).thenReturn(quotationProduct);

        when(quotationProductTaxService.saveQuickProductTax(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(quotationProductTax);

        when(quotationRiskService.saveQuickQuotationRisk(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(quotationRisk);

        when(receiptClient.getTotalAmountPaidByQuotationNo(ArgumentMatchers.anyString())).thenReturn(quickQuote.getPremium());
        when(valuationClient.findOriginalQuote(ArgumentMatchers.anyString())).thenReturn(quickQuote);

        var result = quotationProductService.saveQuickQuotationProduct(quickQuote, quotationProduct);

        assertNotNull(result);
        verify(quotationProductRepository).save(ArgumentMatchers.any());
        verify(quotationRiskService).saveQuickQuotationRisk(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any());

    }

    @Test
    void computeWithHoldingTax() {
        var quotationProduct = testQuotationProduct();
        quotationProduct.setQuotation(testQuote());

        AccountTypeDto accountTypeDto = new AccountTypeDto();
        accountTypeDto.setId(299L);
        accountTypeDto.setWithHoldingTaxRate(BigDecimal.valueOf(10));

        OrganizationDto organizationDto = new OrganizationDto();
        organizationDto.setId(23L);
        organizationDto.setAccountType(accountTypeDto);

        when(agencyClient.findByOrganizationId(ArgumentMatchers.anyLong())).thenReturn(organizationDto);

        QuotationProduct result = quotationProductService.computeWithHoldingTax(quotationProduct, quotationProduct.getQuotation().getOrganizationId());

        assertNotNull(result);
        assertNotNull(result.getWithHoldingTax());
        verify(agencyClient, times(1)).findByOrganizationId(quotationProduct.getQuotation().getOrganizationId());
        assertEquals(quotationProduct.getCommissionAmount().abs().multiply(BigDecimal.valueOf(0.1)).setScale(25, RoundingMode.HALF_UP), result.getWithHoldingTax());
    }

    @Test
    void changePaymentOption() {
        var quotationProduct = testQuotationProduct();
        when(quotationProductRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(quotationProduct));

        quotationProduct.setInstallmentAllowed(YesNo.Y);

        when(quotationProductRepository.save(ArgumentMatchers.any())).thenReturn(quotationProduct);

        QuotationProduct paymentOptionChanged = quotationProductService.changePaymentOption(1L, YesNo.Y);

        assertEquals(YesNo.Y, paymentOptionChanged.getInstallmentAllowed());
    }

    private Quotation testQuote() {
        var quotation = new Quotation();
        quotation.setId(706L);
        quotation.setStatus("NB");
        quotation.setOrganizationId(23L);
        quotation.setPremium(BigDecimal.valueOf(2000));
        quotation.setCurrentStatus("D");
        quotation.setAgencyId(808L);
        quotation.setBranchId(18L);
        quotation.setCurrencyId(22L);
        quotation.setCurrencySymbol("KES");
        quotation.setPanelId(70L);
        quotation.setPolicyNo("POLICY_NO");
        quotation.setPaymentFrequency("FREQ");
        quotation.setClientId(889L);
        quotation.setQuotationNo("QUOTATION_NO");
        quotation.setPaymentRef("PAYMNENT_REF");
        quotation.setTotalSumInsured(BigDecimal.valueOf(1000000));

        return quotation;
    }

    private QuotationProduct testQuotationProduct() {
        var quotationProduct = new QuotationProduct();
        quotationProduct.setId(1L);
        quotationProduct.setProductId(33L);
        quotationProduct.setCode("NOT_UPDATED");

        quotationProduct.setQuotationRisks(new LinkedList<>());
        quotationProduct.setQuotationProductTaxes(new LinkedList<>());

        quotationProduct.setInstallmentAllowed(YesNo.Y);

        quotationProduct.setPaymentFrequency("SOME_FREQ");
        quotationProduct.setInstallmentAmount(BigDecimal.valueOf(300));
        quotationProduct.setNextInstallmentNo(4L);
        quotationProduct.setMaturityDate(System.currentTimeMillis());
        quotationProduct.setTotalNoOfInstallments(12L);
        quotationProduct.setOutstandingInstallmentAmount(BigDecimal.valueOf(300));
        quotationProduct.setInstallmentPremium(BigDecimal.valueOf(20));
        quotationProduct.setProductInstallmentId(46L);
        quotationProduct.setOutstandingCommission(BigDecimal.valueOf(10));
        quotationProduct.setCommInstallmentPremium(BigDecimal.valueOf(40));
        quotationProduct.setCommInstallmentAmount(BigDecimal.valueOf(80));
        quotationProduct.setPaidInstallmentAmount(BigDecimal.valueOf(100));
        quotationProduct.setPaidInstallmentComm(BigDecimal.valueOf(50));

        quotationProduct.setQuotationId(706L);

        quotationProduct.setTotalSumInsured(BigDecimal.valueOf(20000));
        quotationProduct.setTotalPremium(BigDecimal.valueOf(2000));
        quotationProduct.setBasicPremium(BigDecimal.valueOf(200));
        quotationProduct.setFutureAnnualPremium(BigDecimal.valueOf(18000));
        quotationProduct.setCommissionAmount(BigDecimal.valueOf(400));
        quotationProduct.setWithHoldingTax(BigDecimal.valueOf(100));
        quotationProduct.setFromBinder("BINDER");
        quotationProduct.setBinderId(92L);

        quotationProduct.setWithEffectFromDate(1637743597L);
        quotationProduct.setWithEffectToDate(1763973997L);

        quotationProduct.setPolicyCoverTo(1763973997L);

        return quotationProduct;
    }

    QuotationRisk testRisk() {
        var qr = new QuotationRisk();

        qr.setWithEffectFromDate(Timestamp.valueOf("2029-2-2 00:00:00").getTime());
        qr.setWithEffectToDate(Timestamp.valueOf("2029-3-1 00:00:00").getTime());
        qr.setRiskId("KDJ 777K");
        qr.setValuationStatus(ValuationStatus.OPEN);

        return qr;
    }

    @Test
    void shouldDeleteQuotationProductRisk() {
        // Given
        Quotation quotation = new Quotation();
        // Initialize the quotationProducts list
        quotation.setQuotationProducts(new ArrayList<>());
        QuotationProduct quotationProduct = new QuotationProduct();
        // Initialize the quotationRisks list
        quotationProduct.setQuotationRisks(new ArrayList<>());
        QuotationRisk quotationRisk = new QuotationRisk();
        quotationRisk.setId(1L);
        quotationProduct.getQuotationRisks().add(quotationRisk);
        quotation.getQuotationProducts().add(quotationProduct);

        // When
        Quotation result = quotationProductService.deleteQuotationProductRisk(quotation, 1L);

        // Then
        assertTrue(result.getQuotationProducts().getFirst().getQuotationRisks().isEmpty());
        verify(quotationRiskService).delete(1L);
    }

    @Test
    void shouldUpdateQuotationProductWhenProductExists() {
        // Given
        UpdateQuotationDto updateQuotationDto = new UpdateQuotationDto();
        updateQuotationDto.setQuotationProductId(1L);
        QuotationProduct existingProduct = new QuotationProduct();
        existingProduct.setId(1L);

        when(quotationProductRepository.findById(any(Long.class))).thenReturn(Optional.of(existingProduct));

        // When
        quotationProductService.updateQuotationProduct(updateQuotationDto);

        // Then
        verify(quotationProductRepository, times(1)).save(any(QuotationProduct.class));
        verify(updateQuotationPublisher, times(1)).updateQuotation(any(UpdateQuotationDto.class));
    }

    @Test
    void shouldNotUpdateQuotationProductWhenProductDoesNotExist() {
        // Given
        UpdateQuotationDto updateQuotationDto = new UpdateQuotationDto();
        updateQuotationDto.setQuotationProductId(1L);

        when(quotationProductRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // When
        quotationProductService.updateQuotationProduct(updateQuotationDto);

        // Then
        verify(quotationProductRepository, never()).save(any(QuotationProduct.class));
        verify(updateQuotationPublisher, never()).updateQuotation(any(UpdateQuotationDto.class));
    }

    @Test
    void shouldComputeWithHoldingTaxWhenOrganizationExists() {
        // Given
        Long id = 1L;
        QuotationProduct quotationProduct = new QuotationProduct();
        quotationProduct.setId(id);

        OrganizationDto organizationDto = new OrganizationDto();
        AccountTypeDto accountTypeDto = new AccountTypeDto();
        accountTypeDto.setWithHoldingTaxRate(BigDecimal.ONE); // Set any non-null value for withHoldingTaxRate
        organizationDto.setAccountType(accountTypeDto);

        when(agencyClient.findByOrganizationId(any(Long.class))).thenReturn(organizationDto);

        // When
        QuotationProduct result = quotationProductService.computeWithHoldingTax(quotationProduct, id);

        // Then
        assertNotNull(result);
        verify(agencyClient, times(1)).findByOrganizationId(id);
    }

    @Test
    void shouldThrowExceptionWhenQuotationProductDoesNotExist() {
        // Given
        Long id = 1L;

        when(quotationProductService.find(id)).thenReturn(Optional.empty());

        // Then
        assertThrows(QuoteConversionException.class, () -> {
            // When
            quotationProductService.convertToPolicy(id);
        });
    }


}
