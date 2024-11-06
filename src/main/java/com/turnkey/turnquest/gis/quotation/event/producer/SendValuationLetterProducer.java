package com.turnkey.turnquest.gis.quotation.event.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turnkey.turnquest.gis.quotation.dto.novunotification.BulkTriggerEventDto;
import com.turnkey.turnquest.gis.quotation.exception.error.JsonProcessingRuntimeException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Titus Murithi Bundi
 */
@Component
public class SendValuationLetterProducer extends AbstractProducerEvent {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public SendValuationLetterProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendValuationLetterToSupport(BulkTriggerEventDto bulkTriggerEventDto) {
        var response = kafkaTemplate.send("global.sendpaymentreceipt.notification", triggerEventNotificationStringtoPaylod(bulkTriggerEventDto));
        handleKafkaResponse(response);
    }

    private String triggerEventNotificationStringtoPaylod(BulkTriggerEventDto bulkTriggerEventDto) {
        try {
            return new ObjectMapper().writeValueAsString(bulkTriggerEventDto);
        } catch (JsonProcessingException e) {
            throw new JsonProcessingRuntimeException("Error occurred while processing JSON", e);
        }
    }

}
