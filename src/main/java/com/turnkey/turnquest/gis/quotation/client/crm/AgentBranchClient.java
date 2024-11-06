package com.turnkey.turnquest.gis.quotation.client.crm;

import com.turnkey.turnquest.gis.quotation.dto.crm.AgentBranchDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("crm-setups-service")
public interface AgentBranchClient {
    @RequestMapping(method = RequestMethod.GET, value = "agent-branches/{insurerOrgId}/insurer/}")
    AgentBranchDto findAgentBranchByInsurer(@PathVariable Long insurerOrgId);
}
