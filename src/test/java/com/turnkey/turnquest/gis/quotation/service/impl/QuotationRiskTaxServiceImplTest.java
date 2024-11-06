package com.turnkey.turnquest.gis.quotation.service.impl;

import com.turnkey.turnquest.gis.quotation.client.gis.TaxRateClient;
import com.turnkey.turnquest.gis.quotation.dto.gis.TaxRateDto;
import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyRiskTaxDto;
import com.turnkey.turnquest.gis.quotation.enums.TaxRateCategory;
import com.turnkey.turnquest.gis.quotation.enums.TaxRateInstallmentType;
import com.turnkey.turnquest.gis.quotation.model.QuotationRisk;
import com.turnkey.turnquest.gis.quotation.model.QuotationRiskTax;
import com.turnkey.turnquest.gis.quotation.repository.QuotationRiskTaxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class QuotationRiskTaxServiceImplTest {
    @Mock
    QuotationRiskTaxRepository quotationRiskTaxRepository;

    @Mock
    TaxRateClient taxRateClient;

    @InjectMocks
    QuotationRiskTaxServiceImpl quotationRiskTaxService;

    QuotationRiskTax returnedQuotationRiskTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        returnedQuotationRiskTask = new QuotationRiskTax();
        returnedQuotationRiskTask.setId(1L);
    }

    @Test
    void find() {
        Optional<QuotationRiskTax> optional = Optional.of(returnedQuotationRiskTask);
        Mockito.when(quotationRiskTaxRepository.findById(ArgumentMatchers.anyLong())).thenReturn(optional);

        Optional<QuotationRiskTax> found = quotationRiskTaxService.find(1L);

        assertEquals(found.get().getId(), optional.get().getId());

    }

    @Test
    void create() {
        QuotationRiskTax toCreate = new QuotationRiskTax();
        toCreate.setId(1L);
        toCreate.setTaxRateCategory(TaxRateCategory.TX);

        returnedQuotationRiskTask.setTaxRateCategory(TaxRateCategory.TX);
        Mockito.when(quotationRiskTaxRepository.save(ArgumentMatchers.any())).thenReturn(returnedQuotationRiskTask);

        QuotationRiskTax created = quotationRiskTaxService.create(toCreate);

        assertNotNull(created);
        assertEquals(created, returnedQuotationRiskTask);
    }

    @Test
    void update() {
        QuotationRiskTax toUpdate = new QuotationRiskTax();
        toUpdate.setTaxRateCategory(TaxRateCategory.AC);

        returnedQuotationRiskTask.setTaxRateCategory(TaxRateCategory.TX);

        Mockito.when(quotationRiskTaxRepository.save(ArgumentMatchers.any())).thenReturn(toUpdate);

        QuotationRiskTax updated = quotationRiskTaxService.update(toUpdate, returnedQuotationRiskTask.getId());

        assertNotNull(updated);
        assertEquals(updated.getId(), returnedQuotationRiskTask.getId());
        assertNotEquals(updated.getTaxRateCategory(), returnedQuotationRiskTask.getTaxRateCategory());
    }

    @Test
    void findByQuotationRiskId() {
        QuotationRiskTax one = new QuotationRiskTax();
        one.setId(1L);
        one.setQuotationRiskId(77L);

        QuotationRiskTax two = new QuotationRiskTax();
        two.setId(2L);
        two.setQuotationRiskId(77L);

        QuotationRiskTax three = new QuotationRiskTax();
        three.setId(3L);
        three.setQuotationRiskId(77L);

        QuotationRiskTax four = new QuotationRiskTax();
        four.setId(4L);
        four.setQuotationRiskId(77L);

        List<QuotationRiskTax> returnedList = new LinkedList<>();
        returnedList.add(one);
        returnedList.add(two);
        returnedList.add(three);
        returnedList.add(four);

        Mockito.when(
                        quotationRiskTaxRepository.findAllByQuotationRiskId(ArgumentMatchers.anyLong()))
                .thenReturn(returnedList);

        List<QuotationRiskTax> found = quotationRiskTaxService.findByQuotationRiskId(77L);

        assertNotNull(found);
        assertEquals(found.size(), returnedList.size());
        assertEquals(found.get(0).getQuotationRiskId(), returnedList.get(2).getQuotationRiskId());
    }

    @Test
    void createMultiple() {
        QuotationRiskTax one = new QuotationRiskTax();
        one.setId(1L);
        one.setQuotationRiskId(77L);

        QuotationRiskTax two = new QuotationRiskTax();
        two.setId(2L);
        two.setQuotationRiskId(77L);

        List<QuotationRiskTax> returnedList = new LinkedList<>();
        returnedList.add(one);
        returnedList.add(two);

        Mockito.when(quotationRiskTaxRepository.saveAll(ArgumentMatchers.anyList())).thenReturn(returnedList);


        List<QuotationRiskTax> toSave = new LinkedList<>();
        toSave.add(one);
        toSave.add(two);

        List<QuotationRiskTax> saved = quotationRiskTaxService.createMultiple(toSave);

        assertNotNull(saved);
        assertEquals(saved, returnedList);

    }

    @Test
    void updateTaxAmount() {
        Optional<QuotationRiskTax> optional = Optional.of(returnedQuotationRiskTask);
        Mockito.when(quotationRiskTaxRepository.findById(ArgumentMatchers.anyLong())).thenReturn(optional);

        BigDecimal taxAmount = BigDecimal.valueOf(20);

        QuotationRiskTax toUpdate = new QuotationRiskTax();
        toUpdate.setId(1L);
        toUpdate.setTaxAmount(taxAmount);

        Mockito.when(quotationRiskTaxRepository.save(ArgumentMatchers.any())).thenReturn(toUpdate);

        QuotationRiskTax updated = quotationRiskTaxService.updateTaxAmount(taxAmount, 1L);

        assertNotNull(updated);
        assertEquals(updated.getTaxAmount(), toUpdate.getTaxAmount());
    }

    @Test
    void convertToPolicyRiskTaxes() {

    }

    @Test
    void convertToPolicyRiskTax() {
        QuotationRiskTax quotationRiskTax = new QuotationRiskTax();
        quotationRiskTax.setTransactionTypeCode("TT_CODE");
        quotationRiskTax.setRate(BigDecimal.valueOf(20));
        quotationRiskTax.setTaxAmount(BigDecimal.valueOf(20));
        quotationRiskTax.setTlLvlCode("TlLvl_CODE");
        quotationRiskTax.setTaxRateType("TR_TYPE");
        quotationRiskTax.setTaxRateDescription("TR_DESC");
        quotationRiskTax.setApplicationArea("A_AREA");
        quotationRiskTax.setTaxType("T_TYPE");
        quotationRiskTax.setTaxRateCategory(TaxRateCategory.TX);
        quotationRiskTax.setTaxRateInstallmentType(TaxRateInstallmentType.FIRST);
        quotationRiskTax.setCalculationMode("C_MODE");
        quotationRiskTax.setProductSubClassId(345L);
        quotationRiskTax.setDivisionFactor(BigDecimal.valueOf(1L));
        quotationRiskTax.setTaxRateId(432L);

        PolicyRiskTaxDto policyRiskTaxDto = quotationRiskTaxService.convertToPolicyRiskTax(quotationRiskTax);

        assertNotNull(policyRiskTaxDto);
        assertEquals(policyRiskTaxDto.getTlLevelCode(), quotationRiskTax.getTlLvlCode());
        assertEquals(policyRiskTaxDto.getTaxRateId(), quotationRiskTax.getTaxRateId());
    }

    @Test
    void saveQuickQuotationRiskTax() {
        TaxRateDto taxRate = new TaxRateDto();
        taxRate.setApplicationArea("A_AREA");
        taxRate.setCalculationMode("C_MODE");
        taxRate.setCategory(TaxRateCategory.TX);
        taxRate.setInstallmentType(TaxRateInstallmentType.FIRST);
        taxRate.setRateDesc("R_DESC");
        taxRate.setRate(BigDecimal.valueOf(1L));
        taxRate.setProductSubClassId(261L);
        taxRate.setTaxType("T_TYE");
        taxRate.setRiskPolLevel("RPL");
        taxRate.setTlLvlCode("TlLvl_CODE");
        taxRate.setTransactionTypeCode("TT_CODE");
        taxRate.setRateType("R_TYPE");
        taxRate.setDivisionFactor(BigDecimal.valueOf(1L));

        returnedQuotationRiskTask.setTaxRateId(1L);

        Mockito.when(taxRateClient.find(ArgumentMatchers.anyLong())).thenReturn(taxRate);

        QuotationRisk quotationRisk = new QuotationRisk();
        quotationRisk.setId(29L);
        quotationRiskTaxService.saveQuickQuotationRiskTax(quotationRisk, returnedQuotationRiskTask);

        Mockito.verify(quotationRiskTaxRepository).save(ArgumentMatchers.any());
    }

    @Test
    void convertToPolicyRiskTaxesReturnsEmptyListWhenInputIsEmpty() {
        List<QuotationRiskTax> quotationRiskTaxes = new ArrayList<>();

        List<PolicyRiskTaxDto> result = quotationRiskTaxService.convertToPolicyRiskTaxes(quotationRiskTaxes);

        assertTrue(result.isEmpty());
    }

    @Test
    void convertToPolicyRiskTaxesReturnsPolicyRiskTaxDtos() {
        QuotationRiskTax quotationRiskTax = new QuotationRiskTax();
        quotationRiskTax.setTransactionTypeCode("TT_CODE");
        quotationRiskTax.setRate(BigDecimal.valueOf(20));
        quotationRiskTax.setTaxAmount(BigDecimal.valueOf(20));
        quotationRiskTax.setTlLvlCode("TlLvl_CODE");
        quotationRiskTax.setTaxRateType("TR_TYPE");
        quotationRiskTax.setTaxRateDescription("TR_DESC");
        quotationRiskTax.setApplicationArea("A_AREA");
        quotationRiskTax.setTaxType("T_TYPE");
        quotationRiskTax.setTaxRateCategory(TaxRateCategory.TX);
        quotationRiskTax.setTaxRateInstallmentType(TaxRateInstallmentType.FIRST);
        quotationRiskTax.setCalculationMode("C_MODE");
        quotationRiskTax.setProductSubClassId(345L);
        quotationRiskTax.setDivisionFactor(BigDecimal.valueOf(1L));
        quotationRiskTax.setTaxRateId(432L);

        List<QuotationRiskTax> quotationRiskTaxes = Arrays.asList(quotationRiskTax);

        List<PolicyRiskTaxDto> result = quotationRiskTaxService.convertToPolicyRiskTaxes(quotationRiskTaxes);

        assertEquals(1, result.size());
        assertEquals(quotationRiskTax.getTlLvlCode(), result.get(0).getTlLevelCode());
        assertEquals(quotationRiskTax.getTaxRateId(), result.get(0).getTaxRateId());
    }
}
