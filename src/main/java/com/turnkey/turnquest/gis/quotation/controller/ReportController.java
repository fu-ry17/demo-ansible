package com.turnkey.turnquest.gis.quotation.controller;

import com.turnkey.turnquest.gis.quotation.auth.TokenUtils;
import com.turnkey.turnquest.gis.quotation.dto.Reports.BooleanResult;
import com.turnkey.turnquest.gis.quotation.dto.Reports.QuotationReportDto;
import com.turnkey.turnquest.gis.quotation.dto.quotation.ResendReportRequestDto;
import com.turnkey.turnquest.gis.quotation.dto.quotation.ResendReportResponseDto;
import com.turnkey.turnquest.gis.quotation.enums.QuotationReportType;
import com.turnkey.turnquest.gis.quotation.model.QuotationReports;
import com.turnkey.turnquest.gis.quotation.service.ReportService;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;
    private final TokenUtils tokenUtils;

    public ReportController(ReportService reportService, TokenUtils tokenUtils) {
        this.reportService = reportService;
        this.tokenUtils = tokenUtils;
    }

    /**
     * Gets all reports for a specific quotation
     * @param policyId the policy Id
     * @param authentication authentication
     * @return list of quotation documents
     */

    @RolesAllowed({"quot_rpt_get","agent"})
    @GetMapping("/quotation/{policyId}")
    public ResponseEntity<List<QuotationReports>> getQuotationReports(@PathVariable("policyId") Long policyId,
                                                                           Authentication authentication){
        tokenUtils.init(authentication);
        return ResponseEntity.ok(reportService.getQuotationReport(policyId));
    }

    /**
     * Gets all reports for a specific policy
     * @param policyId the policy Id
     * @param authentication authentication
     * @return list of quotation documents
     */

    @RolesAllowed({"quot_rpt_get","agent"})
    @GetMapping("/policy/{policyId}")
    public ResponseEntity<List<QuotationReports>> getPolicyReports(@PathVariable("policyId") Long policyId,
                                                                           Authentication authentication){
        tokenUtils.init(authentication);
        return ResponseEntity.ok(reportService.getPolicyReports(policyId));
    }

    /**
     * Saves a quotation report
     * @param quotationReports Quotation Report
     * @param authentication authentication. Nullable when called via the kafka process
     * @return QuotationReport
     */

    @RolesAllowed({"quot_rpt_save","quotation_service_admin"})
    @PostMapping("/save-report")
    public ResponseEntity<QuotationReports> saveQuotationReport(@RequestBody QuotationReports quotationReports,
                                                                @Nullable Authentication authentication){
        if(authentication != null)
            tokenUtils.init(authentication);

        return ResponseEntity.ok(reportService.saveQuotationReport(quotationReports));
    }

    @RolesAllowed({"quot_rpt_gen_rn_notice","agent"})
    @GetMapping(produces = MediaType.APPLICATION_PDF_VALUE,value = "/renewal-notice/{quotationId}")
    public byte[] generateRenewalNotice(@PathVariable("quotationId") Long quotationId,Authentication authentication){
        tokenUtils.init(authentication);
        return reportService.generateRenewalNotice(quotationId);
    }

    @RolesAllowed({"quot_rpt_gen_quot_summary","agent"})
    @PostMapping(produces = MediaType.APPLICATION_PDF_VALUE,value = "/quotation-summary")
    public byte[] generateQuotationSummary(@RequestBody QuotationReportDto quotationReportDto, Authentication authentication){
        tokenUtils.init(authentication);
        return reportService.generateQuoteSummaryReport(quotationReportDto);
    }

    @RolesAllowed({"quot_rpt_exists","quotation_service_admin"})
    @GetMapping("/report-exists/{quotationId}/{reportType}")
    public ResponseEntity<BooleanResult> reportExists(@PathVariable("quotationId") Long quotationId, @PathVariable("reportType") QuotationReportType quotationReportType){
        return ResponseEntity.ok(reportService.reportExists(quotationId,quotationReportType));
    }


    @PostMapping("/resend-to-client")
    public ResponseEntity<ResendReportResponseDto> resendReportsToClient(@RequestBody ResendReportRequestDto request){
        return ResponseEntity.ok(reportService.resendReportsToClient(request));
    }
}
