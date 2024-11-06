package com.turnkey.turnquest.gis.quotation.controller.unit;

import com.turnkey.turnquest.gis.quotation.controller.QuotationProductController;
import com.turnkey.turnquest.gis.quotation.dto.quotation.UpdateInstallmentDto;
import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import com.turnkey.turnquest.gis.quotation.model.QuotationProduct;
import com.turnkey.turnquest.gis.quotation.model.QuotationRisk;
import com.turnkey.turnquest.gis.quotation.service.QuotationProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Titus Murithi Bundi
 */
class QuotationProductControllerTest {


    @Mock
    private QuotationProductService quotationProductService;

    @InjectMocks
    private QuotationProductController quotationProductController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateQuotationProduct() {
        // Given
        QuotationProduct quotationProduct = new QuotationProduct();
        when(quotationProductService.create(quotationProduct)).thenReturn(quotationProduct);

        // When
        ResponseEntity<QuotationProduct> response = quotationProductController.create(quotationProduct);

        // Then
        assertEquals(quotationProduct, response.getBody());
    }

    @Test
    void shouldUpdateQuotationProduct() {
        // Given
        QuotationProduct quotationProduct = new QuotationProduct();
        when(quotationProductService.update(quotationProduct, 1L)).thenReturn(quotationProduct);

        // When
        ResponseEntity<QuotationProduct> response = quotationProductController.update(quotationProduct, 1L);

        // Then
        assertEquals(quotationProduct, response.getBody());
    }


    @Test
    void shouldGetQuotationRisks() {
        // Given
        QuotationRisk quotationRisk1 = new QuotationRisk();
        QuotationRisk quotationRisk2 = new QuotationRisk();
        List<QuotationRisk> quotationRisks = Arrays.asList(quotationRisk1, quotationRisk2);
        when(quotationProductService.getQuotationRisks(1L)).thenReturn(quotationRisks);

        // When
        ResponseEntity<List<QuotationRisk>> response = quotationProductController.getQuotationRisks(1L);

        // Then
        assertEquals(quotationRisks, response.getBody());
    }

    @Test
    void shouldUpdateInstallmentAmounts() {
        // Given
        UpdateInstallmentDto updateInstallmentDto = new UpdateInstallmentDto();
        QuotationProduct quotationProduct = new QuotationProduct();
        when(quotationProductService.updateInstallmentAmounts(updateInstallmentDto)).thenReturn(quotationProduct);

        // When
        ResponseEntity<QuotationProduct> response = quotationProductController.updateInstallmentAmounts(updateInstallmentDto);

        // Then
        assertEquals(quotationProduct, response.getBody());
    }

    @Test
    void shouldChangePaymentOption() {
        // Given
        QuotationProduct quotationProduct = new QuotationProduct();
        when(quotationProductService.changePaymentOption(1L, YesNo.Y)).thenReturn(quotationProduct);

        // When
        ResponseEntity<QuotationProduct> response = quotationProductController.changePaymentOption(1L, YesNo.Y);

        // Then
        assertEquals(quotationProduct, response.getBody());
    }

    @Test
    void shouldReturnQuotationProductWhenIdExists() {
        Long id = 1L;
        QuotationProduct expectedQuotationProduct = new QuotationProduct();

        when(quotationProductService.find(id)).thenReturn(Optional.of(expectedQuotationProduct));

        ResponseEntity<QuotationProduct> result = quotationProductController.find(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedQuotationProduct, result.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenIdDoesNotExist() {
        Long id = 1L;

        when(quotationProductService.find(id)).thenReturn(Optional.empty());

        ResponseEntity<QuotationProduct> result = quotationProductController.find(id);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void shouldConvertToPolicySuccessfully() throws Exception {
        Long id = 1L;

        doNothing().when(quotationProductService).convertToPolicy(id);

        ResponseEntity<String> result = quotationProductController.convertToPolicy(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Conversion started successfully", result.getBody());
    }

    @Test
    void shouldHandleExceptionWhenConversionFails() throws Exception {
        Long id = 1L;

        doThrow(new RuntimeException()).when(quotationProductService).convertToPolicy(id);

        assertThrows(Exception.class, () -> quotationProductController.convertToPolicy(id));
    }
}
