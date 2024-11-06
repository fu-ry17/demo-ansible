package com.turnkey.turnquest.gis.quotation.repository;

/**
 * @author sammynerd
 */

import com.turnkey.turnquest.gis.quotation.model.QuotationRiskSection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuotationRiskSectionRepository extends JpaRepository<QuotationRiskSection, Long> {

    /**
     * Find quotation risk sections by quotationRiskId
     *
     * @param quotationRiskId
     * @return
     */
    List<QuotationRiskSection> findAllByQuotationRiskId(Long quotationRiskId);

    /**
     * Find quotation risk sections by quotationRiskId and sectionId
     *
     * @param quotationRiskId
     * @param sectionId
     * @return
     */
    List<QuotationRiskSection> findAllByQuotationRiskIdAndSectionId(Long quotationRiskId, Long sectionId);

    Optional<QuotationRiskSection> findByForeignId(Long foreignId);
}
