/**
 * 2018-07-04
 */
package com.turnkey.turnquest.gis.quotation.service;

import com.turnkey.turnquest.gis.quotation.dto.ScheduleDetailsDto;
import com.turnkey.turnquest.gis.quotation.dto.computation.ComputationResponse;
import com.turnkey.turnquest.gis.quotation.dto.gis.ComputationRequest;
import com.turnkey.turnquest.gis.quotation.dto.quotation.PremiumCardDto;
import com.turnkey.turnquest.gis.quotation.dto.quotation.QuotationDto;
import com.turnkey.turnquest.gis.quotation.dto.quotation.UpdateQuotationDto;
import com.turnkey.turnquest.gis.quotation.enums.SortType;
import com.turnkey.turnquest.gis.quotation.exception.error.FailedToGenerateTransmittalException;
import com.turnkey.turnquest.gis.quotation.exception.error.QuoteCreationException;
import com.turnkey.turnquest.gis.quotation.model.Quotation;
import com.turnkey.turnquest.gis.quotation.model.QuotationProduct;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

/**
 * @author Paul Gichure
 */
public interface QuotationService {

    /**
     * @param quotation
     * @return
     * @throws QuoteCreationException
     */
    Quotation save(Quotation quotation) throws QuoteCreationException;

    Optional<Quotation> findByPaymentRef(String paymentRef);

    /**
     * @param quotation
     * @return
     */
    Quotation create(Quotation quotation);

    /**
     * @param id
     * @param organizationId
     * @return
     */
    Optional<Quotation> findById(Long id, Long organizationId);

    /**
     * @param id
     * @return
     */
    Optional<Quotation> findById(Long id);

    /**
     * @param id
     */
    void deleteById(Long id);

    /**
     * @param organizationId
     * @param pageable
     * @return
     */
    List<Quotation> findAll(Long organizationId, Pageable pageable);

    /**
     * @param quotationNo
     * @param organizationId
     * @return
     * @throws Exception
     */
    List<Quotation> findByQuotationNo(String quotationNo, Long organizationId);

    /**
     * @param clientIds
     * @param organizationId
     * @param pageable
     * @return
     */
    List<Quotation> findByClientIds(List<Long> clientIds, Long organizationId, Pageable pageable);

    /**
     * Get all renewals based on the client ids
     *
     * @param clientIds
     * @param organizationId
     * @return
     */

    List<Quotation> findClientRenewals(List<Long> clientIds, Long organizationId, Pageable pageable);

    /**
     * @param id
     * @param quotation
     * @param organizationId
     * @return
     */
    Quotation update(Long id, QuotationDto quotation, Long organizationId);

    /**
     * @param organizationId
     * @return
     */
    Long count(Long organizationId);


    /**
     * @param insurerOrgId
     * @param productId
     * @return
     */
    String generateQuotationNumber(Long insurerOrgId, Long productId);

    /**
     * @param organization
     * @param pageable
     * @return
     */
    List<Quotation> findAllQuotations(Long organization, Pageable pageable);


    List<Quotation> findAllByCurrentStatusAndStatus(String currentStatus, String status, Long organization, Pageable pageable);


    /**
     * @param id
     * @param organizationId
     * @return
     */
    List<QuotationProduct> findQuotationProducts(Long id, Long organizationId);

    /**
     * @param id
     * @param organizationId
     * @return
     */
    void convertQuotationToPolicies(Long id, Long organizationId);

    /**
     * @param id
     * @return
     */
    void convertQuotationToPolicies(Long id);

    void convertQuoteToPolicies(Long id, Long receiptId, String sourceRef);

    /**
     * @param quotation
     * @param organizationId
     * @return
     * @throws ParseException
     */
    void convertQuotationToPolicies(Quotation quotation, Long organizationId);


    /**
     * @param quotation
     * @param organizationId
     * @return
     * @throws Exception
     */
    Quotation saveQuickQuotation(Quotation quotation, Long organizationId) throws QuoteCreationException;

    /**
     * @param organizationId
     * @param pageable
     * @return
     */
    List<Quotation> getRenewals(Long organizationId, Pageable pageable);

    /**
     * @param quotation
     * @return
     * @throws FailedToGenerateTransmittalException
     */
    PremiumCardDto initPayment(Quotation quotation) throws FailedToGenerateTransmittalException;


    List<Quotation> sortFilterAndSearch(String searchText, Long clientId, Long insOrgId, Long coverFromDate,
                                        Long coverToDate, BigDecimal priceFrom, BigDecimal priceTo, SortType sortBy,
                                        SortType sortPrice, int page, int size, Long organizationId, String status);

    List<Quotation> getQuotationsByClientIdAndOrgId(Long clientId, Long organizationId);

    Boolean verifyQuotation(Long quotationId);

    int getNoRenewalsFromDate(Long startDate, Long endDate, Long organizationId);

    Optional<Quotation> findByRenewalBatchNo(Long batchNo);

    Mono<Integer> findUnreadQuotes(Long organizationId);

    Mono<Integer> findUnreadRenewals(Long organizationId);

    /**
     * Get quotations based on the policy no
     *
     * @param policyNo policy no
     * @return list of quotations
     */

    List<Quotation> getByPolicyNo(String policyNo);

    /**
     * Delete quotations based on the policy no
     *
     * @param policyNo policy no
     * @return list of quotations
     */
    boolean deleteByPolicyNo(String policyNo);

    /**
     * Find quotations based on the policy no
     *
     * @param policyNo policy no
     * @return list of quotations
     */
    Quotation findByPolicyNo(String policyNo);

    /**
     * Temporary work around for getting schedules due to lack of policy schedules
     *
     * @param quotationNo
     * @return
     */

    List<Quotation> findByQuotationNo(String quotationNo);

    Optional<Quotation> findQuotationDraft(String policyNo);

    Quotation deleteQuotationRisk(Long riskId, Long quotationId);

    ScheduleDetailsDto findScheduleDetails(String propertyId);

    Quotation createComputationQuotation(ComputationResponse computationResponse, Long quotationId, Long organizationId);

    List<ComputationRequest> createComparisonRequestObject(Long quotationId);

    void updateQuotation(UpdateQuotationDto updateQuotationDto);
}
