package com.turnkey.turnquest.gis.quotation.client.crm;

import com.turnkey.turnquest.gis.quotation.dto.crm.OrganizationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("crm-setups-service")
public interface OrganizationClient {

    @GetMapping("organizations/{id}")
    OrganizationDto findById(@PathVariable Long id);

}
