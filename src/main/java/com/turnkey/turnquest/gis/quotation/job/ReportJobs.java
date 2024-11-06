package com.turnkey.turnquest.gis.quotation.job;

import com.turnkey.turnquest.gis.quotation.model.QuotationReports;
import com.turnkey.turnquest.gis.quotation.repository.QuotationReportsRepository;
import com.turnkey.turnquest.gis.quotation.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ReportJobs {

    private final QuotationReportsRepository quotationReportsRepository;
    private final ReportService reportService;

    private final Logger logger = LoggerFactory.getLogger(ReportJobs.class);

    public ReportJobs(QuotationReportsRepository quotationReportsRepository, ReportService reportService) {
        this.quotationReportsRepository = quotationReportsRepository;
        this.reportService = reportService;
    }



    /**
     * Runs every 15 seconds and checks for reports not sent
     * and mails them to the insurer,client and agent
     */

    @Transactional
    @Scheduled(fixedDelay = 30000)
    public void mailSender() {
        Map<Long, List<QuotationReports>> mailGroup = new HashMap<>();
        List<QuotationReports> reports = quotationReportsRepository.findBySentFalse();

        if (reports != null) {
            mailGroup = reports
                    .stream()
                    .filter(quotationReports -> quotationReports.getQuotationId() != null)
                    .collect(Collectors.groupingBy(QuotationReports::getQuotationId));
        }

        mailGroup.forEach((aLong, quotationReports) ->  reportService.sendQuotationDocuments(aLong));
    }

    /**
     * Attempts to correct the policy batch nos that are empty or null
     * By taking the policy batch no from the reports with similar quote id
     */

    @Transactional
    @Scheduled(fixedDelay = 15000)
    public void policyBatchNumberCorrector() {
        Map<Long, List<QuotationReports>> fixGroup = new HashMap<>();
        List<QuotationReports> reports = quotationReportsRepository.findByPolicyId(-1L);

        if (!reports.isEmpty()) {
            fixGroup = reports
                    .stream()
                    .filter(quotationReports -> quotationReports.getQuotationId() != null)
                    .collect(Collectors.groupingBy(QuotationReports::getQuotationId));
        }

        fixGroup.forEach((aLong, quotationReports) ->
                quotationReportsRepository.findTopByQuotationIdAndPolicyIdIsNotNull(aLong).ifPresent(rpt ->
                        quotationReports.forEach(report -> {
                            logger.info("Correcting {} report with quotation id {}", report.getFileCategory(), report.getQuotationId());
                            report.setPolicyId(rpt.getPolicyId());
                        })
                )
        );
    }
}
