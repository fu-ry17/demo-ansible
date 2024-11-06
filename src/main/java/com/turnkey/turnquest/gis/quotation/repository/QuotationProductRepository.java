package com.turnkey.turnquest.gis.quotation.repository;

import com.turnkey.turnquest.gis.quotation.model.QuotationProduct;
import com.turnkey.turnquest.gis.quotation.model.QuotationProductTax;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuotationProductRepository extends JpaRepository<QuotationProduct, Long> {

    List<QuotationProduct> findByQuotationId(Long quotationId);

    Optional<QuotationProduct> findByForeignId(Long foreignId);
}
