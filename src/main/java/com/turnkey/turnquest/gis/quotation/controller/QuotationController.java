/**
 * 2018-07-04
 */
package com.turnkey.turnquest.gis.quotation.controller;

import com.turnkey.turnquest.gis.quotation.auth.TokenUtils;
import com.turnkey.turnquest.gis.quotation.config.DmvicValidation;
import com.turnkey.turnquest.gis.quotation.dto.ScheduleDetailsDto;
import com.turnkey.turnquest.gis.quotation.dto.computation.ComputationResponse;
import com.turnkey.turnquest.gis.quotation.dto.gis.ComputationRequest;
import com.turnkey.turnquest.gis.quotation.dto.quotation.QuotationDto;
import com.turnkey.turnquest.gis.quotation.enums.SortType;
import com.turnkey.turnquest.gis.quotation.exception.error.QuoteCreationException;
import com.turnkey.turnquest.gis.quotation.model.Quotation;
import com.turnkey.turnquest.gis.quotation.model.QuotationProduct;
import com.turnkey.turnquest.gis.quotation.service.QuotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.security.RolesAllowed;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @author Paul Gichure
 */
@RestController
@RequestMapping(value = "/quotations")
public class QuotationController {

    private final QuotationService quotationService;
    private final TokenUtils tokenUtils;

    private final DmvicValidation dmvicValidation;

    @Autowired
    public QuotationController(QuotationService quotationService, TokenUtils tokenUtils, DmvicValidation dmvicValidation) {
        this.quotationService = quotationService;
        this.tokenUtils = tokenUtils;
        this.dmvicValidation = dmvicValidation;
    }

    /**
     * @param quotation
     * @param authentication
     * @return
     * @throws Exception
     */
    @RolesAllowed({"quot_create", "agent"})
    @PostMapping()
    public ResponseEntity<Quotation> create(@RequestBody Quotation quotation, Authentication authentication) throws Exception {
        tokenUtils.init(authentication);
        quotation.setOrganizationId(tokenUtils.getOrganizationId());
        quotation = quotationService.create(quotation);
        return new ResponseEntity<>(quotation, HttpStatus.OK);
    }

    /**
     * @param quotation
     * @return
     */
    @RolesAllowed({"quot_create", "platform_admin"})
    @PostMapping("/save")
    public ResponseEntity<Quotation> create(@RequestBody Quotation quotation) throws QuoteCreationException {
        return ResponseEntity.ok(quotationService.save(quotation));
    }

    /**
     * @param authentication
     * @return
     */
    @RolesAllowed({"quot_count_all", "agent"})
    @GetMapping(value = "/count")
    public Long count(Authentication authentication) {
        tokenUtils.init(authentication);
        return quotationService.count(tokenUtils.getOrganizationId());
    }

    /**
     * @param id
     * @param authentication
     * @return
     */
    @RolesAllowed({"quot_get_one", "agent"})
    @GetMapping("/{id}")
    public ResponseEntity<Quotation> find(@PathVariable("id") Long id, Authentication authentication) {
        tokenUtils.init(authentication);
        Optional<Quotation> optionalQuotation = quotationService.findById(id);
        return optionalQuotation
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * @param id
     * @return
     */
    @RolesAllowed({"quot_get_one", "agent", "quotation_service_admin"})
    @GetMapping("/get-by/{id}")
    public ResponseEntity<Quotation> find(@PathVariable("id") Long id) {
        Optional<Quotation> optionalQuotation = quotationService.findById(id);
        return optionalQuotation
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * @param pageable
     * @param authentication
     * @return
     */
    @RolesAllowed({"quot_get_all", "agent"})
    @GetMapping()
    public ResponseEntity<List<Quotation>> all(@PageableDefault(size = 50, page = 0, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication) {
        tokenUtils.init(authentication);
        List<Quotation> quotations = quotationService.findAllQuotations(tokenUtils.getOrganizationId(), pageable);
        return ResponseEntity.ok(quotations);
    }

    /**
     * Fetch draft quotations from endorsements
     *
     * @param pageable
     * @param authentication
     * @return
     */
    @RolesAllowed({"quot_get_end_draft", "agent"})
    @GetMapping("/drafts")
    public ResponseEntity<List<Quotation>> findAllDraftEndorsements(@PageableDefault(size = 50, page = 0, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication) {
        tokenUtils.init(authentication);
        List<Quotation> quotations = quotationService.findAllByCurrentStatusAndStatus("D", "EN", tokenUtils.getOrganizationId(), pageable);
        return ResponseEntity.ok(quotations);
    }

    @RolesAllowed({"quotation_service_admin", "agent"})
    @GetMapping("/draft-quote")
    public ResponseEntity<Quotation> getDraftQuotation(@RequestParam("policyNo") String policyNo) {
        return quotationService.findQuotationDraft(policyNo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    /***
     *
     * @param quotationNo
     * @param authentication
     * @return
     * @throws Exception
     */
    @RolesAllowed("quot_get_by_quote_no")
    @GetMapping(value = "/find")
    public ResponseEntity<List<Quotation>> findByQuotationNo(@RequestParam("quotationNo") String quotationNo, Authentication authentication) {
        tokenUtils.init(authentication);
        return ResponseEntity.ok(quotationService.findByQuotationNo(quotationNo, tokenUtils.getOrganizationId()));
    }

    /**
     * @param pageable
     * @param clientIds
     * @param authentication
     * @return
     */
    @RolesAllowed({"quot_get_by_client_ids", "agent"})
    @PostMapping("filter-by/clients")
    public ResponseEntity<List<Quotation>> findQuotationsByClientIds(@PageableDefault(size = 50, page = 0, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable, @RequestBody List<Long> clientIds, Authentication authentication) {
        tokenUtils.init(authentication);
        return ResponseEntity.ok(quotationService.findByClientIds(clientIds, tokenUtils.getOrganizationId(), pageable));
    }

    /**
     * Get client renewals
     *
     * @param clientIds
     * @param authentication
     * @return
     */
    @RolesAllowed({"quot_get_renewals_by_client_ids", "agent"})
    @PostMapping("filter-by/clients/renewals")
    public ResponseEntity<List<Quotation>> findRenewalsByClientIds(@PageableDefault(size = 50, page = 0, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable, @RequestBody List<Long> clientIds, Authentication authentication) {
        tokenUtils.init(authentication);
        return ResponseEntity.ok(quotationService.findClientRenewals(clientIds, tokenUtils.getOrganizationId(), pageable));
    }

    /**
     * @param quotation
     * @param id
     * @param authentication
     * @return
     */
    @RolesAllowed({"quot_update", "agent"})
    @PutMapping(value = "/{id}")
    public Quotation update(@RequestBody QuotationDto quotation, @PathVariable("id") Long id, Authentication authentication) {
        tokenUtils.init(authentication);
        return quotationService.update(id, quotation, tokenUtils.getOrganizationId());
    }

    /**
     * @param id
     * @return
     */
    @RolesAllowed("quot_delete")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable(value = "id") Long id) {
        quotationService.deleteById(id);
        return new ResponseEntity<>("Quotation deleted successfully", HttpStatus.OK);
    }


    /**
     * @param id
     * @param authentication
     * @return
     */
    @RolesAllowed({"quot_get_quote_prod", "agent"})
    @GetMapping(value = "/{id}/quotation-products")
    public ResponseEntity<List<QuotationProduct>> getQuotationProducts(@PathVariable("id") Long id, Authentication authentication) {
        tokenUtils.init(authentication);
        List<QuotationProduct> quotationProducts = quotationService.findQuotationProducts(id, tokenUtils.getOrganizationId());
        return ResponseEntity.ok(quotationProducts);
    }

    /**
     * @param id
     * @param authentication
     * @return
     */
    @RolesAllowed("quot_convert_to_policies")
    @PostMapping(value = "/{id}/convert")
    public ResponseEntity<String> convertToPolicies(@PathVariable("id") Long id,
                                                    Authentication authentication) {
        tokenUtils.init(authentication);
        quotationService.convertQuotationToPolicies(id, tokenUtils.getOrganizationId());
        return ResponseEntity.ok("Quote conversion started successfully");
    }

    /**
     * @param id
     * @return
     */
    @RolesAllowed("quot_convert_to_policies")
    @PostMapping(value = "/payment/{id}/convert")
    public ResponseEntity<String> convertToPolicies(@PathVariable("id") Long id) {
        quotationService.convertQuotationToPolicies(id);
        return ResponseEntity.ok("Quote conversion started successfully");
    }


    /**
     * @param quotation
     * @param authentication
     * @return
     * @throws Exception
     */
    @RolesAllowed({"quot_save_quick", "agent", "quotation_service_admin"})
    @PostMapping(value = "/save-quick-quote")
    public ResponseEntity<Quotation> saveQuickQuotation(@RequestBody Quotation quotation, Authentication authentication) throws QuoteCreationException {
        var orgId = quotation.getOrganizationId();
        if (orgId == null) {
            tokenUtils.init(authentication);
            orgId = tokenUtils.getOrganizationId();
            quotation.setOrganizationId(orgId);
        }
        return ResponseEntity.ok(quotationService.saveQuickQuotation(quotation, orgId));
    }

    /**
     * @param searchText
     * @param clientId
     * @param insOrgId
     * @param dateFrom
     * @param dateTo
     * @param priceFrom
     * @param priceTo
     * @param sortBy
     * @param sortPrice
     * @param page
     * @param size
     * @param authentication
     * @return
     */
    @RolesAllowed({"quot_sort_filter", "agent"})
    @GetMapping("sort-filter-search")
    public ResponseEntity<List<Quotation>> sortAndFilter(@RequestParam(required = false) String searchText,
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

        return ResponseEntity.ok(quotationService.sortFilterAndSearch(searchText, clientId, insOrgId, dateFrom, dateTo, priceFrom,
                priceTo, sortBy, sortPrice, page, size, tokenUtils.getOrganizationId(), "NB"));
    }

    ///Work around for quote conversion

    @RolesAllowed("quot_sort_filter")
    @GetMapping("conversion/find-by/{clientId}/{organizationId}")
    public ResponseEntity<List<Quotation>> findQuotationsByClientIds(@PathVariable("clientId") Long clientId, @PathVariable("organizationId") Long organizationId) {
        return ResponseEntity.ok(quotationService.getQuotationsByClientIdAndOrgId(clientId, organizationId));
    }

    /**
     * Validates whether risk(s) are aki verified within the quotation
     *
     * @param quotationId
     * @param authentication
     * @return
     * @throws Exception
     */

    @RolesAllowed({"quot_aki_verify", "agent"})
    @GetMapping(value = "/aki/verified/{quotationId}")
    public ResponseEntity<Boolean> verifyQuotation(@PathVariable("quotationId") Long quotationId, Authentication authentication) {

        if (dmvicValidation.getValidation()) {
            tokenUtils.init(authentication);
            return ResponseEntity.ok(quotationService.verifyQuotation(quotationId));
        } else {
            return ResponseEntity.ok(true);
        }

    }

    @RolesAllowed({"quot_undread", "agent"})
    @GetMapping(value = "/unread-quotes")
    public ResponseEntity<Mono<Integer>> findUnreadQuotes(Authentication authentication) {

        tokenUtils.init(authentication);

        return ResponseEntity.ok(quotationService.findUnreadQuotes(tokenUtils.getOrganizationId()));
    }

    /**
     * Gets quotations based on the policy number
     *
     * @param policyNo       policy no
     * @param authentication authentication. Nullable since it is used by the endorsement report
     *                       generation process
     * @return List
     */

    @RolesAllowed({"quot_sort_filter", "quotation_service_admin", "agent"})
    @GetMapping(value = "/policy-no")
    public List<Quotation> getQuotationsByPolicyNo(@RequestParam("policyNumber") String policyNo,
                                                   Authentication authentication) {
        if (authentication != null)
            tokenUtils.init(authentication);

        return quotationService.getByPolicyNo(policyNo);
    }

    @RolesAllowed({"quot_delete", "quotation_service_admin"})
    @DeleteMapping(value = "/policy-no")
    public ResponseEntity<Boolean> deleteQuotationsByPolicyNo(@RequestParam("policyNumber") String policyNo,
                                                              Authentication authentication) {
        if (authentication != null)
            tokenUtils.init(authentication);

        return ResponseEntity.ok(quotationService.deleteByPolicyNo(policyNo));
    }

    @RolesAllowed({"quot_get_by_pol", "quotation_service_admin"})
    @GetMapping(value = "/find-by/policy-no")
    public ResponseEntity<Quotation> findQuotationsByPolicyNo(@RequestParam("policyNumber") String policyNo, Authentication authentication) {
        if (authentication != null)
            tokenUtils.init(authentication);

        return ResponseEntity.ok(quotationService.findByPolicyNo(policyNo));
    }

    @RolesAllowed("quot_get_by_quot_no")
    @GetMapping(value = "/quotation-no")
    public ResponseEntity<List<Quotation>> getQuotationsByQuotationNo(@RequestParam("quotationNo") String quotationNo) {
        return ResponseEntity.ok(quotationService.findByQuotationNo(quotationNo));
    }

    @DeleteMapping("{quotationId}/risk/{riskId}")
    public ResponseEntity<Quotation> deleteRisk(@PathVariable Long riskId, @PathVariable Long quotationId) {
        return ResponseEntity.ok(quotationService.deleteQuotationRisk(riskId, quotationId));
    }

    @RolesAllowed("platform-admin")
    @GetMapping("find-schedule-details")
    public ResponseEntity<ScheduleDetailsDto> findScheduleDetails(@RequestParam() String propertyId) {
        return ResponseEntity.ok(quotationService.findScheduleDetails(propertyId));
    }

    @RolesAllowed({"platform-admin", "agent"})
    @PostMapping("/comparison-quote")
    public ResponseEntity<Quotation> createComputationQuotation(@RequestBody ComputationResponse computationResponse,
                                                                @RequestParam(required = false) Long quotationId,
                                                                Authentication authentication) {
        if (authentication != null)
            tokenUtils.init(authentication);
        return ResponseEntity.ok(quotationService.createComputationQuotation(computationResponse, quotationId, tokenUtils.getOrganizationId()));
    }

    @RolesAllowed({"platform-admin", "agent"})
    @GetMapping("create-comparison-object/{quotationId}")
    public ResponseEntity<List<ComputationRequest>> createComparisonQuoteRequestObject(@PathVariable Long quotationId, Authentication authentication) {
        if (authentication != null)
            tokenUtils.init(authentication);
        return ResponseEntity.ok(quotationService.createComparisonRequestObject(quotationId));
    }
}
