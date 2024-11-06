package com.turnkey.turnquest.gis.quotation.event.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turnkey.turnquest.gis.quotation.dto.notification.PushNotificationDto;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationProducer {

    private final KafkaTemplate<String,String> kafkaTemplate;


    public NotificationProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void queuePushNotification(PushNotificationDto pushNotificationDto){
        kafkaTemplate.send("push-notification",payloadToString(pushNotificationDto));
    }

    @SneakyThrows
    private String payloadToString(PushNotificationDto pushNotificationDto){
        return new ObjectMapper().writeValueAsString(pushNotificationDto);
    }
}
