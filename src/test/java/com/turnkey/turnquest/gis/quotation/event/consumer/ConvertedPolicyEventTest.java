package com.turnkey.turnquest.gis.quotation.event.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turnkey.turnquest.gis.quotation.dto.Reports.QuotationReportDto;
import com.turnkey.turnquest.gis.quotation.dto.notification.PushNotificationDto;
import com.turnkey.turnquest.gis.quotation.dto.quotation.ConvertedPolicyDto;
import com.turnkey.turnquest.gis.quotation.event.producer.NotificationProducer;
import com.turnkey.turnquest.gis.quotation.event.producer.ReportProducer;
import com.turnkey.turnquest.gis.quotation.exception.error.StringDeserializationException;
import com.turnkey.turnquest.gis.quotation.model.Quotation;
import com.turnkey.turnquest.gis.quotation.service.QuotationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.ctc.wstx.shaded.msv_core.grammar.Expression.anyString;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


public class ConvertedPolicyEventTest {

    @Mock
    private QuotationService quotationService;

    @Mock
    private NotificationProducer notificationProducer;

    @Mock
    private ReportProducer<QuotationReportDto> valuationReportProducer;

    @Mock
    private ReportProducer<QuotationReportDto> quotationSummaryProducer;

    @InjectMocks
    private ConvertedPolicyEvent convertedPolicyEvent;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConsumeConvertedPolicy_QuotationFound() throws Exception {
        // Arrange
        String payload = "{\"quotationId\": 1, \"policyNumber\": \"PN123\", \"policyId\": 2}";
        Quotation quotation = new Quotation();
        quotation.setId(1L);
        quotation.setStatus("NB");
        quotation.setOrganizationId(1L);

        when(quotationService.findById(anyLong())).thenReturn(Optional.of(quotation));
        when(quotationService.save(any(Quotation.class))).thenReturn(quotation);
        doNothing().when(quotationSummaryProducer).queueReportWithNonScalarPayload(any(QuotationReportDto.class), anyString());
        doNothing().when(notificationProducer).queuePushNotification(any(PushNotificationDto.class));
        doNothing().when(valuationReportProducer).queueReportWithNonScalarPayload(any(QuotationReportDto.class), anyString());


        // Act
        convertedPolicyEvent.consumeConvertedPolicy(payload);

        // Assert
        verify(quotationService, times(1)).findById(anyLong());
        verify(quotationService, times(1)).save(any(Quotation.class));
    }

    @Test
    void testConsumeConvertedPolicy_QuotationNotFound() throws Exception {
        // Arrange
        String payload = "{\"quotationId\": 1, \"policyNumber\": \"PN123\", \"policyId\": 2}";

        when(quotationService.findById(1L)).thenReturn(Optional.empty());

        // Act
        convertedPolicyEvent.consumeConvertedPolicy(payload);

        // Assert
        verify(quotationService).findById(1L);
        verifyNoMoreInteractions(quotationService, notificationProducer, quotationSummaryProducer, valuationReportProducer);
    }

    @Test
    void testStringToPayload_StringDeserializationException() {
        // Arrange
        String invalidPayload = "invalid json";

        // Act & Assert
        assertThrows(StringDeserializationException.class, () -> convertedPolicyEvent.consumeConvertedPolicy(invalidPayload));
    }
}

