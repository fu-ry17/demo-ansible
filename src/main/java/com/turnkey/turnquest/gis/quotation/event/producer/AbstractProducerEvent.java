package com.turnkey.turnquest.gis.quotation.event.producer;


import com.turnkey.turnquest.gis.quotation.exception.error.JsonProcessingRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public abstract class AbstractProducerEvent {


    protected void handleKafkaResponse(CompletableFuture<SendResult<String, String>> response) {
        response.thenAccept(sendResult -> {
            log.info("Message sent successfully!");
            log.info("Topic: " + sendResult.getRecordMetadata().topic());
            log.info("Partition: " + sendResult.getRecordMetadata().partition());
            log.info("Offset: " + sendResult.getRecordMetadata().offset());
        }).exceptionally(throwable -> {
            log.error("An error occurred while sending the message ERROR: {}", throwable.getMessage());
            throw new JsonProcessingRuntimeException("An error occurred while sending the message", throwable);
        });
    }
}
