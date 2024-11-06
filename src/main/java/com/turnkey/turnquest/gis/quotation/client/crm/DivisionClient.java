package com.turnkey.turnquest.gis.quotation.client.crm;

import com.turnkey.turnquest.gis.quotation.dto.crm.DivisionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "crm-setups-service")
public interface DivisionClient {
    @RequestMapping(method = RequestMethod.GET, value = "/divisions/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    DivisionDto findById(@PathVariable("id") Long id);

}
