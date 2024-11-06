package com.turnkey.turnquest.gis.quotation.event.consumer;

import com.turnkey.turnquest.gis.quotation.dto.quotation.UpdateQuotationDto;
import com.turnkey.turnquest.gis.quotation.event.appevent.UpdateQuotationEvent;
import com.turnkey.turnquest.gis.quotation.service.QuotationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Titus Murithi Bundi
 */
class UpdateQuotationListenerTest {

    private UpdateQuotationListener updateQuotationListener;
    private QuotationService mockQuotationService;

    @BeforeEach
    void setUp() {
        mockQuotationService = mock(QuotationService.class);
        updateQuotationListener = new UpdateQuotationListener(mockQuotationService);
    }

    @Test
    void shouldUpdateQuotationOnEvent() {
        UpdateQuotationDto updateQuotationDto = new UpdateQuotationDto();
        UpdateQuotationEvent event = new UpdateQuotationEvent(this, updateQuotationDto);

        updateQuotationListener.onApplicationEvent(event);

        ArgumentCaptor<UpdateQuotationDto> captor = ArgumentCaptor.forClass(UpdateQuotationDto.class);
        verify(mockQuotationService, times(1)).updateQuotation(captor.capture());
        assertEquals(updateQuotationDto, captor.getValue());
    }

}
