package com.turnkey.turnquest.gis.quotation.controllertest;

import com.turnkey.turnquest.gis.quotation.auth.TokenUtils;
import com.turnkey.turnquest.gis.quotation.controller.ReportController;
import com.turnkey.turnquest.gis.quotation.dto.Reports.BooleanResult;
import com.turnkey.turnquest.gis.quotation.dto.Reports.QuotationReportDto;
import com.turnkey.turnquest.gis.quotation.dto.quotation.ResendReportRequestDto;
import com.turnkey.turnquest.gis.quotation.dto.quotation.ResendReportResponseDto;
import com.turnkey.turnquest.gis.quotation.enums.QuotationReportType;
import com.turnkey.turnquest.gis.quotation.model.QuotationReports;
import com.turnkey.turnquest.gis.quotation.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class ReportControllerTest {

    @Mock
    Authentication authentication;

    @Mock
    TokenUtils tokenUtils;

    @Mock
    ReportService reportService;

    @InjectMocks
    ReportController reportController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Long organizationId = 1L;

        when(tokenUtils.getOrganizationId()).thenReturn(organizationId);
        reportController = new ReportController(reportService, tokenUtils);
    }

    @Test
    public void getQuotationReportsTest() {
        List<QuotationReports> reports = new ArrayList<>();
        reports.add(generateQuotationReport());
        reports.add(generateQuotationReport());
        reports.add(generateQuotationReport());

        Long policyId = 1L;

        when(reportService.getQuotationReport(ArgumentMatchers.any())).thenReturn(reports);

        var reportsList = reportController.getQuotationReports(policyId, authentication);

        assertNotNull(reportsList);
        assertEquals(HttpStatus.OK, reportsList.getStatusCode());
        assertEquals(reports, reportsList.getBody());
        verify(reportService, times(1)).getQuotationReport(policyId);
    }

    @Test
    public void saveQuotationReportTest() throws Exception {

        QuotationReports quotationReports = generateQuotationReport();

        when(reportService.saveQuotationReport(ArgumentMatchers.any())).thenReturn(quotationReports);

        var result = reportController.saveQuotationReport(quotationReports, authentication);

        assertNotNull(result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(quotationReports, result.getBody());
        verify(reportService, times(1)).saveQuotationReport(quotationReports);
    }

    @Test
    public void getPolicyReportsTest() {
        List<QuotationReports> reports = new ArrayList<>();
        reports.add(generateQuotationReport());
        reports.add(generateQuotationReport());
        reports.add(generateQuotationReport());

        Long policyId = 1L;

        when(reportService.getPolicyReports(ArgumentMatchers.any())).thenReturn(reports);

        var reportsList = reportController.getPolicyReports(policyId, authentication);

        assertNotNull(reportsList);
        assertEquals(HttpStatus.OK, reportsList.getStatusCode());
        assertEquals(reports, reportsList.getBody());
        verify(reportService, times(1)).getPolicyReports(policyId);
    }


    @Test
    void shouldGenerateRenewalNotice() {
        byte[] expected = new byte[10];
        Long quotationId = 1L;

        when(reportService.generateRenewalNotice(quotationId)).thenReturn(expected);

        byte[] result = reportController.generateRenewalNotice(quotationId, authentication);

        assertEquals(expected, result);
    }

    @Test
    void shouldGenerateQuotationSummary() {
        byte[] expected = new byte[10];
        QuotationReportDto quotationReportDto = new QuotationReportDto();

        when(reportService.generateQuoteSummaryReport(quotationReportDto)).thenReturn(expected);

        byte[] result = reportController.generateQuotationSummary(quotationReportDto, authentication);

        assertEquals(expected, result);
    }

    @Test
    void shouldCheckReportExists() {
        BooleanResult expected = new BooleanResult(true);
        Long quotationId = 1L;
        QuotationReportType quotationReportType = QuotationReportType.POLICY_DOCUMENT;

        when(reportService.reportExists(quotationId, quotationReportType)).thenReturn(expected);

        ResponseEntity<BooleanResult> result = reportController.reportExists(quotationId, quotationReportType);

        assertEquals(expected, result.getBody());
    }

    @Test
    void shouldResendReportsToClient() {
        ResendReportRequestDto request = new ResendReportRequestDto();
        ResendReportResponseDto expected = new ResendReportResponseDto();

        when(reportService.resendReportsToClient(request)).thenReturn(expected);

        ResponseEntity<ResendReportResponseDto> result = reportController.resendReportsToClient(request);

        assertEquals(expected, result.getBody());
    }

    //<editor-fold defaultstate="collapsed" desc="Test Data"">
    private QuotationReports generateQuotationReport() {
        QuotationReports quotationReports = new QuotationReports();
        quotationReports.setFileName("test123.png");
        quotationReports.setMimeType("pdf");
        quotationReports.setOrganizationId(4L);
        quotationReports.setFileUrl("http://fakeurl1/test.pdf,http://fakeurl1/test2.pdf");
        return quotationReports;
    }
    //</editor-fold>

}
