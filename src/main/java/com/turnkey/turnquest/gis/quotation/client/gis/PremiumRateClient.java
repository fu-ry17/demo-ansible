package com.turnkey.turnquest.gis.quotation.client.gis;

import com.turnkey.turnquest.gis.quotation.dto.gis.PremiumRateDto;
import com.turnkey.turnquest.gis.quotation.dto.gis.PremiumRateFilterDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("gis-setups-service")
public interface PremiumRateClient {
    @PostMapping("premium-rates/filter")
    List<PremiumRateDto> filter(@RequestBody PremiumRateFilterDto premiumRateFilterDto);
}
