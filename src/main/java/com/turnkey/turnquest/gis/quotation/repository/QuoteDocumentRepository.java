package com.turnkey.turnquest.gis.quotation.repository;

import com.turnkey.turnquest.gis.quotation.model.QuoteDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuoteDocumentRepository extends JpaRepository<QuoteDocument,Long> {

    /**
     *
     * @param id
     * @param organizationId
     * @return
     */
    Optional<QuoteDocument> findByIdAndOrganizationId(Long id, Long organizationId);

    /**
     *
     * @param organizationId
     * @return
     */
    List<QuoteDocument> findAllByOrganizationId(Long organizationId);

    /**
     *
     * @param organizationId
     * @param quotationId
     * @return
     */
    List<QuoteDocument> findAllByOrganizationIdAndQuotationId(Long organizationId, Long quotationId);

    /**
     *
     * @param quotationNo
     * @return
     */
    List<QuoteDocument> findAllByQuotationNoAndOrganizationId(String quotationNo, Long organizationId);

}
