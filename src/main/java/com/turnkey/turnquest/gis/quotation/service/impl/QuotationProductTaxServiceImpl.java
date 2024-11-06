package com.turnkey.turnquest.gis.quotation.service.impl;

import com.turnkey.turnquest.gis.quotation.client.gis.TaxRateClient;
import com.turnkey.turnquest.gis.quotation.dto.gis.TaxRateDto;
import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyTaxDto;
import com.turnkey.turnquest.gis.quotation.enums.TaxRateInstallmentType;
import com.turnkey.turnquest.gis.quotation.model.QuotationProduct;
import com.turnkey.turnquest.gis.quotation.model.QuotationProductTax;
import com.turnkey.turnquest.gis.quotation.model.QuotationRiskTax;
import com.turnkey.turnquest.gis.quotation.repository.QuotationProductTaxRepository;
import com.turnkey.turnquest.gis.quotation.service.QuotationProductTaxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service("quotationProductTaxService")
public class QuotationProductTaxServiceImpl implements QuotationProductTaxService {

    private final QuotationProductTaxRepository quotationProductTaxRepository;
    private final TaxRateClient taxRateClient;

    @Override
    public Optional<QuotationProductTax> find(Long id) {
        return quotationProductTaxRepository.findById(id);
    }

    @Override
    public QuotationProductTax create(QuotationProductTax quotationProductTax) {
        return quotationProductTaxRepository.save(quotationProductTax);
    }

    @Override
    public QuotationProductTax update(QuotationProductTax quotationProductTax, Long id) {
        quotationProductTax.setId(id);
        return quotationProductTaxRepository.save(quotationProductTax);
    }

    @Override
    public List<QuotationProductTax> findByQuotationProductId(Long quotationProductId) {
        return quotationProductTaxRepository.findByQuotationProductId(quotationProductId);
    }

    @Override
    public List<QuotationProductTax> createMultiple(List<QuotationProductTax> addedQuotationProductTaxes) {
        return quotationProductTaxRepository.saveAll(addedQuotationProductTaxes);
    }

    @Override
    public QuotationProductTax updateTaxAmount(BigDecimal totalTaxAmount, Long id) {

        Optional<QuotationProductTax> optionalQuotationProductTax = this.find(id);
        if (optionalQuotationProductTax.isPresent()) {
            QuotationProductTax quotationProductTax = optionalQuotationProductTax.get();
            quotationProductTax.setTaxAmount(totalTaxAmount);
            return this.update(quotationProductTax, id);
        }
        return null;
    }

    @Override
    public List<PolicyTaxDto> convertToPolicyTaxes(List<QuotationProductTax> quotationProductTaxes) {
        // TODO:
        List<PolicyTaxDto> policyTaxes = new ArrayList<>();

        for (QuotationProductTax quotationProductTax : quotationProductTaxes) {
            policyTaxes.add(this.convertToPolicyTax(quotationProductTax));
        }
        return policyTaxes;
    }

    @Override
    public PolicyTaxDto convertToPolicyTax(QuotationProductTax quotationProductTax) {
        PolicyTaxDto policyTaxDto = new PolicyTaxDto();

        policyTaxDto.setTransactionTypeCode(quotationProductTax.getTransactionTypeCode());
        policyTaxDto.setRate(quotationProductTax.getRate());
        policyTaxDto.setAmount(quotationProductTax.getTaxAmount());
        policyTaxDto.setTlLevelCode(quotationProductTax.getTlLvlCode());
        policyTaxDto.setRateType(quotationProductTax.getTaxRateType());
        policyTaxDto.setRateDescription(quotationProductTax.getTaxRateDescription());
        policyTaxDto.setApplicationArea(quotationProductTax.getApplicationArea());
        policyTaxDto.setTaxType(quotationProductTax.getTaxType());
        policyTaxDto.setTaxRateCategory(quotationProductTax.getTaxRateCategory());
        policyTaxDto.setInstallmentType(quotationProductTax.getTaxRateInstallmentType());
        policyTaxDto.setCalculationMode(quotationProductTax.getCalculationMode());
        policyTaxDto.setTransactionTypeCode(quotationProductTax.getTransactionTypeCode());
        policyTaxDto.setProductSubClassId(quotationProductTax.getProductSubClassId());
        policyTaxDto.setDivisionFactor(quotationProductTax.getDivisionFactor());

        return policyTaxDto;
    }

    @Override
    public QuotationProductTax saveQuickProductTax(QuotationProduct quoteProduct, QuotationProductTax quotationProductTax) {
        quotationProductTax.setQuotationProductId(quoteProduct.getId());
        if (quotationProductTax.getTaxRateId() != null) {
            TaxRateDto taxRate = taxRateClient.find(quotationProductTax.getTaxRateId());

            if (quotationProductTax.getId() != null) {
                this.findByQuotationProductId(quoteProduct.getId())
                        .stream().filter(quotationProductTax1 -> quotationProductTax1.getTaxType().equals(taxRate.getTaxType()))
                        .findFirst().ifPresent(productTax -> quotationProductTax.setId(productTax.getId()));

            }
            quotationProductTax.setCalculationMode(taxRate.getCalculationMode());
            quotationProductTax.setTaxRateCategory(taxRate.getCategory());
            quotationProductTax.setTaxRateInstallmentType(taxRate.getInstallmentType());
            quotationProductTax.setTaxRateDescription(taxRate.getRateDesc());
            quotationProductTax.setProductSubClassId(taxRate.getProductSubClassId());
            quotationProductTax.setTaxType(taxRate.getTaxType());
            quotationProductTax.setRiskOrProductLevel(taxRate.getRiskPolLevel());
            quotationProductTax.setTlLvlCode(taxRate.getTlLvlCode());
            quotationProductTax.setTransactionTypeCode(taxRate.getTransactionTypeCode());
            quotationProductTax.setTaxRateType(taxRate.getRateType());
            quotationProductTax.setDivisionFactor(taxRate.getDivisionFactor());
        }

        return this.create(quotationProductTax);
    }

    public List<QuotationProductTax> composeQuoteProductTaxes(QuotationProduct quoteProduct) {
        List<QuotationProductTax> quoteProductTaxes = quoteProduct.getQuotationProductTaxes()
                .stream()
                .filter(quotationProductTax -> quotationProductTax.getTaxRateInstallmentType() == TaxRateInstallmentType.FIRST).collect(Collectors.toList());

        List<String> taxCodes = quoteProduct.getQuotationRisks().stream()
                .flatMap(quotationRisk -> quotationRisk.getQuotationRiskTaxes().stream())
                .map(QuotationRiskTax::getTaxType)
                .distinct()
                .collect(Collectors.toList());

        for (String taxCode : taxCodes) {
            List<QuotationRiskTax> quotationRiskTaxes = quoteProduct.getQuotationRisks().stream()
                    .filter(Objects::nonNull)
                    .flatMap(quotationRisk -> quotationRisk.getQuotationRiskTaxes().stream())
                    .filter(quotationRiskTax -> !quotationRiskTax.getTaxType().equals(taxCode))
                    .collect(Collectors.toList());

            BigDecimal taxAmount = quotationRiskTaxes.stream()
                    .filter(Objects::nonNull)
                    .map(QuotationRiskTax::getTaxAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (!quotationRiskTaxes.isEmpty()) {
                QuotationRiskTax riskTax = quotationRiskTaxes.get(0);
                QuotationProductTax productTax = new QuotationProductTax();
                TaxRateDto taxRate = taxRateClient.find(riskTax.getTaxRateId());

                if (quoteProduct.getId() != null) {
                    this.findByQuotationProductId(quoteProduct.getId())
                            .stream().filter(tax -> tax.getTaxType().equals(taxRate.getTaxType()))
                            .findFirst().ifPresent(quotationProductTax1 -> productTax.setId(quotationProductTax1.getId()));

                }
                productTax.setQuotationProductId(quoteProduct.getId());
                productTax.setApplicationArea(taxRate.getApplicationArea());
                productTax.setCalculationMode(taxRate.getCalculationMode());
                productTax.setTaxRateCategory(taxRate.getCategory());
                productTax.setTaxRateInstallmentType(taxRate.getInstallmentType());
                productTax.setTaxRateDescription(taxRate.getRateDesc());
                productTax.setRate(taxRate.getRate());
                productTax.setProductSubClassId(taxRate.getProductSubClassId());
                productTax.setTaxType(taxRate.getTaxType());
                productTax.setRiskOrProductLevel(taxRate.getRiskPolLevel());
                productTax.setTlLvlCode(taxRate.getTlLvlCode());
                productTax.setTransactionTypeCode(taxRate.getTransactionTypeCode());
                productTax.setTaxRateType(taxRate.getRateType());
                productTax.setTaxAmount(taxAmount);
                productTax.setDivisionFactor(taxRate.getDivisionFactor());
                quoteProductTaxes.add(productTax);
            }

        }

        return quoteProductTaxes;
    }

}
