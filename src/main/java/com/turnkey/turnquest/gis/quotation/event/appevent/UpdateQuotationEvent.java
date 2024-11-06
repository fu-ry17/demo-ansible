package com.turnkey.turnquest.gis.quotation.event.appevent;

import com.turnkey.turnquest.gis.quotation.dto.quotation.UpdateQuotationDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateQuotationEvent extends ApplicationEvent {

    private final UpdateQuotationDto updateQuotationDto;

    public UpdateQuotationEvent(Object source, UpdateQuotationDto updateQuotationDto) {
        super(source);
        this.updateQuotationDto = updateQuotationDto;
    }

}
