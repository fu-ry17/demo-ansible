package com.turnkey.turnquest.gis.quotation.controller.unit;

import com.turnkey.turnquest.gis.quotation.controller.QuotationRiskTaxController;
import com.turnkey.turnquest.gis.quotation.model.QuotationRiskTax;
import com.turnkey.turnquest.gis.quotation.service.QuotationRiskTaxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author Titus Murithi Bundi
 */
class QuotationRiskTaxControllerTest {

    @Mock
    private QuotationRiskTaxService quotationRiskTaxService;

    @InjectMocks
    private QuotationRiskTaxController quotationRiskTaxController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnQuotationRiskTaxById() {
        QuotationRiskTax quotationRiskTax = new QuotationRiskTax();

        when(quotationRiskTaxService.find(1L)).thenReturn(Optional.of(quotationRiskTax));

        ResponseEntity<QuotationRiskTax> response = quotationRiskTaxController.findById(1L);

        assertEquals(quotationRiskTax, response.getBody());
    }

    @Test
    void shouldReturnNoContentWhenQuotationRiskTaxNotFound() {
        when(quotationRiskTaxService.find(1L)).thenReturn(Optional.empty());

        ResponseEntity<QuotationRiskTax> response = quotationRiskTaxController.findById(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void shouldCreateQuotationRiskTax() {
        QuotationRiskTax quotationRiskTax = new QuotationRiskTax();

        when(quotationRiskTaxService.create(quotationRiskTax)).thenReturn(quotationRiskTax);

        ResponseEntity<QuotationRiskTax> response = quotationRiskTaxController.createRiskTax(quotationRiskTax);

        assertEquals(quotationRiskTax, response.getBody());
    }

    @Test
    void shouldUpdateQuotationRiskTax() {
        QuotationRiskTax quotationRiskTax = new QuotationRiskTax();
        quotationRiskTax.setQuotationRiskId(1L);

        when(quotationRiskTaxService.update(quotationRiskTax, quotationRiskTax.getQuotationRiskId())).thenReturn(quotationRiskTax);

        ResponseEntity<QuotationRiskTax> response = quotationRiskTaxController.updateRiskTax(quotationRiskTax);

        assertEquals(quotationRiskTax, response.getBody());
    }

    @Test
    void shouldCreateMultipleQuotationRiskTaxes() {
        QuotationRiskTax quotationRiskTax1 = new QuotationRiskTax();
        QuotationRiskTax quotationRiskTax2 = new QuotationRiskTax();
        List<QuotationRiskTax> quotationRiskTaxes = Arrays.asList(quotationRiskTax1, quotationRiskTax2);

        when(quotationRiskTaxService.createMultiple(quotationRiskTaxes)).thenReturn(quotationRiskTaxes);

        ResponseEntity<List<QuotationRiskTax>> response = quotationRiskTaxController.createMultipleRiskTax(quotationRiskTaxes);

        assertEquals(quotationRiskTaxes, response.getBody());
    }

    @Test
    void shouldUpdateTaxAmount() {
        QuotationRiskTax quotationRiskTax = new QuotationRiskTax();

        when(quotationRiskTaxService.updateTaxAmount(BigDecimal.valueOf(100), 1L)).thenReturn(quotationRiskTax);

        ResponseEntity<QuotationRiskTax> response = quotationRiskTaxController.updateTaxAmount(BigDecimal.valueOf(100), 1L);

        assertEquals(quotationRiskTax, response.getBody());
    }

}
