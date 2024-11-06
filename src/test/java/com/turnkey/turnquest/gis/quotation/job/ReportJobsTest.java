package com.turnkey.turnquest.gis.quotation.job;

import com.turnkey.turnquest.gis.quotation.model.QuotationReports;
import com.turnkey.turnquest.gis.quotation.repository.QuotationReportsRepository;
import com.turnkey.turnquest.gis.quotation.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * @author Titus Murithi Bundi
 */
class ReportJobsTest {

    @Mock
    private QuotationReportsRepository quotationReportsRepository;

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ReportJobs reportJobs;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSendMailForUnsentReports() {
        QuotationReports report1 = new QuotationReports();
        report1.setQuotationId(1L);
        QuotationReports report2 = new QuotationReports();
        report2.setQuotationId(2L);
        List<QuotationReports> reports = Arrays.asList(report1, report2);

        when(quotationReportsRepository.findBySentFalse()).thenReturn(reports);

        reportJobs.mailSender();

        verify(reportService, times(1)).sendQuotationDocuments(1L);
        verify(reportService, times(1)).sendQuotationDocuments(2L);
    }

    @Test
    void shouldCorrectPolicyBatchNumbers() {
        QuotationReports report1 = new QuotationReports();
        report1.setQuotationId(1L);
        report1.setPolicyId(-1L);
        QuotationReports report2 = new QuotationReports();
        report2.setQuotationId(2L);
        report2.setPolicyId(-1L);
        List<QuotationReports> reports = Arrays.asList(report1, report2);

        when(quotationReportsRepository.findByPolicyId(-1L)).thenReturn(reports);
        when(quotationReportsRepository.findTopByQuotationIdAndPolicyIdIsNotNull(1L)).thenReturn(java.util.Optional.of(report1));
        when(quotationReportsRepository.findTopByQuotationIdAndPolicyIdIsNotNull(2L)).thenReturn(java.util.Optional.of(report2));

        reportJobs.policyBatchNumberCorrector();

        verify(quotationReportsRepository, times(1)).findTopByQuotationIdAndPolicyIdIsNotNull(1L);
        verify(quotationReportsRepository, times(1)).findTopByQuotationIdAndPolicyIdIsNotNull(2L);
    }

    @Test
    void shouldNotCorrectPolicyBatchNumbersWhenReportsAreEmpty() {
        List<QuotationReports> reports = Collections.emptyList();

        when(quotationReportsRepository.findByPolicyId(-1L)).thenReturn(reports);

        reportJobs.policyBatchNumberCorrector();

        verify(quotationReportsRepository, times(0)).findTopByQuotationIdAndPolicyIdIsNotNull(anyLong());
    }

}
