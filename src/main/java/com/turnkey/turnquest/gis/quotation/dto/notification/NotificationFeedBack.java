package com.turnkey.turnquest.gis.quotation.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationFeedBack {

    private boolean success;

    private Throwable cause;

}
