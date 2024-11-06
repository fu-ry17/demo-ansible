package com.turnkey.turnquest.gis.quotation.client.gis;

import com.turnkey.turnquest.gis.quotation.dto.gis.SubClassCoverTypeDto;
import com.turnkey.turnquest.gis.quotation.dto.gis.SubClassDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "gis-setups-service")
public interface SubClassClient {
    @RequestMapping(value = "/sub-classes/{id}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    SubClassDto findById(@PathVariable("id") Long id);


    @RequestMapping(value = "/sub-classes/{id}/cover-types/{coverTypeId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    SubClassCoverTypeDto findBySubClassCoverTypeByCoverTypeId(@PathVariable("id") Long id, @PathVariable("coverTypeId") Long coverTypeId);

}
