package com.turnkey.turnquest.gis.quotation.repository;

import com.turnkey.turnquest.gis.quotation.model.AkiResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AkiResponseRepository extends JpaRepository<AkiResponse,Long>  {

    List<AkiResponse> findByQuotationId(Long quotationId);

    Optional<AkiResponse> findTopByRiskIdAndQuotationId(String riskId, Long quotationId);

}
