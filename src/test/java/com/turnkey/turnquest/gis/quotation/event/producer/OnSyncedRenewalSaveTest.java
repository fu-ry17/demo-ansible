package com.turnkey.turnquest.gis.quotation.event.producer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Titus Murithi Bundi
 */
class OnSyncedRenewalSaveTest {

    private OnSyncedRenewalSave onSyncedRenewalSave;
    private KafkaTemplate<String, String> mockKafkaTemplate;

    @BeforeEach
    void setUp() {
        mockKafkaTemplate = mock(KafkaTemplate.class);
        onSyncedRenewalSave = new OnSyncedRenewalSave(mockKafkaTemplate);
    }

    @Test
    void shouldPublishRenewal() {
        Long renewalBatchNo = 123L;
        onSyncedRenewalSave.publishRenewal(renewalBatchNo);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockKafkaTemplate, times(1)).send(eq("renewal-onboarding"), captor.capture());
        assertEquals(renewalBatchNo.toString(), captor.getValue());
    }

}
