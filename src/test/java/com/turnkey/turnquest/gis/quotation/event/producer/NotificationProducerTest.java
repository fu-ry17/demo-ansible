package com.turnkey.turnquest.gis.quotation.event.producer;

import com.turnkey.turnquest.gis.quotation.dto.notification.PushNotificationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Titus Murithi Bundi
 */
class NotificationProducerTest {

    private NotificationProducer notificationProducer;
    private KafkaTemplate<String, String> mockKafkaTemplate;

    @BeforeEach
    void setUp() {
        mockKafkaTemplate = mock(KafkaTemplate.class);
        notificationProducer = new NotificationProducer(mockKafkaTemplate);
    }

    @Test
    void shouldQueuePushNotification() {
        PushNotificationDto pushNotificationDto = new PushNotificationDto();
        pushNotificationDto.setTemplateCode("templateCode");
        pushNotificationDto.setId("id");
        pushNotificationDto.setOrganizationId(123L);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("key", "value");
        pushNotificationDto.setAttributes(attributes);

        notificationProducer.queuePushNotification(pushNotificationDto);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockKafkaTemplate, times(1)).send(eq("push-notification"), captor.capture());
    }
}
