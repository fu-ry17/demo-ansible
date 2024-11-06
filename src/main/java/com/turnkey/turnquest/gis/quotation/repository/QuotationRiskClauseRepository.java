package com.turnkey.turnquest.gis.quotation.repository;

import com.turnkey.turnquest.gis.quotation.model.QuotationRiskClause;
import com.turnkey.turnquest.gis.quotation.model.id.QuotationClauseId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuotationRiskClauseRepository extends JpaRepository<QuotationRiskClause, QuotationClauseId> {

    List<QuotationRiskClause> findByQuotationRiskId(Long quotationRiskId);

}
