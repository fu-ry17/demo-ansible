package com.turnkey.turnquest.gis.quotation.event.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OnSyncedRenewalSave {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OnSyncedRenewalSave(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishRenewal(Long renewalBatchNo) {
        log.debug("Renewal onboarding: publishing onboarded renewal {}", renewalBatchNo);
        kafkaTemplate.send("renewal-onboarding", renewalBatchNo.toString());
    }
}
