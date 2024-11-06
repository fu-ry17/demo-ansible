package com.turnkey.turnquest.gis.quotation.util;

import com.turnkey.turnquest.gis.quotation.client.notification.PushNotificationClient;
import com.turnkey.turnquest.gis.quotation.dto.notification.PushNotificationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PushNotifications {

    private final PushNotificationClient pushNotificationClient;

    Logger log = LoggerFactory.getLogger(PushNotifications.class);

    public PushNotifications(PushNotificationClient pushNotificationClient) {
        this.pushNotificationClient = pushNotificationClient;
    }

    @Deprecated(forRemoval = true)
    public void sendPushNotification(PushNotificationDto pushNotificationDto, Long organizationId) {
        pushNotificationClient.sendPushNotification(pushNotificationDto, organizationId);
        log.info("========================Quotation converted successfully==========================");
    }

}
