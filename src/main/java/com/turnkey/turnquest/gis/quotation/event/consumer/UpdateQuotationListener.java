package com.turnkey.turnquest.gis.quotation.event.consumer;

import com.turnkey.turnquest.gis.quotation.event.appevent.UpdateQuotationEvent;
import com.turnkey.turnquest.gis.quotation.service.QuotationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class UpdateQuotationListener implements ApplicationListener<UpdateQuotationEvent> {


    private  final QuotationService quotationService;

    UpdateQuotationListener(QuotationService quotationService){
        this.quotationService = quotationService;
    }

    @Override
    public void onApplicationEvent(@NotNull UpdateQuotationEvent event) {
        quotationService.updateQuotation(event.getUpdateQuotationDto());
    }
}
