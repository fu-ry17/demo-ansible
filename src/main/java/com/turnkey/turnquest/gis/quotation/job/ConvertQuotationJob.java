package com.turnkey.turnquest.gis.quotation.job;

import com.turnkey.turnquest.gis.quotation.dto.quotation.QuoteConvertDto;
import com.turnkey.turnquest.gis.quotation.service.QuotationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConvertQuotationJob {

    private final QuotationService quotationService;

    public ConvertQuotationJob(QuotationService quotationService) {
        this.quotationService = quotationService;
    }


    public void processQuoteConversion(QuoteConvertDto quoteConvertDto) throws Exception {
        quotationService.convertQuoteToPolicies(quoteConvertDto.getQuotationId(), quoteConvertDto.getReceiptId(), quoteConvertDto.getSourceRef());
    }


}
