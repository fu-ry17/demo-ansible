package com.turnkey.turnquest.gis.quotation.client.crm;

import com.turnkey.turnquest.gis.quotation.dto.crm.AgencyDto;
import com.turnkey.turnquest.gis.quotation.dto.crm.OrganizationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "crm-setups-service")
public interface AgencyClient {

    @GetMapping("agencies/insurer")
    AgencyDto findByPanelIdAndOrganizationId(
            @RequestParam Long panelId,
            @RequestParam Long organizationId
    );

    @GetMapping("agencies/agent/organization/{organizationId}")
    OrganizationDto findByOrganizationId(@PathVariable Long organizationId);
}
