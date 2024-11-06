package com.turnkey.turnquest.gis.quotation.dto.billing;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("billing-service")
public interface PremiumCardClient {

    @GetMapping("/premium-card/{paymentRef}/payments")
    PremiumCardDto findPremiumCardByPaymentRef(@PathVariable String paymentRef);

    @GetMapping("/premium-card/{id}")
    PremiumCardDto findPremiumCardById(@PathVariable Long id);

    @PostMapping("/premium-card")
    PremiumCardDto savePremiumCard(@RequestBody PremiumCardDto premiumCard);
}
