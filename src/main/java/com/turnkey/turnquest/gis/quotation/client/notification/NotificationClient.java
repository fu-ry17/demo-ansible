package com.turnkey.turnquest.gis.quotation.client.notification;

import com.turnkey.turnquest.gis.quotation.dto.notification.SendNotificationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("notifications-service")
public interface NotificationClient {
    @PostMapping("/notifications/send")
    Object initiate(@RequestBody SendNotificationDto sendNotificationDto);
}
