package com.turnkey.turnquest.gis.quotation.event.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CloseTodoEvent {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public CloseTodoEvent(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void closeTodo(Long dealId) {
        kafkaTemplate.send("close-deal-todo", dealId.toString());
        log.info("Todo deal closed");
    }
}
