package com.turnkey.turnquest.gis.quotation.event.consumer;

import com.turnkey.turnquest.gis.quotation.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class PollReportsForMail {

    private final Logger logger = LoggerFactory.getLogger(PollReportsForMail.class);

    private final ReportService reportService;

    public PollReportsForMail(ReportService reportService) {
        this.reportService = reportService;
    }

    @KafkaListener(topics = "send-reports")
    public void sendMail(String quotationId) {
        logger.info("Generating reports for quotationId {}", quotationId);
        reportService.sendQuotationDocuments(Long.parseLong(quotationId));

    }
}
