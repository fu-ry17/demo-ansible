package com.turnkey.turnquest.gis.quotation.controller;

import com.turnkey.turnquest.gis.quotation.auth.TokenUtils;
import com.turnkey.turnquest.gis.quotation.dto.notification.PushNotificationDto;
import com.turnkey.turnquest.gis.quotation.enums.SortType;
import com.turnkey.turnquest.gis.quotation.event.producer.NotificationProducer;
import com.turnkey.turnquest.gis.quotation.model.Quotation;
import com.turnkey.turnquest.gis.quotation.service.QuotationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.security.RolesAllowed;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/renewals")
public class RenewalController {

    private final QuotationService quotationService;
    private final TokenUtils tokenUtils;
    private final NotificationProducer notificationProducer;

    @Autowired
    public RenewalController(QuotationService quotationService, TokenUtils tokenUtils, NotificationProducer notificationProducer) {
        this.quotationService = quotationService;
        this.tokenUtils = tokenUtils;
        this.notificationProducer = notificationProducer;
    }

    /**
     * Save a policy as a renewal quotation for renewal purposes
     * Status is RN
     * Send a notification after renewal creation process is dome
     *
     * @param quotation
     * @return
     * @throws Exception
     */
    @RolesAllowed("quot_renewal_create")
    @PostMapping("/conversion")
    public ResponseEntity<Quotation> createRenewal(@RequestBody @Valid Quotation quotation) throws Exception {
        Quotation quote = quotationService.saveQuickQuotation(quotation, quotation.getOrganizationId());

        PushNotificationDto pushNotificationDto = new PushNotificationDto();
        pushNotificationDto.setTemplateCode("RN_CONVERSION_COMPLETE");
        pushNotificationDto.setId(quote.getId().toString());
        pushNotificationDto.setOrganizationId(quote.getOrganizationId());
        pushNotificationDto.setAttributes(Collections.singletonMap("[POLICY_NO]", quote.getPolicyNo()));
        notificationProducer.queuePushNotification(pushNotificationDto);

        return ResponseEntity.ok(quote);
    }

    @RolesAllowed({"quot_renewal_get_all", "agent"})
    @GetMapping("get/renewals")
    public ResponseEntity<List<Quotation>> getRenewals(@PageableDefault(size = 50, page = 0, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication) {
        tokenUtils.init(authentication);
        return ResponseEntity.ok(quotationService.getRenewals(tokenUtils.getOrganizationId(), pageable));
    }

    @RolesAllowed({"quot_renewal_sort_filter", "agent"})
    @GetMapping("sort-filter-search")
    public ResponseEntity<List<Quotation>> sortAndFilter(@RequestParam(required = false) String searchText,
                                                         @RequestParam(required = false) Long clientId,
                                                         @RequestParam(required = false) Long insOrgId,
                                                         @RequestParam(required = false) Long coverFromDate,
                                                         @RequestParam(required = false) Long coverToDate,
                                                         @RequestParam(required = false) BigDecimal priceFrom,
                                                         @RequestParam(required = false) BigDecimal priceTo,
                                                         @RequestParam(required = false) SortType sortBy,
                                                         @RequestParam(required = false) SortType sortPrice,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "50") int size,
                                                         Authentication authentication
    ) {
        tokenUtils.init(authentication);

        return ResponseEntity.ok(quotationService.sortFilterAndSearch(searchText, clientId, insOrgId, coverFromDate, coverToDate, priceFrom,
                priceTo, sortBy, sortPrice, page, size, tokenUtils.getOrganizationId(), "RN"));
    }

    @RolesAllowed({"quot_renewal_get_to_date", "agent"})
    @GetMapping("renewals-to-date")
    public ResponseEntity<Integer> getNoRenewalsFromDate(@RequestParam Long startDate,
                                                         @RequestParam(required = false) Long endDate,
                                                         Authentication authentication) {
        tokenUtils.init(authentication);
        return ResponseEntity.ok(quotationService.getNoRenewalsFromDate(startDate, endDate, tokenUtils.getOrganizationId()));
    }

    @RolesAllowed({"quot_renewal_get_undread", "agent"})
    @GetMapping(value = "/unread-renewals", consumes = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public ResponseEntity<Mono<Integer>> findUnreadRenewals(Authentication authentication) {

        tokenUtils.init(authentication);

        return ResponseEntity.ok(quotationService.findUnreadRenewals(tokenUtils.getOrganizationId()));
    }
}
