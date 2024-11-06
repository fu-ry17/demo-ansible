package com.turnkey.turnquest.gis.quotation.event.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turnkey.turnquest.gis.quotation.model.Quotation;
import com.turnkey.turnquest.gis.quotation.service.QuotationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Slf4j
@Component
public class SaveQuoteConsumer {
    private final QuotationService quotationService;

    public SaveQuoteConsumer(QuotationService quotationService) {
        this.quotationService = quotationService;
    }

    @KafkaListener(topics = "endorsement.createenquote.quotation")
    public void saveQuotation(@Payload String payload) throws JsonProcessingException {
        log.info("Quoate after valuation {}", payload);
        Quotation quotation = stringToPayload(payload);

        var quote = quotationService.saveQuickQuotation(quotation, quotation.getOrganizationId());
        log.info("Quote basic premium {}", quote.getBasicPremium().setScale(0, RoundingMode.HALF_UP));
        log.info("Quote big decimal zero {}", BigDecimal.ZERO.setScale(0, RoundingMode.HALF_UP));
        if(quote.getBasicPremium().setScale(0, RoundingMode.HALF_UP).equals(BigDecimal.ZERO.setScale(0, RoundingMode.HALF_UP))) {
            quotationService.convertQuotationToPolicies(quote, quote.getOrganizationId());
        }
    }

    private Quotation stringToPayload(String payload) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(payload, Quotation.class);
    }


}
