package com.turnkey.turnquest.gis.quotation.client.valuation;

import com.turnkey.turnquest.gis.quotation.model.Quotation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient("valuation-service")
public interface ValuationClient {

    @PostMapping("quotes")
    Quotation saveValuationQuote(@RequestBody Quotation quotation);

    @GetMapping("quotes/find-original")
    Quotation findOriginalQuote(@RequestParam String policyNo);

    @GetMapping("valuation/quote/static-taxes")
    BigDecimal getOriginalStaticTaxes(@RequestParam String policyNo);
}
