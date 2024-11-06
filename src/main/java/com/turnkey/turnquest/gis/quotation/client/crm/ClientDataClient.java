package com.turnkey.turnquest.gis.quotation.client.crm;

import com.turnkey.turnquest.gis.quotation.dto.crm.ClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("crm-setups-service")
public interface ClientDataClient {

    @GetMapping("clients/{id}")
    ClientDto findById(@PathVariable Long id);
}
