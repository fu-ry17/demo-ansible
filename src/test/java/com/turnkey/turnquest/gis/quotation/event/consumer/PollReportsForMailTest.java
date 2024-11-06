package com.turnkey.turnquest.gis.quotation.event.consumer;

import com.turnkey.turnquest.gis.quotation.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PollReportsForMailTest {

    @Mock
    private ReportService reportService;

    @InjectMocks
    private PollReportsForMail pollReportsForMail;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }




    @Test
    void shouldSendQuotationDocumentsWhenQuotationIdIsValid() {
        String validQuotationId = "1";
        pollReportsForMail.sendMail(validQuotationId);
        verify(reportService, times(1)).sendQuotationDocuments(Long.parseLong(validQuotationId));
    }

}
