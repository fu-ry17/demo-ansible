package com.turnkey.turnquest.gis.quotation.event.producer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyDto;
import com.turnkey.turnquest.gis.quotation.exception.error.QuoteConversionException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class ConvertQuoteEvent {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public ConvertQuoteEvent(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void convertQuotationProductToPolicy(PolicyDto policy) {
        var response = kafkaTemplate.send("create-conversion-policy", payloadToString(policy));
        response.thenAccept(sendResult -> {
            System.out.println("Message sent successfully!");
            System.out.println("Topic: " + sendResult.getRecordMetadata().topic());
            System.out.println("Partition: " + sendResult.getRecordMetadata().partition());
            System.out.println("Offset: " + sendResult.getRecordMetadata().offset());
        }).exceptionally(throwable -> {
            log.error("An error occurred while sending convert policy. ERROR: {}", throwable.getMessage());
            throw new QuoteConversionException("An error occurred while sending convert policy",throwable);
        });
    }


    @SneakyThrows
    private String payloadToString(PolicyDto policyDto) {
        var mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.writeValueAsString(policyDto);
    }
}
