package com.turnkey.turnquest.gis.quotation.repository;

import com.turnkey.turnquest.gis.quotation.model.QuotationProductTax;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuotationProductTaxRepository extends JpaRepository<QuotationProductTax, Long> {

    List<QuotationProductTax> findByQuotationProductId(Long quotationProductId);

}
