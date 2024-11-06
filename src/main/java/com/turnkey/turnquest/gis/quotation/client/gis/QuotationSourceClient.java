package com.turnkey.turnquest.gis.quotation.client.gis;

import com.turnkey.turnquest.gis.quotation.dto.quotation.QuotationSourceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "gis-setups-service")
public interface QuotationSourceClient {
    @RequestMapping(method = RequestMethod.GET, value = "/quotation-sources/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    QuotationSourceDto findById(@PathVariable("id") Long id);

}
