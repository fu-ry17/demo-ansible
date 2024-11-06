package com.turnkey.turnquest.gis.quotation.repository;

import com.turnkey.turnquest.gis.quotation.model.QuotationRevision;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationRevisionRepository extends JpaRepository<QuotationRevision, Long> {
}
