package com.turnkey.turnquest.gis.quotation.repository;

import com.turnkey.turnquest.gis.quotation.enums.QuotationReportType;
import com.turnkey.turnquest.gis.quotation.model.QuotationReports;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuotationReportsRepository extends JpaRepository<QuotationReports,Long> {

    List<QuotationReports> findByQuotationIdAndFileCategory(Long quotationId,QuotationReportType quotationReportType);

    List<QuotationReports> findByQuotationId(Long quotationId);

    List<QuotationReports> findBySentFalse();

    List<QuotationReports> findByPolicyId(Long policyId);

    Optional<QuotationReports> findTopByQuotationIdAndPolicyIdIsNotNull(Long quotationId);

    List<QuotationReports> findByPolicyIdAndFileCategoryOrderByCreatedDateDesc(Long policyId, QuotationReportType quotationReportType);

    List<QuotationReports> findByQuotationIdAndFileCategoryOrderByCreatedDateDesc(Long quotationId, QuotationReportType quotationReportType);

    List<QuotationReports> findByPolicyNumber(String policyNo);

}
