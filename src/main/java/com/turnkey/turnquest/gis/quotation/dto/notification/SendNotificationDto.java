package com.turnkey.turnquest.gis.quotation.dto.notification;

import lombok.Data;

import java.util.Map;


@Data
public class SendNotificationDto {

    private String message;

    private String subject;

    private String channels;

    private NotificationRecipientDto notificationRecipient;

    private String delivery = "now";

    private Boolean hasAttachment = false;

    private String attachmentName = "";

    private String attachment;

    private Map<String,String> attributes;

    private String templateShortCode;

    private Long organizationId;

}
