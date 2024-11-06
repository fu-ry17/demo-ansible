package com.turnkey.turnquest.gis.quotation.event.consumer;

import com.turnkey.turnquest.gis.quotation.dto.Reports.QuotationReportDto;
import com.turnkey.turnquest.gis.quotation.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class QuoteSummaryReportConsumerTest {

    @Mock
    private ReportService reportService;

    @InjectMocks
    private QuoteSummaryReportConsumer quoteSummaryReportConsumer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void shouldGenerateQuotationSummaryWhenPayloadIsValid() throws IOException {
        // Arrange
        String validPayload = "{\"policyBatchNo\": 1, \"quotationId\": 2}";

        // Act
        quoteSummaryReportConsumer.generateQuotationSummary(validPayload);

        // Assert
        verify(reportService, times(1)).generateQuoteSummaryReport(any(QuotationReportDto.class));
    }

}
