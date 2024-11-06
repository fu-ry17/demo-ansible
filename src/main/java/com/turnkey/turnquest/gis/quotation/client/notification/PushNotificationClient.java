package com.turnkey.turnquest.gis.quotation.client.notification;

import com.turnkey.turnquest.gis.quotation.dto.notification.PushNotificationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("notifications-service")
public interface PushNotificationClient {

    @RequestMapping(method = RequestMethod.POST, value = "/push-notifications/send-push-notification/{organizationId}")
    String sendPushNotification(@RequestBody PushNotificationDto message, @PathVariable Long organizationId);


}
