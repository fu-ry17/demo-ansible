package com.turnkey.turnquest.gis.quotation.client.crm;

import com.turnkey.turnquest.gis.quotation.dto.crm.IntroducerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "gis-setups-service")
public interface IntroducerClient {
    @RequestMapping(method = RequestMethod.GET, value = "/introducers/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    IntroducerDto findById(@PathVariable("id") Long id);

}
