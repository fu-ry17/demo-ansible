package com.turnkey.turnquest.gis.quotation.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turnkey.turnquest.gis.quotation.client.crm.AgencyClient;
import com.turnkey.turnquest.gis.quotation.client.crm.ClientDataClient;
import com.turnkey.turnquest.gis.quotation.client.crm.OrganizationClient;
import com.turnkey.turnquest.gis.quotation.client.gis.CertClient;
import com.turnkey.turnquest.gis.quotation.client.gis.SubClassCoverTypeSectionClient;
import com.turnkey.turnquest.gis.quotation.dto.crm.OrganizationDto;
import com.turnkey.turnquest.gis.quotation.dto.gis.PremiumRateDto;
import com.turnkey.turnquest.gis.quotation.dto.gis.SubClassCoverTypeSectionDto;
import com.turnkey.turnquest.gis.quotation.dto.quotation.UpdateQuotationDto;
import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyRiskDto;
import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyRiskSectionDto;
import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyRiskTaxDto;
import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyScheduleDto;
import com.turnkey.turnquest.gis.quotation.enums.EndorsementType;
import com.turnkey.turnquest.gis.quotation.enums.PremiumRateType;
import com.turnkey.turnquest.gis.quotation.enums.ValuationStatus;
import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import com.turnkey.turnquest.gis.quotation.event.producer.UpdateQuotationProductPublisher;
import com.turnkey.turnquest.gis.quotation.model.*;
import com.turnkey.turnquest.gis.quotation.repository.QuotationProductRepository;
import com.turnkey.turnquest.gis.quotation.repository.QuotationRiskRepository;
import com.turnkey.turnquest.gis.quotation.service.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 */
@Slf4j
@Service("quotationRiskService")
public class QuotationRiskServiceImpl extends AbstractQuotationService implements QuotationRiskService {

    private final QuotationRiskRepository quotationRiskRepository;
    private final QuotationRiskSectionService quotationRiskSectionService;
    private final SubClassCoverTypeSectionClient subClassCoverTypeSectionClient;
    private final QuotationRiskTaxService quotationRiskTaxService;
    private final ModelMapper modelMapper;
    private final QuotationProductRepository quotationProductRepository;
    private final AgencyClient agencyClient;
    private final QuoteDocumentService quoteDocumentService;
    private final UpdateQuotationProductPublisher updateQuotationProductPublisher;
    private final CertClient certClient;
    private final ObjectMapper objectMapper;


    public QuotationRiskServiceImpl(QuotationRiskRepository quotationRiskRepository,
                                    QuotationRiskSectionService quotationRiskSectionService,
                                    SubClassCoverTypeSectionClient subClassCoverTypeSectionClient,
                                    QuotationRiskTaxService quotationRiskTaxService,
                                    ModelMapper modelMapper,
                                    QuotationProductRepository quotationProductRepository,
                                    AgencyClient agencyClient,
                                    ClientDataClient clientDataClient,
                                    OrganizationClient organizationClient,
                                    QuoteDocumentService quoteDocumentService,
                                    UpdateQuotationProductPublisher updateQuotationProductPublisher, CertClient certClient, @Qualifier("objectMapper") ObjectMapper objectMapper) {
        super(clientDataClient, agencyClient, organizationClient);
        this.quotationRiskRepository = quotationRiskRepository;
        this.quotationRiskSectionService = quotationRiskSectionService;
        this.subClassCoverTypeSectionClient = subClassCoverTypeSectionClient;
        this.quotationRiskTaxService = quotationRiskTaxService;
        this.modelMapper = modelMapper;
        this.quotationProductRepository = quotationProductRepository;
        this.agencyClient = agencyClient;
        this.quoteDocumentService = quoteDocumentService;
        this.updateQuotationProductPublisher = updateQuotationProductPublisher;
        this.certClient = certClient;
        this.objectMapper = objectMapper;
    }

    /**
     * @param quotationRisk
     * @return
     */
    @Override
    public QuotationRisk create(QuotationRisk quotationRisk) {

        if (quotationRisk.getForeignId() != null && quotationRisk.getForeignId() != 0) {
            quotationRiskRepository.findByForeignId(quotationRisk.getForeignId()).ifPresent(qr -> quotationRisk.setId(qr.getId()));
        }

        quotationRisk.setClientType("I");
        quotationRisk.setQuotationRevisionNumber(BigDecimal.ZERO);

        return quotationRiskRepository.save(quotationRisk);

    }

    /**
     * @param quotationRisk
     * @param id
     * @return
     */
    @SneakyThrows
    @Override
    public QuotationRisk update(QuotationRisk quotationRisk, Long id) {
        quotationRisk.setId(id);
        updateItemDescription(quotationRisk);
        var quotationProduct = quotationProductRepository.findById(quotationRisk.getQuotationProductId());
        if (quotationProduct.isPresent()) {
            var updateQuotationDto = new UpdateQuotationDto();
            updateQuotationDto.setQuotationProductId(quotationRisk.getQuotationProductId());
            updateQuotationDto.setCoverFromDate(quotationRisk.getWithEffectFromDate());
            updateQuotationDto.setCoverToDate(quotationRisk.getWithEffectToDate());
            updateQuotationDto.setSumInsured(quotationRisk.getValue());
            updateQuotationProductPublisher.updateQuotationProduct(updateQuotationDto);
        }

        return quotationRiskRepository.save(quotationRisk);
    }

    /**
     * Deletes a risk. Annotated with @Transactional to allow for rolling back in the case of an error
     *
     * @param id the risk Id
     * @return true if deletion was successful, false otherwise
     */
    @Transactional
    @Override
    public boolean delete(Long id) {
        quotationRiskRepository.deleteById(id);
        return true;
    }


    /**
     * @param id
     * @return
     */
    @Override
    public Optional<QuotationRisk> find(Long id) {
        return quotationRiskRepository.findById(id);
    }

    /**
     * @param quotationProductId
     * @return
     */
    @Override
    public List<QuotationRisk> findByQuotationProductId(Long quotationProductId) {
        return quotationRiskRepository.findByQuotationProduct_Id(quotationProductId);
    }

    /**
     * @param quotationId
     * @return
     */
    @Override
    public List<QuotationRisk> findByQuotationId(Long quotationId) {
        return quotationRiskRepository.findByQuotationId(quotationId);
    }

    /**
     * @param quotationRisks
     * @return
     */
    @Override
    public List<QuotationRisk> saveMultiple(List<QuotationRisk> quotationRisks) {
        quotationRisks = quotationRiskRepository.saveAll(quotationRisks);

        for (QuotationRisk quotationRisk : quotationRisks) {
            populateDefaultSections(quotationRisk);
        }
        return quotationRisks;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public List<QuotationRiskSection> getQuotationRiskSections(Long id) {
        return quotationRiskSectionService.findByQuotationRiskId(id);
    }

    @Override
    public List<QuotationRiskSection> populateDefaultSections(QuotationRisk quotationRisk) {
        List<SubClassCoverTypeSectionDto> mandatorySections = subClassCoverTypeSectionClient.findMandatorySectionsWithPremiumRates(
                quotationRisk.getSubClassId(),
                quotationRisk.getCoverTypeId(),
                quotationRisk.getBinderId()
        );
        if (quotationRisk.getQuotationRiskSections() == null) {
            quotationRisk.setQuotationRiskSections(new ArrayList<>());
        }

        List<QuotationRiskSection> quotationRiskSectionList = new ArrayList<>();
        List<QuotationRiskSection> quotationRiskSections = quotationRisk.getQuotationRiskSections();

        List<SubClassCoverTypeSectionDto> sectionsToPopulate = mandatorySections
                .stream()
                .filter(subClassCoverTypeSectionDto -> subClassCoverTypeSectionDto.getSection() != null)
                .filter(subClassCoverTypeSectionDto -> quotationRiskSections.stream()
                        .anyMatch(quotationRiskSection -> quotationRiskSection.getSectionId().equals(subClassCoverTypeSectionDto.getSection().getId()))).collect(Collectors.toList());

        List<QuotationRiskSection> populatedRiskSections = new ArrayList<>();

        for (SubClassCoverTypeSectionDto subClassCoverTypeSectionDto : sectionsToPopulate) {

            if (subClassCoverTypeSectionDto.getSection() == null) {
                continue;
            }
            QuotationRiskSection quotationRiskSection = new QuotationRiskSection();

            // To get the rate type
            Optional<PremiumRateDto> optionalPremiumRateDto = subClassCoverTypeSectionDto.getPremiumRates()
                    .stream()
                    .findFirst();

            quotationRiskSection.setQuotationRiskId(quotationRisk.getId());
            quotationRiskSection.setSectionId(subClassCoverTypeSectionDto.getSection().getId());
            quotationRiskSection.setSectionCode(subClassCoverTypeSectionDto.getSection().getCode());
            quotationRiskSection.setCompute("Y");
            quotationRiskSection.setRowNumber(BigDecimal.ONE);
            quotationRiskSection.setCalculationGroup(BigDecimal.ONE);
            quotationRiskSection.setDualBasis("N");

            // Set the rates from gin_premium_rates. More-less the default rates.
            // The case can, and should be different with SRG and ARG Rate types
            optionalPremiumRateDto.ifPresent(premiumRateDto -> {
                quotationRiskSection.setRateType(PremiumRateType.valueOf(premiumRateDto.getType()));
                quotationRiskSection.setPremiumRate(premiumRateDto.getRate());
                quotationRiskSection.setFreeLimitAmount(premiumRateDto.getFreeLimit());
                quotationRiskSection.setRateDivisionFactor(premiumRateDto.getDivisionFactor());
                quotationRiskSection.setMultiplierDivisionFactor(premiumRateDto.getMultiplierDivisionFactor());
                quotationRiskSection.setMultiplierRate(premiumRateDto.getMultiplierRate());

            });

            quotationRiskSection.setPremiumAmount(BigDecimal.ZERO);
            quotationRiskSection.setLimitAmount(BigDecimal.ZERO);
            quotationRiskSection.setSectionType(subClassCoverTypeSectionDto.getSection().getType());

            populatedRiskSections.add(quotationRiskSection);
        }

        populatedRiskSections = quotationRiskSectionService.createMultiple(populatedRiskSections);

        quotationRiskSectionList.addAll(populatedRiskSections);
        quotationRiskSectionList.addAll(quotationRiskSections);
        return quotationRiskSectionList;
    }

    @Override
    public QuotationRiskSection createOrUpdateQuotationRiskSection(Long id, QuotationRiskSection quotationRiskSection) {

        Optional<QuotationRisk> optionalQuotationRisk = this.find(id);

        if (optionalQuotationRisk.isPresent()) {

            quotationRiskSection.setQuotationRiskId(id);

            List<QuotationRiskSection> quotationRiskSections = this.getQuotationRiskSections(id);

            Optional<QuotationRiskSection> optionalQuotationRiskSection = quotationRiskSections.stream()
                    .filter(quotationRiskSection1 -> quotationRiskSection1.getSectionId().equals(quotationRiskSection.getSectionId()))
                    .findFirst();


            return optionalQuotationRiskSection.map(quotationRiskSection1 ->
                            quotationRiskSectionService.update(quotationRiskSection, quotationRiskSection1.getId()))
                    .orElse(quotationRiskSectionService.create(quotationRiskSection));

        } else {
            throw new NullPointerException("Risk with Id: " + id + " not found");
        }
    }


    @Override
    public List<PolicyRiskDto> convertToPolicyRisks(List<QuotationRisk> quotationRisks) {
        List<PolicyRiskDto> policyRisks = new ArrayList<>();
        for (QuotationRisk quotationRisk : quotationRisks) {
            policyRisks.add(this.convertToPolicyRisk(quotationRisk));
        }
        return policyRisks;
    }

    @Override
    public PolicyRiskDto convertToPolicyRisk(QuotationRisk quotationRisk) {
        PolicyRiskDto policyRiskDto = new PolicyRiskDto();

        List<PolicyRiskSectionDto> policyRiskSections = quotationRiskSectionService
                .convertToPolicyRiskSections(quotationRisk.getQuotationRiskSections());

        List<PolicyRiskTaxDto> policyRiskTaxes = quotationRiskTaxService
                .convertToPolicyRiskTaxes(quotationRisk.getQuotationRiskTaxes());

        /*installment items*/
        policyRiskDto.setInstallmentAllowed(quotationRisk.getInstallmentAllowed());
        if (quotationRisk.getInstallmentAllowed() == YesNo.Y) {
            policyRiskDto.setButCharge(quotationRisk.getButCharge());
            policyRiskDto.setPaymentFrequency(quotationRisk.getPaymentFrequency());
            policyRiskDto.setInstallmentAmount(quotationRisk.getInstallmentAmount().setScale(25, RoundingMode.HALF_UP));
            policyRiskDto.setNextInstallmentNo(quotationRisk.getNextInstallmentNo());
            policyRiskDto.setPaidToDate(quotationRisk.getMaturityDate());
            policyRiskDto.setTotalNoOfInstallments(quotationRisk.getTotalNoOfInstallments());
            policyRiskDto.setMaturityDate(quotationRisk.getMaturityDate());
            policyRiskDto.setOutstandingInstallmentAmount(quotationRisk.getOutstandingInstallmentAmount().setScale(25, RoundingMode.HALF_UP));
            policyRiskDto.setInstallmentPremium(quotationRisk.getInstallmentPremium().setScale(25, RoundingMode.HALF_UP));
            policyRiskDto.setProductInstallmentId(quotationRisk.getProductInstallmentId());
            policyRiskDto.setOutstandingCommission(quotationRisk.getOutstandingCommission());
            policyRiskDto.setCommInstallmentPremium(quotationRisk.getCommInstallmentPremium().setScale(25, RoundingMode.HALF_UP));
            policyRiskDto.setCommInstallmentAmount(quotationRisk.getCommInstallmentAmount().setScale(25, RoundingMode.HALF_UP));
            policyRiskDto.setPaidInstallmentAmount(quotationRisk.getPaidInstallmentAmount());
            policyRiskDto.setPaidInstallmentComm(quotationRisk.getPaidInstallmentComm());
        }

        policyRiskDto.setInsuredId(quotationRisk.getClientId());
        policyRiskDto.setBinderId(quotationRisk.getBinderId());
        policyRiskDto.setItemDescription(quotationRisk.getItemDescription());
        policyRiskDto.setPropertyId(quotationRisk.getRiskId());
        policyRiskDto.setBinderId(quotationRisk.getBinderId());
        policyRiskDto.setSubClassId(quotationRisk.getSubClassId());
        policyRiskDto.setProductSubClassId(quotationRisk.getProductSubClassId());
        policyRiskDto.setQuantity(quotationRisk.getQuantity());
        policyRiskDto.setWitholdingTax(quotationRisk.getWithHoldingTax());
        policyRiskDto.setCoverTypeId(quotationRisk.getCoverTypeId());
        policyRiskDto.setCoverTypeCode(quotationRisk.getCoverTypeCode());
        policyRiskDto.setValue(quotationRisk.getValue());
        policyRiskDto.setWithEffectFromDate(quotationRisk.getWithEffectFromDate());
        policyRiskDto.setWithEffectToDate(quotationRisk.getWithEffectToDate());
        policyRiskDto.setValuationStatus(quotationRisk.getValuationStatus());
        policyRiskDto.setValuerOrgId(quotationRisk.getValuerOrgId());
        policyRiskDto.setValuerBranchId(quotationRisk.getValuerBranchId());

        policyRiskDto.setTotalPremium(quotationRisk.getTotalPremium());
        policyRiskDto.setFutureAnnualPremium(quotationRisk.getFutureAnnualPremium());
        policyRiskDto.setBasicPremium(quotationRisk.getBasicPremium());
        policyRiskDto.setNetPremium(quotationRisk.getBasicPremium());

        policyRiskDto.setSumInsuredDiffAmount(quotationRisk.getValue());
        policyRiskDto.setTotalValue(quotationRisk.getValue());
        policyRiskDto.setEndorsementType(quotationRisk.getEndorsementType());

        if (quotationRisk.getPolicyRiskId() != null) {
            policyRiskDto.setPreviousRiskId(quotationRisk.getPolicyRiskId());
        }
        policyRiskDto.setNcdLevel(quotationRisk.getNcdLevel());

        policyRiskDto.setCommissionAmount(quotationRisk.getCommissionAmount());
        policyRiskDto.setCommEndosDiffAmount(quotationRisk.getCommissionAmount());
        policyRiskDto.setCommissionRate(quotationRisk.getCommissionRate());

        policyRiskDto.setMarketerCommAMount(quotationRisk.getMarketerCommissionAmount());
        policyRiskDto.setMarketerCommRate(quotationRisk.getMarketerCommissionRate());

        policyRiskDto.setSubAgentCommAmount(quotationRisk.getSubAgentCommissionAmount());
        policyRiskDto.setSubAgentCommissionRate(quotationRisk.getSubAgentCommissionRate());

        policyRiskDto.setPolicyRiskSections(policyRiskSections);
        policyRiskDto.setPolicyRiskTaxes(policyRiskTaxes);
        policyRiskDto.setLastValuationDate(quotationRisk.getLastValuationDate());

        //Create policy schedules from the quotation risk motor schedules
        if (quotationRisk.getMotorSchedules() != null) {
            PolicyScheduleDto policyScheduleDto = new PolicyScheduleDto();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            modelMapper.map(quotationRisk.getMotorSchedules(), policyScheduleDto);
            policyRiskDto.setPolicyRiskSchedules(policyScheduleDto);
        }

        /* add documents */
        if (quotationRisk.getQuoteDocument() != null) {
            policyRiskDto.setPolicyDocuments(
                    quoteDocumentService.convertToPolicyDocuments(quotationRisk.getQuoteDocument()));
        }

        return policyRiskDto;
    }

    @Override
    public QuotationRisk updateCertificateNo(String certificateNo, Long quotationRiskId) {
        Optional<QuotationRisk> quotationRisk = quotationRiskRepository.findById(quotationRiskId);
        if (quotationRisk.isPresent()) {
            quotationRisk.get().setCertificateNo(certificateNo);
            return quotationRiskRepository.save(quotationRisk.get());
        }
        return null;
    }

    @Override
    public List<QuotationRiskTax> findTaxes(Long id) {
        return quotationRiskTaxService.findByQuotationRiskId(id);
    }

    @Override
    public QuotationRisk saveQuickQuotationRisk(Quotation quotation, QuotationProduct quotationProduct, QuotationProduct quoteProduct, QuotationRisk quotationRisk) {
        List<QuotationRiskSection> sectionList = new ArrayList<>(quotationRisk.getQuotationRiskSections());
        var taxAmount = BigDecimal.ZERO;
        if (quotationRisk.getQuotationRiskTaxes() != null) {
            taxAmount = quotationRisk.getQuotationRiskTaxes()
                    .stream().map(QuotationRiskTax::getTaxAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        quotationRisk.setInstallmentAllowed(quotationProduct.getInstallmentAllowed());
        quotationRisk.setQuotationProductId(quoteProduct.getId());
        quotationRisk.setClientId(quotation.getClientId());
        if (quotationRisk.getBasicPremium().compareTo(BigDecimal.ZERO) > 0) {
            quotationRisk.setCommissionAmount(quotationRisk.getCommissionAmount()
                    .abs()
                    .negate()
                    .setScale(25, RoundingMode.HALF_UP));

        } else {
            quotationRisk.setCommissionAmount(quotationRisk.getCommissionAmount().abs().setScale(25, RoundingMode.HALF_UP));
        }

        quotationRisk.setTotalPremium(quotationRisk.getBasicPremium().add(taxAmount).setScale(25, RoundingMode.HALF_UP));
        quotationRisk.setBasicPremium(quotationRisk.getBasicPremium().setScale(25, RoundingMode.HALF_UP));
        quotationRisk.setWithHoldingTax(computeRiskWithHoldingTax(quotationRisk, quotation.getOrganizationId()));
        if (quotationRisk.getEndorsementType() == null) quotationRisk.setEndorsementType(EndorsementType.SUM_INSURED);
        if (quotationRisk.getId() == null && quotationRisk.getForeignId() == null) {
            quotationRisk.setFutureAnnualPremium(quotationRisk.getTotalPremium().setScale(25, RoundingMode.HALF_UP));
        }

        if (quotation.getStatus().equals("RN")) {
            quotationRisk.setButCharge(quotationRisk.getBasicPremium().setScale(25, RoundingMode.HALF_UP));
            quotationRisk.setMaturityDate(quotationRisk.getWithEffectFromDate());

            if (quotationRisk.getCoverTypeCode().equals("TPO") || quotationRisk.getCoverTypeCode().equals("THIRD PARTY ONLY")) {
                quotationRisk.setValuationStatus(ValuationStatus.NONE);
            } else {
                quotationRisk.setValuationStatus(quotationRisk.getValuationStatus());
            }

        }

        log.info("Risk WET {}", quotationRisk.getWithEffectToDate());
        setCertificateType(quotationRisk);
        updateItemDescription(quotationRisk);
        QuotationRisk quoteRisk = this.create(quotationRisk);

        if (quotationRisk.getQuotationRiskTaxes() != null) {
            for (QuotationRiskTax quotationRiskTax : quoteRisk.getQuotationRiskTaxes()) {
                quotationRiskTaxService.saveQuickQuotationRiskTax(quoteRisk, quotationRiskTax);
            }
        }

        /**
         * Save quotataion risk sections
         */
        if (!sectionList.isEmpty()) {
            log.info("Quotation Risk id: {}", quoteRisk.getId());
            for (QuotationRiskSection quotationRiskSection : sectionList) {
                quotationRiskSectionService.saveQuickQuotationRiskSection(quoteRisk, quotationRiskSection);
            }
        }

        if (quotationRisk.getQuoteDocument() != null) {
            for (QuoteDocument quoteDocument : quoteRisk.getQuoteDocument()) {
                quoteDocument.setQuotationRiskId(quoteRisk.getId());
                quoteDocumentService.save(quoteDocument, -1L);
            }
        }
        quotationRisk.setQuotationRiskSections(sectionList);
        return quotationRisk;
    }


    @Override
    public List<QuotationRisk> findRisksByRegistrationNo(String registrationNo) {
        return quotationRiskRepository.findAllByRiskId(registrationNo);
    }

    /**
     * Updates the item description on the risk with schedule details
     * when schedules are added
     *
     * @param quotationRisk quotation risk object
     */
    void updateItemDescription(QuotationRisk quotationRisk) {
        if (quotationRisk.getMotorSchedules() != null) {
            MotorSchedules schedules = quotationRisk.getMotorSchedules();
            var itemDesc = "";
            if (schedules.getMake() != null) {
                itemDesc = schedules.getMake() + "/" + schedules.getModel() + "/" + schedules.getBodyType();
            } else if (schedules.getChasisNo() != null) {
                itemDesc = schedules.getChasisNo();
            } else {
                itemDesc = "item_description"; // temporary fix
            }
            quotationRisk.setItemDescription(itemDesc);
        }
    }
@SneakyThrows
    private void setCertificateType(QuotationRisk quotationRisk) {
        if (quotationRisk.getMotorSchedules() != null) {
            MotorSchedules schedules = quotationRisk.getMotorSchedules();
            try {
                String certType = certClient.getCertificateType(quotationRisk.getSubClassId(), quotationRisk.getCoverTypeId());
                log.info("Certificate Type: {}", new ObjectMapper().writeValueAsString(certType));
                if (certType != null) {
                    schedules.setCertificateType(certType);
                }
            } catch (Exception e) {
                log.error("Error occurred while fetching certificate type: {}", e.getMessage());
            }

        }
    }

    @SneakyThrows
    public BigDecimal computeRiskWithHoldingTax(QuotationRisk quotationRisk, Long organizationId) {
        OrganizationDto org = agencyClient.findByOrganizationId(organizationId);
        log.info("Compute Risk whtx org: {}", new ObjectMapper().writeValueAsString(org));
        if (org.getAccountType() != null) {
            log.info("Current risk commission: {}", quotationRisk.getCommissionAmount());
            BigDecimal withHolding = org.getAccountType()
                    .getWithHoldingTaxRate()
                    .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
                    .multiply((quotationRisk.getCommissionAmount().abs()));
            withHolding = withHolding.setScale(25, RoundingMode.HALF_UP);
            int premSignage = quotationRisk.getTotalPremium().compareTo(BigDecimal.ZERO);
            return premSignage > 0 ? withHolding : withHolding.negate();
        }
        return BigDecimal.ZERO;
    }

}