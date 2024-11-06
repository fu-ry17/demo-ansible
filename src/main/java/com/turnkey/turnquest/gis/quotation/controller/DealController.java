package com.turnkey.turnquest.gis.quotation.controller;

import com.turnkey.turnquest.gis.quotation.auth.TokenUtils;
import com.turnkey.turnquest.gis.quotation.enums.DealStatus;
import com.turnkey.turnquest.gis.quotation.enums.OverAllStatus;
import com.turnkey.turnquest.gis.quotation.enums.SortType;
import com.turnkey.turnquest.gis.quotation.model.Deal;
import com.turnkey.turnquest.gis.quotation.service.DealService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(value = "/deals")
public class DealController {

    private final DealService dealService;
    private final TokenUtils tokenUtils;

    public DealController(DealService dealService, TokenUtils tokenUtils) {
        this.dealService = dealService;
        this.tokenUtils = tokenUtils;
    }

    @RolesAllowed({"quot_deal_get_all","agent"})
    @GetMapping
    public ResponseEntity<List<Deal>> getAllDeals(@PageableDefault(size = 50, page = 0, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication) {
        tokenUtils.init(authentication);
        Long organizationId = tokenUtils.getOrganizationId();
        return ResponseEntity.ok(dealService.findAll(organizationId, pageable));
    }


    @RolesAllowed({"quot_deal_get","agent"})
    @GetMapping("/{id}")
    public ResponseEntity<Deal> getOneById(@PathVariable Long id, Authentication authentication) {
        tokenUtils.init(authentication);
        Long organizationId = tokenUtils.getOrganizationId();
        return ResponseEntity.ok(dealService.findById(id, organizationId));
    }


    @RolesAllowed({"quot_deal_get_by_status","agent"})
    @GetMapping("/filter/dealstatus/{status}")
    public ResponseEntity<List<Deal>> filterByDealStatus(@PageableDefault(size = 50, page = 0, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable DealStatus status, Authentication authentication) {
        tokenUtils.init(authentication);
        Long organizationId = tokenUtils.getOrganizationId();
        return ResponseEntity.ok(dealService.filterByDealsStatus(status, organizationId, pageable));
    }


    @RolesAllowed({"quot_deal_get_by_status","agent"})
    @GetMapping("/filter/overallstatus/{overallStatus}")
    public ResponseEntity<List<Deal>> filterByOverAllStatus(@PageableDefault(size = 50, page = 0) Pageable pageable, @PathVariable OverAllStatus overallStatus, Authentication authentication) {
        tokenUtils.init(authentication);
        Long organizationId = tokenUtils.getOrganizationId();
        return ResponseEntity.ok(dealService.filterByOverAllStatus(overallStatus, organizationId, pageable));
    }


    @RolesAllowed({"quot_deal_get_by_client","agent"})
    @PostMapping("/filter/clients")
    public ResponseEntity<List<Deal>> filterByClientId(@PageableDefault(size = 50, page = 0, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable, @RequestBody List<Long> clientIds, Authentication authentication) {
        tokenUtils.init(authentication);
        Long organizationId = tokenUtils.getOrganizationId();
        return ResponseEntity.ok(dealService.filterDealsByClients(clientIds, organizationId, pageable));
    }

    @RolesAllowed({"quot_deal_get_by_deal","agent"})
    @GetMapping("/search/quotation/{quotationId}/agent/{organizationId}")
    public ResponseEntity<List<Deal>> filterByDealId(@PathVariable Long quotationId, @PathVariable Long organizationId) {
        return ResponseEntity.ok(dealService.filterDealsByQuotationId(quotationId, organizationId));
    }

    @RolesAllowed({"quot_deal_close","agent"})
    @PostMapping("quote/update-status")
    public ResponseEntity<List<Deal>> closeDealsByQuote(@RequestBody List<Deal> deals) {
        return ResponseEntity.ok(dealService.closeDeals(deals));
    }

    @RolesAllowed({"quot_deal_close","agent"})
    @PostMapping("/update-status")
    public ResponseEntity<Deal> closeDeal(@RequestBody Deal deal, Authentication authentication) {
        tokenUtils.init(authentication);
        Long organizationId = tokenUtils.getOrganizationId();
        return ResponseEntity.ok(dealService.closeDeal(deal, organizationId));
    }

    @RolesAllowed({"quot_deal_save","agent"})
    @PostMapping
    public ResponseEntity<Deal> saveDeal(@RequestBody Deal deal, Authentication authentication) {
        tokenUtils.init(authentication);
        Long organizationId = tokenUtils.getOrganizationId();

        return ResponseEntity.ok(dealService.save(deal, organizationId));
    }

    @RolesAllowed({"quot_deal_save_all","agent"})
    @PostMapping("/all")
    public ResponseEntity<List<Deal>> saveAllDeals(@RequestBody List<Deal> deals, Authentication authentication) {
        tokenUtils.init(authentication);
        Long organizationId = tokenUtils.getOrganizationId();
        return ResponseEntity.ok(dealService.saveAll(deals, organizationId));
    }


    @RolesAllowed("quot_deal_delete")
    @DeleteMapping
    public ResponseEntity<Deal> deleteDeal(@RequestBody Deal deal) {
        dealService.delete(deal);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed({"quot_deal_filter","agent"})
    @GetMapping("sort-filter-search")
    public ResponseEntity<List<Deal>> sortAndFilter(@RequestParam(required = false) String searchText,
                                                    @RequestParam(required = false) Long clientId,
                                                    @RequestParam(required = false) Long insOrgId,
                                                    @RequestParam(required = false) Long dateFrom,
                                                    @RequestParam(required = false) Long dateTo,
                                                    @RequestParam(required = false) BigDecimal priceFrom,
                                                    @RequestParam(required = false) BigDecimal priceTo,
                                                    @RequestParam(required = false) SortType sortBy,
                                                    @RequestParam(required = false) SortType sortPrice,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "50") int size,
                                                    Authentication authentication
    ) {
        tokenUtils.init(authentication);

        return ResponseEntity.ok(dealService.sortFilterAndSearch(searchText, clientId, insOrgId, dateFrom, dateTo, priceFrom,
                priceTo, sortBy, sortPrice, page, size, tokenUtils.getOrganizationId()));
    }
}
