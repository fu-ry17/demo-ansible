package com.turnkey.turnquest.gis.quotation.client.partial;

import com.turnkey.turnquest.gis.quotation.model.Quotation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("partial-service")
public interface PartialQuotationClient {

    @PostMapping("compute/quotation/installments")
    Quotation computeQuotationInstallments(@RequestBody Quotation quotation);
}
