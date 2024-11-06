package com.turnkey.turnquest.gis.quotation.dto.notification;

import lombok.Data;


@Data
public class NotificationRecipientDto {

    private String phoneNumber;

    private String emailAddress;

    private String name;
}
