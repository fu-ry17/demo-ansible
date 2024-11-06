package com.turnkey.turnquest.gis.quotation.repository;

import com.turnkey.turnquest.gis.quotation.model.QuotationRisk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuotationRiskRepository extends JpaRepository<QuotationRisk, Long> {
    List<QuotationRisk> findByQuotationProduct_Id(Long quotationProductId);

    @Query("select qr from QuotationRisk  qr where qr.quotationProduct.quotation.id = :quotationId")
    List<QuotationRisk> findByQuotationId(@Param("quotationId") Long quotationId);

    Optional<QuotationRisk> findByForeignId(Long foreignId);

    List<QuotationRisk> findAllByRiskId(String riskId);

}
