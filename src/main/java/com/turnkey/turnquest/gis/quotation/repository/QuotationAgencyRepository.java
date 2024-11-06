package com.turnkey.turnquest.gis.quotation.repository;

import com.turnkey.turnquest.gis.quotation.model.QuotationAgency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuotationAgencyRepository extends JpaRepository<QuotationAgency, Long> {

    Optional<QuotationAgency> findByQuotationId(Long quotationId);
}


