package com.turnkey.turnquest.gis.quotation.event.consumer;

import com.turnkey.turnquest.gis.quotation.event.appevent.UpdateQuotationProductEvent;
import com.turnkey.turnquest.gis.quotation.service.QuotationProductService;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class UpdateQuotationProductListener implements ApplicationListener<UpdateQuotationProductEvent> {

    private final QuotationProductService quotationProductService;

    public UpdateQuotationProductListener(QuotationProductService quotationProductService) {
        this.quotationProductService = quotationProductService;
    }

    @Override
    public void onApplicationEvent(@NotNull UpdateQuotationProductEvent event) {

        quotationProductService.updateQuotationProduct(event.getUpdateQuotationDto());


    }
}
