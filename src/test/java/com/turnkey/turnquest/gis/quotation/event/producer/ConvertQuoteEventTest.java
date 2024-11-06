package com.turnkey.turnquest.gis.quotation.event.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyDto;
import com.turnkey.turnquest.gis.quotation.exception.error.QuoteConversionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Titus Murithi Bundi
 */
class ConvertQuoteEventTest {

    private ConvertQuoteEvent convertQuoteEvent;
    private KafkaTemplate<String, String> mockKafkaTemplate;

    @BeforeEach
    void setUp() {
        mockKafkaTemplate = mock(KafkaTemplate.class);
        convertQuoteEvent = new ConvertQuoteEvent(mockKafkaTemplate);
    }

    @Test
    void shouldConvertQuotationProductToPolicy() throws Exception {
        PolicyDto policyDto = new PolicyDto();
        SendResult<String, String> sendResultMock = mock(SendResult.class);
        when(mockKafkaTemplate.send(anyString(), anyString())).thenReturn(CompletableFuture.completedFuture(sendResultMock));

        convertQuoteEvent.convertQuotationProductToPolicy(policyDto);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockKafkaTemplate, times(1)).send(eq("create-conversion-policy"), captor.capture());

        ObjectMapper mapper = new ObjectMapper();
        String policyDtoJson = mapper.writeValueAsString(policyDto);

        assertEquals(policyDtoJson, captor.getValue());
    }

    @Test
    void shouldThrowQuoteConversionException() {
        PolicyDto policyDto = new PolicyDto();
        when(mockKafkaTemplate.send(anyString(), anyString())).thenThrow(new QuoteConversionException("Quote conversion exception", new Exception()));

        try {
            convertQuoteEvent.convertQuotationProductToPolicy(policyDto);
        } catch (Exception e) {
            assertEquals(QuoteConversionException.class, e.getClass());
        }
    }

}
