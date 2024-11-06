package com.turnkey.turnquest.gis.quotation.event.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turnkey.turnquest.gis.quotation.dto.Reports.QuotationReportDto;
import com.turnkey.turnquest.gis.quotation.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ValuationReportConsumer {

    private final Logger logger = LoggerFactory.getLogger(ValuationReportConsumer.class);

    private final ReportService reportService;

    public ValuationReportConsumer(ReportService reportService) {
        this.reportService = reportService;
    }

    @KafkaListener(topics = "valuation-generation")
    public void generatePolicyValuationReport(@Payload String valuation) throws IOException {
        logger.info("Received payload for valuer report generation: {}", valuation);
        reportService.generateValuationReport(stringToPayload(valuation));
    }

    private QuotationReportDto stringToPayload(String payload) throws IOException {
        return new ObjectMapper().readValue(payload, QuotationReportDto.class);
    }
}
