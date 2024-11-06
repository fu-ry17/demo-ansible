package com.turnkey.turnquest.gis.quotation.event.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turnkey.turnquest.gis.quotation.dto.underwriting.RiskCertUpdateDto;
import com.turnkey.turnquest.gis.quotation.service.QuotationRiskService;
import com.turnkey.turnquest.gis.quotation.service.QuotationService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class UpdateQuoteRiskCertNo {

    private final QuotationService quotationService;

    private final QuotationRiskService quotationRiskService;

    public UpdateQuoteRiskCertNo(QuotationService quotationService, QuotationRiskService quotationRiskService) {
        this.quotationService = quotationService;
        this.quotationRiskService = quotationRiskService;
    }


    @KafkaListener(topics = "update_quotation_risk")
    void updateQuotationRiskCertNo(@Payload String payload) {
        var riskCertUpdateDto = stringToPayload(payload);
        try {
            quotationService.findById(riskCertUpdateDto.getQuotationId()).ifPresent(quotation -> {
                log.info("Quotation id to update {} with payload {}", quotation.getId(), payload);
                quotation.getQuotationProducts().stream().flatMap(quotationProduct -> quotationProduct.getQuotationRisks().stream())
                        .filter(qr -> Objects.equals(qr.getRiskId(), riskCertUpdateDto.getRegistrationNo()))
                        .findFirst().ifPresent(riskToUpdate -> quotationRiskService.updateCertificateNo(riskCertUpdateDto.getCertificateNo(), riskToUpdate.getId()));
            });
        } catch (Exception e) {
            log.error("Error updating quotation risk certificate number", e);
        }

    }

    @SneakyThrows
    private RiskCertUpdateDto stringToPayload(String payload) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(payload, RiskCertUpdateDto.class);
    }
}
