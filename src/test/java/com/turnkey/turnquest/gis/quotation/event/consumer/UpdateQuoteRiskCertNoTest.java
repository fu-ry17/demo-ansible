package com.turnkey.turnquest.gis.quotation.event.consumer;

import com.turnkey.turnquest.gis.quotation.model.Quotation;
import com.turnkey.turnquest.gis.quotation.model.QuotationProduct;
import com.turnkey.turnquest.gis.quotation.model.QuotationRisk;
import com.turnkey.turnquest.gis.quotation.service.QuotationRiskService;
import com.turnkey.turnquest.gis.quotation.service.QuotationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

/**
 * @author Titus Murithi Bundi
 */
class UpdateQuoteRiskCertNoTest {


    @Mock
    private QuotationService quotationService;

    @Mock
    private QuotationRiskService quotationRiskService;

    @InjectMocks
    private UpdateQuoteRiskCertNo updateQuoteRiskCertNo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateQuotationRiskCertNoProcessesValidPayload() {
        String validPayload = "{\"quotationId\":1,\"registrationNo\":\"1\",\"certificateNo\":\"certNo\"}";

        QuotationProduct quotationProduct = new QuotationProduct();
        QuotationRisk quotationRisk = new QuotationRisk();
        quotationRisk.setRiskId("1");
        quotationRisk.setId(1L); // Set an id for the QuotationRisk object
        quotationProduct.setQuotationRisks(Collections.singletonList(quotationRisk));

        Quotation quotation = new Quotation();
        quotation.setQuotationProducts(Collections.singletonList(quotationProduct));
        when(quotationService.findById(anyLong())).thenReturn(Optional.of(quotation));

        updateQuoteRiskCertNo.updateQuotationRiskCertNo(validPayload);

        verify(quotationRiskService, times(1)).updateCertificateNo(anyString(), anyLong());
    }

    @Test
    void updateQuotationRiskCertNoIgnoresInvalidPayload() {
        String invalidPayload = "invalid";

        assertThrows(com.fasterxml.jackson.core.JsonParseException.class, () -> updateQuoteRiskCertNo.updateQuotationRiskCertNo(invalidPayload));

        verify(quotationRiskService, times(0)).updateCertificateNo(anyString(), anyLong());
    }

    @Test
    void updateQuotationRiskCertNoIgnoresNonexistentQuotation() {
        String validPayload = "{\"quotationId\":1,\"registrationNo\":1,\"certificateNo\":\"certNo\"}";

        when(quotationService.findById(anyLong())).thenReturn(Optional.empty());

        updateQuoteRiskCertNo.updateQuotationRiskCertNo(validPayload);

        verify(quotationRiskService, times(0)).updateCertificateNo(anyString(), anyLong());
    }
}
