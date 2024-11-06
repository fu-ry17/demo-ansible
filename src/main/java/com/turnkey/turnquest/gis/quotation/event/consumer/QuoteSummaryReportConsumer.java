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
public class QuoteSummaryReportConsumer {

    private final Logger logger = LoggerFactory.getLogger(QuoteSummaryReportConsumer.class);

    private final ReportService reportService;

    public QuoteSummaryReportConsumer(ReportService reportService) {
        this.reportService = reportService;
    }

    @KafkaListener(topics = "summary-generation")
    public void generateQuotationSummary(@Payload String quotationSummary) throws IOException {
        logger.info("Received payload for summary report generation: {}", quotationSummary);
        reportService.generateQuoteSummaryReport(stringToPayload(quotationSummary));

    }

    private QuotationReportDto stringToPayload(String payload) throws IOException {
        return new ObjectMapper().readValue(payload, QuotationReportDto.class);
    }
}
