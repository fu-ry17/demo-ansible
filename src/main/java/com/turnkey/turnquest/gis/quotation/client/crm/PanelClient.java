package com.turnkey.turnquest.gis.quotation.client.crm;

import com.turnkey.turnquest.gis.quotation.dto.crm.PanelDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("crm-setups-service")
public interface PanelClient {

    @PostMapping("panels/{panelType}/panel-type")
    PanelDto getAllInsurerPanel(@RequestBody List<Long> panelIds, @PathVariable String panelType);

}
