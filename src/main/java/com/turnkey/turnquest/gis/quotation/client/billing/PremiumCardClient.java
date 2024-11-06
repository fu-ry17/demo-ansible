package com.turnkey.turnquest.gis.quotation.client.billing;

import com.turnkey.turnquest.gis.quotation.dto.quotation.PremiumCardDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("billing-service")
public interface PremiumCardClient {

    @PostMapping("/premium-card/save")
    PremiumCardDto initPayment(@RequestBody PremiumCardDto premiumCardDto);

}
