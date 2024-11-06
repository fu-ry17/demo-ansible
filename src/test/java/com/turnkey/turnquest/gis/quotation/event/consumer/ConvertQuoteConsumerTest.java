package com.turnkey.turnquest.gis.quotation.event.consumer;

import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import com.turnkey.turnquest.gis.quotation.exception.error.QuoteConversionException;
import com.turnkey.turnquest.gis.quotation.model.*;
import com.turnkey.turnquest.gis.quotation.service.EndorsementService;
import com.turnkey.turnquest.gis.quotation.service.QuotationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ConvertQuoteConsumerTest {

    @Mock
    private QuotationService quotationService;

    @Mock
    private EndorsementService endorsementService;

    @InjectMocks
    private ConvertQuoteConsumer convertQuoteConsumer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConvertFirstQuotation_AnnualQuote() throws Exception {
        // Arrange
        String payload = "{\"paymentRef\":\"ref123\", \"paymentFrequency\":\"A\", \"policyStatus\":\"NB\"}";
        Quotation quotation = new Quotation();
        QuotationRisk quotationRisk = new QuotationRisk();
        quotationRisk.setId(1L);
        QuotationProduct quotationProduct = new QuotationProduct();
        quotationProduct.setQuotationRisks(List.of(quotationRisk));
        QuoteDocument quoteDocument = new QuoteDocument();
        quoteDocument.setDocument("document");
        quotationRisk.setQuoteDocument(List.of(quoteDocument));
        quotation.setQuotationProducts(List.of(quotationProduct));
        quotation.setId(1L);
        quotation.setOrganizationId(1L);

        when(quotationService.findByPaymentRef(anyString())).thenReturn(Optional.of(quotation));
        when(endorsementService.saveAnnualQuotationRisk(any(Quotation.class), any(YesNo.class))).thenReturn(quotation);

        // Act
        convertQuoteConsumer.convertFirstQuotation(payload);

        // Assert
        verify(quotationService).findByPaymentRef(anyString());
        verify(endorsementService).saveAnnualQuotationRisk(any(Quotation.class), any(YesNo.class));
        verify(quotationService).convertQuotationToPolicies(any(Quotation.class), anyLong());
    }

    @Test
    void testConvertFirstQuotation_InstallmentQuote_FirstInstallment() throws Exception {
        // Arrange
        String payload = "{\"paymentRef\":\"ref123\", \"paymentFrequency\":\"M\", \"policyStatus\":\"NB\", \"installmentNumber\":[1]}";
        Quotation quotation = new Quotation();
        QuotationProduct quotationProduct = new QuotationProduct();
        quotationProduct.setQuotationProductTaxes(List.of(new QuotationProductTax()));
        quotation.setQuotationProducts(List.of(quotationProduct));
        quotation.setId(1L);
        quotation.setOrganizationId(1L);

        when(quotationService.findByPaymentRef(anyString())).thenReturn(Optional.of(quotation));
        when(endorsementService.computeFirstInstallment(any(Quotation.class))).thenReturn(quotation);

        // Act
        convertQuoteConsumer.convertFirstQuotation(payload);

        // Assert
        verify(quotationService).findByPaymentRef(anyString());
        verify(endorsementService).computeFirstInstallment(any(Quotation.class));
        verify(quotationService).saveQuickQuotation(any(Quotation.class), anyLong());
        verify(quotationService).convertQuotationToPolicies(any(Quotation.class), anyLong());
    }

    @Test
    void testConvertFirstQuotation_InstallmentQuote_SubsequentInstallment() throws Exception {
        // Arrange
        String payload = "{\"paymentRef\":\"ref123\", \"paymentFrequency\":\"M\", \"policyStatus\":\"NB\", \"installmentNumber\":[2], \"policyNumber\":\"PN123\"}";
        Quotation quotation = new Quotation();
        quotation.setQuotationProducts(List.of(new QuotationProduct()));
        quotation.setId(1L);
        quotation.setOrganizationId(1L);

        when(quotationService.findByPaymentRef(anyString())).thenReturn(Optional.of(quotation));
        when(endorsementService.getQuotationToPay(anyString())).thenReturn(quotation);
        when(quotationService.saveQuickQuotation(any(Quotation.class), anyLong())).thenReturn(quotation);

        // Act
        convertQuoteConsumer.convertFirstQuotation(payload);

        // Assert
        verify(quotationService).findByPaymentRef(anyString());
        verify(endorsementService).getQuotationToPay(anyString());
        verify(quotationService).saveQuickQuotation(any(Quotation.class), anyLong());
        verify(quotationService).convertQuotationToPolicies(any(Quotation.class), anyLong());
    }


    @Test
    void testConvertFirstQuotation_QuotationNotFound() {
        // Arrange
        String payload = "{\"paymentRef\":\"ref123\"}";
        when(quotationService.findByPaymentRef(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(QuoteConversionException.class, () -> convertQuoteConsumer.convertFirstQuotation(payload));
        verify(quotationService).findByPaymentRef(anyString());
        verifyNoMoreInteractions(quotationService);
        verifyNoInteractions(endorsementService);
    }
}
