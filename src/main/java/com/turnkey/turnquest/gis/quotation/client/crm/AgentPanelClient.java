package com.turnkey.turnquest.gis.quotation.client.crm;

import com.turnkey.turnquest.gis.quotation.dto.crm.AgentPanelDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("crm-setups-service")
public interface AgentPanelClient {

    @GetMapping("agent-panels/{agentOrganizationId}/agent/{insurerOrganizationId}/insurer")
    List<AgentPanelDto> getByAgentOrgAndInsurerOrg(
            @PathVariable("agentOrganizationId") Long agentOrganizationId,
            @PathVariable("insurerOrganizationId") Long insurerOrganizationId
    );

}
