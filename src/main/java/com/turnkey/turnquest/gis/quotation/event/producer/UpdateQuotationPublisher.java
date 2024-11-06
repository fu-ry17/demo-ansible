package com.turnkey.turnquest.gis.quotation.event.producer;

import com.turnkey.turnquest.gis.quotation.dto.quotation.UpdateQuotationDto;
import com.turnkey.turnquest.gis.quotation.event.appevent.UpdateQuotationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class UpdateQuotationPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    UpdateQuotationPublisher(ApplicationEventPublisher applicationEventPublisher){
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void updateQuotation(UpdateQuotationDto updateQuotationDto){
        UpdateQuotationEvent updateQuotationEvent = new UpdateQuotationEvent(this, updateQuotationDto);
        applicationEventPublisher.publishEvent(updateQuotationEvent);
    }

}
