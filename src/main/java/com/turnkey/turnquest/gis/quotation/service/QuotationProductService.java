package com.turnkey.turnquest.gis.quotation.service;

import com.turnkey.turnquest.gis.quotation.dto.quotation.UpdateQuotationDto;
import com.turnkey.turnquest.gis.quotation.dto.quotation.UpdateInstallmentDto;
import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import com.turnkey.turnquest.gis.quotation.model.Quotation;
import com.turnkey.turnquest.gis.quotation.model.QuotationProduct;
import com.turnkey.turnquest.gis.quotation.model.QuotationRisk;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

/**
 *
 */
public interface QuotationProductService {
    /**
     * @param id
     * @return
     */
    Optional<QuotationProduct> find(Long id);

    /**
     * @param quotationProduct
     * @return
     */
    QuotationProduct create(QuotationProduct quotationProduct);

    /**
     * @param quotationProduct
     * @param id
     * @return
     */
    QuotationProduct update(QuotationProduct quotationProduct, Long id);

    /**
     * @param quotationId
     * @return
     */
    List<QuotationProduct> findByQuotationId(Long quotationId);

    /**
     * @param id
     * @return
     */
    void convertToPolicy(Long id) throws Exception;

    /**
     * @param quotationProduct
     * @return
     */
    void convertToPolicy(QuotationProduct quotationProduct) throws ParseException;

    /**
     * @param id
     * @return
     */
    List<QuotationRisk> getQuotationRisks(Long id);

    /**
     * Updates the installment amount and commission installment amount
     * @param updateInstallmentDto UpdateInstallmentDto
     * @return QuotationProduct
     */

    QuotationProduct updateInstallmentAmounts(UpdateInstallmentDto updateInstallmentDto);


    QuotationProduct saveQuickQuotationProduct(Quotation quotation, QuotationProduct quotationProduct);

    QuotationProduct computeWithHoldingTax(QuotationProduct quotationProduct, Long organizationId);

    /**
     * Change the payment option from installment based to annual and vice versa
     * @param quotationProductId QuotationProductId
     * @param installmentAllowed Y or N String value
     * @return QuotationProduct
     */

    QuotationProduct changePaymentOption(Long quotationProductId, YesNo installmentAllowed);

    Quotation deleteQuotationProductRisk(Quotation quotation, Long riskId);

    void updateQuotationProduct(UpdateQuotationDto updateQuotationDto);
}
