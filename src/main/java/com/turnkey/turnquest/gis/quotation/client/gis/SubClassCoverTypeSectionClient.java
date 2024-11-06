package com.turnkey.turnquest.gis.quotation.client.gis;

import com.turnkey.turnquest.gis.quotation.dto.gis.SubClassCoverTypeSectionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "gis-setups-service")
public interface SubClassCoverTypeSectionClient {

    @GetMapping("/sub-class-cover-type-sections/mandatory")
    List<SubClassCoverTypeSectionDto> findMandatorySections(@RequestParam("subClassId") Long subClassId, @RequestParam("coverTypeId") Long coverTypeId);


    @GetMapping("/sub-class-cover-type-sections/mandatory/with-rates")
    List<SubClassCoverTypeSectionDto> findMandatorySectionsWithPremiumRates(@RequestParam("subClassId") Long subClassId, @RequestParam("coverTypeId") Long coverTypeId, @RequestParam("binderId") Long binderId);

    @GetMapping("/sub-class-cover-type-sections")
    SubClassCoverTypeSectionDto findBySubClassIdAndCoverTypeIdAndSectionId(@RequestParam(name = "coverTypeId", defaultValue = "-1") Long coverTypeId,
                                                                                           @RequestParam(name = "subClassId", defaultValue = "-1") Long subClassId,
                                                                                           @RequestParam(name = "sectionId", defaultValue = "-1") Long sectionId);

}
