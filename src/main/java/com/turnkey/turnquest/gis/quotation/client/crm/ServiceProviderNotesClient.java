package com.turnkey.turnquest.gis.quotation.client.crm;

import com.turnkey.turnquest.gis.quotation.dto.crm.ServiceProviderNotesDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("crm-setups-service")
public interface ServiceProviderNotesClient {
    @GetMapping("service-provider-notes/find-by-identifier/{serviceProviderId}/{identifier}")
    ServiceProviderNotesDto getByIdentifier(@PathVariable Long serviceProviderId, @PathVariable String identifier);

    @GetMapping("service-provider-notes/insurer/{insurerOrgId}")
    List<ServiceProviderNotesDto> getByInsurer(@PathVariable Long insurerOrgId);
}
