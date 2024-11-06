package com.turnkey.turnquest.gis.quotation.event.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
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
class ReportProducerTest {

    private ReportProducer<String> reportProducer;
    private KafkaTemplate<String, String> mockKafkaTemplate;

    @BeforeEach
    void setUp() {
        mockKafkaTemplate = mock(KafkaTemplate.class);
        reportProducer = new ReportProducer<>(mockKafkaTemplate);
    }

    @Test
    void shouldQueueReportWithNonScalarPayload() throws Exception {
        String reportPayload = "testPayload";
        String topic = "testTopic";

        reportProducer.queueReportWithNonScalarPayload(reportPayload, topic);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockKafkaTemplate, times(1)).send(eq(topic), captor.capture());
        assertEquals(new ObjectMapper().writeValueAsString(reportPayload), captor.getValue());
    }

    @Test
    void shouldQueueReportWithScalarPayload() {
        String reportPayload = "testPayload";
        String topic = "testTopic";

        reportProducer.queueReportWithScalarPayload(reportPayload, topic);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockKafkaTemplate, times(1)).send(eq(topic), captor.capture());
        assertEquals(reportPayload, captor.getValue());
    }

}
