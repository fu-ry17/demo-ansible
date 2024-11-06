/**
 * 2018-07-04
 */
package com.turnkey.turnquest.gis.quotation.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turnkey.turnquest.gis.quotation.aki.ValidateRiskService;
import com.turnkey.turnquest.gis.quotation.aki.error.AKIValidationException;
import com.turnkey.turnquest.gis.quotation.client.billing.PremiumCardClient;
import com.turnkey.turnquest.gis.quotation.client.certificate.CertificateClient;
import com.turnkey.turnquest.gis.quotation.client.crm.AgencyClient;
import com.turnkey.turnquest.gis.quotation.client.crm.ClientDataClient;
import com.turnkey.turnquest.gis.quotation.client.crm.InsurerCurrencyClient;
import com.turnkey.turnquest.gis.quotation.client.crm.OrganizationClient;
import com.turnkey.turnquest.gis.quotation.client.gis.CoverTypeSectionClient;
import com.turnkey.turnquest.gis.quotation.client.gis.ProductClient;
import com.turnkey.turnquest.gis.quotation.client.gis.SequenceGeneratorClient;
import com.turnkey.turnquest.gis.quotation.dto.ScheduleDetailsDto;
import com.turnkey.turnquest.gis.quotation.dto.certificate.VehicleSearchRequestDto;
import com.turnkey.turnquest.gis.quotation.dto.certificate.VehicleSearchResponseDto;
import com.turnkey.turnquest.gis.quotation.dto.computation.*;
import com.turnkey.turnquest.gis.quotation.dto.crm.*;
import com.turnkey.turnquest.gis.quotation.dto.gis.ComputationRequest;
import com.turnkey.turnquest.gis.quotation.dto.gis.FilterCoverTypeSectionsDto;
import com.turnkey.turnquest.gis.quotation.dto.notification.PushNotificationDto;
import com.turnkey.turnquest.gis.quotation.dto.quotation.PremiumCardDto;
import com.turnkey.turnquest.gis.quotation.dto.quotation.QuotationDto;
import com.turnkey.turnquest.gis.quotation.dto.quotation.QuotationRiskSectionDto;
import com.turnkey.turnquest.gis.quotation.dto.quotation.UpdateQuotationDto;
import com.turnkey.turnquest.gis.quotation.enums.*;
import com.turnkey.turnquest.gis.quotation.event.producer.NotificationProducer;
import com.turnkey.turnquest.gis.quotation.exception.error.*;
import com.turnkey.turnquest.gis.quotation.model.*;
import com.turnkey.turnquest.gis.quotation.repository.QuotationRepository;
import com.turnkey.turnquest.gis.quotation.service.EndorsementService;
import com.turnkey.turnquest.gis.quotation.service.QuotationProductService;
import com.turnkey.turnquest.gis.quotation.service.QuotationRiskService;
import com.turnkey.turnquest.gis.quotation.service.QuotationService;
import com.turnkey.turnquest.gis.quotation.specifications.QuotationsSpecifications;
import com.turnkey.turnquest.gis.quotation.util.EntitySpecification;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


/**
 * @author Kevin Kibet
 */
@Slf4j
@Service
public class QuotationServiceImpl extends AbstractQuotationService implements QuotationService {

    private final QuotationRepository repository;
    private final QuotationProductService quotationProductService;
    private final ProductClient productClient;
    private final PremiumCardClient premiumCardClient;
    private final AgencyClient agencyClient;
    private final InsurerCurrencyClient insurerCurrencyClient;
    private final SequenceGeneratorClient sequenceGeneratorClient;
    private final ValidateRiskService validateRiskService;
    private final NotificationProducer notificationProducer;
    private final QuotationRiskService quotationRiskService;
    private final CertificateClient certificateClient;
    private final CoverTypeSectionClient coverTypeSectionClient;
    private final EndorsementService endorsementService;

    public QuotationServiceImpl(QuotationRepository repository, QuotationProductService quotationProductService, ProductClient productClient, OrganizationClient organizationClient, ClientDataClient clientDataClient, PremiumCardClient premiumCardClient, AgencyClient agencyClient, InsurerCurrencyClient insurerCurrencyClient, SequenceGeneratorClient sequenceGeneratorClient, ValidateRiskService validateRiskService, NotificationProducer notificationProducer, QuotationRiskService quotationRiskService, CertificateClient certificateClient, CoverTypeSectionClient coverTypeSectionClient, EndorsementService endorsementService) {
        super(clientDataClient, agencyClient, organizationClient);
        this.repository = repository;
        this.quotationProductService = quotationProductService;
        this.productClient = productClient;
        this.premiumCardClient = premiumCardClient;
        this.agencyClient = agencyClient;
        this.insurerCurrencyClient = insurerCurrencyClient;
        this.sequenceGeneratorClient = sequenceGeneratorClient;
        this.validateRiskService = validateRiskService;
        this.notificationProducer = notificationProducer;
        this.quotationRiskService = quotationRiskService;
        this.certificateClient = certificateClient;
        this.coverTypeSectionClient = coverTypeSectionClient;
        this.endorsementService = endorsementService;
    }

    @Override
    public Quotation save(Quotation quotation) throws QuoteCreationException {
        return repository.save(quotation);
    }

    @Override
    public Optional<Quotation> findByPaymentRef(String paymentRef) {
        return repository.findByPaymentRef(paymentRef);
    }

    @Override
    public Quotation create(Quotation quotation) {

        if (quotation.getQuotationNo() != null
                && quotation.getId() != null
                && (Objects.equals(quotation.getStatus(), "NB") || Objects.equals(quotation.getStatus(), "RN"))
        ) {
            Quotation finalQuotation = quotation;
            var updatedPreviousQuotes = this.findByQuotationNo(quotation.getQuotationNo(), quotation.getOrganizationId())
                    .stream()
                    .filter(quote -> !quote.getId().equals(finalQuotation.getId()))
                    .peek(quote -> quote.setStatus(quote.getStatus() + "-ARCHIVED"))
                    .peek(quote -> quote.setCurrentStatus(quote.getCurrentStatus() + "-ARCHIVED"))
                    .map(this::save).toList();
            log.debug("Updated previous quotes {}", updatedPreviousQuotes.size());
        }


        if (!quotation.getCurrentStatus().equals("A") && !quotation.getCurrentStatus().equals("D")) {
            quotation.setCurrentStatus("R");
        }

        quotation = repository.save(quotation);
        quotation.setCurrentStatus("D");
        try {
            if (quotation.getClientId() != null) {
                var premiumCard = initPayment(quotation);
                quotation.setPaymentRef(premiumCard.getPaymentRef());
            }

        } catch (FailedToGenerateTransmittalException e) {
            throw new FailedToGenerateTransmittalException("Failed to create payment Reference Number.");
        }

        return composeQuotation(repository.save(quotation));

    }

    @Override
    public Optional<Quotation> findById(Long id, Long organizationId) {
        return repository.findByIdAndAgencyId(id, organizationId).map(quotation -> Optional.of(this.composeQuotation(quotation))).orElseThrow(() -> new ResourceNotFoundException(Quotation.class, "id", id));
    }


    @Override
    public Optional<Quotation> findById(Long id) {
        return repository.findById(id).map(quotation -> Optional.of(this.composeQuotation(quotation))).orElseThrow(() -> new ResourceNotFoundException(Quotation.class, "id", id));
    }


    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }


    @Override
    public List<Quotation> findAll(Long organizationId, Pageable pageable) {
        Page<Quotation> quotations = repository.findAllByOrganizationIdOrderByQuotationNoAscCreatedDateDesc(organizationId, pageable);
        Map<String, Quotation> latestQuotationsMap = new LinkedHashMap<>();
        for (Quotation quotation : quotations.getContent()) {
            if (quotation.getQuotationNo() != null) {
                latestQuotationsMap.putIfAbsent(quotation.getQuotationNo(), quotation);
            }
        }
        return latestQuotationsMap.values()
                .stream()
                .filter(Objects::nonNull)
                .map(this::composeQuotation)
                .toList();
    }


    @Override
    public List<Quotation> findByQuotationNo(String quotationNo, Long organization) {
        return repository.findByQuotationNoAndOrganizationId(quotationNo, organization);
    }


    @Override
    public List<Quotation> findByClientIds(List<Long> clientIds, Long organizationId, Pageable pageable) {
        return repository.findAllByClientIdInAndOrganizationId(clientIds, organizationId, pageable).stream().map(this::composeQuotation).collect(Collectors.toList());
    }


    @Override
    public List<Quotation> findClientRenewals(List<Long> clientIds, Long organizationId, Pageable pageable) {
        return repository.findAllByClientIdInAndOrganizationIdAndStatus(clientIds, organizationId, "RN", pageable).stream().map(this::composeQuotation).collect(Collectors.toList());
    }


    @Override
    public Quotation update(Long id, QuotationDto quotation, Long organizationId) {
        var mapper = new ModelMapper();
        var quotationDetails = mapper.map(quotation, Quotation.class);
        var quote = repository.findByIdAndOrganizationId(quotationDetails.getId(), organizationId);
        if (quote.isPresent()) {
            return repository.save(quotationDetails);
        } else {
            throw new ResourceNotFoundException(Quotation.class, "ID", quotationDetails.getId());
        }
    }

    @Override
    public Long count(Long organizationId) {
        return repository.countByOrganizationId(organizationId);
    }


    @Override
    public String generateQuotationNumber(Long insurerOrgId, Long productId) {
        var quotationAttributes = new HashMap<String, String>();

        var product = productClient.findById(productId);

        quotationAttributes.put("productCode", product.getPolicyPrefix());
        quotationAttributes.put("organizationId", insurerOrgId.toString());
        return sequenceGeneratorClient.generateQuotationNumber(quotationAttributes);
    }


    /**
     * Find all quotations for a specific quotation that are not renewal based
     *
     * @param organizationId user org id
     * @return list of quotations
     */
    @Override
    public List<Quotation> findAllQuotations(Long organizationId, Pageable pageable) {
        return repository.findByOrganizationIdAndStatus(organizationId, "NB", pageable).stream().map(this::composeQuotation).collect(Collectors.toList());
    }

    @Override
    public List<Quotation> findAllByCurrentStatusAndStatus(String currentStatus, String status, Long
            organization, Pageable pageable) {
        return repository.findAllByCurrentStatusAndStatusAndOrganizationId(currentStatus, status, organization, pageable).stream().map(this::composeQuotation).collect(Collectors.toList());
    }

    @Override
    public List<QuotationProduct> findQuotationProducts(Long id, Long organization) {
        return quotationProductService.findByQuotationId(id);
    }

    @Override
    public void convertQuotationToPolicies(Long id, Long organizationId) {
        Optional<Quotation> optionalQuotation = this.findById(id, organizationId);
        if (optionalQuotation.isPresent()) {
            this.convertQuotationToPolicies(optionalQuotation.get(), organizationId);
        } else {
            throw new ResourceNotFoundException(Quotation.class, "quotationId", id);
        }
    }


    @Override
    public void convertQuotationToPolicies(Long id) {
        Quotation quotation = this.findById(id).orElse(null);
        if (quotation != null) {
            this.convertQuotationToPolicies(quotation);
        } else {
            throw new ResourceNotFoundException(Quotation.class, "quotationId", id);
        }

    }

    @SneakyThrows
    @Override
    public void convertQuoteToPolicies(Long id, Long receiptId, String sourceRef) {
        Quotation quotation = this.findById(id).orElse(null);

        if (quotation != null && !Objects.equals(quotation.getCurrentStatus(), "A")) {
            for (QuotationProduct quotationProduct : quotation.getQuotationProducts()) {
                if (quotationProduct.getInstallmentAllowed().isTrue()) {

                    /*Save Annual Risks as active risks*/
                    var savedActiveRisks = endorsementService.saveActiveRisks(quotation, YesNo.Y);
                    log.debug("Saved annual active risks ======= {}", new ObjectMapper().writeValueAsString(savedActiveRisks));

                    /*Generate partial quote with first installment*/
                    var partialActiveRisks = endorsementService.computeFirstInstallment(quotation);
                    this.convertQuotationToPolicies(partialActiveRisks);
                } else {
                    /*Save Annual Risks as active risks*/
                    var savedActiveRisks = endorsementService.saveActiveRisks(quotation, YesNo.Y);
                    log.debug("Saved annual active risks ======= {}", new ObjectMapper().writeValueAsString(savedActiveRisks));
                    this.convertQuotationToPolicies(quotation);
                }
            }
        } else {
            throw new ResourceNotFoundException(Quotation.class, "quotationId", id);
        }
    }

    @Override
    public void convertQuotationToPolicies(Quotation quotation, Long organizationId) {
        if (quotation != null) {
            this.convertQuotationToPolicies(quotation);
        } else {
            throw new ItemCannotBeNullException("Quotation cannot be null");
        }

    }

    @SneakyThrows
    public void convertQuotationToPolicies(Quotation quotation) {
        for (QuotationProduct quotationProduct : quotation.getQuotationProducts()) {
            quotationProduct.setQuotation(quotation);
            quotationProductService.convertToPolicy(quotationProduct);
        }
    }


    @SneakyThrows
    @Override
    public Quotation saveQuickQuotation(Quotation quickQuote, Long organizationId) throws QuoteCreationException {

        log.info("Quotation: raw quote {}", new ObjectMapper().writeValueAsString(quickQuote));


        List<QuotationProduct> productList = new ArrayList<>(quickQuote.getQuotationProducts());
        quickQuote.setOrganizationId(organizationId);

        /*To be fixed latter with dynamic currency */
        quickQuote.setCurrencyId(35L);
        checkRenewalExists(quickQuote);

        if (quickQuote.getStatus() != null && quickQuote.getStatus().equals("RN")) {
            quickQuote.setRenewal(true);
        }

        /*Generate quotation Number*/
        if (quickQuote.getQuotationNo() == null) {
            quickQuote.setQuotationNo(this.generateQuotationNumber(quickQuote.getInsurerOrgId(), quickQuote.getQuotationProducts().getFirst().getProductId()));
        }

        /*Update currency*/
        updateCurrencySymbol(quickQuote, organizationId);
        Quotation quotation = this.save(quickQuote);


        for (QuotationProduct quotationProduct : productList) {
            quotationProductService.saveQuickQuotationProduct(quotation, quotationProduct);
        }

        quotation.setQuotationProducts(productList);


        quotation.setCoverToDate(productList.getLast().getWithEffectToDate());

        if (quickQuote.getLastValuationDate() != null) {
            quotation.setLastValuationDate(quickQuote.getLastValuationDate());
        }

        quotation = this.create(quotation);

        log.info("Quotation: saved {}", new ObjectMapper().writeValueAsString(quotation));
        return composeQuotation(quotation);
    }

    private void checkRenewalExists(Quotation quickQuote) {
        if (quickQuote.getRenewalBatchNo() != null) {
            Quotation existing = this.findByRenewalBatchNo(quickQuote.getRenewalBatchNo()).orElse(null);
            if (existing != null && Objects.equals(existing.getStatus(), "RN")) {
                quickQuote.setRenewalBatchNo(existing.getRenewalBatchNo());
                quickQuote.setId(existing.getId());
            }
        }
    }

    private Quotation updateCurrencySymbol(Quotation quotation, Long organizationId) {
        /*Find insurer currency symbol*/
        AgencyDto agencyDto = agencyClient.findByPanelIdAndOrganizationId(quotation.getPanelId(), organizationId);
        if (agencyDto != null) {
            InsurerCurrencyDto insurerCurrency = insurerCurrencyClient.findInsurerCurrency(quotation.getCurrencyId(), agencyDto.getInsurerId());
            if (insurerCurrency != null) {
                quotation.setCurrencySymbol(insurerCurrency.getSymbol());
            } else {
                quotation.setCurrencySymbol("KES");
            }
        } else {
            quotation.setCurrencySymbol("KES");
        }
        return quotation;
    }


    @Override
    public List<Quotation> getRenewals(Long organizationId, Pageable pageable) {
        return repository.findByOrganizationIdAndStatus(organizationId, "RN", pageable).stream().map(this::composeQuotation).collect(Collectors.toList());
    }


    /**
     * Create a transmittal record (invoice)
     *
     * @param quotation quotation object
     * @return Transmittal response object
     * @throws FailedToGenerateTransmittalException
     */
    @Override
    public PremiumCardDto initPayment(Quotation quotation) {
        PremiumCardDto premiumCardDto = new PremiumCardDto();

        AtomicBoolean isInstallmentAllowed = new AtomicBoolean(false);
        AtomicReference<BigDecimal> amount = new AtomicReference<>(BigDecimal.ZERO);

        //This implementation will change once we have more than one quotation product
        quotation.getQuotationProducts().forEach(quotationProduct -> {
            if (quotationProduct.getInstallmentAllowed() == YesNo.Y) {
                isInstallmentAllowed.set(true);
                amount.set(quotationProduct.getInstallmentAmount());
            }
        });

        BigDecimal policyTaxes = quotation.getQuotationProducts().stream()
                .flatMap(quotationProduct -> quotationProduct.getQuotationProductTaxes().stream())
                .filter(policyTax -> policyTax.getApplicationArea().equals("P"))
                .map(QuotationProductTax::getTaxAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal riskTaxes = quotation.getQuotationProducts().stream()
                .flatMap(quotationProduct -> quotationProduct.getQuotationProductTaxes().stream())
                .filter(riskTax -> riskTax.getApplicationArea().equals("R"))
                .map(QuotationProductTax::getTaxAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        premiumCardDto.setPolicyTaxes(policyTaxes);
        premiumCardDto.setQuotationNo(quotation.getQuotationNo());
        premiumCardDto.setBasicPremium(quotation.getBasicPremium());
        premiumCardDto.setPaymentFrequency(quotation.getPaymentFrequency());
        premiumCardDto.setTotalPremium(quotation.getPremium());
        premiumCardDto.setPolicyTaxes(policyTaxes);
        premiumCardDto.setRiskTaxes(riskTaxes);
        premiumCardDto.setCommission(quotation.getCommissionAmount());
        premiumCardDto.setClientId(quotation.getClientId());
        premiumCardDto.setQuotationId(quotation.getId());
        premiumCardDto.setOrganizationId(quotation.getOrganizationId());
        premiumCardDto.setPaymentRef(quotation.getPaymentRef());
        premiumCardDto.setWithHoldingTax(quotation.getWithholdingTax());


        return premiumCardClient.initPayment(premiumCardDto);
    }

    @Override
    public List<Quotation> sortFilterAndSearch(String searchText, Long clientId, Long insOrgId, Long
            coverFromDate, Long coverToDate, BigDecimal priceFrom, BigDecimal priceTo, SortType sortBy, SortType sortPrice,
                                               int page, int size, Long organizationId, String status) {

        Pageable pageable = PageRequest.of(page, size);
        if (sortBy != null && sortPrice != null) {
            pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(Sort.Direction.fromString(sortPrice.name()), "premium"), new Sort.Order(Sort.Direction.fromString(sortBy.name()), "createdDate")));
        } else if (sortBy == null && sortPrice != null) {
            pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(Sort.Direction.fromString(sortPrice.name()), "premium")));
        } else if (sortBy != null) {
            pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(Sort.Direction.fromString(sortBy.name()), "createdDate")));
        }

        Page<Quotation> quote;
        QuotationsSpecifications<Quotation> quotationsSpecifications = new QuotationsSpecifications<>();
        if (searchText != null) {
            EntitySpecification<Quotation> entitySpecification = new EntitySpecification<>();
            quote = repository.findAll(Specification.where(entitySpecification.textInAllColumns(searchText)).and(quotationsSpecifications.longValueEquals("organizationId", organizationId)).and(quotationsSpecifications.longValueEquals("clientId", clientId)).and(quotationsSpecifications.longValueBetween("createdDate", coverFromDate, coverToDate)).and(quotationsSpecifications.stringValueEquals("status", status)).and(quotationsSpecifications.bigDecimalValueBetween("premium", priceFrom, priceTo)).and(quotationsSpecifications.longValueEquals("insurerOrgId", insOrgId)), pageable);
            log.info("Quotations 1 {}", quote.getSize());
        } else {
            quote = repository.findAll(Specification.where(quotationsSpecifications.longValueEquals("organizationId", organizationId)).and(quotationsSpecifications.longValueEquals("clientId", clientId)).and(quotationsSpecifications.longValueBetween("createdDate", coverFromDate, coverToDate)).and(quotationsSpecifications.stringValueEquals("status", status)).and(quotationsSpecifications.bigDecimalValueBetween("premium", priceFrom, priceTo)).and(quotationsSpecifications.longValueEquals("insurerOrgId", insOrgId)), pageable);
            log.info("Quotations 2 {}", quote.getSize());
        }
        return quote.stream().map(this::composeQuotation).collect(Collectors.toList());
    }

    @Override
    public List<Quotation> getQuotationsByClientIdAndOrgId(Long clientId, Long organizationId) {
        return repository.findByClientIdAndOrganizationIdAndCurrentStatus(clientId, organizationId, "R");
    }

    @SneakyThrows
    @Override
    public Boolean verifyQuotation(Long quotationId) {

        var isAkiIntegrated = repository.findById(quotationId).filter(quote -> quote.getPanelId() != null).map(quote -> agencyClient.findByPanelIdAndOrganizationId(quote.getPanelId(), quote.getOrganizationId())).map(AgencyDto::getOrganization).map(OrganizationDto::getEntities).map(EntitiesDto::getAkiIntegrated).orElse(false);

        var validationResponse = validateRiskService.validateRisk(quotationId);
        boolean isValid = isAkiIntegrated && validationResponse.getSuccess();

        if (!isValid && validationResponse != null && validationResponse.getErrors() != null) {
            StringBuilder stringBuilder = new StringBuilder();
            validationResponse.getErrors().forEach(errorDto -> {
                stringBuilder.append(errorDto.getErrorCode());
                stringBuilder.append("\n");
                stringBuilder.append(errorDto.getErrorText());
                stringBuilder.append("\n");
            });
            throw new AKIValidationException(stringBuilder.toString());

        } else {
            return true;
        }

    }

    @Override
    public int getNoRenewalsFromDate(Long startDate, Long endDate, Long organizationId) {
        if (endDate == null) {
            return repository.findAllByOrganizationIdAndCreatedDateIsGreaterThanAndStatus(organizationId, startDate, "RN").size();
        } else {
            return repository.findAllByOrganizationIdAndCreatedDateIsGreaterThanAndCreatedDateIsLessThanAndStatus(organizationId, startDate, endDate, "RN").size();
        }
    }

    @Override
    public Optional<Quotation> findByRenewalBatchNo(Long batchNo) {
        return repository.findByRenewalBatchNo(batchNo);
    }

    @Override
    public Mono<Integer> findUnreadQuotes(Long organizationId) {
        return Mono.just(repository.findAllByReadStatusAndStatusAndOrganizationId(false, "NB", organizationId).size());
    }

    @Override
    public Mono<Integer> findUnreadRenewals(Long organizationId) {
        return Mono.just(repository.findAllByReadStatusAndStatusAndOrganizationId(false, "RN", organizationId).size());
    }

    @Override
    public List<Quotation> getByPolicyNo(String policyNo) {
        return repository.findByPolicyNo(policyNo);
    }

    public List<Quotation> getByPolicyNoOrderByIdDesc(String policyNo) {
        return repository.findByPolicyNoOrderByIdDesc(policyNo);
    }

    @Override
    public boolean deleteByPolicyNo(String policyNo) {

        var deletedQuotes = repository.findByPolicyNo(policyNo).stream().filter(quotation -> quotation.getCurrentStatus().equals("D")).map(Quotation::getId).map(id -> {
            repository.deleteById(id);
            return true;
        });
        log.debug("{} quotes deleted", deletedQuotes.count());
        return true;
    }

    @Override
    public Quotation findByPolicyNo(String policyNo) {
        return repository.findByPolicyNo(policyNo).stream().filter(quotation -> quotation.getCurrentStatus().equals("D")).findFirst().orElse(null);
    }

    @Override
    public List<Quotation> findByQuotationNo(String quotationNo) {
        return repository.findByQuotationNo(quotationNo);
    }

    public List<Quotation> getByQuotationNoOrderByIdDesc(String quotationNo) {
        return repository.findByQuotationNoOrderByIdDesc(quotationNo);
    }

    private void sendValuationNotification(Quotation quotation, String templateCode) {
//        QuotationRisk risk = geTCurrentlyValuedRisk(quotation);
        //TODO: Move this to endorsement service
        QuotationRisk risk = new QuotationRisk();
        log.debug("Risk with latest valuation {}", risk.getRiskId());

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("[IPU_CODE]", risk.getRiskId());

        PushNotificationDto pushNotificationDto = new PushNotificationDto();
        pushNotificationDto.setId(quotation.getQuotationNo());
        pushNotificationDto.setOrganizationId(quotation.getOrganizationId());
        pushNotificationDto.setTemplateCode(templateCode);
        pushNotificationDto.setAttributes(attributes);

        notificationProducer.queuePushNotification(pushNotificationDto);
    }


    @Override
    public Optional<Quotation> findQuotationDraft(String policyNo) {
        return repository.findTopByPolicyNoAndStatusAndCurrentStatusOrderByIdDesc(policyNo, "EN", "D");
    }


    @SneakyThrows
    @Override
    public Quotation deleteQuotationRisk(Long riskId, Long quotationId) {

        var optionalQuote = this.findById(quotationId);
        if (optionalQuote.isPresent()) {
            var quotation = optionalQuote.get();
            for (var qp : quotation.getQuotationProducts()) {

                if (qp.getQuotationRisks().size() > 1) {
                    quotation = quotationProductService.deleteQuotationProductRisk(quotation, riskId);
                } else {
                    throw new QuoteRiskDeletionException("Cannot delete primary risk!");
                }
            }
            log.info("Quotation after deletion: {}", quotation.getQuotationProducts().getFirst().getQuotationRisks().size());
            var updatedQuote = updateQuotationValues(quotation);

            return saveQuickQuotation(updatedQuote, updatedQuote.getOrganizationId());
        }
        throw new ResourceNotFoundException(Quotation.class, "id", quotationId);
    }

    @SneakyThrows
    @Override
    public Quotation createComputationQuotation(ComputationResponse computationResponse, Long quotationId, Long
            organizationId) {
        log.debug("Comparison obj {}", new ObjectMapper().writeValueAsString(computationResponse));
        Quotation quotation = new Quotation();

        if (quotationId != null) {
            quotation = this.findById(quotationId).orElse(new Quotation());
        }

        var quoteRisks = new ArrayList<QuotationRisk>();
        var quoteProducts = new ArrayList<QuotationProduct>();

        var rate = computationResponse.getPremiums().stream().map(Premium::getRate).findFirst().orElse(new Rate());
        var riskSections = createQuotationRiskSections(computationResponse, quotation);

        var riskTaxes = buildQuotationRiskTax(computationResponse, quotation);

        var riskCommission = computationResponse.getCommissions().stream().findFirst().orElse(null);

        var riskSchedules = buildQuotationRiskSchedule(computationResponse, organizationId, quotation);

        var quotationRisk = buildQuotationRisk(computationResponse, riskSections, riskTaxes, riskCommission, quotation);
        quotationRisk.setMotorSchedules(riskSchedules);

        quoteRisks.add(quotationRisk);

        var quotationProduct = buildQuotationProduct(computationResponse, quoteRisks, rate, quotation);

        log.debug("Initial product Quote {}", quotationProduct);

        quoteProducts.add(quotationProduct);

        quotation = buildQuotation(organizationId, quoteProducts, rate, quotation);

        log.debug("Initial Quote tosave {}", quotation);

        return this.saveQuickQuotation(quotation, organizationId);
    }

    private MotorSchedules buildQuotationRiskSchedule(ComputationResponse computationResponse, Long
            organizationId, Quotation quotation) {
        var motorSchedules = initSchedules(quotation);
        try {
            var dmvicSchedules = new VehicleSearchResponseDto();

            if (!computationResponse.getPropertyId().isEmpty()) {
                dmvicSchedules = certificateClient.vehicleDetails(new VehicleSearchRequestDto(computationResponse.getPropertyId()));
            }

            motorSchedules.setRiskId(computationResponse.getPropertyId());
            motorSchedules.setOrganizationId(organizationId);
            motorSchedules.setBodyType(dmvicSchedules.getBodyType());
            motorSchedules.setValue(computationResponse.getSumInsured());
            motorSchedules.setCarryCapacity(dmvicSchedules.getCarryCapacity() != null ? Long.parseLong(dmvicSchedules.getCarryCapacity()) : null);
            motorSchedules.setChasisNo(dmvicSchedules.getChasisNo());
            motorSchedules.setEngineNo(dmvicSchedules.getEngineNo());
            motorSchedules.setMake(dmvicSchedules.getMake());
            motorSchedules.setModel(dmvicSchedules.getModel());
            motorSchedules.setYearOfManufacture(dmvicSchedules.getYearOfManufacture());
        } catch (Exception e) {
            log.error("Failed to get vehicle details from DMVIC {}", e.getMessage());
        }
        return motorSchedules;
    }

    private MotorSchedules initSchedules(Quotation quotation) {
        if (quotation.getQuotationProducts() == null) {
            return new MotorSchedules();
        }
        return quotation.getQuotationProducts().stream().flatMap(product -> product.getQuotationRisks().stream()).map(QuotationRisk::getMotorSchedules).filter(Objects::nonNull).findFirst().orElse(new MotorSchedules());
    }

    private Quotation buildQuotation(Long organizationId, ArrayList<QuotationProduct> quoteProducts, Rate
            rate, Quotation existingQuotation) {
        var quotation = existingQuotation == null ? new Quotation() : existingQuotation;

        var sumInsured = quoteProducts.stream().map(QuotationProduct::getTotalSumInsured).reduce(BigDecimal.ZERO, BigDecimal::add);

        var productTotalPremium = quoteProducts.stream().map(QuotationProduct::getTotalPremium).reduce(BigDecimal.ZERO, BigDecimal::add);

        var productBasicPremium = quoteProducts.stream().map(QuotationProduct::getBasicPremium).reduce(BigDecimal.ZERO, BigDecimal::add);

        var productCommission = quoteProducts.stream().map(QuotationProduct::getCommissionAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        quotation.setQuotationProducts(quoteProducts);
        quotation.setPremium(productTotalPremium);
        quotation.setGrossPremium(productTotalPremium);
        quotation.setBasicPremium(productBasicPremium);
        quotation.setCommissionAmount(productCommission);
        quotation.setWithholdingTax(productCommission.multiply(BigDecimal.valueOf(0.1)));
        quotation.setOrganizationId(organizationId);
        quotation.setTotalSumInsured(sumInsured);
        quotation.setCoverFromDate(quoteProducts.getFirst().getPolicyCoverFrom());
        quotation.setCoverToDate(quoteProducts.getFirst().getPolicyCoverTo());
        quotation.setPanelId(rate != null ? rate.getBinder().getPanelId() : null);
        quotation.setInsurerOrgId(rate != null ? rate.getBinder().getOrganization().getId() : null);
        quotation.setStatus("NB");
        quotation.setCurrentStatus("R");
        return quotation;
    }

    @NotNull
    private QuotationProduct buildQuotationProduct(ComputationResponse
                                                           computationResponse, ArrayList<QuotationRisk> quoteRisks, Rate rate, Quotation quotation) {
        var quoteProductTaxes = buildQuotationProductTax(computationResponse, quotation);

        var sumInsured = quoteRisks.stream().filter(quote -> quote.getValue() != null).map(QuotationRisk::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);

        var productBasicPremium = quoteRisks.stream().map(QuotationRisk::getBasicPremium).reduce(BigDecimal.ZERO, BigDecimal::add);

        var productTaxAmount = quoteProductTaxes.stream().map(QuotationProductTax::getTaxAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        var productCommissionAmount = quoteRisks.stream().map(QuotationRisk::getCommissionAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        var quotationProduct = initQuotationProduct(quotation);
        quotationProduct.setQuotationRisks(quoteRisks);
        quotationProduct.setTotalSumInsured(sumInsured);
        quotationProduct.setQuotationProductTaxes(quoteProductTaxes);
        quotationProduct.setBasicPremium(productBasicPremium);
        quotationProduct.setTotalPremium(productBasicPremium.add(productTaxAmount));
        quotationProduct.setCommissionAmount(productCommissionAmount);
        quotationProduct.setPolicyCoverFrom(computationResponse.getCoverFromDate());
        quotationProduct.setPolicyCoverTo(computationResponse.getCoverToDate());
        quotationProduct.setWithEffectFromDate(computationResponse.getCoverFromDate());
        quotationProduct.setWithEffectToDate(computationResponse.getCoverToDate());
        quotationProduct.setProductId(rate.getBinder().getProductId());
        quotationProduct.setProductGroupId(rate.getBinder().getProductGroupId());

        return quotationProduct;
    }

    private QuotationProduct initQuotationProduct(Quotation quotation) {
        if (quotation.getId() == null) {
            return new QuotationProduct();
        }
        return quotation.getQuotationProducts().stream().filter(Objects::nonNull).findFirst().orElse(new QuotationProduct());
    }

    @SneakyThrows
    @NotNull
    private QuotationRisk buildQuotationRisk(ComputationResponse
                                                     computationResponse, List<QuotationRiskSection> riskSections, List<QuotationRiskTax> riskTaxes, Commission
                                                     riskCommission, Quotation quotation) {
        var rate = computationResponse.getPremiums().stream().map(Premium::getRate).findAny().orElse(new Rate());

        var riskBasicPremium = riskSections.stream().map(QuotationRiskSection::getPremiumAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        var riskTaxAmount = riskTaxes.stream().map(QuotationRiskTax::getTaxAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        var quotationRisk = initQuotationRisk(quotation);
        quotationRisk.setCoverTypeId(rate.getSubClassCoverTypeSection().getCoverTypeId());
        quotationRisk.setCoverTypeCode(rate.getCoverType().getShtDesc());
        quotationRisk.setBasicPremium(riskBasicPremium);
        quotationRisk.setTotalPremium(riskBasicPremium.add(riskTaxAmount));
        quotationRisk.setFutureAnnualPremium(riskBasicPremium.add(riskTaxAmount));
        quotationRisk.setValue(computationResponse.getSumInsured());
        quotationRisk.setBinderId(rate.getBinderId());
        quotationRisk.setProductSubClassId(rate.getSubClassCoverTypeSection().getProductSubClassId());
        quotationRisk.setSubClassId(rate.getSubClassCoverTypeSection().getSubClassId());
        quotationRisk.setRiskId(computationResponse.getPropertyId());
        quotationRisk.setWithEffectFromDate(computationResponse.getCoverFromDate());
        quotationRisk.setWithEffectToDate(computationResponse.getCoverToDate());
        quotationRisk.setQuotationRiskTaxes(riskTaxes);
        quotationRisk.setQuotationRiskSections(riskSections);
        if (riskCommission != null) {
            quotationRisk.setCommissionAmount(riskCommission.getCommission().negate());
            quotationRisk.setCommissionRate(riskCommission.getRate().getRate());
            quotationRisk.setOutstandingCommission(riskCommission.getCommission());
        }
        return quotationRisk;
    }

    private QuotationRisk initQuotationRisk(Quotation quotation) {
        if (quotation.getQuotationProducts() == null) {
            return new QuotationRisk();
        }
        return quotation.getQuotationProducts().stream().flatMap(product -> product.getQuotationRisks().stream()).filter(Objects::nonNull).findAny().orElse(new QuotationRisk());
    }

    @NotNull
    private List<QuotationRiskTax> buildQuotationRiskTax(ComputationResponse computationResponse, Quotation
            quotation) {
        return computationResponse.getTaxes().stream().filter(tax -> Objects.equals(tax.getRate().getApplicationArea(), "R")).map(tax -> {
            var quotationRiskTax = initQuotationRiskTax(quotation, tax);
            quotationRiskTax.setTaxAmount(tax.getTax());
            quotationRiskTax.setRate(tax.getRate().getRate());
            quotationRiskTax.setApplicationArea(tax.getRate().getApplicationArea());
            quotationRiskTax.setDivisionFactor(tax.getRate().getDivisionFactor());
            quotationRiskTax.setProductSubClassId(tax.getRate().getProductSubclassId());
            quotationRiskTax.setRiskOrProductLevel(tax.getRate().getApplicationArea());
            quotationRiskTax.setTaxRateCategory(TaxRateCategory.valueOf(tax.getRate().getCategory()));
            quotationRiskTax.setTaxRateDescription(tax.getRate().getRateDesc());
            quotationRiskTax.setTaxRateId(tax.getRate().getId());
            quotationRiskTax.setTaxRateType(tax.getRate().getRateType());
            quotationRiskTax.setTaxType(tax.getRate().getTaxType());
            quotationRiskTax.setTransactionTypeCode(tax.getRate().getTransactionTypeCode());
            return quotationRiskTax;
        }).toList();
    }

    private QuotationRiskTax initQuotationRiskTax(Quotation quotation, Tax tax) {
        if (quotation.getId() == null) {
            return new QuotationRiskTax();
        }
        return quotation.getQuotationProducts().stream().flatMap(product -> product.getQuotationRisks().stream()).flatMap(risk -> risk.getQuotationRiskTaxes().stream()).filter(quotationRiskTax -> Objects.equals(quotationRiskTax.getTaxType(), tax.getRate().getTaxType())).findFirst().orElse(new QuotationRiskTax());
    }

    private List<QuotationProductTax> buildQuotationProductTax(ComputationResponse computationResponse, Quotation
            quotation) {
        return computationResponse.getTaxes().stream().map(tax -> {
            var quotationProductTax = initQuotationProductTax(quotation, tax);
            quotationProductTax.setTaxAmount(tax.getTax());
            quotationProductTax.setRate(tax.getRate().getRate());
            quotationProductTax.setApplicationArea(tax.getRate().getApplicationArea());
            quotationProductTax.setDivisionFactor(tax.getRate().getDivisionFactor());
            quotationProductTax.setProductSubClassId(tax.getRate().getProductSubclassId());
            quotationProductTax.setRiskOrProductLevel(tax.getRate().getApplicationArea());
            quotationProductTax.setTaxRateCategory(TaxRateCategory.valueOf(tax.getRate().getCategory()));
            quotationProductTax.setTaxRateDescription(tax.getRate().getRateDesc());
            quotationProductTax.setTaxRateId(tax.getRate().getId());
            quotationProductTax.setTaxRateType(tax.getRate().getRateType());
            quotationProductTax.setTaxType(tax.getRate().getTaxType());
            quotationProductTax.setTransactionTypeCode(tax.getRate().getTransactionTypeCode());
            return quotationProductTax;
        }).toList();
    }

    private QuotationProductTax initQuotationProductTax(Quotation quotation, Tax tax) {
        if (quotation.getId() == null) {
            return new QuotationProductTax();
        }
        return quotation.getQuotationProducts().stream().flatMap(product -> product.getQuotationProductTaxes().stream()).filter(quotationRiskTax -> Objects.equals(quotationRiskTax.getTaxType(), tax.getRate().getTaxType())).findFirst().orElse(new QuotationProductTax());
    }

    @NotNull
    private List<QuotationRiskSection> createQuotationRiskSections(ComputationResponse
                                                                           computationResponse, Quotation quotation) {

        return computationResponse.getPremiums().stream().map(premium -> {
            var riskSection = initQuotationRiskSections(quotation, premium);
            riskSection.setLimitAmount(premium.getSectionLimit());
            riskSection.setPremiumAmount(premium.getPremiumAmt());
            riskSection.setSectionId(premium.getRate().getSubClassCoverTypeSection().getSectionId());
            riskSection.setAnnualPremiumAmount(premium.getPremiumAmt());
            riskSection.setDescription(premium.getRate().getSubClassCoverTypeSection().getSubClassSection().getShtDesc());
            riskSection.setFreeLimitAmount(premium.getRate().getFreeLimit());
            riskSection.setMinimumPremiumAmount(premium.getRate().getPremiumMinimumAmount());
            riskSection.setMultiplierDivisionFactor(premium.getRate().getMultiplierDivisionFactor());
            riskSection.setRateDivisionFactor(premium.getRate().getDivisionFactor());
            riskSection.setMultiplierRate(premium.getRate().getMultiplierRate());
            riskSection.setPremiumRate(premium.getRate().getRate());
            riskSection.setProrated(premium.getRate().getProratedFull());
            riskSection.setPremiumRateDescription(premium.getRate().getRateDescription());
            riskSection.setSectionType(premium.getRate().getSubClassCoverTypeSection().getSubClassSection().getSectType());
            riskSection.setSubClassSectionId(premium.getRate().getSubClassSectionId());
            riskSection.setSumInsuredRate(premium.getRate().getRate());
            riskSection.setSubClassSectionDesc(premium.getRate().getSubClassCoverTypeSection().getSubClassSection().getShtDesc());
            riskSection.setRateType(PremiumRateType.valueOf(premium.getRate().getRateType()));
            riskSection.setBenefitType(Objects.equals(premium.getRate().getRateDesc(), "Percent") ? BenefitType.LIMIT : BenefitType.AMOUNT);
            return riskSection;
        }).toList();
    }

    private QuotationRiskSection initQuotationRiskSections(Quotation quotation, Premium premium) {
        if (quotation.getId() == null) {
            return new QuotationRiskSection();
        }
        return quotation.getQuotationProducts().stream().flatMap(qp -> qp.getQuotationRisks().stream()).flatMap(qr -> qr.getQuotationRiskSections().stream()).filter(riskSection -> Objects.equals(riskSection.getSubClassSectionId(), premium.getRate().getSubClassCoverTypeSection().getSubClassSectionId())).findFirst().orElse(new QuotationRiskSection());
    }


    /**
     * Updates the quotation and quotation product values upon risk deletion.
     * This method is public to allow for the use of @Transactional. This allows rolling back in case of an error
     *
     * @param quotation the quotation to which the risk belongs to
     * @return
     */
    private Quotation updateQuotationValues(Quotation quotation) {
        for (var qp : quotation.getQuotationProducts()) {
            updateQuotationProductValuesFromRisks(qp);
        }

        return updateQuotationValuesFromProducts(quotation);
    }

    private static Quotation updateQuotationValuesFromProducts(Quotation quotation) {
        var qpSumInsured = quotation.getQuotationProducts().stream().map(QuotationProduct::getTotalSumInsured).reduce(BigDecimal.ZERO, BigDecimal::add);

        var qpTotalPremium = quotation.getQuotationProducts().stream().map(QuotationProduct::getTotalPremium).reduce(BigDecimal.ZERO, BigDecimal::add);

        log.info("quotationProduct1 {}", qpTotalPremium);

        var qpCommission = quotation.getQuotationProducts().stream().map(QuotationProduct::getCommissionAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        var qpInstallmentPremium = quotation.getQuotationProducts().stream().map(QuotationProduct::getInstallmentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        log.info("quotationProduct12 {}", qpInstallmentPremium);

        var qpInstallmentCommission = quotation.getQuotationProducts().stream().map(QuotationProduct::getCommInstallmentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);


        quotation.setTotalSumInsured(qpSumInsured);
        quotation.setPremium(qpTotalPremium);
        quotation.setCommissionAmount(qpCommission);
        quotation.setInstallmentPremium(qpInstallmentPremium);
        quotation.setInstallmentCommission(qpInstallmentCommission);
        log.info("quotationProduct13 {}", quotation.getQuotationProducts().getFirst());
        return quotation;
    }

    private static void updateQuotationProductValuesFromRisks(QuotationProduct qp) {
        var riskSumInsured = qp.getQuotationRisks().stream().map(QuotationRisk::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);

        var riskInstallmentPremium = qp.getQuotationRisks().stream().map(QuotationRisk::getInstallmentPremium).reduce(BigDecimal.ZERO, BigDecimal::add);

        var riskInstallmentAmount = qp.getQuotationRisks().stream().map(QuotationRisk::getInstallmentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        var riskCommissions = qp.getQuotationRisks().stream().map(QuotationRisk::getCommissionAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        var riskBasicPremium = qp.getQuotationRisks().stream().map(QuotationRisk::getBasicPremium).reduce(BigDecimal.ZERO, BigDecimal::add);

        var riskTotalPremium = qp.getQuotationRisks().stream().map(QuotationRisk::getTotalPremium).reduce(BigDecimal.ZERO, BigDecimal::add);

        var riskFutureAnnualPremium = qp.getQuotationRisks().stream().map(QuotationRisk::getFutureAnnualPremium).reduce(BigDecimal.ZERO, BigDecimal::add);

        var riskPaidInstallmentCommissions = qp.getQuotationRisks().stream().map(QuotationRisk::getPaidInstallmentComm).reduce(BigDecimal.ZERO, BigDecimal::add);

        var riskCommInstallmentAmount = qp.getQuotationRisks().stream().map(QuotationRisk::getCommInstallmentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        var riskCommInstallmentPremium = qp.getQuotationRisks().stream().map(QuotationRisk::getCommInstallmentPremium).reduce(BigDecimal.ZERO, BigDecimal::add);

        var riskOutstandingInstallmentAmount = qp.getQuotationRisks().stream().map(QuotationRisk::getOutstandingInstallmentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        var riskOutstandingCommission = qp.getQuotationRisks().stream().map(QuotationRisk::getOutstandingCommission).reduce(BigDecimal.ZERO, BigDecimal::add);

        var riskWithHoldingTax = qp.getQuotationRisks().stream().map(QuotationRisk::getWithHoldingTax).reduce(BigDecimal.ZERO, BigDecimal::add);

        var staticTax = BigDecimal.ZERO;

        var riskTaxes = qp.getQuotationRisks().stream().map(QuotationRisk::getQuotationRiskTaxes).flatMap(quotationRiskTaxes -> quotationRiskTaxes.stream().map(QuotationRiskTax::getTransactionTypeCode)).collect(Collectors.toList());

        staticTax = qp.getQuotationProductTaxes().stream().filter(quotationProductTax -> !riskTaxes.contains(quotationProductTax.getTransactionTypeCode())).map(QuotationProductTax::getTaxAmount).reduce(BigDecimal.ZERO, BigDecimal::add);


        qp.setTotalSumInsured(riskSumInsured);
        qp.setInstallmentPremium(riskInstallmentPremium);
        qp.setInstallmentAmount(riskInstallmentAmount);
        qp.setCommissionAmount(riskCommissions);
        qp.setBasicPremium(riskBasicPremium);
        qp.setTotalPremium(riskTotalPremium.add(staticTax));
        qp.setFutureAnnualPremium(riskFutureAnnualPremium);
        qp.setPaidInstallmentComm(riskPaidInstallmentCommissions);
        qp.setCommInstallmentAmount(riskCommInstallmentAmount);
        qp.setCommInstallmentPremium(riskCommInstallmentPremium);
        qp.setOutstandingInstallmentAmount(riskOutstandingInstallmentAmount);
        qp.setOutstandingCommission(riskOutstandingCommission);
        qp.setWithHoldingTax(riskWithHoldingTax);
    }

    public ScheduleDetailsDto findScheduleDetails(String propertyId) {
        var scheduleDetails = new ScheduleDetailsDto();
        Optional<QuotationRisk> quotationRisk = quotationRiskService.findRisksByRegistrationNo(propertyId).stream().findAny();
        if (quotationRisk.isPresent()) {
            Optional<QuotationProduct> quotationProduct = quotationProductService.find(quotationRisk.get().getQuotationProductId());
            if (quotationProduct.isPresent()) {
                Optional<Quotation> quotation = this.findById(quotationProduct.get().getQuotationId());
                if (quotation.isPresent()) {
                    scheduleDetails.setPolicyNo(quotation.get().getPolicyNo() == null ? quotation.get().getQuotationNo() : quotation.get().getPolicyNo());
                    scheduleDetails.setInsurerOrgId(quotation.get().getInsurerOrgId());
                    var client = quotation.get().getClient();
                    var quoteRisk = quotation.get().getQuotationProducts().stream().flatMap(quoteProduct -> quoteProduct.getQuotationRisks().stream()).filter(risk -> risk.getRiskId().equals(propertyId)).findFirst().orElse(null);
                    setScheduleDetails(scheduleDetails, client, quoteRisk);
                    assert quoteRisk != null;
                    setMotorScheduleDetails(scheduleDetails, quoteRisk.getMotorSchedules());
                    return scheduleDetails;
                }
            }
        }
        return scheduleDetails;
    }

    private static void setScheduleDetails(ScheduleDetailsDto scheduleDetails, ClientDto client, QuotationRisk quoteRisk) {
        try {
            if (client != null) {
                scheduleDetails.setClientName(client.getFirstName() + " " + client.getLastName());
                scheduleDetails.setClientEmail(client.getEmailAddress());
                scheduleDetails.setClientPIN(client.getKraPin());
                scheduleDetails.setClientPhoneNumber(client.getPhoneNumber());
            }
            if (quoteRisk != null) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                scheduleDetails.setCoverTypeId(quoteRisk.getCoverTypeId());
                scheduleDetails.setVehicleRegistrationNumber(quoteRisk.getRiskId());
                scheduleDetails.setCommencingDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(quoteRisk.getWithEffectFromDate()), TimeZone.getTimeZone("Africa/Nairobi").toZoneId()).format(dtf));
                scheduleDetails.setExpiryDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(quoteRisk.getWithEffectToDate()), TimeZone.getTimeZone("Africa/Nairobi").toZoneId()).format(dtf));
                scheduleDetails.setSumInsured(quoteRisk.getValue());
                scheduleDetails.setCoverTypeCode(quoteRisk.getCoverTypeCode());
            }
        } catch (Exception e) {
            // Log the exception here
            log.error("An error occurred: {}", e.getMessage());
        }
    }

    private static void setMotorScheduleDetails(ScheduleDetailsDto scheduleDetails, MotorSchedules schedule) {
        try {
            scheduleDetails.setVehicleMake(schedule.getMake());
            scheduleDetails.setVehicleModel(schedule.getModel());
            scheduleDetails.setBodyType(schedule.getBodyType());
            scheduleDetails.setChassisNumber(schedule.getChasisNo());
            scheduleDetails.setEngineNumber(schedule.getEngineNo());
            scheduleDetails.setVehicleManufactureYear(schedule.getYearOfManufacture());
            scheduleDetails.setVehicleRegistrationYear(schedule.getYearOfRegistration());
        } catch (Exception e) {
            log.error("Unexpected exception encountered in setMotorScheduleDetails: {}", e.getMessage() );
        }
    }

    @Override
    public List<ComputationRequest> createComparisonRequestObject(Long quotationId) {
        Quotation quotation = repository.findById(quotationId).orElseThrow(() -> new ResourceNotFoundException(Quotation.class, "id", quotationId));
        List<ComputationRequest> computationRequests = new ArrayList<>();

        for (QuotationProduct qp : quotation.getQuotationProducts()) {
            ComputationRequest quoteRequest = createComputationRequest(qp, quotation);
            computationRequests.add(quoteRequest);
        }

        return computationRequests;
    }

    private ComputationRequest createComputationRequest(QuotationProduct qp, Quotation quotation) {
        ComputationRequest quoteRequest = new ComputationRequest();
        quoteRequest.setQuickQuotationRiskSections(new ArrayList<>());
        quoteRequest.setInitialSections(new ArrayList<>());
        quoteRequest.setProductGroupId(qp.getProductGroupId());

        for (QuotationRisk qr : qp.getQuotationRisks()) {
            quoteRequest.setCoverTypeId(qr.getCoverTypeId());
            quoteRequest.setCoverFromDate(qr.getWithEffectFromDate());
            quoteRequest.setCoverToDate(qr.getWithEffectToDate());
            quoteRequest.setCurrencyId(quotation.getCurrencyId());
            quoteRequest.setRiskId(qr.getRiskId());
            quoteRequest.setOrganizationId(quotation.getOrganizationId());
            quoteRequest.setInsurerOrgId(quotation.getInsurerOrgId());
            quoteRequest.setYearOfManufacture(qr.getMotorSchedules().getYearOfManufacture());

            List<Long> sectionIds = qr.getQuotationRiskSections().stream().map(QuotationRiskSection::getSectionId).toList();
            FilterCoverTypeSectionsDto filterCoverTypeSectionsDto = new FilterCoverTypeSectionsDto(List.of(qr.getCoverTypeId()), sectionIds);
            List<CoverTypeSection> coverTypeSections = getCoverTypeSections(filterCoverTypeSectionsDto);

            for (QuotationRiskSection qrs : qr.getQuotationRiskSections()) {
                QuotationRiskSectionDto quotationRiskSection = new QuotationRiskSectionDto();
                quotationRiskSection.setSubClassSectionId(qrs.getSubClassSectionId());
                quotationRiskSection.setLimitAmount(qrs.getLimitAmount());
                quotationRiskSection.setPremiumRate(qrs.getPremiumRate());
                quotationRiskSection.setBenefitType(qrs.getBenefitType());
                quoteRequest.getQuickQuotationRiskSections().add(quotationRiskSection);

                CoverTypeSection coverTypeSection = coverTypeSections.stream().filter(section -> section.getSectionId().equals(qrs.getSectionId())).findAny().orElse(null);

                if (coverTypeSection != null) {
                    CoverTypeSection initialSection = new CoverTypeSection();
                    initialSection.setBenefitType(qrs.getBenefitType());
                    initialSection.setLimitAmount(qrs.getLimitAmount());
                    initialSection.setCoverTypeSectionId(coverTypeSection.getId());
                    quoteRequest.getInitialSections().add(initialSection);
                }
            }
        }

        return quoteRequest;
    }


    @Override
    public void updateQuotation(UpdateQuotationDto updateQuotationDto) {
        var quotation = repository.findById(updateQuotationDto.getQuotationId());
        if (quotation.isPresent()) {
            var quote = quotation.get();
            quote.setCoverFromDate(updateQuotationDto.getCoverFromDate());
            quote.setCoverToDate(updateQuotationDto.getCoverToDate());
            quote.setTotalSumInsured(updateQuotationDto.getSumInsured());
            repository.save(quote);
        }
    }

    private List<CoverTypeSection> getCoverTypeSections(FilterCoverTypeSectionsDto filterCoverTypeSectionsDto) {
        return coverTypeSectionClient.filterByCoverTypesAndSections(filterCoverTypeSectionsDto);
    }
}
