package com.turnkey.turnquest.gis.quotation.service.impl;

import com.turnkey.turnquest.gis.quotation.client.gis.TaxRateClient;
import com.turnkey.turnquest.gis.quotation.dto.gis.TaxRateDto;
import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyTaxDto;
import com.turnkey.turnquest.gis.quotation.enums.TaxRateCategory;
import com.turnkey.turnquest.gis.quotation.enums.TaxRateInstallmentType;
import com.turnkey.turnquest.gis.quotation.model.*;
import com.turnkey.turnquest.gis.quotation.repository.QuotationProductTaxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class QuotationProductTaxServiceImplTest {

    @Mock
    QuotationProductTaxRepository quotationProductTaxRepository;

    @Mock
    TaxRateClient taxRateClient;

    @InjectMocks
    QuotationProductTaxServiceImpl quotationProductTaxService;

    QuotationProductTax returnedQuotationProductTax;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        returnedQuotationProductTax = new QuotationProductTax();
        returnedQuotationProductTax.setId(1L);
    }

    @Test
    void find() {
        Optional<QuotationProductTax> optional = Optional.of(returnedQuotationProductTax);

        QuotationProductTax quotationProductTax = new QuotationProductTax();
        quotationProductTax.setId(1L);

        Mockito.when(quotationProductTaxRepository.findById(anyLong())).thenReturn(optional);

        Optional<QuotationProductTax> found = quotationProductTaxService.find(1L);

        assertNotNull(found);
        assertEquals(found.get().getId(), returnedQuotationProductTax.getId());
    }

    @Test
    void create() {
        QuotationProductTax toCreate = new QuotationProductTax();
        toCreate.setId(1L);
        toCreate.setTaxType("TYPE");

        returnedQuotationProductTax.setTaxType("TYPE");
        Mockito.when(
                        quotationProductTaxRepository.save(any()))
                .thenReturn(returnedQuotationProductTax);

        QuotationProductTax created = quotationProductTaxService.create(toCreate);

        assertNotNull(created);
        assertEquals(created.getTaxType(), returnedQuotationProductTax.getTaxType());
    }

    @Test
    void update() {
        QuotationProductTax toUpdate = new QuotationProductTax();
        toUpdate.setCalculationMode("UPDATED");

        returnedQuotationProductTax.setCalculationMode("BEFORE_UPDATE");
        Mockito.when(quotationProductTaxRepository.save(any())).thenReturn(toUpdate);

        QuotationProductTax updated = quotationProductTaxService.update(toUpdate, returnedQuotationProductTax.getId());

        assertNotNull(updated);
        assertEquals(returnedQuotationProductTax.getId(), updated.getId());
        assertNotEquals(returnedQuotationProductTax.getCalculationMode(), updated.getCalculationMode());
    }

    @Test
    void findByQuotationProductId() {
        QuotationProductTax one = new QuotationProductTax();
        one.setId(1L);
        one.setQuotationProductId(455L);

        QuotationProductTax two = new QuotationProductTax();
        two.setId(2L);
        two.setQuotationProductId(455L);

        List<QuotationProductTax> returnedList = new LinkedList<>();
        returnedList.add(one);
        returnedList.add(two);

        Mockito.when(
                        quotationProductTaxRepository.findByQuotationProductId(anyLong()))
                .thenReturn(returnedList);
        List<QuotationProductTax> foundList = quotationProductTaxService.findByQuotationProductId(455L);

        assertNotNull(foundList);
        assertEquals(foundList.size(), returnedList.size());
        assertEquals(foundList, returnedList);

    }

    @Test
    void createMultiple() {
        QuotationProductTax one = new QuotationProductTax();
        one.setId(1L);
        one.setQuotationProductId(455L);

        QuotationProductTax two = new QuotationProductTax();
        two.setId(2L);
        two.setQuotationProductId(455L);

        List<QuotationProductTax> returnedList = new LinkedList<>();
        returnedList.add(one);
        returnedList.add(two);

        Mockito.when(quotationProductTaxRepository.saveAll(ArgumentMatchers.anyList())).thenReturn(returnedList);

        List<QuotationProductTax> toSave = new LinkedList<>();
        returnedList.add(one);
        returnedList.add(two);

        List<QuotationProductTax> saved = quotationProductTaxService.createMultiple(toSave);
        assertNotNull(saved);
        assertEquals(saved, returnedList);

    }

    @Test
    void updateTaxAmount() {
        Optional<QuotationProductTax> optional = Optional.of(returnedQuotationProductTax);
        Mockito.when(quotationProductTaxRepository.findById(anyLong())).thenReturn(optional);

        BigDecimal taxAmount = BigDecimal.valueOf(75);

        QuotationProductTax toUpdate = new QuotationProductTax();
        toUpdate.setId(1L);
        toUpdate.setTaxAmount(taxAmount);

        Mockito.when(quotationProductTaxRepository.save(any())).thenReturn(toUpdate);

        QuotationProductTax updated = quotationProductTaxService.updateTaxAmount(taxAmount, 1L);

        assertNotNull(updated);
        assertEquals(updated.getTaxAmount(), taxAmount);
    }

    @Test
    void convertToPolicyTaxes() {
    }

    @Test
    void convertToPolicyTax() {
        QuotationProductTax quotationProductTax = new QuotationProductTax();
        quotationProductTax.setTransactionTypeCode("TT_CODE");
        quotationProductTax.setRate(BigDecimal.valueOf(10));
        quotationProductTax.setTaxAmount(BigDecimal.valueOf(10));
        quotationProductTax.setTlLvlCode("TlLvl_CDOE");
        quotationProductTax.setTaxRateType("TT_TYPE");
        quotationProductTax.setTaxRateDescription("TT_DESC");
        quotationProductTax.setApplicationArea("A_AREA");
        quotationProductTax.setTaxType("T_TYPE");
        quotationProductTax.setTaxRateCategory(TaxRateCategory.TX);
        quotationProductTax.setTaxRateInstallmentType(TaxRateInstallmentType.FIRST);
        quotationProductTax.setCalculationMode("C_MODE");
        quotationProductTax.setProductSubClassId(234L);
        quotationProductTax.setDivisionFactor(BigDecimal.valueOf(10));

        PolicyTaxDto policyTaxDto = quotationProductTaxService.convertToPolicyTax(quotationProductTax);

        assertNotNull(quotationProductTax);
        assertEquals(quotationProductTax.getTaxType(), policyTaxDto.getTaxType());
    }

    @Test
    void saveQuickProductTax() {
        TaxRateDto taxRate = new TaxRateDto();
        taxRate.setTaxType("T_TYPE");
        taxRate.setCalculationMode("C_MODE");
        taxRate.setCategory(TaxRateCategory.TX);
        taxRate.setInstallmentType(TaxRateInstallmentType.FIRST);
        taxRate.setRateDesc("R_DESC");
        taxRate.setProductSubClassId(476L);
        taxRate.setRiskPolLevel("RP_LEVEL");
        taxRate.setTlLvlCode("TlLvl_CODE");
        taxRate.setTransactionTypeCode("TT_CODE");
        taxRate.setRateType("R_TYPE");
        taxRate.setDivisionFactor(BigDecimal.valueOf(10));

        QuotationProductTax one = new QuotationProductTax();
        one.setId(1L);
        one.setQuotationProductId(455L);
        one.setTaxType("T_TYPE");

        QuotationProductTax two = new QuotationProductTax();
        two.setId(2L);
        two.setQuotationProductId(455L);
        two.setTaxType("T_TYPE");

        List<QuotationProductTax> returnedList = new LinkedList<>();
        returnedList.add(one);
        returnedList.add(two);

        QuotationProductTax quotationProductTax = new QuotationProductTax();
        quotationProductTax.setId(75L);

        QuotationProduct quotationProduct = new QuotationProduct();
        quotationProduct.setId(455L);

        Mockito.when(taxRateClient.find(anyLong())).thenReturn(taxRate);
        Mockito.when(
                        quotationProductTaxRepository.findByQuotationProductId(anyLong()))
                .thenReturn(returnedList);

        quotationProductTaxService.saveQuickProductTax(quotationProduct, quotationProductTax);
        verify(quotationProductTaxRepository).save(any());
    }

    @Test
    void composeQuoteProductTaxes() {
        QuotationProductTax one = new QuotationProductTax();
        one.setId(1L);
        one.setTaxRateInstallmentType(TaxRateInstallmentType.FIRST);
        one.setQuotationProductId(433L);
        one.setTaxType("T_TYPE");

        QuotationProductTax two = new QuotationProductTax();
        two.setId(2L);
        two.setTaxRateInstallmentType(TaxRateInstallmentType.FIRST);
        two.setQuotationProductId(433L);
        two.setTaxType("T_TYPE");

        List<QuotationProductTax> returnedList = new LinkedList<>();
        returnedList.add(one);
        returnedList.add(two);


        QuotationRiskTax quotationRiskTax = new QuotationRiskTax();
        quotationRiskTax.setId(560L);
        quotationRiskTax.setTaxType("T_TYPE");
        quotationRiskTax.setTaxAmount(BigDecimal.valueOf(10));
        quotationRiskTax.setTaxRateId(505L);

        QuotationRisk quotationRisk = new QuotationRisk();
        quotationRisk.setId(1L);
        quotationRisk.setQuotationRiskTaxes(new LinkedList<>());
        quotationRisk.getQuotationRiskTaxes().add(quotationRiskTax);


        QuotationProduct quotationProduct = new QuotationProduct();
        quotationProduct.setId(433L);
        quotationProduct.setQuotationProductTaxes(returnedList);
        quotationProduct.setQuotationRisks(new LinkedList<>());
        quotationProduct.getQuotationRisks().add(quotationRisk);


        TaxRateDto taxRate = new TaxRateDto();
        taxRate.setId(505L);

        taxRate.setTaxType("T_TYPE");
        taxRate.setApplicationArea("A_AREA");
        taxRate.setCalculationMode("C_MODE");
        taxRate.setCategory(TaxRateCategory.TX);
        taxRate.setInstallmentType(TaxRateInstallmentType.FIRST);
        taxRate.setRateDesc("R_DESC");
        taxRate.setRate(BigDecimal.valueOf(10));
        taxRate.setProductSubClassId(922L);
        taxRate.setRiskPolLevel("RP_LEVEL");
        taxRate.setTlLvlCode("TlLvl_CODE");
        taxRate.setTransactionTypeCode("TT_CODE");
        taxRate.setRateType("R_TYPE");
        taxRate.setDivisionFactor(BigDecimal.valueOf(1));


        Mockito.when(taxRateClient.find(anyLong())).thenReturn(taxRate);

        Mockito.when(
                        quotationProductTaxRepository.findByQuotationProductId(anyLong()))
                .thenReturn(returnedList);

        List<QuotationProductTax> composed = quotationProductTaxService.composeQuoteProductTaxes(quotationProduct);

        assertNotNull(composed);
        assertEquals(composed.get(0).getTaxType(), taxRate.getTaxType());
    }

    @Test
    void shouldConvertToPolicyTaxes() {
        PolicyTaxDto policyTaxDto = new PolicyTaxDto();
        policyTaxDto.setTaxType("T_TYPE");

        QuotationProductTax quotationProductTax = new QuotationProductTax();
        quotationProductTax.setTaxType("T_TYPE");
        quotationProductTax.setTransactionTypeCode("TT_CODE");

        List<PolicyTaxDto> policyTaxDtos = quotationProductTaxService.convertToPolicyTaxes(Collections.singletonList(quotationProductTax));

        assertEquals(1, policyTaxDtos.size());
        assertEquals("T_TYPE", policyTaxDtos.getFirst().getTaxType());
    }

    @Test
    void shouldReturnEmptyListWhenNoQuotationProductTaxes() {
        List<PolicyTaxDto> policyTaxDtos = quotationProductTaxService.convertToPolicyTaxes(List.of());

        assertEquals(0, policyTaxDtos.size());
    }

    @Test
    void shouldSaveQuickProductTaxWithExistingTaxRateId() {
        QuotationProduct quoteProduct = new QuotationProduct();
        quoteProduct.setId(1L);

        QuotationProductTax quotationProductTax = new QuotationProductTax();
        quotationProductTax.setTaxRateId(1L);
        quotationProductTax.setId(1L);

        TaxRateDto taxRate = new TaxRateDto();
        taxRate.setCalculationMode("C_MODE");
        taxRate.setCategory(TaxRateCategory.TX);
        taxRate.setInstallmentType(TaxRateInstallmentType.FIRST);
        taxRate.setRateDesc("R_DESC");
        taxRate.setProductSubClassId(922L);
        taxRate.setRiskPolLevel("RP_LEVEL");
        taxRate.setTlLvlCode("TlLvl_CODE");
        taxRate.setTransactionTypeCode("TT_CODE");
        taxRate.setRateType("R_TYPE");
        taxRate.setDivisionFactor(BigDecimal.valueOf(1));

        when(taxRateClient.find(anyLong())).thenReturn(taxRate);
        when(quotationProductTaxRepository.save(any())).thenReturn(quotationProductTax);

        QuotationProductTax result = quotationProductTaxService.saveQuickProductTax(quoteProduct, quotationProductTax);

        assertNotNull(result);
        assertEquals(quotationProductTax.getId(), result.getId());
        assertEquals(taxRate.getCalculationMode(), result.getCalculationMode());
        assertEquals(taxRate.getCategory(), result.getTaxRateCategory());
        assertEquals(taxRate.getInstallmentType(), result.getTaxRateInstallmentType());
        assertEquals(taxRate.getRateDesc(), result.getTaxRateDescription());
        assertEquals(taxRate.getProductSubClassId(), result.getProductSubClassId());
        assertEquals(taxRate.getRiskPolLevel(), result.getRiskOrProductLevel());
        assertEquals(taxRate.getTlLvlCode(), result.getTlLvlCode());
        assertEquals(taxRate.getTransactionTypeCode(), result.getTransactionTypeCode());
        assertEquals(taxRate.getRateType(), result.getTaxRateType());
        assertEquals(taxRate.getDivisionFactor(), result.getDivisionFactor());
    }

    @Test
    void shouldSaveQuickProductTaxWithoutExistingTaxRateId() {
        QuotationProduct quoteProduct = new QuotationProduct();
        quoteProduct.setId(1L);

        QuotationProductTax quotationProductTax = new QuotationProductTax();
        quotationProductTax.setId(1L);

        when(quotationProductTaxRepository.save(any())).thenReturn(quotationProductTax);

        QuotationProductTax result = quotationProductTaxService.saveQuickProductTax(quoteProduct, quotationProductTax);

        assertNotNull(result);
        assertEquals(quotationProductTax.getId(), result.getId());
    }

    @Test
    void testComposeQuoteProductTaxes_WithEmptyRiskTaxes() {
        // Arrange
        QuotationProduct quotationProduct = createQuotationProductWithEmptyRiskTaxes();

        // Act
        List<QuotationProductTax> result = quotationProductTaxService.composeQuoteProductTaxes(quotationProduct);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void testComposeQuoteProductTaxes_WithMultipleRiskTaxes() {
        // Arrange
        QuotationProduct quotationProduct = createQuotationProductWithMultipleRiskTaxes();
        TaxRateDto taxRateDto = createTaxRateDto();

        when(taxRateClient.find(anyLong())).thenReturn(taxRateDto);

        // Act
        List<QuotationProductTax> result = quotationProductTaxService.composeQuoteProductTaxes(quotationProduct);

        // Assert
        assertEquals(1, result.size());
        assertEquals(new BigDecimal("0"), result.getFirst().getTaxAmount());
    }

    private QuotationProduct createQuotationProduct() {
        QuotationProduct quotationProduct = new QuotationProduct();
        quotationProduct.setId(1L);

        QuotationProductTax productTax = new QuotationProductTax();
        productTax.setTaxRateInstallmentType(TaxRateInstallmentType.FIRST);
        quotationProduct.setQuotationProductTaxes(Collections.singletonList(productTax));

        QuotationRiskTax riskTax = new QuotationRiskTax();
        riskTax.setTaxType("TAX_CODE");
        riskTax.setTaxAmount(BigDecimal.TEN);
        riskTax.setTaxRateId(1L);

        QuotationRisk quotationRisk = new QuotationRisk();
        quotationRisk.setQuotationRiskTaxes(Collections.singletonList(riskTax));

        quotationProduct.setQuotationRisks(Collections.singletonList(quotationRisk));

        return quotationProduct;
    }

    private QuotationProduct createQuotationProductWithEmptyRiskTaxes() {
        QuotationProduct quotationProduct = new QuotationProduct();
        quotationProduct.setId(1L);

        QuotationProductTax productTax = new QuotationProductTax();
        productTax.setTaxRateInstallmentType(TaxRateInstallmentType.FIRST);
        quotationProduct.setQuotationProductTaxes(Collections.singletonList(productTax));

        QuotationRisk quotationRisk = new QuotationRisk();
        quotationRisk.setQuotationRiskTaxes(Collections.emptyList());

        quotationProduct.setQuotationRisks(Collections.singletonList(quotationRisk));

        return quotationProduct;
    }

    private QuotationProduct createQuotationProductWithMultipleRiskTaxes() {
        QuotationProduct quotationProduct = new QuotationProduct();
        quotationProduct.setId(1L);

        QuotationProductTax productTax = new QuotationProductTax();
        productTax.setTaxRateInstallmentType(TaxRateInstallmentType.FIRST);
        quotationProduct.setQuotationProductTaxes(Collections.singletonList(productTax));

        QuotationRiskTax riskTax1 = new QuotationRiskTax();
        riskTax1.setTaxType("TAX_CODE");
        riskTax1.setTaxAmount(BigDecimal.TEN);
        riskTax1.setTaxRateId(1L);

        QuotationRiskTax riskTax2 = new QuotationRiskTax();
        riskTax2.setTaxType("TAX_CODE");
        riskTax2.setTaxAmount(new BigDecimal("10"));
        riskTax2.setTaxRateId(1L);

        QuotationRisk quotationRisk = new QuotationRisk();
        quotationRisk.setQuotationRiskTaxes(List.of(riskTax1, riskTax2));

        quotationProduct.setQuotationRisks(Collections.singletonList(quotationRisk));

        return quotationProduct;
    }

    private TaxRateDto createTaxRateDto() {
        TaxRateDto taxRateDto = new TaxRateDto();
        taxRateDto.setTaxType("TAX_CODE");
        taxRateDto.setApplicationArea("AREA");
        taxRateDto.setCalculationMode("MODE");
        taxRateDto.setCategory(TaxRateCategory.TX);
        taxRateDto.setInstallmentType(TaxRateInstallmentType.FIRST);
        taxRateDto.setRateDesc("DESCRIPTION");
        taxRateDto.setRate(BigDecimal.ONE);
        taxRateDto.setProductSubClassId(1L);
        taxRateDto.setRiskPolLevel("LEVEL");
        taxRateDto.setTlLvlCode("CODE");
        taxRateDto.setTransactionTypeCode("TYPE_CODE");
        taxRateDto.setRateType("RATE_TYPE");
        taxRateDto.setDivisionFactor(BigDecimal.ONE);
        return taxRateDto;
    }
}

