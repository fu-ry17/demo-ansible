package com.turnkey.turnquest.gis.quotation.event.producer;

import com.turnkey.turnquest.gis.quotation.dto.quotation.UpdateQuotationDto;
import com.turnkey.turnquest.gis.quotation.event.appevent.UpdateQuotationEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Titus Murithi Bundi
 */
class UpdateQuotationPublisherTest {

    private UpdateQuotationPublisher updateQuotationPublisher;
    private ApplicationEventPublisher mockApplicationEventPublisher;

    @BeforeEach
    void setUp() {
        mockApplicationEventPublisher = mock(ApplicationEventPublisher.class);
        updateQuotationPublisher = new UpdateQuotationPublisher(mockApplicationEventPublisher);
    }

    @Test
    void shouldPublishUpdateQuotationEvent() {
        UpdateQuotationDto updateQuotationDto = new UpdateQuotationDto();
        updateQuotationPublisher.updateQuotation(updateQuotationDto);

        ArgumentCaptor<UpdateQuotationEvent> captor = ArgumentCaptor.forClass(UpdateQuotationEvent.class);
        verify(mockApplicationEventPublisher, times(1)).publishEvent(captor.capture());
        assertEquals(updateQuotationDto, captor.getValue().getUpdateQuotationDto());
    }
}
