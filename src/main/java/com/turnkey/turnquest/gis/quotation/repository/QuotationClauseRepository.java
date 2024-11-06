package com.turnkey.turnquest.gis.quotation.repository;

import com.turnkey.turnquest.gis.quotation.model.QuotationClause;
import com.turnkey.turnquest.gis.quotation.model.id.QuotationClauseId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuotationClauseRepository extends JpaRepository<QuotationClause, QuotationClauseId> {

    List<QuotationClause> findByQuotationId(Long quotationId);

}
