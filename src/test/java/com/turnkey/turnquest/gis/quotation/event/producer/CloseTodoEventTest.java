package com.turnkey.turnquest.gis.quotation.event.producer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Titus Murithi Bundi
 */
class CloseTodoEventTest {

    private CloseTodoEvent closeTodoEvent;
    private KafkaTemplate<String, String> mockKafkaTemplate;

    @BeforeEach
    void setUp() {
        mockKafkaTemplate = mock(KafkaTemplate.class);
        closeTodoEvent = new CloseTodoEvent(mockKafkaTemplate);
    }

    @Test
    void shouldSendCloseDealTodoMessage() {
        Long dealId = 123L;
        closeTodoEvent.closeTodo(dealId);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockKafkaTemplate, times(1)).send(eq("close-deal-todo"), captor.capture());
        assertEquals(dealId.toString(), captor.getValue());
    }

}
