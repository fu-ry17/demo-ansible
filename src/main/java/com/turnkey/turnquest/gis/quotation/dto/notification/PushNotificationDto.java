package com.turnkey.turnquest.gis.quotation.dto.notification;

import lombok.Data;

import java.util.Map;

@Data
public class PushNotificationDto {

    String templateCode;

    String id;

    Long organizationId;

    Map<String,Object> attributes;
}
