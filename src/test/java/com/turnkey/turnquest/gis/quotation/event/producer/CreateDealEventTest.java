package com.turnkey.turnquest.gis.quotation.event.producer;

import com.turnkey.turnquest.gis.quotation.exception.error.DealException;
import com.turnkey.turnquest.gis.quotation.model.Deal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Titus Murithi Bundi
 */
class CreateDealEventTest {

    private CreateDealEvent createDealEvent;
    private KafkaTemplate<String, String> mockKafkaTemplate;

    @BeforeEach
    void setUp() {
        mockKafkaTemplate = mock(KafkaTemplate.class);
        createDealEvent = new CreateDealEvent(mockKafkaTemplate);
    }

    @Test
    void shouldSendDealCreatedMessage() throws Exception {
        Deal deal = new Deal();
        CompletableFuture<Void> future = new CompletableFuture<>();
        when(mockKafkaTemplate.send(anyString(), anyString())).thenAnswer(invocation -> {
            future.complete(null);
            return CompletableFuture.completedFuture(null);
        });

        createDealEvent.dealCreated(deal);

        future.get(); // wait for the dealCreated method to complete

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockKafkaTemplate, times(1)).send(eq("deal-created"), captor.capture());
    }

    @Test
    void shouldThrowDealException() {
        Deal deal = new Deal();
        when(mockKafkaTemplate.send(anyString(), anyString())).thenThrow(new DealException("Deal exception", new Exception()));

        assertThrows(DealException.class, () -> createDealEvent.dealCreated(deal));
    }

}
