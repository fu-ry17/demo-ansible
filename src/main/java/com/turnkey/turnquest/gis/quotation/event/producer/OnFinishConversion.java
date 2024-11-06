package com.turnkey.turnquest.gis.quotation.event.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turnkey.turnquest.gis.quotation.dto.quotation.ConvertedPolicyDto;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OnFinishConversion {

    private final KafkaTemplate<String, String> kafkaTemplate;

    Logger log = LoggerFactory.getLogger(OnFinishConversion.class);


    public OnFinishConversion(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

//    public void queueConvertedPolicy(ConvertedPolicyDto convertedPolicy){
//
//        kafkaTemplate.send("finished-conversion", payloadToString(convertedPolicy));
//        log.info("Quotation queued for processing...");
//    }

    @SneakyThrows
    private String payloadToString(ConvertedPolicyDto convertedPolicy) {
        return new ObjectMapper().writeValueAsString(convertedPolicy);
    }
}
