package com.turnkey.turnquest.gis.quotation.event.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turnkey.turnquest.gis.quotation.model.Quotation;
import com.turnkey.turnquest.gis.quotation.service.QuotationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

public class SaveQuoteConsumerTest {

    @Mock
    private QuotationService quotationService;

    @InjectMocks
    private SaveQuoteConsumer saveQuoteConsumer;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void shouldSaveQuotationWhenBasicPremiumIsZero() throws JsonProcessingException {

        Quotation quote = new Quotation();
        quote.setBasicPremium(BigDecimal.ZERO);
        String payload = "{\"organizationId\": 1, \"basicPremium\": 0}";


        when(quotationService.saveQuickQuotation(any(Quotation.class), anyLong()))
                .thenReturn(quote);
        doNothing().when(quotationService).convertQuotationToPolicies(any(Quotation.class), anyLong());


        saveQuoteConsumer.saveQuotation(payload);

        verify(quotationService, times(1)).saveQuickQuotation(any(Quotation.class), anyLong());
        verify(quotationService, times(1)).convertQuotationToPolicies(quote, quote.getOrganizationId());
    }

    @Test
    void shouldSaveQuotationWhenBasicPremiumIsNotZero() throws JsonProcessingException {

        Quotation quote = new Quotation();
        quote.setBasicPremium(BigDecimal.ZERO);
        String payload = "{\"organizationId\": 1, \"basicPremium\": 100}";

        when(quotationService.saveQuickQuotation(any(Quotation.class), anyLong()))
                .thenReturn(quote);

        saveQuoteConsumer.saveQuotation(payload);

        verify(quotationService, times(1)).saveQuickQuotation(any(Quotation.class), anyLong());
        verify(quotationService, times(0)).convertQuotationToPolicies(any(Quotation.class), anyLong());
    }
}
