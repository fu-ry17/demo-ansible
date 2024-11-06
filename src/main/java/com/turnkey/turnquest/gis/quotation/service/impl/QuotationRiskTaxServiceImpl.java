package com.turnkey.turnquest.gis.quotation.service.impl;

import com.turnkey.turnquest.gis.quotation.client.gis.TaxRateClient;
import com.turnkey.turnquest.gis.quotation.dto.gis.TaxRateDto;
import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyRiskTaxDto;
import com.turnkey.turnquest.gis.quotation.model.QuotationRisk;
import com.turnkey.turnquest.gis.quotation.model.QuotationRiskTax;
import com.turnkey.turnquest.gis.quotation.repository.QuotationRiskTaxRepository;
import com.turnkey.turnquest.gis.quotation.service.QuotationRiskTaxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service("quotationRiskTaxService")
public class QuotationRiskTaxServiceImpl implements QuotationRiskTaxService {

    private final QuotationRiskTaxRepository quotationRiskTaxRepository;
    private final TaxRateClient taxRateClient;

    @Override
    public Optional<QuotationRiskTax> find(Long id) {
        return quotationRiskTaxRepository.findById(id);
    }

    @Override
    public QuotationRiskTax create(QuotationRiskTax quotationRiskTax) {
        return quotationRiskTaxRepository.save(quotationRiskTax);
    }

    @Override
    public QuotationRiskTax update(QuotationRiskTax quotationRiskTax, Long id) {
        quotationRiskTax.setId(id);
        return quotationRiskTaxRepository.save(quotationRiskTax);
    }

    @Override
    public List<QuotationRiskTax> findByQuotationRiskId(Long quotationRiskId) {
        return quotationRiskTaxRepository.findAllByQuotationRiskId(quotationRiskId);
    }

    @Override
    public List<QuotationRiskTax> createMultiple(List<QuotationRiskTax> addedQuotationRiskTaxes) {
        return quotationRiskTaxRepository.saveAll(addedQuotationRiskTaxes);
    }

    @Override
    public QuotationRiskTax updateTaxAmount(BigDecimal totalTaxAmount, Long id) {

        Optional<QuotationRiskTax> optionalQuotationRiskTax = this.find(id);
        if (optionalQuotationRiskTax.isPresent()) {
            QuotationRiskTax quotationRiskTax = optionalQuotationRiskTax.get();
            quotationRiskTax.setTaxAmount(totalTaxAmount);
            return this.update(quotationRiskTax, id);
        }
        return null;
    }

    @Override
    public List<PolicyRiskTaxDto> convertToPolicyRiskTaxes(List<QuotationRiskTax> quotationRiskTaxes) {
        List<PolicyRiskTaxDto> policyTaxes = new ArrayList<>();

        for (QuotationRiskTax quotationRiskTax : quotationRiskTaxes) {
            policyTaxes.add(this.convertToPolicyRiskTax(quotationRiskTax));
        }
        return policyTaxes;
    }

    @Override
    public PolicyRiskTaxDto convertToPolicyRiskTax(QuotationRiskTax quotationRiskTax) {
        PolicyRiskTaxDto policyRiskTaxDto = new PolicyRiskTaxDto();

        policyRiskTaxDto.setTransactionTypeCode(quotationRiskTax.getTransactionTypeCode());
        policyRiskTaxDto.setRate(quotationRiskTax.getRate());
        policyRiskTaxDto.setAmount(quotationRiskTax.getTaxAmount());
        policyRiskTaxDto.setTlLevelCode(quotationRiskTax.getTlLvlCode());
        policyRiskTaxDto.setRateType(quotationRiskTax.getTaxRateType());
        policyRiskTaxDto.setRateDescription(quotationRiskTax.getTaxRateDescription());
        policyRiskTaxDto.setApplicationArea(quotationRiskTax.getApplicationArea());
        policyRiskTaxDto.setTaxType(quotationRiskTax.getTaxType());
        policyRiskTaxDto.setTaxRateCategory(quotationRiskTax.getTaxRateCategory());
        policyRiskTaxDto.setTaxRateInstallmentType(quotationRiskTax.getTaxRateInstallmentType());
        policyRiskTaxDto.setCalculationMode(quotationRiskTax.getCalculationMode());
        policyRiskTaxDto.setTransactionTypeCode(quotationRiskTax.getTransactionTypeCode());
        policyRiskTaxDto.setProductSubClassId(quotationRiskTax.getProductSubClassId());
        policyRiskTaxDto.setDivisionFactor(quotationRiskTax.getDivisionFactor());
        policyRiskTaxDto.setTaxRateId(quotationRiskTax.getTaxRateId());

        return policyRiskTaxDto;
    }

    @Override
    public QuotationRiskTax saveQuickQuotationRiskTax(QuotationRisk quoteRisk, QuotationRiskTax quotationRiskTax) {
        quotationRiskTax.setQuotationRiskId(quoteRisk.getId());
        if (quotationRiskTax.getTaxRateId() != null) {
            TaxRateDto taxRate = taxRateClient.find(quotationRiskTax.getTaxRateId());
            quotationRiskTax.setApplicationArea(taxRate.getApplicationArea());
            quotationRiskTax.setCalculationMode(taxRate.getCalculationMode());
            quotationRiskTax.setTaxRateCategory(taxRate.getCategory());
            quotationRiskTax.setTaxRateInstallmentType(taxRate.getInstallmentType());
            quotationRiskTax.setTaxRateDescription(taxRate.getRateDesc());
            quotationRiskTax.setRate(taxRate.getRate());
            quotationRiskTax.setProductSubClassId(taxRate.getProductSubClassId());
            quotationRiskTax.setTaxType(taxRate.getTaxType());
            quotationRiskTax.setRiskOrProductLevel(taxRate.getRiskPolLevel());
            quotationRiskTax.setTlLvlCode(taxRate.getTlLvlCode());
            quotationRiskTax.setTransactionTypeCode(taxRate.getTransactionTypeCode());
            quotationRiskTax.setTaxRateType(taxRate.getRateType());
            quotationRiskTax.setDivisionFactor(taxRate.getDivisionFactor());
        }
        return this.create(quotationRiskTax);
    }
}
