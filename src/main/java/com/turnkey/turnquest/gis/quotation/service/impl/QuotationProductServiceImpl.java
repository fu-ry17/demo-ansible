package com.turnkey.turnquest.gis.quotation.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turnkey.turnquest.gis.quotation.client.crm.AgencyClient;
import com.turnkey.turnquest.gis.quotation.client.crm.ClientDataClient;
import com.turnkey.turnquest.gis.quotation.client.crm.OrganizationClient;
import com.turnkey.turnquest.gis.quotation.dto.crm.OrganizationDto;
import com.turnkey.turnquest.gis.quotation.dto.quotation.UpdateInstallmentDto;
import com.turnkey.turnquest.gis.quotation.dto.quotation.UpdateQuotationDto;
import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyDto;
import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyRiskDto;
import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyTaxDto;
import com.turnkey.turnquest.gis.quotation.enums.PolicyInterfaceType;
import com.turnkey.turnquest.gis.quotation.enums.ValuationStatus;
import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import com.turnkey.turnquest.gis.quotation.event.producer.ConvertQuoteEvent;
import com.turnkey.turnquest.gis.quotation.event.producer.UpdateQuotationPublisher;
import com.turnkey.turnquest.gis.quotation.exception.error.QuoteConversionException;
import com.turnkey.turnquest.gis.quotation.model.Quotation;
import com.turnkey.turnquest.gis.quotation.model.QuotationProduct;
import com.turnkey.turnquest.gis.quotation.model.QuotationProductTax;
import com.turnkey.turnquest.gis.quotation.model.QuotationRisk;
import com.turnkey.turnquest.gis.quotation.repository.QuotationProductRepository;
import com.turnkey.turnquest.gis.quotation.service.QuotationProductService;
import com.turnkey.turnquest.gis.quotation.service.QuotationProductTaxService;
import com.turnkey.turnquest.gis.quotation.service.QuotationRiskService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
@Slf4j
@Service("quotationService")
public class QuotationProductServiceImpl extends AbstractQuotationService implements QuotationProductService {

    private final QuotationProductRepository quotationProductRepository;
    private final QuotationRiskService quotationRiskService;
    private final QuotationProductTaxService quotationProductTaxService;
    private final AgencyClient agencyClient;
    private final ConvertQuoteEvent convertQuoteEvent;
    private final UpdateQuotationPublisher updateQuotationPublisher;

    public QuotationProductServiceImpl(QuotationProductRepository quotationProductRepository,
                                       QuotationRiskService quotationRiskService,
                                       QuotationProductTaxService quotationProductTaxService,
                                       AgencyClient agencyClient,
                                        ClientDataClient clientDataClient,
                                        OrganizationClient organizationClient,
                                       ConvertQuoteEvent convertQuoteEvent,
                                       UpdateQuotationPublisher updateQuotationPublisher
    ) {
        super(clientDataClient, agencyClient, organizationClient);
        this.quotationProductRepository = quotationProductRepository;
        this.quotationRiskService = quotationRiskService;
        this.quotationProductTaxService = quotationProductTaxService;
        this.agencyClient = agencyClient;
        this.convertQuoteEvent = convertQuoteEvent;
        this.updateQuotationPublisher = updateQuotationPublisher;
    }


    /**
     * @param id
     * @return
     */
    @Override
    public Optional<QuotationProduct> find(Long id) {
        return quotationProductRepository.findById(id);
    }

    /**
     * @param quotationProduct
     * @return
     */
    @Override
    public QuotationProduct create(QuotationProduct quotationProduct) {
        if (quotationProduct.getForeignId() != null) {
            quotationProductRepository.findByForeignId(quotationProduct.getForeignId()).ifPresent(quotationProduct1 -> quotationProduct.setId(quotationProduct1.getId()));
        }
        return quotationProductRepository.save(quotationProduct);
    }

    /**
     * @param quotationProduct
     * @param id
     * @return
     */
    @Override
    public QuotationProduct update(QuotationProduct quotationProduct, Long id) {
        quotationProduct.setId(id);
        return quotationProductRepository.save(quotationProduct);
    }

    /**
     * @param quotationId
     * @return
     */
    @Override
    public List<QuotationProduct> findByQuotationId(Long quotationId) {
        return quotationProductRepository.findByQuotationId(quotationId);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public List<QuotationRisk> getQuotationRisks(Long id) {
        return quotationRiskService.findByQuotationProductId(id);
    }

    @Override
    public QuotationProduct updateInstallmentAmounts(UpdateInstallmentDto updateInstallmentDto) {
        QuotationProduct quotationProduct = quotationProductRepository.findById(updateInstallmentDto.getQuotationProductId())
                .orElseThrow(() -> new RuntimeException("Quotation Product with Id " + updateInstallmentDto.getQuotationProductId() + " not found"));
        quotationProduct.setInstallmentPremium(updateInstallmentDto.getNewInstallmentAmount());
        quotationProduct.setCommInstallmentAmount(updateInstallmentDto.getNewCommissionInstallmentAmount());
        quotationProduct.setNextInstallmentNo(quotationProduct.getTotalNoOfInstallments());

        //Update the risks as well
        List<QuotationRisk> quotationRisks = quotationRiskService.findByQuotationProductId(updateInstallmentDto.getQuotationProductId());
        quotationRisks.forEach(quotationRisk -> {
            quotationRisk.setInstallmentPremium(quotationRisk.getOutstandingInstallmentAmount());
            quotationRisk.setCommInstallmentAmount(quotationRisk.getOutstandingCommission());
            quotationRisk.setNextInstallmentNo(quotationRisk.getTotalNoOfInstallments());
            quotationRiskService.update(quotationRisk, quotationRisk.getId());
        });

        return quotationProductRepository.save(quotationProduct);
    }

    @Override
    public void convertToPolicy(Long id) {
        Optional<QuotationProduct> optionalQuotationProduct = this.find(id);
        if (optionalQuotationProduct.isPresent()) {
            this.convertToPolicy(optionalQuotationProduct.get());
            log.info("Exited converting quote to policy");
        } else {
            throw new QuoteConversionException("Quotation product does not exist", null);
        }
    }

    @Override
    public void convertToPolicy(QuotationProduct quotationProduct) {
        PolicyDto policyDto = new PolicyDto();

        List<PolicyRiskDto> policyRisks = this.quotationRiskService.convertToPolicyRisks(quotationProduct.getQuotationRisks());

        List<PolicyTaxDto> policyTaxes = this.quotationProductTaxService.convertToPolicyTaxes(quotationProduct.getQuotationProductTaxes());


        policyDto.setPolicyTaxes(policyTaxes);
        policyDto.setPolicyRisks(policyRisks);

        policyDto.setInstallmentAllowed(quotationProduct.getInstallmentAllowed());
        computeWithHoldingTax(quotationProduct, quotationProduct.getQuotation().getOrganizationId());
        if (quotationProduct.getInstallmentAllowed() == YesNo.Y) {
            composeInstalmentFields(quotationProduct, policyDto);
        }
        policyDto.setQuotationId(quotationProduct.getQuotationId());
        policyDto.setGrossPremium(quotationProduct.getQuotation().getGrossPremium());
        policyDto.setProductionDate(quotationProduct.getQuotation().getProductionDate());
        policyDto.setPolicyStatus(quotationProduct.getQuotation().getStatus());
        policyDto.setAgencyId(quotationProduct.getQuotation().getAgencyId());
        policyDto.setOrganizationId(quotationProduct.getQuotation().getOrganizationId());
        policyDto.setInsurerOrgId(quotationProduct.getQuotation().getInsurerOrgId());
        policyDto.setProductId(quotationProduct.getProductId());
        policyDto.setBranchId(quotationProduct.getQuotation().getBranchId());
        policyDto.setCoinsurance("N");
        policyDto.setCurrencyId(quotationProduct.getQuotation().getCurrencyId());
        policyDto.setCurrencyId(quotationProduct.getQuotation().getCurrencyId());
        policyDto.setCurrencySymbol(quotationProduct.getCurrencySymbol());
        policyDto.setPanelId(quotationProduct.getQuotation().getPanelId());
        policyDto.setPolicyNumber(quotationProduct.getQuotation().getPolicyNo());
        policyDto.setPolicyRisks(policyRisks);
        policyDto.setPolicyTaxes(policyTaxes);

        policyDto.setFrequencyOfPayment(quotationProduct.getQuotation().getPaymentFrequency());

        policyDto.setClientId(quotationProduct.getQuotation().getClientId());

        policyDto.setTotalSumInsured(quotationProduct.getTotalSumInsured().setScale(25, RoundingMode.HALF_UP));
        policyDto.setTotalPremium(quotationProduct.getTotalPremium().setScale(25, RoundingMode.HALF_UP));
        policyDto.setNettPremium(quotationProduct.getBasicPremium().setScale(25, RoundingMode.HALF_UP));
        policyDto.setBasicPremium(quotationProduct.getBasicPremium().setScale(25, RoundingMode.HALF_UP));
        policyDto.setFutureAnnualPremium(quotationProduct.getFutureAnnualPremium());
        policyDto.setCommissionAmount(quotationProduct.getCommissionAmount().setScale(25, RoundingMode.HALF_UP));
        policyDto.setEndorsementCommission(quotationProduct.getCommissionAmount().setScale(25, RoundingMode.HALF_UP));
        policyDto.setWithholding(quotationProduct.getWithHoldingTax().setScale(25, RoundingMode.HALF_UP));
        policyDto.setOrganizationId(quotationProduct.getQuotation().getOrganizationId());
        policyDto.setQuotationNo(quotationProduct.getQuotation().getQuotationNo());
        policyDto.setBinderPolicy(quotationProduct.getFromBinder());
        policyDto.setBinderId(quotationProduct.getBinderId());


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(quotationProduct.getWithEffectFromDate());
        policyDto.setInceptionUnderwritingYear((long) calendar.get(Calendar.YEAR));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MM yyyy");
        DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
        String inputString1 = dateFormat.format(new Date(quotationProduct.getWithEffectFromDate()));
        String inputString2 = dateFormat.format(new Date(quotationProduct.getWithEffectToDate()));
        LocalDate date1 = LocalDate.parse(inputString1, dtf);
        LocalDate date2 = LocalDate.parse(inputString2, dtf);

        long underWritingYearLength = ChronoUnit.DAYS.between(date1, date2);
        policyDto.setUnderwritingYearLength(underWritingYearLength);

        policyDto.setWithEffectFromDate(quotationProduct.getPolicyCoverFrom());
        policyDto.setWithEffectToDate(quotationProduct.getPolicyCoverTo());

        policyDto.setPolicyCoverFromDate(quotationProduct.getWithEffectFromDate());
        policyDto.setPolicyCoverToDate(quotationProduct.getWithEffectToDate());

        policyDto.setRenewalDate(quotationProduct.getWithEffectToDate());
        policyDto.setMaturityDate(quotationProduct.getMaturityDate());

        policyDto.setLastValuationDate(quotationProduct.getLastValuationDate());

        /*
         * create a renewal notification date field under quotation product object
         * Carry over renewal notification date to policy notification date
         * */

        Calendar cd = Calendar.getInstance();
        cd.setTimeInMillis(quotationProduct.getPolicyCoverTo() == null ? quotationProduct.getWithEffectToDate() : quotationProduct.getPolicyCoverTo());
        cd.add(Calendar.DAY_OF_YEAR, -30);
        var firstNotificationDate = new Date(cd.getTimeInMillis());

        if (quotationProduct.getRenewalNotificationDate() == null) {
            policyDto.setRenewalNotificationDate(firstNotificationDate);
        } else {
            policyDto.setRenewalNotificationDate(quotationProduct.getRenewalNotificationDate());
        }


        policyDto.setCurrentStatus(quotationProduct.getQuotation().getStatus());
        policyDto.setPostStatus("N");
        policyDto.setAuthorized("Y");
        policyDto.setRenewal(quotationProduct.getQuotation().isRenewal());

        policyDto.setCancelDate(new Date().getTime());

        policyDto.setLoaded("N");
        policyDto.setCommissionAllowed("Y");
        policyDto.setExchangeRateFixed("N");
        policyDto.setProductInterfaceType(PolicyInterfaceType.CASH);
        policyDto.setTransmittalRefNo(quotationProduct.getQuotation().getPaymentRef());

        convertQuoteEvent.convertQuotationProductToPolicy(policyDto);
    }

    private static void composeInstalmentFields(QuotationProduct quotationProduct, PolicyDto policyDto) {
        policyDto.setFrequencyOfPayment(quotationProduct.getPaymentFrequency());
        policyDto.setInstallmentAmount(quotationProduct.getInstallmentAmount().setScale(25, RoundingMode.HALF_UP));
        policyDto.setNextInstallmentNo(quotationProduct.getNextInstallmentNo());
        policyDto.setPaidToDate(quotationProduct.getMaturityDate());
        policyDto.setTotalNoOfInstallments(quotationProduct.getTotalNoOfInstallments());
        policyDto.setMaturityDate(quotationProduct.getMaturityDate());
        policyDto.setOutstandingInstallmentAmount((quotationProduct.getOutstandingInstallmentAmount().subtract(quotationProduct.getTotalPremium())).setScale(0, RoundingMode.HALF_UP));
        policyDto.setInstallmentPremium(quotationProduct.getInstallmentPremium().setScale(25, RoundingMode.HALF_UP));
        policyDto.setProductInstallmentId(quotationProduct.getProductInstallmentId());
        policyDto.setOutstandingCommission(quotationProduct.getOutstandingCommission().subtract(quotationProduct.getCommissionAmount()));
        policyDto.setCommInstallmentPremium(quotationProduct.getCommInstallmentPremium().setScale(25, RoundingMode.HALF_UP));
        policyDto.setCommInstallmentAmount(quotationProduct.getCommInstallmentAmount().setScale(25, RoundingMode.HALF_UP));
        policyDto.setPaidInstallmentAmount(quotationProduct.getQuotation().getPremium());
        policyDto.setPaidInstallmentComm(quotationProduct.getQuotation().getCommissionAmount());
    }

    @SneakyThrows
    @Override
    public QuotationProduct saveQuickQuotationProduct(Quotation quotation, QuotationProduct quotationProduct) {
        /*Compute withholding tax*/

        if ((quotationProduct.getWithHoldingTax() == null || Objects.equals(quotationProduct.getWithHoldingTax(), BigDecimal.ZERO))) {
            computeWithHoldingTax(quotationProduct, quotation.getOrganizationId());
        }
        if (quotationProduct.getMaturityDate() == null) {
            quotationProduct.setMaturityDate(quotationProduct.getWithEffectToDate());
        }

        List<QuotationRisk> riskList;
        if (quotationProduct.getQuotationRisks() != null && !Objects.equals(quotation.getStatus(), "RN")) {
            riskList = checkAndRemoveDuplicateRisks(new ArrayList<>(quotationProduct.getQuotationRisks()));
            log.info("QP: saved risks 1 {}", new ObjectMapper().writeValueAsString(riskList));
        } else {
            riskList = quotationProduct.getQuotationRisks();
        }

        List<QuotationProductTax> taxList = new ArrayList<>();
        if (quotationProduct.getQuotationProductTaxes() != null) {
            taxList = new ArrayList<>(quotationProduct.getQuotationProductTaxes());
        }

        int premSignage = quotationProduct.getTotalPremium().compareTo(BigDecimal.ZERO);
        if (quotationProduct.getWithHoldingTax() != null) {
            quotationProduct.setWithHoldingTax(premSignage < 0 ? quotationProduct.getWithHoldingTax().abs().negate() :
                    quotationProduct.getWithHoldingTax().abs());
        }

        if (Objects.equals(quotationProduct.getOutstandingInstallmentAmount(), BigDecimal.ZERO)) {
            quotationProduct.setOutstandingInstallmentAmount(quotationProduct.getTotalPremium());
            quotationProduct.setOutstandingCommission(quotationProduct.getCommissionAmount());
        }

        if (!Objects.equals(quotation.getStatus(), "RN")) {
            var fap = riskList
                    .stream()
                    .map(QuotationRisk::getFutureAnnualPremium)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            quotationProduct.setFutureAnnualPremium(fap.setScale(25, RoundingMode.HALF_UP));
        }


        quotationProduct.setTotalPremium(quotationProduct.getTotalPremium().setScale(25, RoundingMode.HALF_UP));
        quotationProduct.setBasicPremium(quotationProduct.getBasicPremium().setScale(25, RoundingMode.HALF_UP));
        quotationProduct.setCommissionAmount(premSignage > 0 ?
                quotationProduct.getCommissionAmount().abs().negate().setScale(25, RoundingMode.HALF_UP) :
                quotationProduct.getCommissionAmount().abs().setScale(25, RoundingMode.HALF_UP));
        quotation.setCommissionAmount(premSignage > 0 ?
                quotationProduct.getCommissionAmount().abs().negate().setScale(25, RoundingMode.HALF_UP) :
                quotationProduct.getCommissionAmount().abs().setScale(25, RoundingMode.HALF_UP));
        quotationProduct.setQuotationId(quotation.getId());
        quotationProduct.setCurrencySymbol(quotation.getCurrencySymbol());
        quotationProduct.setQuotationNo(quotation.getQuotationNo());
        quotationProduct.setQuotationId(quotation.getId());
        quotationProduct.setTotalSumInsured(quotation.getTotalSumInsured().setScale(25, RoundingMode.HALF_UP));

        if (quotation.getLastValuationDate() != null) {
            quotationProduct.setLastValuationDate(quotation.getLastValuationDate());
        }

        log.info("QP: saved risks 3 {}", new ObjectMapper().writeValueAsString(riskList));

        QuotationProduct quoteProduct = this.create(quotationProduct);


        taxList = taxList.stream()
                .map(tax -> quotationProductTaxService.saveQuickProductTax(quoteProduct, tax))
                .collect(Collectors.toList());

        riskList = riskList.stream()
                .map(risk -> quotationRiskService.saveQuickQuotationRisk(quotation, quotationProduct, quoteProduct, risk))
                .collect(Collectors.toList());

        log.info("QP: saved risks {}", new ObjectMapper().writeValueAsString(riskList));

        quotationProduct.setQuotationRisks(riskList);
        quotationProduct.setQuotationProductTaxes(taxList);

        log.info("QP WET {}", quotationProduct.getWithEffectToDate());


        return quotationProduct;
    }

    @Override
    public QuotationProduct computeWithHoldingTax(QuotationProduct quotationProduct, Long organizationId) {
        OrganizationDto org = agencyClient.findByOrganizationId(organizationId);
        if (org.getAccountType() != null) {
            BigDecimal withHolding = org.getAccountType().getWithHoldingTaxRate()
                    .divide(BigDecimal.valueOf(100))
                    .multiply((quotationProduct.getCommissionAmount().abs()));
            withHolding = withHolding.setScale(25, RoundingMode.HALF_UP);
            int premSignage = quotationProduct.getTotalPremium().compareTo(BigDecimal.ZERO);
            quotationProduct.setWithHoldingTax(premSignage > 0 ? withHolding : withHolding.negate());
        }
        log.info("Quotation Product: commission withholding tax {}", quotationProduct.getWithHoldingTax());
        return quotationProduct;
    }


    @Override
    public QuotationProduct changePaymentOption(Long quotationProductId, YesNo installmentAllowed) {
        Optional<QuotationProduct> quotationProduct = quotationProductRepository.findById(quotationProductId);
        if (quotationProduct.isPresent()) {
            quotationProduct.get().setInstallmentAllowed(installmentAllowed);
            return quotationProductRepository.save(quotationProduct.get());
        }
        return null;
    }

    @Override
    @Transactional
    public Quotation deleteQuotationProductRisk(Quotation quotation, Long riskId) {
        for (var qp : quotation.getQuotationProducts()) {
            var risksToDelete = qp.getQuotationRisks().stream()
                    .filter(qr -> Objects.equals(qr.getId(), riskId))
                    .toList();
            qp.getQuotationRisks().removeAll(risksToDelete);
        }
        quotationRiskService.delete(riskId);

        return quotation;
    }

    @Override
    public void updateQuotationProduct(UpdateQuotationDto updateQuotationDto) {
        var quotationProduct = quotationProductRepository.findById(updateQuotationDto.getQuotationProductId());
        if (quotationProduct.isPresent()) {
            var qp = quotationProduct.get();
            qp.setWithEffectFromDate(updateQuotationDto.getCoverFromDate());
            qp.setWithEffectToDate(updateQuotationDto.getCoverToDate());
            qp.setTotalSumInsured(updateQuotationDto.getSumInsured());

            quotationProductRepository.save(qp);

            /*Add quotation id and publish event */
            updateQuotationDto.setQuotationId(qp.getQuotationId());

            updateQuotationPublisher.updateQuotation(updateQuotationDto);
        }
    }
}
