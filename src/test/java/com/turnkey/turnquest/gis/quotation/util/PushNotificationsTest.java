//package com.turnkey.turnquest.gis.quotation.util;
//
//import com.turnkey.turnquest.gis.quotation.client.notification.PushNotificationClient;
//import com.turnkey.turnquest.gis.quotation.dto.notification.PushNotificationDto;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
//public class PushNotificationsTest {
//
//    private PushNotificationClient pushNotificationClient;
//    private PushNotifications pushNotifications;
//
//    @BeforeEach
//    public void setup() {
//        pushNotificationClient = Mockito.mock(PushNotificationClient.class);
//        pushNotifications = new PushNotifications(pushNotificationClient);
//    }
//
//    @Test
//    public void testSendPushNotification() {
//        PushNotificationDto pushNotificationDto = new PushNotificationDto();
//        Long organizationId = 1L;
//
//        pushNotifications.sendPushNotification(pushNotificationDto, organizationId);
//
//        verify(pushNotificationClient, times(1)).sendPushNotification(any(PushNotificationDto.class), any(Long.class));
//    }
//
//}
