package com.turnkey.turnquest.gis.quotation.event.producer;

import com.turnkey.turnquest.gis.quotation.dto.quotation.UpdateQuotationDto;
import com.turnkey.turnquest.gis.quotation.event.appevent.UpdateQuotationProductEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Titus Murithi Bundi
 */
class UpdateQuotationProductPublisherTest {

    private UpdateQuotationProductPublisher updateQuotationProductPublisher;
    private ApplicationEventPublisher mockApplicationEventPublisher;

    @BeforeEach
    void setUp() {
        mockApplicationEventPublisher = mock(ApplicationEventPublisher.class);
        updateQuotationProductPublisher = new UpdateQuotationProductPublisher(mockApplicationEventPublisher);
    }

    @Test
    void shouldPublishUpdateQuotationProductEvent() {
        UpdateQuotationDto updateQuotationDto = new UpdateQuotationDto();
        updateQuotationProductPublisher.updateQuotationProduct(updateQuotationDto);

        ArgumentCaptor<UpdateQuotationProductEvent> captor = ArgumentCaptor.forClass(UpdateQuotationProductEvent.class);
        verify(mockApplicationEventPublisher, times(1)).publishEvent(captor.capture());
        assertEquals(updateQuotationDto, captor.getValue().getUpdateQuotationDto());
    }

}
