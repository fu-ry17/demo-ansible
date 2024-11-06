package com.turnkey.turnquest.gis.quotation.event.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turnkey.turnquest.gis.quotation.dto.billing.ConversionDto;
import com.turnkey.turnquest.gis.quotation.dto.novunotification.BulkTriggerEventDto;
import com.turnkey.turnquest.gis.quotation.dto.novunotification.Topic;
import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import com.turnkey.turnquest.gis.quotation.event.producer.SendValuationLetterProducer;
import com.turnkey.turnquest.gis.quotation.exception.error.QuoteConversionException;
import com.turnkey.turnquest.gis.quotation.exception.error.StringDeserializationException;
import com.turnkey.turnquest.gis.quotation.model.Quotation;
import com.turnkey.turnquest.gis.quotation.model.QuotationProductTax;
import com.turnkey.turnquest.gis.quotation.model.QuotationRisk;
import com.turnkey.turnquest.gis.quotation.model.QuoteDocument;
import com.turnkey.turnquest.gis.quotation.service.EndorsementService;
import com.turnkey.turnquest.gis.quotation.service.QuotationService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.util.*;


@Slf4j
@Component
public class ConvertQuoteConsumer {
    private final QuotationService quotationService;
    private final EndorsementService endorsementService;
    private final SendValuationLetterProducer sendValuationLetterProducer;

    public ConvertQuoteConsumer(QuotationService quotationService, EndorsementService endorsementService, SendValuationLetterProducer sendValuationLetterProducer) {
        this.quotationService = quotationService;
        this.endorsementService = endorsementService;
        this.sendValuationLetterProducer = sendValuationLetterProducer;
    }

    @RetryableTopic(backoff = @Backoff(delay = 3000, multiplier = 1.5, maxDelay = 15000), attempts = "4")

    @KafkaListener(topics = "billing.enconversion.quotation")
    public void convertFirstQuotation(@Payload String payload) {
        log.info("EN conversion Payload:{}", payload);
        ConversionDto conversionDto = stringToConversionDto(payload);
        var quote = quotationService.findByPaymentRef(conversionDto.paymentRef().trim());

        if (quote.isEmpty()) {
            throw new QuoteConversionException("Quotation not found", null);
        }
        quote.ifPresent(quotation -> {
            if (conversionDto.paymentFrequency().equals("A") && (Objects.equals(conversionDto.policyStatus(), "NB") || Objects.equals(conversionDto.policyStatus(), "RN"))) {
                var annualQuote = endorsementService.saveAnnualQuotationRisk(quotation, YesNo.Y);
                annualQuote.getQuotationProducts().forEach(qp -> qp.setQuotationId(quotation.getId()));
                log.info("Annual quote {}", annualQuote);

                updateDates(quotation, annualQuote);
                quotationService.convertQuotationToPolicies(quotation, quotation.getOrganizationId());
                sendValuationDocumentToSupport(quotation);
            } else if ((conversionDto.installmentNumber().size() == 1) && (conversionDto.installmentNumber().getFirst() == 1L) && !conversionDto.paymentFrequency().equals("A")) {
                var installmentQuote = endorsementService.computeFirstInstallment(quotation);
                installmentQuote.setPaymentRef(conversionDto.installmentRef());
                installmentQuote.setStatus(conversionDto.policyStatus());
                addProductTaxes(quotation, installmentQuote); // Manually add product taxes

                try {
                    log.info("InstallmentQuote after tax update:{}", new ObjectMapper().writer().writeValueAsString(installmentQuote));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                quotationService.saveQuickQuotation(installmentQuote, installmentQuote.getOrganizationId());
                quotationService.convertQuotationToPolicies(installmentQuote, installmentQuote.getOrganizationId());
            } else {
                var installmentQuote = endorsementService.getQuotationToPay(conversionDto.policyNumber());
                log.info("valuation payment InstallmentQuote:{}", installmentQuote);
                var savedQuote = quotationService.saveQuickQuotation(installmentQuote, installmentQuote.getOrganizationId());
                quotationService.convertQuotationToPolicies(savedQuote, savedQuote.getOrganizationId());
            }
        });
    }

    @SneakyThrows
    private void addProductTaxes(Quotation quote, Quotation installmentQuote) {
        var productTaxes = quote.getQuotationProducts().stream()
                .flatMap(it -> it.getQuotationProductTaxes().stream())
                .filter(it -> Objects.equals(it.getApplicationArea(), "P"))
                .toList();

        log.info("Policy taxes {}", new ObjectMapper().writeValueAsString(productTaxes));
        installmentQuote.getQuotationProducts().forEach(qp -> {
            var productSubClassId = qp.getQuotationProductTaxes().getFirst().getProductSubClassId();
            var taxes = productTaxes.stream()
                    .filter(it -> Objects.equals(it.getProductSubClassId(), productSubClassId))
                    .peek(it -> {
                                it.setId(null);
                                it.setQuotationProductId(qp.getId());
                            }
                    )
                    .toList();
            try {
                log.info("Policy taxes {}", new ObjectMapper().writeValueAsString(taxes));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            List<QuotationProductTax> mutableTaxes = new ArrayList<>(qp.getQuotationProductTaxes());
            mutableTaxes.addAll(taxes);
            qp.setQuotationProductTaxes(mutableTaxes); // assuming there's a setter method
        });
        log.info("Installment quote after tax {}", new ObjectMapper().writeValueAsString(installmentQuote));
    }

    private ConversionDto stringToConversionDto(String payload) {
        try {
            return new ObjectMapper().readValue(payload, ConversionDto.class);
        } catch (JsonProcessingException e) {
            throw new StringDeserializationException(e.getMessage());
        }
    }


    private void updateDates(Quotation quotation, Quotation annualQuote) {
        quotation.getQuotationProducts().forEach(qp -> {
            qp.setWithEffectFromDate(annualQuote.getCoverFromDate());
            qp.setWithEffectToDate(annualQuote.getCoverToDate());
            qp.getQuotationRisks().forEach(qpr -> {
                qpr.setWithEffectFromDate(annualQuote.getCoverFromDate());
                qpr.setWithEffectToDate(annualQuote.getCoverToDate());
            });
        });

        // Update the quotation dates
        quotation.setCoverFromDate(annualQuote.getCoverFromDate());
        quotation.setCoverToDate(annualQuote.getCoverToDate());

        log.info("Quotation dates updated:{}", quotation);

    }


    //Method to check the status of the valuation and send the notification
    private void sendValuationDocumentToSupport(Quotation quotation) {
        quotation.getQuotationProducts().forEach(qp ->
                qp.getQuotationRisks().forEach(qpr ->
                        qpr.getQuoteDocument().forEach(qd -> {
                            if (qd.getIsValuationLetter().equals(YesNo.Y)) {
                                composeBulkTriggerEventDto(qpr, qd);
                            }
                        })
                )
        );
    }

    private void composeBulkTriggerEventDto(QuotationRisk quotationRisk, QuoteDocument quoteDocument) {
        BulkTriggerEventDto bulkTriggerEventDto = new BulkTriggerEventDto();
        bulkTriggerEventDto.setWorkflowId("send-valuation-letter");

        List<Topic> topics = new ArrayList<>();
        Topic topic = new Topic("Topic", "DeleteAccount");
        topics.add(topic);

        bulkTriggerEventDto.setTo(topics);

        Map<String, String> payload = new HashMap<>();
        payload.put("riskId", quotationRisk.getRiskId());

        log.info("Risk Id: {}", quotationRisk.getRiskId());
        payload.put("DocumentUrl", quoteDocument.getDocument());

        bulkTriggerEventDto.setPayload(payload);

        log.info("Valuation letter sent to support, {}", bulkTriggerEventDto);

        sendValuationLetterProducer.sendValuationLetterToSupport(bulkTriggerEventDto);

    }
}
