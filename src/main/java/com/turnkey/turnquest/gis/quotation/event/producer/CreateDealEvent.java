package com.turnkey.turnquest.gis.quotation.event.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turnkey.turnquest.gis.quotation.exception.error.DealException;
import com.turnkey.turnquest.gis.quotation.model.Deal;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class CreateDealEvent {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public CreateDealEvent(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @SneakyThrows
    public boolean dealCreated(Deal deal) {
        var response = kafkaTemplate.send("deal-created", payloadToString(deal));
        AtomicBoolean successful = new AtomicBoolean(false);
        response.thenAccept(sendResult -> {
            System.out.println("Message sent successfully!");
            System.out.println("Topic: " + sendResult.getRecordMetadata().topic());
            System.out.println("Partition: " + sendResult.getRecordMetadata().partition());
            System.out.println("Offset: " + sendResult.getRecordMetadata().offset());
            successful.set(true);
        }).exceptionally(throwable -> {
            log.error("An error occurred while creating a deal event {}", throwable.getMessage());
            throw new DealException("An error occurred while creating a deal event.", throwable);
        });
        return successful.get();
    }

    private String payloadToString(Deal deal) throws IOException {
        return new ObjectMapper().writeValueAsString(deal);
    }
}
