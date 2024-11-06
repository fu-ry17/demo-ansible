package com.turnkey.turnquest.gis.quotation.event.consumer;

import com.turnkey.turnquest.gis.quotation.event.producer.OnSyncedRenewalSave;
import com.turnkey.turnquest.gis.quotation.model.Quotation;
import com.turnkey.turnquest.gis.quotation.service.QuotationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RenewalConsumerSinkTest {

    @Mock
    private QuotationService quotationService;

    @Mock
    private OnSyncedRenewalSave onSyncedRenewalSave;

    @InjectMocks
    private RenewalConsumerSink renewalConsumerSink;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void shouldThrowExceptionWhenPayloadIsInvalid() {
        String invalidPayload = "invalid";

        assertThrows(com.fasterxml.jackson.core.JsonProcessingException.class, () -> renewalConsumerSink.renewalConsumer(invalidPayload));
    }
}