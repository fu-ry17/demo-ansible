package com.turnkey.turnquest.gis.quotation.client.gis;

import com.turnkey.turnquest.gis.quotation.dto.computation.CoverTypeSection;
import com.turnkey.turnquest.gis.quotation.dto.gis.FilterCoverTypeSectionsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "gis-setups-service")
public interface CoverTypeSectionClient {
    @PostMapping("covertype-sections/sections")
    List<CoverTypeSection> filterByCoverTypesAndSections(@RequestBody FilterCoverTypeSectionsDto filterCoverTypeSectionsDto);
}