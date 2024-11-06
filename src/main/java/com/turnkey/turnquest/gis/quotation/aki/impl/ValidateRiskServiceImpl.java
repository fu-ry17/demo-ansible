package com.turnkey.turnquest.gis.quotation.aki.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turnkey.turnquest.gis.quotation.aki.ValidateRiskService;
import com.turnkey.turnquest.gis.quotation.aki.client.DMVICService;
import com.turnkey.turnquest.gis.quotation.aki.dto.CertificateRequestDto;
import com.turnkey.turnquest.gis.quotation.aki.dto.CertificateResponseDto;
import com.turnkey.turnquest.gis.quotation.aki.dto.ErrorDto;
import com.turnkey.turnquest.gis.quotation.aki.dto.ValidationResponseDto;
import com.turnkey.turnquest.gis.quotation.aki.enums.CertificateType;
import com.turnkey.turnquest.gis.quotation.aki.enums.CoverTypeCode;
import com.turnkey.turnquest.gis.quotation.aki.error.AKIValidationException;
import com.turnkey.turnquest.gis.quotation.client.crm.AgencyClient;
import com.turnkey.turnquest.gis.quotation.client.crm.ClientDataClient;
import com.turnkey.turnquest.gis.quotation.client.gis.CoverTypeClient;
import com.turnkey.turnquest.gis.quotation.client.gis.ProductInstallmentClient;
import com.turnkey.turnquest.gis.quotation.client.gis.SubClassCoverTypeClient;
import com.turnkey.turnquest.gis.quotation.dto.crm.AgencyDto;
import com.turnkey.turnquest.gis.quotation.dto.crm.ClientDto;
import com.turnkey.turnquest.gis.quotation.dto.crm.EntitiesDto;
import com.turnkey.turnquest.gis.quotation.dto.gis.CoverTypeDto;
import com.turnkey.turnquest.gis.quotation.dto.notification.PushNotificationDto;
import com.turnkey.turnquest.gis.quotation.enums.ClientTypes;
import com.turnkey.turnquest.gis.quotation.enums.InstallmentCalculation;
import com.turnkey.turnquest.gis.quotation.enums.ValuationStatus;
import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import com.turnkey.turnquest.gis.quotation.event.producer.NotificationProducer;
import com.turnkey.turnquest.gis.quotation.model.MotorSchedules;
import com.turnkey.turnquest.gis.quotation.model.Quotation;
import com.turnkey.turnquest.gis.quotation.model.QuotationProduct;
import com.turnkey.turnquest.gis.quotation.model.QuotationRisk;
import com.turnkey.turnquest.gis.quotation.repository.QuotationProductRepository;
import com.turnkey.turnquest.gis.quotation.repository.QuotationRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service("validateRiskService")
public class ValidateRiskServiceImpl implements ValidateRiskService {

    private final QuotationRepository quotationRepository;

    private final NotificationProducer notificationProducer;

    private final QuotationProductRepository quotationProductRepository;

    private final AgencyClient agencyClient;

    private final ClientDataClient clientDataClient;

    private final CoverTypeClient coverTypeClient;

    private final ProductInstallmentClient productInstallmentClient;

    private final SubClassCoverTypeClient subClassCoverTypeClient;

    private final DMVICService dmvicService;

    private final Logger logger = LoggerFactory.getLogger(ValidateRiskServiceImpl.class);

    public ValidateRiskServiceImpl(QuotationRepository quotationRepository, NotificationProducer notificationProducer, QuotationProductRepository quotationProductRepository,
                                   AgencyClient agencyClient,
                                   ClientDataClient clientDataClient, CoverTypeClient coverTypeClient,
                                   ProductInstallmentClient productInstallmentClient, SubClassCoverTypeClient subClassCoverTypeClient, DMVICService dmvicService) {
        this.quotationRepository = quotationRepository;
        this.notificationProducer = notificationProducer;
        this.quotationProductRepository = quotationProductRepository;
        this.agencyClient = agencyClient;
        this.clientDataClient = clientDataClient;
        this.coverTypeClient = coverTypeClient;
        this.productInstallmentClient = productInstallmentClient;
        this.subClassCoverTypeClient = subClassCoverTypeClient;
        this.dmvicService = dmvicService;
    }

    @SneakyThrows
    @Override
    public ValidationResponseDto validateRisk(Long quotationId) {

        AtomicReference<Long> invalidRisks = new AtomicReference<>(0L);
        AtomicReference<Long> successResponse = new AtomicReference<>(0L);
        List<ErrorDto> allErrors = new ArrayList<>();

        var validationResponse = new ValidationResponseDto();

        var quotation = quotationRepository.findById(quotationId);


        if (quotation.isPresent()) {
            ClientDto clientDto = getClient(quotation.get().getClientId());
            EntitiesDto entitiesDto = getEntity(quotation.get().getOrganizationId(), quotation.get().getPanelId());

            quotation.get().getQuotationProducts()
                    .stream()
                    .flatMap(quotationProduct -> quotationProduct.getQuotationRisks().stream())
                    .forEach(quotationRisk -> {
                        var request =
                                buildCertificateRequestPayload(quotationRisk, entitiesDto, clientDto,
                                        quotationProductRepository.findById(quotationRisk.getQuotationProductId())
                                                .orElseThrow(() -> new AKIValidationException("Quotation product not found")),
                                        quotation.get());

                        var errors = validateRequestPayload(request, getCertificateType(request.getType()));

                        if (!errors.isEmpty()) {
                            invalidRisks.updateAndGet(v -> v + 1);
                            allErrors.addAll(errors);
                        }

                        var response = getVerificationResponse(request);
                        try {
                            logger.info("Response: {}", new ObjectMapper().writeValueAsString(response));
                        } catch (JsonProcessingException e) {
                            logger.error(e.getMessage());
                        }

                        if (response != null) {
                            try {
                                logger.info("Response: {}", new ObjectMapper().writeValueAsString(response));
                            } catch (JsonProcessingException e) {
                                logger.error(e.getMessage());
                            }
                            var errorResponse = response.getErrors().stream()
                                    .filter(errorDto -> !Objects.equals(errorDto.getErrorCode(), "ER007"))
                                    .collect(Collectors.toSet());

                            var warningResponse = response.getErrors().stream()
                                    .filter(errorDto -> !Objects.equals(errorDto.getErrorCode(), "ER007"))
                                    .collect(Collectors.toSet());

                            sendWarningNotifications(quotationId, warningResponse, quotation);


                            allErrors.addAll(errorResponse);
                        }
                    });

            if (invalidRisks.get() > 0L) {
                validationResponse.setSuccess(false);
                validationResponse.setErrors(allErrors);
            }


            if (!allErrors.isEmpty()) {
                validationResponse.setSuccess(false);
                validationResponse.setErrors(allErrors);
            } else {
                validationResponse.setSuccess(true);
            }

        }

        logger.info("Validation response: {}", new ObjectMapper().writeValueAsString(validationResponse));

        return validationResponse;
    }

    private void sendWarningNotifications(Long quotationId, Set<ErrorDto> warningResponse, Optional<Quotation> quotation) {
        var errorTexts = warningResponse.stream()
                .map(ErrorDto::getErrorText)
                .toList();
        AtomicReference<String> errorNotifications = new AtomicReference<>("");
        errorTexts.forEach(errorNotification ->
                errorNotifications.set(errorNotifications.get().concat(",")
                        .concat(errorNotification)));
        sendAkiErrorNotification(quotationId, errorNotifications.get(), quotation.get().getInsurerOrgId());
    }


    private void sendAkiErrorNotification(Long quotationId, String errorNotifications, Long orgId) {

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("[ERROR_TEXT]", errorNotifications);
        PushNotificationDto pushNotificationDto = new PushNotificationDto();
        pushNotificationDto.setId(String.valueOf(quotationId));
        pushNotificationDto.setOrganizationId(orgId);
        pushNotificationDto.setTemplateCode("AKI_WARNING");
        pushNotificationDto.setAttributes(attributes);

        notificationProducer.queuePushNotification(pushNotificationDto);

    }

    //TODO:add function to push notifications

    private CertificateResponseDto getVerificationResponse(CertificateRequestDto certificateRequestDto) {
        return dmvicService.validateRisk(certificateRequestDto);
    }

    private CertificateRequestDto buildCertificateRequestPayload(QuotationRisk quotationRisk,
                                                                 EntitiesDto entitiesDto, ClientDto clientDto,
                                                                 QuotationProduct quotationProduct,
                                                                 Quotation quotation) {

        MotorSchedules motorSchedules = quotationRisk.getMotorSchedules();

        if (motorSchedules == null) {
            throw new AKIValidationException("Schedules is null");
        }

        String requiredCertificate = motorSchedules.getCertificateType();

        CertificateType certificateType = Stream.of(CertificateType.values())
                .filter(cert -> cert.getName().equals(requiredCertificate))
                .findFirst().orElse(null);

        if (certificateType == null) {
            throw new AKIValidationException("Certificate type is null");
        }

        CoverTypeCode coverTypeCode = null;
        Optional<CoverTypeDto> coverTypeDto = Optional.ofNullable(coverTypeClient.findById(quotationRisk.getCoverTypeId()));
        if (coverTypeDto.isPresent()) {
            coverTypeCode = CoverTypeCode.valueOf(coverTypeDto.get().getCode().toUpperCase());
            logger.info("Cover Type Description {}", coverTypeCode.getCode());
        }


        boolean isInstallmentsAllowed = false;
        boolean isLastInstallment = false;

        if (quotationProduct.getInstallmentAllowed() != null) {
            isInstallmentsAllowed = quotationProduct.getInstallmentAllowed().getLiteral().equals("Y");
        }

        if (isInstallmentsAllowed) {
            isLastInstallment = isLastInstallment(quotationProduct, quotationProduct.getNextInstallmentNo() - 1);
        }

        //If we are in the last installment and valuation is still open do not issue a certificate
        if (isLastInstallment && riskRequiresValuation(quotationRisk) &&
                quotationRisk.getValuationStatus() == ValuationStatus.OPEN) {
            throw new AKIValidationException("Cannot issue certificate on last installment while valuation is open");
        }


        Map<String, Long> dates = getCertificateDates(isInstallmentsAllowed, quotationRisk);

        //Build the request object
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String wefDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(dates.get("wef")), ZoneId.of("Africa/Nairobi")).format(df);
        String wetDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(dates.get("wet")), ZoneId.of("Africa/Nairobi")).format(df);

        //Client Name
        String clientName;

        if (clientDto != null) {
            if (clientDto.getClientType() == ClientTypes.INDIVIDUAL) {
                String firstName = clientDto.getFirstName() == null ? "" : clientDto.getFirstName();
                String lastName = clientDto.getLastName() == null ? "" : clientDto.getLastName();
                clientName = firstName + " " + lastName;
            } else if (clientDto.getClientType() == ClientTypes.CORPORATE) {
                clientName = clientDto.getCompanyName() == null ? "" : clientDto.getCompanyName();
            } else {
                clientName = "";
            }
        } else {
            clientName = "";
        }

        BigDecimal tonnage = motorSchedules.getTonnage() == null ? BigDecimal.ZERO : new BigDecimal(motorSchedules.getTonnage());
        CertificateRequestDto request = CertificateRequestDto.builder()
                .memberCompanyId(entitiesDto.getAkiMemberCompanyId())
                .registrationNumber(quotationRisk.getRiskId())
                .bodyType(motorSchedules.getBodyType() == null ?
                        RandomStringUtils.randomAlphanumeric(12).toUpperCase() : motorSchedules.getBodyType())
                .chassisNumber(motorSchedules.getChasisNo())
                .commencingDate(wefDate)
                .email(clientDto != null ? clientDto.getEmailAddress() : null)
                .engineNumber(motorSchedules.getEngineNo())
                .expiringDate(wetDate)
                .hudumaNumber(null)
                .insuredPin(clientDto != null ? clientDto.getKraPin() : null)
                .licensedToCarry(null)
                .phoneNumber(clientDto != null ? clientDto.getPhoneNumber() : null)
                .policyHolder(WordUtils.capitalizeFully(clientName))
                .policyNumber(quotation.getQuotationNo())
                .sumInsured(quotationProduct.getTotalSumInsured())
                .tonnageCarryingCapacity(tonnage.intValue())
                .type(motorSchedules.getCertificateType())
                .typeOfCertificate(getCertType(certificateType))
                .typeOfCover(String.valueOf(coverTypeCode.getCode()))
                .vehicleMake(motorSchedules.getMake())
                .vehicleModel(motorSchedules.getModel())
                .vehicleType(null)
                .yearOfManufacture(motorSchedules.getYearOfManufacture() == null ? null : Integer.parseInt(motorSchedules.getYearOfManufacture().toString()))
                .yearOfRegistration(motorSchedules.getYearOfRegistration() == null ? null : Integer.parseInt(motorSchedules.getYearOfRegistration().toString()))
                .policyBatchNo(-1L)
                .quotationId(quotation.getId())
                .insurerOrganizationId(quotation.getInsurerOrgId())
                .build();

        String phoneNo = request.getPhoneNumber();

        if (phoneNo.length() != 9)
            phoneNo = phoneNo.substring(phoneNo.length() - 9);
        request.setPhoneNumber(phoneNo);

        return request;

    }


    private CertificateType getCertificateType(String certType) {
        return Stream.of(CertificateType.values())
                .filter(cert -> cert.getName().equals(certType))
                .findFirst().orElse(null);
    }

    private ClientDto getClient(Long clientId) {
        return clientDataClient.findById(clientId);
    }

    private EntitiesDto getEntity(Long organizationId, Long panelId) {
        AgencyDto agencyDto = agencyClient.findByPanelIdAndOrganizationId(panelId, organizationId);

        if (agencyDto == null) {
            throw new NullPointerException("Agency Dto is null");
        }
        return agencyDto.getOrganization().getEntities();
    }

    private boolean isLastInstallment(QuotationProduct quotationProduct, Long installmentNo) {
        AtomicInteger installment = new AtomicInteger(0);
        if (quotationProduct != null) {
            //Get the product installment
            Optional.ofNullable(productInstallmentClient.getInstallmentsById(quotationProduct.getProductInstallmentId()))
                    .ifPresent(productInstallmentDto -> {
                        if (productInstallmentDto.getCalculation().equals(InstallmentCalculation.RAT)) {
                            installment.set(productInstallmentDto.getCertificateDistribution().split(":").length);
                        }
                    });
        }

        return installment.get() == installmentNo;
    }


    private String getCertType(CertificateType certificateType) {
        switch (certificateType) {
            case TYPE_A:
                return "1";
            case TYPE_B:
                return "6";
            case TYPE_C:
                return "7";
            case TYPE_D:
                return "8";
            default:
                return "";
        }
    }

    private Map<String, Long> getCertificateDates(boolean isInstallmentAllowed,
                                                  QuotationRisk quotationRisk) {
        Map<String, Long> dates = new HashMap<>();
        Long wet;
        Long wef;

        ValuationStatus valuationStatus = quotationRisk.getValuationStatus();

        logger.info("Installments allowed: {}", isInstallmentAllowed);

        logger.info("Valuation status: {}", valuationStatus);

        wef = quotationRisk.getWithEffectFromDate();
        wet = quotationRisk.getWithEffectToDate();

        dates.put("wef", wef);
        dates.put("wet", wet);

        try {
            logger.info("Current date values: {}", new ObjectMapper().writeValueAsString(dates));
        } catch (JsonProcessingException ex) {
            logger.error(ex.getMessage());
        }

        return dates;
    }

    private Long getEndDate(Long withEffectFromDate, int monthsFromStartDate) {
        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(withEffectFromDate);

        if (monthsFromStartDate <= 12 && monthsFromStartDate >= 1) {

            startDate.add(Calendar.MONTH, monthsFromStartDate);
            startDate.add(Calendar.DAY_OF_MONTH, -1);

            return startDate.getTimeInMillis();
        }

        startDate.add(Calendar.MONTH, 12);
        startDate.add(Calendar.DAY_OF_MONTH, -1);
        return startDate.getTimeInMillis();
    }

    private Long offSetDateByMonths(Long dateValue, int months) {
        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(dateValue);

        startDate.add(Calendar.MONTH, months);
        return startDate.getTimeInMillis();
    }

    private boolean riskRequiresValuation(QuotationRisk quoteRisk) {
        return Optional.ofNullable(subClassCoverTypeClient.requiresValuation(quoteRisk.getProductSubClassId(),
                        quoteRisk.getCoverTypeId()))
                .map(YesNo.Y::equals)
                .orElse(false);
    }

    private List<ErrorDto> validateRequestPayload(CertificateRequestDto certificateRequestDto,
                                                  CertificateType certificateType) {

        List<ErrorDto> errors = new ArrayList<>();

        String agErrorCode = "AG-500";
        if (certificateRequestDto.getInsuredPin() == null) {
            errors.add(new ErrorDto("Missing PIN for the insured.", agErrorCode));
        }
        if (certificateRequestDto.getSumInsured() == null) {
            errors.add(new ErrorDto("Missing Sum Insured Value", agErrorCode));
        }
        if (certificateRequestDto.getRegistrationNumber() == null) {
            errors.add(new ErrorDto("Missing Year Of Registration", agErrorCode));
        }

        int code = Integer.parseInt(certificateRequestDto.getTypeOfCover());
        CoverTypeCode coverTypeCode = Arrays.stream(CoverTypeCode.values())
                .filter(coverType -> coverType.getCode() == code)
                .findFirst()
                .orElse(null);

        if (coverTypeCode != null) {
            if (coverTypeCode == CoverTypeCode.COMP && (certificateRequestDto.getYearOfManufacture() == null
                    || certificateRequestDto.getYearOfManufacture() < 1900)) {
                errors.add(new ErrorDto("Invalid Year Of Manufacture", agErrorCode));
            }
        } else {
            errors.add(new ErrorDto("Error on cover type code", agErrorCode));
        }

        if ((certificateType.equals(CertificateType.TYPE_D) || certificateType.equals(CertificateType.TYPE_A)) && certificateRequestDto.getTypeOfCertificate() == null) {
            errors.add(new ErrorDto("Type of Certificate is required for " + certificateType.getName() + " cover.", agErrorCode));
        }

        if (certificateType.equals(CertificateType.TYPE_A) && certificateRequestDto.getLicensedToCarry() == null) {
            errors.add(new ErrorDto("Carrying capacity is required for " + certificateType.getName() + " cover.", agErrorCode));
        }
        if (certificateType.equals(CertificateType.TYPE_B) && certificateRequestDto.getVehicleType() == null) {
            errors.add(new ErrorDto("Vehicle Type is required for " + certificateType.getName() + " cover.", agErrorCode));
        }

        if (certificateType.equals(CertificateType.TYPE_B) && certificateRequestDto.getTonnageCarryingCapacity() == null) {
            errors.add(new ErrorDto("Tonnage Carrying Capacity is required for " + certificateType.getName() + " cover.", agErrorCode));
        }

        if (certificateType.equals(CertificateType.TYPE_B)
                && (certificateRequestDto.getRegistrationNumber() == null && certificateRequestDto.getChassisNumber() == null)) {
            errors.add(new ErrorDto("Provide either Chassis number or Registration number for " + certificateType.getName() + " cover.", agErrorCode));
        }

        if (certificateRequestDto.getPhoneNumber() == null) {
            errors.add(new ErrorDto("Client phone number is required", agErrorCode));
        }

        return errors;
    }
}
