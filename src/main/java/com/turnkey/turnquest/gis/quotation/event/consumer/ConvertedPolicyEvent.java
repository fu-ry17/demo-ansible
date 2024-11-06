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
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConvertedPolicyEvent {

    private final QuotationService quotationService;
    private final NotificationProducer notificationProducer;
    private final ReportProducer<QuotationReportDto> valuationReportProducer;
    private final ReportProducer<QuotationReportDto> quotationSummaryProducer;


    @SneakyThrows
    @KafkaListener(topics = "underwritting.complete-conversion-policy.global")
    public void consumeConvertedPolicy(@Payload String payload) {
        log.info("Original quote for valuation {}", payload);
        var quote = quotationService.findById(stringToPayload(payload).getQuotationId());
        quote.ifPresent(quotation -> {
            quotation.setCurrentStatus("A");
            quotation.setPolicyNo(stringToPayload(payload).getPolicyNumber());
            quotation.setPolicyId(stringToPayload(payload).getPolicyId());
            if (quotation.getStatus().equals("RN")) {
                quotation.setStatus("RN-ARCHIVED");
            }

            try {
                log.info("Valuation quote saved === {}", new ObjectMapper().writeValueAsString(quotation));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            quotationService.save(quotation);


            conversionCompleteAlert(quotation);
            sendReports(quotation.getStatus(), quotation);

        });


    }

    @NotNull
    private Quotation updatePreviousQuote(Quotation convertedQuote) {
        var quotation = quotationService.findById(convertedQuote.getId()).orElse(null);
        if (quotation != null) {
            quotation.setPolicyId(convertedQuote.getPolicyId());
            quotation.setPolicyNo(convertedQuote.getPolicyNo());
            convertedQuote.setCurrentStatus("A");
            if (quotation.getStatus().equals("RN")) {
                quotation.setStatus("RN-ARCHIVED");
            }
            return quotation;
        }

        return convertedQuote;
    }

    private void sendReports(String policyStatus, Quotation quotation) {
        if (!policyStatus.equals("EN") && !policyStatus.equals("ME")) {

            QuotationReportDto quotationReportDto = new QuotationReportDto();
            quotationReportDto.setPolicyBatchNo(-2L);
            quotationReportDto.setQuotationId(quotation.getId());

            try {
                //Queue quotation summary for generation
                quotationSummaryProducer.queueReportWithNonScalarPayload(quotationReportDto, "summary-generation");
            } catch (Exception ex) {
                log.debug("Quotation summary report error {}", ex.getMessage());
            }

            try {
                //Queue quotation for valuation report generation
                valuationReportProducer.queueReportWithNonScalarPayload(quotationReportDto, "valuation-generation");
            } catch (Exception ex) {
                log.debug("Valuation report error {}", ex.getMessage());
            }
        }
    }

    private void conversionCompleteAlert(Quotation quotation) {
        PushNotificationDto pushNotificationDto = new PushNotificationDto();
        pushNotificationDto.setOrganizationId(quotation.getOrganizationId());
        pushNotificationDto.setId(quotation.getPolicyId().toString());
        pushNotificationDto.setTemplateCode("QUOT_CONVERSION_COMPLETE");
        notificationProducer.queuePushNotification(pushNotificationDto);
    }

    @SneakyThrows
    private ConvertedPolicyDto stringToPayload(String payload) {
        try {
            return new ObjectMapper().readValue(payload, ConvertedPolicyDto.class);
        } catch (JsonProcessingException e) {
            throw new StringDeserializationException(e.getMessage());
        }
    }
}
