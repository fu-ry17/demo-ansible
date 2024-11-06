package com.turnkey.turnquest.gis.quotation.client.crm;

import com.turnkey.turnquest.gis.quotation.dto.crm.CurrencyRateDto;
import com.turnkey.turnquest.gis.quotation.dto.crm.CurrencyRateRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "crm-setups-service")
public interface CurrencyRateClient {
    @RequestMapping(method = RequestMethod.POST, value = "/currency-rates/active", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    CurrencyRateDto findActive(@RequestBody CurrencyRateRequestDto currencyRateRequestDto);
}
