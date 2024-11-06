package com.turnkey.turnquest.gis.quotation.event.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turnkey.turnquest.gis.quotation.dto.quotation.QuoteConvertDto;
import com.turnkey.turnquest.gis.quotation.job.ConvertQuotationJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PollQuotesForConversion {

    private final ConvertQuotationJob convertQuotationJob;

    public PollQuotesForConversion(ConvertQuotationJob convertQuotationJob) {
        this.convertQuotationJob = convertQuotationJob;
    }

//    @KafkaListener(topics = "convert-quotation")
    public void pollQuotes(@Payload String payload) throws Exception {
//        QuoteConvertDto quoteConvertDto = stringToPayload(payload);
//        convertQuotationJob.processQuoteConversion(quoteConvertDto);
    }

    private QuoteConvertDto stringToPayload(String payload) throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(payload, QuoteConvertDto.class);
    }
}
