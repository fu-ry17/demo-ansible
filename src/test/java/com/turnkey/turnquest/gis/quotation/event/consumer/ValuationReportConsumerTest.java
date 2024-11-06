package com.turnkey.turnquest.gis.quotation.event.consumer;

import com.turnkey.turnquest.gis.quotation.dto.Reports.QuotationReportDto;
import com.turnkey.turnquest.gis.quotation.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Titus Murithi Bundi
 */
class ValuationReportConsumerTest {


    @Mock
    private ReportService reportService;

    @InjectMocks
    private ValuationReportConsumer valuationReportConsumer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generatePolicyValuationReportProcessesValidPayload() throws IOException {
        String validPayload = "{\"policyBatchNo\":-2,\"quotationId\":1}";

        valuationReportConsumer.generatePolicyValuationReport(validPayload);

        verify(reportService, times(1)).generateValuationReport(any(QuotationReportDto.class));
    }

    @Test
    void generatePolicyValuationReportHandlesInvalidPayload() {
        String invalidPayload = "invalid";

        assertThrows(IOException.class, () -> valuationReportConsumer.generatePolicyValuationReport(invalidPayload));

        verify(reportService, times(0)).generateValuationReport(any(QuotationReportDto.class));
    }
}
