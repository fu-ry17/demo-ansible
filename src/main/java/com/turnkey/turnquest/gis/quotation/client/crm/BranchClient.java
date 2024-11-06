package com.turnkey.turnquest.gis.quotation.client.crm;

import com.turnkey.turnquest.gis.quotation.dto.crm.BranchDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "crm-setups-service")
public interface BranchClient {
    @RequestMapping(method = RequestMethod.GET, value = "/branches/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    BranchDto findById(@PathVariable("id") Long id);

}
