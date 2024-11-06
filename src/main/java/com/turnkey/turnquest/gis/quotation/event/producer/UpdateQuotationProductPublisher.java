package com.turnkey.turnquest.gis.quotation.event.producer;

import com.turnkey.turnquest.gis.quotation.dto.quotation.UpdateQuotationDto;
import com.turnkey.turnquest.gis.quotation.event.appevent.UpdateQuotationProductEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class UpdateQuotationProductPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public UpdateQuotationProductPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void updateQuotationProduct(UpdateQuotationDto updateQuotationDto){
        UpdateQuotationProductEvent updateQuotationProductEvent = new UpdateQuotationProductEvent(this, updateQuotationDto);
        applicationEventPublisher.publishEvent(updateQuotationProductEvent);
    }

}
