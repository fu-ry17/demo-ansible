package com.turnkey.turnquest.gis.quotation.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turnkey.turnquest.gis.quotation.client.crm.AgencyClient;
import com.turnkey.turnquest.gis.quotation.client.crm.ClientDataClient;
import com.turnkey.turnquest.gis.quotation.client.crm.OrganizationClient;
import com.turnkey.turnquest.gis.quotation.client.endorsement.EndorsementClient;
import com.turnkey.turnquest.gis.quotation.dto.crm.AgencyDto;
import com.turnkey.turnquest.gis.quotation.dto.crm.ClientDto;
import com.turnkey.turnquest.gis.quotation.dto.crm.OrganizationDto;
import com.turnkey.turnquest.gis.quotation.dto.endorsement.DocumentDto;
import com.turnkey.turnquest.gis.quotation.dto.endorsement.PolicyActiveRiskDto;
import com.turnkey.turnquest.gis.quotation.dto.endorsement.PolicyActiveRiskSectionDto;
import com.turnkey.turnquest.gis.quotation.dto.endorsement.PolicyActiveRiskTaxDto;
import com.turnkey.turnquest.gis.quotation.enums.ValuationStatus;
import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import com.turnkey.turnquest.gis.quotation.exception.error.StringSerializationException;
import com.turnkey.turnquest.gis.quotation.model.Quotation;
import com.turnkey.turnquest.gis.quotation.model.QuotationProduct;
import com.turnkey.turnquest.gis.quotation.model.QuotationProductTax;
import com.turnkey.turnquest.gis.quotation.model.QuotationRisk;
import com.turnkey.turnquest.gis.quotation.service.EndorsementService;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service("endorsementService")
public class EndorsementServiceImpl extends AbstractQuotationService implements EndorsementService {

    private final EndorsementClient endorsementClient;

    private final ClientDataClient clientDataClient;

    private final AgencyClient agencyClient;


    private final OrganizationClient organizationClient;


    String PRODUCT_APPLICATION_AREA = "P";

    public EndorsementServiceImpl(
            EndorsementClient endorsementClient, ClientDataClient clientDataClient, AgencyClient agencyClient, OrganizationClient organizationClient
    ) {
        super(clientDataClient, agencyClient, organizationClient);
        this.endorsementClient = endorsementClient;
        this.clientDataClient = clientDataClient;
        this.agencyClient = agencyClient;
        this.organizationClient = organizationClient;
    }

    @Override
    public List<PolicyActiveRiskDto> saveActiveRisks(Quotation quotation, YesNo isAnnual) {
        var policyActiveRisks = composeActiveRisks(quotation, isAnnual);
        log.info("Policy Active Risks: {}", new ObjectMapper().valueToTree(policyActiveRisks));
        return endorsementClient.saveAllActiveRisk(policyActiveRisks);
    }

    @Override
    public Quotation saveAnnualQuotationRisk(Quotation quotation, YesNo isAnnual) {
        var policyActiveRisks = composeActiveRisks(quotation, isAnnual);
        return endorsementClient.saveAnnualActiveRisk(policyActiveRisks);
    }

    @Override
    public Quotation computeFirstInstallment(Quotation quotation) {
        try {
            this.saveActiveRisks(quotation, YesNo.Y);
            return endorsementClient.computeQuoteFirstInstallment(quotation.getQuotationNo());
        } catch (FeignException e) {
            log.error("Error occurred while computing first installment: {}", e.getMessage());
            throw new RuntimeException("Error occurred while computing first installment", e);
        }
    }

    @Override
    public Quotation getQuotationToPay(String quotationNumber) {
        return endorsementClient.findActiveRiskByQuotationNumber(quotationNumber);
    }

    private List<PolicyActiveRiskDto> composeActiveRisks(Quotation quotation, YesNo isAnnual) {
        var policyActiveRisks = new ArrayList<PolicyActiveRiskDto>();
        for (var qp : quotation.getQuotationProducts()) {
            for (var qpr : qp.getQuotationRisks()) {
                var policyActiveRisk = buildPolicyActiveRiskDto(quotation, qp, qpr, isAnnual);
                policyActiveRisks.add(policyActiveRisk);
            }

        }
        return policyActiveRisks;
    }

    @NotNull
    private PolicyActiveRiskDto buildPolicyActiveRiskDto(Quotation quotation, QuotationProduct
            qp, QuotationRisk qpr, YesNo isAnnual) {
        composeQuotation(quotation);
        try {
            return new PolicyActiveRiskDto(
                    quotation.getOrganizationId(),
                    qp.getProductId(),
                    qpr.getBinderId(),
                    quotation.getAgent().getId(),
                    quotation.getInsurerOrgId(),
                    quotation.getQuotationNo(),
                    Objects.equals(quotation.getStatus(), "RN") ? qpr.getFutureAnnualPremium():quotation.getGrossPremium(),
                    qp.getProductShortDescription(),
                    qp.getRemarks(),
                    quotation.getAgent().getCode(),
                    qp.getMaturityDate(),
                    qp.getProductGroupId(),
                    qpr.getRiskId(),
                    qpr.getCertificateNo(),
                    qpr.getWithEffectFromDate(),
                    qpr.getWithEffectToDate(),
                    qpr.getCoverTypeId(),
                    qpr.getCoverTypeCode(),
                    qpr.getSubClassId(),
                    qpr.getProductSubClassId(),
                    quotation.getClientId(),
                    quotation.getPremium(),
                    BigDecimal.ZERO,
                    qpr.getBasicPremium(),
                    BigDecimal.ZERO,
                    qpr.getFutureAnnualPremium(),
                    qpr.getValue(),
                    qpr.getCommissionAmount(),
                    BigDecimal.ZERO,
                    qpr.getCommissionRate(),
                    qpr.getWithHoldingTax(),
                    BigDecimal.ZERO,
                    quotation.getPaymentFrequency(),
                    quotation.getUnderwritingYear(),
                    Objects.equals(qp.getTotalSumInsured().setScale(0, RoundingMode.HALF_UP), BigDecimal.ZERO) ? ValuationStatus.CLOSED : qpr.getValuationStatus(),
                    quotation.getStatus(),
                    null,
                    isAnnual == YesNo.Y ? "ANNUAL" : "INSTALLMENT",
                    quotation.getPolicyNo(),
                    quotation.getPolicyId(),
                    quotation.getQuotationNo(),
                    quotation.getId(),
                    quotation.getCurrencyId(),
                    quotation.getPanelId(),
                    quotation.getInsurerOrgId(),
                    quotation.getInsurerOrgId(),
                    qp.getInstallmentAllowed(),
                    isAnnual,
                    null,
                    null,
                    qpr.getValuerOrgId(),
                    qpr.getValuerBranchId(),
                    quotation.getPaymentRef(),
                    YesNo.N,
                    quotation.getProductionDate(),
                    buildActiveRiskDocuments(qpr),
                    new ObjectMapper().writeValueAsString(qpr.getMotorSchedules()),
                   YesNo.N,
                    buildActiveRiskTaxes(qpr),
                    buildActiveRiskSections(qpr),
                    quotation.isRenewal()
            );
        } catch (JsonProcessingException e) {
            throw new StringSerializationException(e.getMessage());
        }
    }

    private static List<PolicyActiveRiskSectionDto> buildActiveRiskSections(QuotationRisk quotationRisk) {
        return quotationRisk.getQuotationRiskSections()
                .stream()
                .map(qprs -> new PolicyActiveRiskSectionDto(
                        qprs.getSectionId(),
                        qprs.getDescription(),
                        qprs.getSectionType(),
                        qprs.getFreeLimitAmount(),
                        qprs.getLimitAmount(),
                        qprs.getUsedLimitAmount(),
                        qprs.getPremiumAmount(),
                        qprs.getMinimumPremiumAmount(),
                        qprs.getPremiumRate(),
                        qprs.getRateDivisionFactor(),
                        qprs.getProrated(),
                        qprs.getPremiumRateDescription(),
                        qprs.getRateType(),
                        qprs.getSubClassSectionId(),
                        qprs.getMultiplierDivisionFactor(),
                        qprs.getSubClassSectionDesc()
                )).toList();
    }

    private static List<DocumentDto> buildActiveRiskDocuments(QuotationRisk quotationRisk) {
        return quotationRisk.getQuoteDocument().stream().map(qd -> new DocumentDto(
                qd.getDocument(),
                qd.getOrganizationId(),
                qd.getIsValuationLetter()
        )).toList();
    }

    private static List<PolicyActiveRiskTaxDto> buildActiveRiskTaxes(QuotationRisk quotationRisk) {
        return quotationRisk.getQuotationRiskTaxes()
                .stream()
                .map(qprt -> new PolicyActiveRiskTaxDto(
                        qprt.getTaxRateId(),
                        qprt.getRate(),
                        qprt.getDivisionFactor(),
                        qprt.getTaxAmount(),
                        qprt.getTransactionTypeCode(),
                        qprt.getTaxRateDescription(),
                        qprt.getTaxType(),
                        qprt.getApplicationArea(),
                        qprt.getProductSubClassId()
                )).toList();

    }


}