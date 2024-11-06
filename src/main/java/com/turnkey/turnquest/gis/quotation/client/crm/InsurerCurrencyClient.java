package com.turnkey.turnquest.gis.quotation.client.crm;

import com.turnkey.turnquest.gis.quotation.dto.crm.CurrencyDto;
import com.turnkey.turnquest.gis.quotation.dto.crm.InsurerCurrencyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("crm-setups-service")
public interface InsurerCurrencyClient {

    @GetMapping("insurer-currencies/{currencyId}/organization/{orgId}")
    InsurerCurrencyDto findInsurerCurrency(@PathVariable Long currencyId, @PathVariable Long orgId);

    @GetMapping("currencies/{id}")
    CurrencyDto findById(@PathVariable Long id);
}
