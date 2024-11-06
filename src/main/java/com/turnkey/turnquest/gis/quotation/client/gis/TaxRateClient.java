package com.turnkey.turnquest.gis.quotation.client.gis;

import com.turnkey.turnquest.gis.quotation.dto.gis.TaxRateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("gis-setups-service")
public interface TaxRateClient {
    @GetMapping("tax-rates/{id}")
    TaxRateDto find(@PathVariable Long id);
}
