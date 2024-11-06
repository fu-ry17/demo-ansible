/**
 * 2018-07-04
 */
package com.turnkey.turnquest.gis.quotation.repository;

import com.turnkey.turnquest.gis.quotation.model.Quotation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author Paul Gichure
 */
public interface QuotationRepository extends JpaRepository<Quotation, Long> {

    Optional<Quotation> findByPaymentRef(String paymentRef);

    List<Quotation> findByQuotationNoAndOrganizationId(String quotationNo, Long organizationId);

    Page<Quotation> findAllByOrganizationIdOrderByQuotationNoAscCreatedDateDesc(Long organizationId, Pageable pageable);

    /**
     * For handling Lazy Initialization exception using dynamic projection
     * https://www.baeldung.com/spring-data-jpa-projections
     *
     * @param quotationId
     * @param type
     * @param <T>
     * @return
     */
    @Query("SELECT q FROM Quotation q WHERE q.id = :quotationId")
    <T> T getQuoteById(@Param("quotationId") Long quotationId, Class<T> type);

    Long countByOrganizationId(Long organizationId);

    Optional<Quotation> findByIdAndOrganizationId(Long id, Long organization);

    Optional<Quotation> findByIdAndAgencyId(Long id, Long organization);

    Page<Quotation> findByOrganizationIdAndStatus(Long organizationId, String status, Pageable pageable);

    Page<Quotation> findAllByCurrentStatusAndStatusAndOrganizationId(String currentStatus, String status, Long organizationId, Pageable pageable);

    List<Quotation> findAllByAgencyId(Long agencyId);

    Page<Quotation> findAllByClientIdInAndOrganizationId(List<Long> clientIds, Long organizationId, Pageable pageable);

    Page<Quotation> findAllByClientIdInAndOrganizationIdAndStatus(List<Long> clientIds, Long organizationId, String status, Pageable pageable);

    Page<Quotation> findAll(Specification<Quotation> spec, Pageable pageable);

    List<Quotation> findByClientIdAndOrganizationIdAndCurrentStatus(Long clientId, Long organizationId, String currentStatus);

    List<Quotation> findAllByOrganizationIdAndCreatedDateIsGreaterThanAndStatus(Long organizationId, Long startDate, String status);

    List<Quotation> findAllByOrganizationIdAndCreatedDateIsGreaterThanAndCreatedDateIsLessThanAndStatus(Long organizationId, Long startDate, Long endDate, String status);

    Optional<Quotation> findByRenewalBatchNo(Long batchNo);

    List<Quotation> findAllByReadStatusAndStatusAndOrganizationId(boolean read, String status, Long organizationId);

    List<Quotation> findByPolicyNo(String policyNo);

    List<Quotation> findByPolicyNoOrderByIdDesc(String policyNo);

    List<Quotation> findByQuotationNo(String quotationNo);

    List<Quotation> findByQuotationNoOrderByIdDesc(String quotationNo);

    Optional<Quotation> findTopByPolicyNoAndStatusAndCurrentStatusOrderByIdDesc(String policyNo, String status, String currentStatus);

    @Query("SELECT q FROM Quotation q WHERE q.quotationNo = :quotationNo AND q.organizationId = :organizationId ORDER BY q.createdDate DESC")
    Page<Quotation> findActiveQuotationByQuotationNumber(String quotationNo, Long organizationId, Pageable pageable);
}
