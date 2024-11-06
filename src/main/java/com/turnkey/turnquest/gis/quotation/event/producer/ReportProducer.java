package com.turnkey.turnquest.gis.quotation.event.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Generates reports
 *
 * @param <T> the pay load needed to generate the report
 */

@Component
public class ReportProducer<T> {

    private final Logger logger = LoggerFactory.getLogger(ReportProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    public ReportProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Queues report for generation
     *
     * @param reportPayload payload used to generate the report
     * @throws IOException
     */

    public void queueReportWithNonScalarPayload(T reportPayload, String topic) throws IOException {
        String payload = payloadToString(reportPayload);
        logger.info("Queueing report generation for {}", payload);
        kafkaTemplate.send(topic, payload);
    }

    /**
     * Queues report for generation
     *
     * @param reportPayload scalar payload used to generate the report
     * @throws IOException
     */

    public void queueReportWithScalarPayload(T reportPayload, String topic) {
        logger.info("Queueing report generation for {}", reportPayload);
        kafkaTemplate.send(topic, reportPayload.toString());
    }

    private String payloadToString(T reportPayload) throws IOException {
        return new ObjectMapper().writeValueAsString(reportPayload);
    }

}
