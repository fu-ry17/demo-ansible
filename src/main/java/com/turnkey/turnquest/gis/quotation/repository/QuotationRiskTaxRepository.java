package com.turnkey.turnquest.gis.quotation.repository;

import com.turnkey.turnquest.gis.quotation.model.QuotationRiskTax;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuotationRiskTaxRepository extends JpaRepository<QuotationRiskTax, Long> {

    List<QuotationRiskTax> findAllByQuotationRiskId(Long quotationRiskId);
}
