package com.turnkey.turnquest.gis.quotation.client.crm;

import com.turnkey.turnquest.gis.quotation.dto.crm.EntitiesDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("crm-setups-service")
public interface EntitiesClient {
    @GetMapping("entities/{id}")
    EntitiesDto find(@PathVariable Long id);
}
