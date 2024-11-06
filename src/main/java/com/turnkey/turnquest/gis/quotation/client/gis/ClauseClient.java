package com.turnkey.turnquest.gis.quotation.client.gis;

import com.turnkey.turnquest.gis.quotation.dto.gis.ClauseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "gis-setups-service")
public interface ClauseClient {
    @RequestMapping(method = RequestMethod.GET, value = "/clauses/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ClauseDto findById(@PathVariable("id") Long id);

}