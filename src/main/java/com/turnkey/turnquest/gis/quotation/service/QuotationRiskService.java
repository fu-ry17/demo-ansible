package com.turnkey.turnquest.gis.quotation.service;

import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyRiskDto;
import com.turnkey.turnquest.gis.quotation.model.*;

import java.util.List;
import java.util.Optional;

/**
 *
 */
public interface QuotationRiskService {
    /**
     * @param quotationRisk
     * @return
     */
    QuotationRisk create(QuotationRisk quotationRisk);

    /**
     * @param quotationRisk
     * @param id
     * @return
     */
    QuotationRisk update(QuotationRisk quotationRisk, Long id);

    /**
     * @param id
     * @return
     */
    boolean delete(Long id);


    /**
     * @param id
     * @return
     */
    Optional<QuotationRisk> find(Long id);

    /**
     * @param quotationProductId
     * @return
     */
    List<QuotationRisk> findByQuotationProductId(Long quotationProductId);

    /**
     * @param quotationId
     * @return
     */
    List<QuotationRisk> findByQuotationId(Long quotationId);

    /**
     * @param quotationRisks
     * @return
     */
    List<QuotationRisk> saveMultiple(List<QuotationRisk> quotationRisks);

    /**
     * @param id
     * @return
     */
    List<QuotationRiskSection> getQuotationRiskSections(Long id);

    /**
     * @param quotationRisk
     * @return
     */
    List<QuotationRiskSection> populateDefaultSections(QuotationRisk quotationRisk);

    /**
     * @param id
     * @param quotationRiskSection
     * @return
     */
    QuotationRiskSection createOrUpdateQuotationRiskSection(Long id, QuotationRiskSection quotationRiskSection);

    /**
     * @param quotationRisks
     * @return
     */
    List<PolicyRiskDto> convertToPolicyRisks(List<QuotationRisk> quotationRisks);

    /**
     * @param quotationRisk
     * @return
     */
    PolicyRiskDto convertToPolicyRisk(QuotationRisk quotationRisk);

    /**
     * Update a quotation Risk with the certificate no
     *
     * @param certificateNo   certificate No
     * @param quotationRiskId quotation Risk Id
     * @return QuotationRisk
     */

    QuotationRisk updateCertificateNo(String certificateNo, Long quotationRiskId);

    /**
     * Find quotation Risk Taxes
     *
     * @param id Risk id
     * @return taxes
     */
    List<QuotationRiskTax> findTaxes(Long id);

    /**
     * Save quick quotation risk
     *
     * @param quotation        quotation object
     * @param quotationProduct quotation product
     * @param quoteProduct     quick quote product
     * @param quotationRisk    quick quotation risk
     * @return
     */
    QuotationRisk saveQuickQuotationRisk(Quotation quotation, QuotationProduct quotationProduct, QuotationProduct quoteProduct, QuotationRisk quotationRisk);

    List<QuotationRisk> findRisksByRegistrationNo(String registrationNo);
}
