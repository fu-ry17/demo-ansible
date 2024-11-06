package com.turnkey.turnquest.gis.quotation.event.consumer;

import com.turnkey.turnquest.gis.quotation.dto.quotation.UpdateQuotationDto;
import com.turnkey.turnquest.gis.quotation.event.appevent.UpdateQuotationProductEvent;
import com.turnkey.turnquest.gis.quotation.service.QuotationProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Titus Murithi Bundi
 */
class UpdateQuotationProductListenerTest {

    private UpdateQuotationProductListener updateQuotationProductListener;
    private QuotationProductService mockQuotationProductService;

    @BeforeEach
    void setUp() {
        mockQuotationProductService = mock(QuotationProductService.class);
        updateQuotationProductListener = new UpdateQuotationProductListener(mockQuotationProductService);
    }

    @Test
    void shouldUpdateQuotationProductOnEvent() {
        UpdateQuotationDto updateQuotationDto = new UpdateQuotationDto();
        UpdateQuotationProductEvent event = new UpdateQuotationProductEvent(this, updateQuotationDto);

        updateQuotationProductListener.onApplicationEvent(event);

        ArgumentCaptor<UpdateQuotationDto> captor = ArgumentCaptor.forClass(UpdateQuotationDto.class);
        verify(mockQuotationProductService, times(1)).updateQuotationProduct(captor.capture());
        assertEquals(updateQuotationDto, captor.getValue());
    }

}
