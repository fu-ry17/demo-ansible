package com.turnkey.turnquest.gis.quotation.controller.unit;

import com.turnkey.turnquest.gis.quotation.controller.QuotationRiskController;
import com.turnkey.turnquest.gis.quotation.model.QuotationProduct;
import com.turnkey.turnquest.gis.quotation.model.QuotationRisk;
import com.turnkey.turnquest.gis.quotation.model.QuotationRiskSection;
import com.turnkey.turnquest.gis.quotation.model.QuotationRiskTax;
import com.turnkey.turnquest.gis.quotation.service.QuotationRiskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class QuotationRiskControllerTest {


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @InjectMocks
    QuotationRiskController quotationRiskController;

    @Mock
    private Authentication authentication;

    @Mock
    QuotationRiskService quotationRiskService;

    @Test
    void create() {
        Mockito.when(quotationRiskService.create(any(QuotationRisk.class))).thenReturn(quotationRisk());
        var results = quotationRiskController.create(quotationRisk());
        assertEquals("KBM 123G", results.getBody().getRiskId());

        System.out.println(results);
    }

    @Test
    void saveMultiple() {
        Mockito.when(quotationRiskService.saveMultiple(anyList())).thenReturn(List.of(quotationRisk()));
        var results = quotationRiskController.saveMultiple(List.of(quotationRisk()));
        assertEquals("KBM 123G", results.getBody().get(0).getRiskId());
    }

    @Test
    void update() {
        Mockito.when(quotationRiskService.update(any(QuotationRisk.class), any(Long.class))).thenReturn(quotationRisk());
        var results = quotationRiskController.update(quotationRisk(), 1L);
        assertEquals("KBM 123G", results.getBody().getRiskId());
    }

    @Test
    void find() {
        Mockito.when(quotationRiskService.find(any(Long.class))).thenReturn(java.util.Optional.of(quotationRisk()));
        var results = quotationRiskController.find(1L);
        assertEquals("KBM 123G", results.getBody().getRiskId());
    }

    @Test
    void updateCertificateNo() {
        Mockito.when(quotationRiskService.updateCertificateNo(any(String.class), any(Long.class))).thenReturn(quotationRisk());
        var results = quotationRiskController.updateCertificateNo(1L, "KBM 123G");
        assertEquals("KBM 123G", results.getBody().getRiskId());
    }

    @Test
    void getQuotationRiskSections() {
        Mockito.when(quotationRiskService.getQuotationRiskSections(any(Long.class))).thenReturn(List.of());
        var results = quotationRiskController.getQuotationRiskSections(1L);
        assertEquals(0, results.getBody().size());
    }

    @Test
    void shouldCreateQuotationRiskSectionWhenIdExists() {
        Long id = 1L;
        QuotationRiskSection quotationRiskSection = new QuotationRiskSection();

        when(quotationRiskService.createOrUpdateQuotationRiskSection(id, quotationRiskSection)).thenReturn(quotationRiskSection);

        ResponseEntity<QuotationRiskSection> result = quotationRiskController.createQuotationRiskSection(id, quotationRiskSection);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(quotationRiskSection, result.getBody());
    }

    @Test
    void shouldHandleExceptionWhenCreateQuotationRiskSectionFails() {
        Long id = 1L;
        QuotationRiskSection quotationRiskSection = new QuotationRiskSection();

        when(quotationRiskService.createOrUpdateQuotationRiskSection(id, quotationRiskSection)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> quotationRiskController.createQuotationRiskSection(id, quotationRiskSection));
    }

    @Test
    void shouldReturnRiskTaxesWhenIdExists() {
        Long id = 1L;
        List<QuotationRiskTax> expectedRiskTaxes = Arrays.asList(new QuotationRiskTax(), new QuotationRiskTax());

        when(quotationRiskService.findTaxes(id)).thenReturn(expectedRiskTaxes);

        ResponseEntity<List<QuotationRiskTax>> result = quotationRiskController.getRiskTaxes(id, authentication);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedRiskTaxes, result.getBody());
    }

    @Test
    void shouldReturnEmptyListWhenNoRiskTaxesExistForId() {
        Long id = 1L;

        when(quotationRiskService.findTaxes(id)).thenReturn(Collections.emptyList());

        ResponseEntity<List<QuotationRiskTax>> result = quotationRiskController.getRiskTaxes(id, authentication);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(Objects.requireNonNull(result.getBody()).isEmpty());
    }


    @Test
    void shouldHandleExceptionWhenDeleteFails() {
        Long id = 1L;

        doThrow(new RuntimeException()).when(quotationRiskService).delete(id);

        assertThrows(RuntimeException.class, () -> quotationRiskController.delete(id));
    }


    QuotationRisk quotationRisk() {
        QuotationRisk quotationRisk = new QuotationRisk();
        quotationRisk.setRiskId("KBM 123G");
        quotationRisk.setForeignId(1L);
        quotationRisk.setQuotationProduct(new QuotationProduct());
        quotationRisk.setQuotationRiskTaxes(List.of());
        quotationRisk.setQuotationRiskSections(List.of());
        return quotationRisk;
    }
}
