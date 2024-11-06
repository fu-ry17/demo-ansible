package com.turnkey.turnquest.gis.quotation.repository;

import com.turnkey.turnquest.gis.quotation.model.Deal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DealRepository extends JpaRepository<Deal, Long> {

    Page<Deal> findAllByOrganizationId(Long organizationId, Pageable pageable);

    Deal findByIdAndOrganizationId(Long id, Long organizationId);

    List<Deal> findAllByQuotationIdAndOrganizationId(Long quotationId, Long organizationId);

    Page<Deal> findAllByClientIdInAndOrganizationId(List<Long> clientIds, Long organizationId, Pageable pageable);

    Page<Deal> findAll(Specification<Deal> spec, Pageable pageable);
}
