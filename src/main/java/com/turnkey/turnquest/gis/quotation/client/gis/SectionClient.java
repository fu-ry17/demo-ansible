package com.turnkey.turnquest.gis.quotation.client.gis;

import com.turnkey.turnquest.gis.quotation.dto.gis.SectionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("gis-setups-service")
public interface SectionClient {
    @GetMapping("sections/{id}")
    SectionDto findById(@PathVariable Long id);
}
