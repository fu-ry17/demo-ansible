package com.turnkey.turnquest.gis.quotation.event.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turnkey.turnquest.gis.quotation.model.Quotation;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AllocatePaymentsEvent {
    private final KafkaTemplate<String, String> kafkaTemplate;


    public AllocatePaymentsEvent(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void triggerPaymentsAllocation(Quotation quotation) throws JsonProcessingException {
        String payload = payloadToString(quotation);
        kafkaTemplate.send("allocate-payments", payload);

    }

    private String payloadToString(Quotation quotation) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(quotation);
    }
}
