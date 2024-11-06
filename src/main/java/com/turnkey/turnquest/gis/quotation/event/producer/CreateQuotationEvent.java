package com.turnkey.turnquest.gis.quotation.event.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turnkey.turnquest.gis.quotation.model.Quotation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class CreateQuotationEvent {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public CreateQuotationEvent(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @SneakyThrows
    public boolean quotationCreated(Quotation quotation) {
        CompletableFuture<SendResult<String, String>> result = kafkaTemplate.send("quotation-created", payloadToString(quotation));
        AtomicBoolean successful = new AtomicBoolean(false);
        result.thenAccept(sendResult -> {
            System.out.println("Message sent successfully!");
            System.out.println("Topic: " + sendResult.getRecordMetadata().topic());
            System.out.println("Partition: " + sendResult.getRecordMetadata().partition());
            System.out.println("Offset: " + sendResult.getRecordMetadata().offset());
            successful.set(true);
        }).exceptionally(throwable -> {
            System.err.println("Error sending Kafka message to quotation-created: " + throwable.getMessage());
            successful.set(false);
            return null;
        });
        return successful.get();
    }

    private String payloadToString(Quotation quotation) throws IOException {
        return new ObjectMapper().writeValueAsString(quotation);
    }
}
