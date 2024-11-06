package com.turnkey.turnquest.gis.quotation.client.gis;

import com.turnkey.turnquest.gis.quotation.dto.gis.SectionShortPeriodFilterDto;
import com.turnkey.turnquest.gis.quotation.dto.gis.SectionShortPeriodRateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "gis-setups-service")
public interface SectionShortPeriodRateClient {
    @RequestMapping(method = RequestMethod.POST, value = "/section-short-period-rates/filter", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    List<SectionShortPeriodRateDto> filter(@RequestBody SectionShortPeriodFilterDto sectionShortPeriodFilterDto);
}
