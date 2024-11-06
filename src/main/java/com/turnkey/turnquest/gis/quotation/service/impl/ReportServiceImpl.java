package com.turnkey.turnquest.gis.quotation.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.turnkey.turnquest.gis.quotation.client.DocsService.DocsServiceClient;
import com.turnkey.turnquest.gis.quotation.client.billing.ReceiptClient;
import com.turnkey.turnquest.gis.quotation.client.crm.*;
import com.turnkey.turnquest.gis.quotation.client.gis.*;
import com.turnkey.turnquest.gis.quotation.client.notification.NotificationClient;
import com.turnkey.turnquest.gis.quotation.client.underwriting.PolicyClientMock;
import com.turnkey.turnquest.gis.quotation.dto.Reports.*;
import com.turnkey.turnquest.gis.quotation.dto.crm.*;
import com.turnkey.turnquest.gis.quotation.dto.document.AttachmentDto;
import com.turnkey.turnquest.gis.quotation.dto.document.S3Object;
import com.turnkey.turnquest.gis.quotation.dto.gis.CoverTypeDto;
import com.turnkey.turnquest.gis.quotation.dto.gis.ProductDocumentDto;
import com.turnkey.turnquest.gis.quotation.dto.gis.ProductDto;
import com.turnkey.turnquest.gis.quotation.dto.gis.TransactionTypeDto;
import com.turnkey.turnquest.gis.quotation.dto.notification.NotificationFeedBack;
import com.turnkey.turnquest.gis.quotation.dto.notification.NotificationRecipientDto;
import com.turnkey.turnquest.gis.quotation.dto.notification.SendNotificationDto;
import com.turnkey.turnquest.gis.quotation.dto.quotation.ResendReportRequestDto;
import com.turnkey.turnquest.gis.quotation.dto.quotation.ResendReportResponseDto;
import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyDto;
import com.turnkey.turnquest.gis.quotation.enums.ClientTypes;
import com.turnkey.turnquest.gis.quotation.enums.QuotationReportType;
import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import com.turnkey.turnquest.gis.quotation.exception.error.DocumentException;
import com.turnkey.turnquest.gis.quotation.model.Quotation;
import com.turnkey.turnquest.gis.quotation.model.QuotationReports;
import com.turnkey.turnquest.gis.quotation.projections.QuotationProjection;
import com.turnkey.turnquest.gis.quotation.repository.QuotationReportsRepository;
import com.turnkey.turnquest.gis.quotation.repository.QuotationRepository;
import com.turnkey.turnquest.gis.quotation.service.ReportService;
import lombok.SneakyThrows;
import org.apache.commons.lang.WordUtils;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("reportService")
public class ReportServiceImpl implements ReportService {

    private final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    private static final String BASE_URL = "http://localhost:";

    @Value("${server.port}")
    public String servicePort = "";

    private final TemplateEngine templateEngine;
    private final QuotationRepository quotationRepository;
    private final ModelMapper modelMapper;
    private final ProductClient productClient;
    private final BussTransactionClient bussTransactionClient;
    private final CoverTypeClient coverTypeClient;
    private final QuotationReportsRepository quotationReportsRepository;
    private final DocsServiceClient docsServiceClient;
    private final OrganizationClient organizationClient;
    private final EntitiesClient entitiesClient;
    private final NotificationClient notificationClient;
    private final ClientDataClient clientDataClient;
    private final AgencyClient agencyClient;
    private final ServiceProviderNotesClient serviceProviderNotesClient;
    private final ProductDocumentClient productDocumentClient;
    private final PolicyClientMock policyClient;
    private final InsurerCurrencyClient currencyClient;
    private final ReceiptClient receiptClient;
    private final SubClassCoverTypeClient subClassCoverTypeClient;
    private final TemplateContent templateContent;
    private final Converter converter;

    public ReportServiceImpl(TemplateEngine templateEngine, QuotationRepository quotationRepository, ModelMapper modelMapper,
                             ProductClient productClient, BussTransactionClient bussTransactionClient,
                             CoverTypeClient coverTypeClient, QuotationReportsRepository quotationReportsRepository,
                             DocsServiceClient docsServiceClient, OrganizationClient organizationClient,
                             EntitiesClient entitiesClient, NotificationClient notificationClient,
                             ClientDataClient clientDataClient, AgencyClient agencyClient, ServiceProviderNotesClient serviceProviderNotesClient,
                             ProductDocumentClient productDocumentClient, InsurerCurrencyClient currencyClient, PolicyClientMock policyClient,
                             ReceiptClient receiptClient, SubClassCoverTypeClient subClassCoverTypeClient,
                             TemplateContent templateContent,Converter converter) {
        this.templateContent = templateContent;
        this.converter = converter;
        this.templateEngine = templateEngine;
        this.quotationRepository = quotationRepository;
        this.modelMapper = modelMapper;
        this.productClient = productClient;
        this.bussTransactionClient = bussTransactionClient;
        this.coverTypeClient = coverTypeClient;
        this.quotationReportsRepository = quotationReportsRepository;
        this.docsServiceClient = docsServiceClient;
        this.organizationClient = organizationClient;
        this.entitiesClient = entitiesClient;
        this.notificationClient = notificationClient;
        this.clientDataClient = clientDataClient;
        this.agencyClient = agencyClient;
        this.serviceProviderNotesClient = serviceProviderNotesClient;
        this.productDocumentClient = productDocumentClient;
        this.currencyClient = currencyClient;
        this.policyClient = policyClient;
        this.receiptClient = receiptClient;
        this.subClassCoverTypeClient = subClassCoverTypeClient;
    }

    /**
     * Generates the quotation summary report
     *
     * @param quotationReportDto quotation report object
     * @return true if succeeded false  otherwise
     */

    @Override
    @Transactional
    public byte[] generateQuoteSummaryReport(QuotationReportDto quotationReportDto) {

        Optional<Quotation> quotation = quotationRepository.findById(quotationReportDto.getQuotationId());

        //Prevents generating the quote summary more than once
        List<QuotationReports> availableReports = quotationReportsRepository.findByQuotationIdAndFileCategory(quotationReportDto.getQuotationId(), QuotationReportType.SUMMARY);
        if (availableReports != null && !availableReports.isEmpty()) {
            return new byte[0];
        }

        if (quotation.isPresent()) {

            QuotationProjection quotationProjection = quotationRepository.getQuoteById(quotation.get().getId(), QuotationProjection.class);

            Quote quote = new Quote();
            modelMapper.getConfiguration().setAmbiguityIgnored(true);
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            modelMapper.map(quotationProjection, quote);

            String quotationHtml = templateContent.getTemplateContent(buildQuotationSummary(quote));
            ByteArrayOutputStream byteArrayOutputStream = converter.convert(quotationHtml);

            logger.info("Uploading quotation summary document...");

            //Upload the file
            String fileName = String.format("QuoteSummary_%s.pdf", quotation.get().getQuotationNo().replace("/", ""));
            List<S3Object> savedDocument = uploadDocument(byteArrayOutputStream.toByteArray(),
                    fileName,
                    getInsurerOrganizationId(quotation.get().getPanelId(), quotation.get().getOrganizationId()),
                    quotation.get().getClientId(),
                    quotation.get().getPolicyNo());

            logger.info("Quotation summary uploaded");

            logger.info("Document url: {}", savedDocument.get(0).getUrl());

            //Save quotationReport
            buildAndSaveReport(quotationReportDto.getQuotationId(), quotationReportDto.getPolicyBatchNo(),
                    Optional.ofNullable(policyClient.findPolicyByBatchNumber(quote.getPolicyId())).map(PolicyDto::getCurrentStatus).orElse(""),
                    quotation.get().getPolicyNo(),
                    quotation.get().getOrganizationId(), quotation.get().getStatus(), savedDocument.get(0).getUrl(), QuotationReportType.SUMMARY, "pdf", byteArrayOutputStream.size(),
                    fileName, false);

            return byteArrayOutputStream.toByteArray();
        }

        return new byte[0];
    }




    /**
     * Generates the valuation report
     *
     * @param quotationReportDto quotation report object
     * @return byte array
     */

    @Override
    @Transactional
    public List<byte[]> generateValuationReport(QuotationReportDto quotationReportDto) {

        //Prevents generating the quote valuation more than once
        List<QuotationReports> availableReports = quotationReportsRepository.findByQuotationIdAndFileCategory(quotationReportDto.getQuotationId(), QuotationReportType.VALUATION);

        if (availableReports != null && !availableReports.isEmpty()) {
            return new ArrayList<>();
        }

        Optional<Quotation> quotation = quotationRepository.findById(quotationReportDto.getQuotationId());
        if (quotation.isPresent()) {

            QuotationProjection quotationProjection = quotationRepository.getQuoteById(quotation.get().getId(), QuotationProjection.class);

            if (!requiresValuation(quotationProjection)) {
                return new ArrayList<>();
            }

            Quote quote = new Quote();

            var totalBytes = new ArrayList<byte[]>();

            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            modelMapper.map(quotationProjection, quote);

            quote.getQuotationProducts().forEach(quoteProduct ->
                        quoteProduct.getQuotationRisks()
                                .stream()
                                .filter(this::riskRequiresValuation)
                                .forEach(quoteRisk -> totalBytes.add(generateValuationReportPerRisk(quote, quoteRisk, quotationReportDto)))
                    );



            return totalBytes;
        }

        return new ArrayList<>();

    }


    /**
     * Generates valuation reports per risk
     * @param quote the quote to which the risk belongs to
     * @param risk the risk against which the valuation report is to be generated
     * @param quotationReportDto the quotation report object
     * @return byte[]
     */
    private byte[] generateValuationReportPerRisk(Quote quote,QuoteRisk risk,QuotationReportDto quotationReportDto){
        Context context = new Context();
        context.setVariable("quote", buildQuotationValuationInfo(quote,risk));

        //Enables locating of css and logo image
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri(BASE_URL + servicePort);

        String quoteValuationHtml = templateEngine.process("quot_valuation.html", context);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        HtmlConverter.convertToPdf(quoteValuationHtml, byteArrayOutputStream, converterProperties);

        logger.info("Uploading policy valuation document...");

        //Upload the file
        String fileName = String.format("PolicyValuation_%s_%s.pdf", quote.getPolicyNo().replace("/", ""),risk.getRiskId().replace(" ",""));
        List<S3Object> savedDocument = uploadDocument(byteArrayOutputStream.toByteArray(),
                fileName,
                getInsurerOrganizationId(quote.getPanelId(),quote.getOrganizationId()),
                quote.getClientId(),
                quote.getPolicyNo());

        logger.info("Valuation document  uploaded");

        logger.info("File url: {}", savedDocument.get(0).getUrl());

        //Save quotationReport
        buildAndSaveReport(quotationReportDto.getQuotationId(), quotationReportDto.getPolicyBatchNo(),
                Optional.ofNullable(policyClient.findPolicyByBatchNumber(quote.getPolicyId())).map(PolicyDto::getCurrentStatus).orElse(""),
                quote.getPolicyNo(),
                quote.getOrganizationId(), quote.getStatus(), savedDocument.get(0).getUrl(), QuotationReportType.VALUATION, "pdf", byteArrayOutputStream.size(),
                fileName, true);

        return byteArrayOutputStream.toByteArray();
    }


    /**
     * Generates the renewal report
     *
     * @param quotationId quotationId
     * @return byte array
     */

    @Override
    @Transactional
    public byte[] generateRenewalNotice(Long quotationId) {

        Optional<Quotation> quotation = quotationRepository.findById(quotationId);

        if (quotation.isPresent() && quotation.get().getStatus().equals("RN")) {
            QuotationProjection quotationProjection = quotationRepository.getQuoteById(quotation.get().getId(), QuotationProjection.class);

            Quote quote = new Quote();

            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            modelMapper.map(quotationProjection, quote);

            Context context = new Context();
            context.setVariable("renewalNotice", buildRenewalNotice(quote));

            //Enables locating of css and logo image
            ConverterProperties converterProperties = new ConverterProperties();
            converterProperties.setBaseUri(BASE_URL + servicePort);

            String quoteValuationHtml = templateEngine.process("renewal_notice.html", context);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            HtmlConverter.convertToPdf(quoteValuationHtml, byteArrayOutputStream, converterProperties);

            logger.info("Uploading renewal notice document...");

            //Upload the file
            String fileName = String.format("RenewalNotice_%s.pdf", quotation.get().getPolicyNo().replace("/", ""));
            List<S3Object> savedDocument = uploadDocument(byteArrayOutputStream.toByteArray(),
                    fileName,
                    getInsurerOrganizationId(quotation.get().getPanelId(), quotation.get().getOrganizationId()),
                    quotation.get().getClientId(),
                    quotation.get().getPolicyNo());

            logger.info("Document uploaded");

            logger.info("File url: {}", savedDocument.get(0).getUrl());

            return byteArrayOutputStream.toByteArray();
        }

        return new byte[0];
    }

    /**
     * Determines if a valuation report is required.
     * This looks for any risk within the quotation that requires valuation be it one or all
     *
     * @param quotationProjection quotation object
     * @return boolean
     */
    private boolean requiresValuation(QuotationProjection quotationProjection) {
        AtomicReference<Long> hasRiskWithValuation = new AtomicReference<>(0L);
        quotationProjection.getQuotationProducts()
                .stream()
                .flatMap(quotationProductView -> quotationProductView.getQuotationRisks().stream())
                .forEach(quotationRiskView ->
                        Optional.ofNullable(subClassCoverTypeClient.requiresValuation(quotationRiskView.getProductSubClassId(),
                                        quotationRiskView.getCoverTypeId()))
                                .ifPresent(response -> {
                                    if (YesNo.Y.equals(response)) {
                                        hasRiskWithValuation.updateAndGet(v -> v + 1L);
                                    }
                                })
                );

        return hasRiskWithValuation.get() > 0L;
    }

    /**
     * Check an individual risk if valuation is allowed
     *
     * @param quoteRisk quotation risk
     * @return boolean
     */
    private boolean riskRequiresValuation(QuoteRisk quoteRisk) {
        return Optional.ofNullable(subClassCoverTypeClient.requiresValuation(quoteRisk.getProductSubClassId(),
                        quoteRisk.getCoverTypeId()))
                .map(YesNo.Y::equals)
                .orElse(false);
    }

    /**
     * Sends the generated quote documents via mail
     *
     * @param quotationId quotationId
     * @return true if operation succeeded false otherwise
     */

    @Override
    @Transactional
    public boolean sendQuotationDocuments(Long quotationId) {

        List<QuotationReports> quotationReports = quotationReportsRepository.findByQuotationId(quotationId)
                .stream()
                .filter(QuotationReports::isMailable)
                .filter(report -> !report.isSent())
                .filter(report -> report.getEmailRetryCount() < report.getMaxRetryCount())
                .toList();

        if (!quotationReports.isEmpty()) {

            logger.info("Available reports for mail {}", quotationReports.size());
            logger.info("Quotation Id {}", quotationId);

            Optional<Quotation> quotation = quotationRepository.findById(quotationId);
            logger.info("Quotation is present {}", quotation.isPresent());

            if (quotation.isPresent()) {

                String clientName = "";
                String clientEmail = "";
                AtomicReference<String> insurerName = new AtomicReference<>("");
                AtomicReference<String> insurerEmail = new AtomicReference<>("");
                AtomicReference<String> insurerPhone = new AtomicReference<>("");

                //Get client names and email
                ClientDto client = getClientDetails(quotation.get().getClientId());

                logger.info("Client is present {}", client);

                if (client != null) {
                    clientName = WordUtils.capitalizeFully(client.getFirstName() + " " + client.getLastName());
                    clientEmail = client.getEmailAddress();
                }

                //Get insurer organization details
                OrganizationDto organizationDto = getInsurerOrganization(quotation.get().getPanelId(),
                        quotation.get().getOrganizationId());

                if (organizationDto != null && organizationDto.getEntities() != null) {
                    insurerName.set(organizationDto.getEntities().getOrganizationName());
                    insurerPhone.set(organizationDto.getEntities().getPhoneNumber());
                    insurerEmail.set(organizationDto.getEntities().getEmailAddress());
                }

                logger.info("Sending mail...");

                //Email attributes
                HashMap<String, String> attributes = new HashMap<>();
                attributes.put("client_name", clientName);
                attributes.put("insurer_name", insurerName.get());
                attributes.put("insurer_phone_number", insurerPhone.get());
                attributes.put("insurer_email", insurerEmail.get());

                String subject = String.format("YOUR POLICY DOCUMENTS FROM %s", insurerName.get());

                NotificationRecipientDto notificationRecipientDto = new NotificationRecipientDto();

                List<String> emailRecipients = new ArrayList<>();
                emailRecipients.add(clientEmail);

                notificationRecipientDto.setEmailAddress(String.join(",", emailRecipients));
                notificationRecipientDto.setPhoneNumber("");
                notificationRecipientDto.setName("");

                SendNotificationDto sendNotificationDto = new SendNotificationDto();
                sendNotificationDto.setChannels("email");
                sendNotificationDto.setMessage("");
                sendNotificationDto.setSubject(subject);
                sendNotificationDto.setOrganizationId(quotation.get().getOrganizationId());
                sendNotificationDto.setHasAttachment(true);
                sendNotificationDto.setTemplateShortCode("REPORT_TEMPLATE");
                sendNotificationDto.setAttributes(attributes);

                List<ProductDocumentDto> insurerDocs = new ArrayList<>();

                //Get insurer documents
                quotation.get().getQuotationProducts().forEach(quotationProduct -> {
                    Optional<List<ProductDocumentDto>> docs = Optional.ofNullable(productDocumentClient.getByProductId(quotationProduct.getProductId()));
                    docs.ifPresent(insurerDocs::addAll);
                });


                //Sending multiple attachments as file urls separated by commas
                List<String> attachments = new ArrayList<>();
                quotationReports.forEach(quotationReport -> attachments.add(quotationReport.getFileUrl()));
                attachments.addAll(insurerDocs.stream()
                        .map(ProductDocumentDto::getFileUrl)
                        .toList());

                List <String> noWhiteSpaceAttachments = attachments.stream()
                        .map(attachment -> attachment.replaceAll("\\s", "_"))
                        .collect(Collectors.toList());

                for (String attachment: noWhiteSpaceAttachments){
                    logger.info("File urls {}", attachment);
                }
                sendNotificationDto.setAttachment(String.join(",", noWhiteSpaceAttachments));

                sendNotificationDto.setNotificationRecipient(notificationRecipientDto);

                //Send to the client
                try {
                    notificationClient.initiate(sendNotificationDto);
                } catch (Exception ex) {
                    quotationReportsRepository.findByQuotationId(quotationId)
                            .forEach(report -> {
                                report.setEmailRetryCount(report.getEmailRetryCount() + 1);
                                report.setError(ex.getMessage());
                                quotationReportsRepository.save(report);
                            });
                }


                //Send the documents to both  insurer
                sendToInsurer(insurerEmail.get(), quotationId, sendNotificationDto);

                logger.info("Mail sending to client and insurer initiated");

                //Due to the @Transactional annotation there is no need to call save
                quotationReportsRepository.findByQuotationId(quotationId)
                        .forEach(report -> report.setSent(true));

                return true;

            }
        }

        logger.info("Quotation reports is empty {}", quotationReports.isEmpty());
        return false;
    }



    @SneakyThrows
    @Override
    public ResendReportResponseDto resendReportsToClient(ResendReportRequestDto request){

        List<QuotationReports> quotationReports = quotationReportsRepository.findByQuotationId(request.getQuotationId());

        SendNotificationDto sendNotificationDto = composeResendNotification(request, quotationReports);

        ResendReportResponseDto response = new ResendReportResponseDto();

        logger.info("Resend Notification Object: {}", new ObjectMapper().writeValueAsString(sendNotificationDto));
        try {
            notificationClient.initiate(sendNotificationDto);
            response.setMessage("Reports resent successfully to "+request.getClientEmail());
        }catch (Exception e){
            response.setMessage("Failed. Check your details and retry");
            logger.error(e.getMessage());
        }

        return response;
    }

    @NotNull
    private SendNotificationDto composeResendNotification(ResendReportRequestDto request, List<QuotationReports> quotationReports) {
        NotificationRecipientDto notificationRecipientDto = new NotificationRecipientDto();
        notificationRecipientDto.setEmailAddress(request.getClientEmail());
        notificationRecipientDto.setPhoneNumber("");
        notificationRecipientDto.setName(request.getClientName());

        OrganizationDto organizationDto = organizationClient.findById(request.getInsurerOrgId());

        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("client_name", request.getClientName());
        attributes.put("insurer_name", organizationDto.getEntities().getOrganizationName());
        attributes.put("insurer_phone_number", organizationDto.getEntities().getPhoneNumber());
        attributes.put("insurer_email", organizationDto.getEntities().getEmailAddress());

        String subject = String.format("YOUR POLICY DOCUMENTS FROM %s", organizationDto.getEntities().getOrganizationName());

        SendNotificationDto sendNotificationDto = new SendNotificationDto();
        sendNotificationDto.setChannels("email");
        sendNotificationDto.setMessage("Resending Email due to: "+ request.getRemarks());
        sendNotificationDto.setSubject(subject);
        sendNotificationDto.setOrganizationId(quotationReports.get(0).getOrganizationId());
        sendNotificationDto.setHasAttachment(true);
        sendNotificationDto.setTemplateShortCode("REPORT_TEMPLATE");
        sendNotificationDto.setAttributes(attributes);

        List<String> attachments = new ArrayList<>();
        quotationReports.forEach(quotationReport -> attachments.add(quotationReport.getFileUrl()));

        List <String> noWhiteSpaceAttachments = attachments.stream()
                .map(attachment -> attachment.replaceAll("\\s", "_"))
                .collect(Collectors.toList());

        sendNotificationDto.setAttachment(String.join(",", noWhiteSpaceAttachments));
        sendNotificationDto.setNotificationRecipient(notificationRecipientDto);
        return sendNotificationDto;
    }


    public NotificationFeedBack sendNotification(SendNotificationDto sendNotificationDto) {
        try {
            notificationClient.initiate(sendNotificationDto);
            return new NotificationFeedBack(true, null);
        } catch (Exception ex) {
            logger.error("Sending mail to client failed. ", ex);
            return new NotificationFeedBack(false, ex);
        }
    }

    /**
     * Gets the report documents of a Converted Quotation (Policy)
     *
     * @param policyBatchNo policy Id
     * @return List of QuotationReports
     */

    @Override
    public List<QuotationReports> getQuotationReport(Long policyBatchNo) {
        logger.info("Fetching quotation reports with policy batch no {}", policyBatchNo);

        List<QuotationReports> reports = new ArrayList<>();
        if (policyBatchNo != null) {

            //Use the quotation no to fetch reports
            Long quotationId = getQuotationIdOnPolicy(policyBatchNo);
            if (quotationId != -1L) {
                //Using this approach while fetching reports in order to get the latest
                //This is majorly due to installments
                reports = Stream.of(QuotationReportType.values())
                        .flatMap(quotationReportType ->
                                quotationReportsRepository.
                                        findByQuotationIdAndFileCategoryOrderByCreatedDateDesc(quotationId, quotationReportType).stream())
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }

            //Second Attempt
            if (reports.isEmpty()) {
                reports = Stream.of(QuotationReportType.values())
                        .flatMap(quotationReportType ->
                                quotationReportsRepository.
                                        findByPolicyIdAndFileCategoryOrderByCreatedDateDesc(policyBatchNo, quotationReportType).stream())
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }

        }
        return reports;
    }

    @Override
    public List<QuotationReports> getPolicyReports(Long policyBatchNo) {
        logger.info("Fetching policy reports with policy batch no {}", policyBatchNo);
        return this.getQuotationReport(policyBatchNo);
    }

    @Override
    public void generateDebitReport(DebitReportDto debitReportDto) {

        //Generate Debit
        try {
            logger.info("Generating debit...");
            receiptClient.generateDebitReport(debitReportDto.getIPayRef(), debitReportDto.getBatchNo(), debitReportDto.getQuotationId());
        } catch (Exception ex) {
            logger.info("Debit generation failed with message {}", ex.getMessage());
        }

    }

    /**
     * Saves a quotation report
     *
     * @param quotationReports quotation report object
     * @return QuotationReport
     */

    @Override
    public QuotationReports saveQuotationReport(QuotationReports quotationReports) {
        return quotationReportsRepository.save(quotationReports);
    }

    /**
     * Checks if a report exists
     *
     * @param quotationId         the quotation Id
     * @param quotationReportType the report type
     * @return BooleanResult Object
     */
    @Override
    public BooleanResult reportExists(Long quotationId, QuotationReportType quotationReportType) {
        List<QuotationReports> availableReports = quotationReportsRepository.findByQuotationIdAndFileCategory(quotationId, quotationReportType);
        if (availableReports != null) {
            return new BooleanResult(!availableReports.isEmpty());
        }
        return new BooleanResult(false);
    }


    /**
     * Sends client document to the associated insurer
     *
     * @param insurerEmail        the insurer email
     * @param quotationId         the quotation Id
     * @param sendNotificationDto the mail payload
     */
    @Transactional
    public void sendToInsurer(String insurerEmail, Long quotationId, SendNotificationDto sendNotificationDto) {
        logger.info("Composing insurer email...");
        if (!insurerEmail.equals("")) {
            Optional<Quotation> quotation = quotationRepository.findById(quotationId);
            if (quotation.isPresent()) {
                sendNotificationDto.getNotificationRecipient().setEmailAddress(insurerEmail);

                List<String> riskDocuments = new ArrayList<>();
                quotation.get().getQuotationProducts().forEach(quotationProduct ->
                        quotationProduct.getQuotationRisks().forEach(risk ->
                                risk.getQuoteDocument().forEach(quoteDocument -> {
                                    logger.info("Adding quote document {}", quoteDocument.getDocument());
                                    riskDocuments.add(quoteDocument.getDocument());
                                })
                        )
                );

                if (!riskDocuments.isEmpty()) {
                    String attachments = sendNotificationDto.getAttachment() + "," + String.join(",", riskDocuments);
                    logger.info("Insurer attachments {}", attachments);
                    sendNotificationDto.setAttachment(attachments);
                }

                sendNotification(sendNotificationDto);
                logger.info("Insurer mail composed");
            }
        } else {
            logger.info("Insurer email cannot be empty");
        }
    }


    /**
     * Builds a quotation valuation object used to generate the valuation report
     *
     * @param quote the quote from which the valuation report is to be generated
     * @param quoteRisk the risk against with the valuation is generated
     * @return update Quote Object
     */
    private Quote buildQuotationValuationInfo(Quote quote,QuoteRisk quoteRisk) {

        //Get client
        quote.setClientName(getClientName(quote.getClientId()));

        //Get Insurer Information
        quote.setOrganization(getInsurerOrganization(quote.getPanelId(), quote.getOrganizationId()));

        //Get Agent Name
        quote.setAgentName(getAgentName(quote.getOrganizationId()));

        quote.setPolicyNo(quote.getPolicyNo().toUpperCase());

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("E, dd MMM yyyy");

        //Set renewal Date
        setRenewalDate(dateFormat, quoteRisk);

        //Set date of issue
        String issueDate = setValuationIssueDate(dateFormat);



        //Set valuation firms
        setValuationFirms(quoteRisk, quote.getOrganization(), issueDate);

        if (quote.getQuotationProducts() != null) {
            quote.getQuotationProducts().forEach(quoteProduct -> quoteProduct.setQuotationRisks(Collections.singletonList(quoteRisk)));
        }

        return quote;

    }

    private void setRenewalDate(DateTimeFormatter dateFormat, QuoteRisk quoteRisk) {
        if (quoteRisk.getWithEffectToDate() != null) {
            LocalDateTime localDateTime = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(quoteRisk.getWithEffectFromDate()),
                    ZoneId.of("Africa/Addis_Ababa")).plusMonths(1);
            quoteRisk.setRenewalDate(localDateTime.format(dateFormat));
        }
    }

    private String setValuationIssueDate(DateTimeFormatter dateTimeFormatter) {
        LocalDate dateOfIssuer = LocalDate.ofInstant(
                Instant.ofEpochMilli(System.currentTimeMillis()),
                ZoneId.of("Africa/Addis_Ababa"));
        return dateOfIssuer.format(dateTimeFormatter);
    }

    /**
     * Builds a quotation summary object used to generate the quotation summary report
     *
     * @param quote the quote from which the quotation summary is to be genrated
     * @return updated Quote object
     */
    private Quote buildQuotationSummary(Quote quote) {

        AtomicReference<BigDecimal> totalTaxAmount = new AtomicReference<>();
        totalTaxAmount.getAndSet(BigDecimal.ZERO);

        //Get client
        quote.setClientName(getClientName(quote.getClientId()));

        //Set the date
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault());

        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        quote.setReportDate("Date " + getDay(calendar.get(Calendar.DAY_OF_MONTH)) + ", " + dateTimeFormatter.format(localDateTime));

        //Make the quotation number upper case
        quote.setQuotationNo(quote.getQuotationNo());

        //Get Insurer Information
        quote.setOrganization(getInsurerOrganization(quote.getPanelId(), quote.getOrganizationId()));

        //Get Currency
        quote.setCurrencyDescription(getCurrencySymbol(quote.getCurrencyId()));


        if (quote.getQuotationProducts() != null) {
            //Get the product description and Tax description
            quote.getQuotationProducts().forEach(quotePrdct -> {

                if (quotePrdct.getInstallmentAllowed() != null && quotePrdct.getInstallmentAllowed() == YesNo.Y) {
                    quotePrdct.setFirstPremium(formatNumberToCurrency(quotePrdct.getInstallmentAmount()));
                    quotePrdct.setNextPremium(formatNumberToCurrency(quotePrdct.getInstallmentPremium()));
                }

                //Set Premium amount and FAP
                quotePrdct.setPremiumAmountString(formatNumberToCurrency(quotePrdct.getBasicPremium()));

                String description = "";
                Optional<ProductDto> product = Optional.ofNullable(productClient.findById(quotePrdct.getProductId()));
                if (product.isPresent()) {
                    description += product.get().getDescription();
                    quotePrdct.setProductDesc(product.get().getDescription());
                }

                if (quotePrdct.getQuotationProductTaxes() != null) {

                    //Remove COPHFUND
                    Predicate<QuotePrdctTax> isCoPhf = quotePrdctTax -> quotePrdctTax.getTransactionTypeCode() != null
                            && quotePrdctTax.getTransactionTypeCode().equals("COPHFUND");
                    quotePrdct.getQuotationProductTaxes().removeIf(isCoPhf);

                    //Filter out null taxes
                    List<QuotePrdctTax> filteredTaxes;

                    filteredTaxes = quotePrdct.getQuotationProductTaxes()
                            .stream()
                            .filter(Objects::nonNull)
                            .filter(quotePrdctTax -> quotePrdctTax.getTransactionTypeCode() != null)
                            .map(quotePrdctTax -> {

                                //Set tax amount
                                quotePrdctTax.setTaxAmountString(formatNumberToCurrency(quotePrdctTax.getTaxAmount()));
                                totalTaxAmount.getAndUpdate(bigDecimal -> bigDecimal.add(quotePrdctTax.getTaxAmount().abs()));

                                updateTaxWording(quotePrdctTax);

                                return quotePrdctTax;
                            }).collect(Collectors.toList());

                    quotePrdct.setQuotationProductTaxes(filteredTaxes);
                }

                String finalDescription = description;
                if (quotePrdct.getQuotationRisks() != null) {
                    quotePrdct.getQuotationRisks().forEach(quoteRisk -> {

                        //Set risk value and premium
                        quoteRisk.setValueString(formatNumberToCurrency(quoteRisk.getValue()));
                        quoteRisk.setPremiumString(formatNumberToCurrency(quoteRisk.getTotalPremium()));

                        Optional<CoverTypeDto> coverTypeDto = Optional.ofNullable(coverTypeClient.findById(quoteRisk.getCoverTypeId()));
                        coverTypeDto.ifPresent(coverType -> quoteRisk
                                .setCoverTypeWording(finalDescription + " - " + coverType.getDescription() + " - " + quoteRisk.getRiskId()));
                    });
                }

                //Add the tax to the FAP
                quotePrdct.setFutureAnnualPremium(quotePrdct.getTotalPremium());
                quotePrdct.setFutureAnnualPremiumString(formatNumberToCurrency(quotePrdct.getTotalPremium()));

            });
        }

        return quote;
    }

    /**
     * Builds a renewal notice object required to generate the renewal notice report
     *
     * @param quote the quote from which the renewal notice is generated
     * @return updated Quote object
     */
    private Quote buildRenewalNotice(Quote quote) {

        //Get client
        ClientDto client = getClientDetails(quote.getClientId());

        //Check for empty names
        if (client != null) {
            client.setFirstName(client.getFirstName() == null ? "" : client.getFirstName());
            client.setLastName((client.getLastName() == null ? "" : client.getLastName()));
            client.setCompanyName(client.getCompanyName() == null ? "" : client.getCompanyName());

            quote.setClientName(WordUtils.capitalizeFully((client.getClientType() == ClientTypes.INDIVIDUAL ?
                    client.getFirstName() : client.getCompanyName()) + " " + client.getLastName()));
            quote.setClientAddress(client.getPhysicalAddress());
            quote.setClientPhoneNumber(client.getPhoneNumber());
        }

        //Get Currency
        quote.setCurrencyDescription(getCurrencySymbol(quote.getCurrencyId()));

        //Get Agent Name
        quote.setAgentName(getAgentName(quote.getOrganizationId()));

        //Get Insurer Information
        quote.setOrganization(getInsurerOrganization(quote.getPanelId(), quote.getOrganizationId()));

        //Renewal Date
        quote.setExpiryDateString(formatMillisecondsToDate("dd/MM/yyyy", quote.getCoverFromDate()));

        StringBuilder valuerNotes = new StringBuilder();

        //Get the product description and Tax description
        if (quote.getQuotationProducts() != null) {
            quote.getQuotationProducts().forEach(quotePrdct -> {

                //Set Premium amount and FAP
                quotePrdct.setPremiumAmountString(formatNumberToCurrency(quotePrdct.getBasicPremium()));

                Optional<ProductDto> product = Optional.ofNullable(productClient.findById(quotePrdct.getProductId()));
                product.ifPresent(productDto -> quotePrdct.setProductDesc(productDto.getDescription()));

                setQuotationProductTaxAmount(quotePrdct);

                formatRiskValueAndPremiumToString(valuerNotes, quotePrdct);

                quotePrdct.setValuerNotes(valuerNotes.toString());

                //Add the tax to the FAP
                quotePrdct.setFutureAnnualPremium(quotePrdct.getTotalPremium());
                quotePrdct.setFutureAnnualPremiumString(formatNumberToCurrency(quotePrdct.getTotalPremium()));

            });
        }


        return quote;
    }

    private void formatRiskValueAndPremiumToString(StringBuilder valuerNotes, QuotePrdct quotePrdct) {
        if (quotePrdct.getQuotationRisks() != null) {
            quotePrdct.getQuotationRisks().forEach(quoteRisk -> {

                //Set risk value and premium
                quoteRisk.setValueString(formatNumberToCurrency(quoteRisk.getValue()));
                quoteRisk.setPremiumString(formatNumberToCurrency(quoteRisk.getTotalPremium()));

                setRiskValuerNotes(valuerNotes, quoteRisk);
            });
        }
    }

    private void setRiskValuerNotes(StringBuilder valuerNotes, QuoteRisk quoteRisk) {
        if (quoteRisk.getQuotationValuationInfo() != null) {
            //Get Valuer notes
            Optional<ServiceProviderNotesDto> notes = Optional.ofNullable(serviceProviderNotesClient
                    .getByIdentifier(quoteRisk.getQuotationValuationInfo().getValuerOrganizationId(), "MECHANICAL_FEE"));
            notes.ifPresent(serviceProviderNotesDto -> valuerNotes.append(serviceProviderNotesDto.getNotes()).append("<br>"));
        }
    }

    private void setQuotationProductTaxAmount(QuotePrdct quotePrdct) {
        if (quotePrdct.getQuotationProductTaxes() != null) {
            quotePrdct.getQuotationProductTaxes().forEach(quotePrdctTax -> {

                //Set tax amount
                quotePrdctTax.setTaxAmountString(formatNumberToCurrency(quotePrdctTax.getTaxAmount()));

                updateTaxWording(quotePrdctTax);
            });
        }
    }

    private void updateTaxWording(QuotePrdctTax quotePrdctTax) {
        Optional<TransactionTypeDto> transactionTypes = Optional.ofNullable(bussTransactionClient.findByCode(quotePrdctTax.getTransactionTypeCode()));
        transactionTypes.ifPresent(transactionTypeDto -> quotePrdctTax.setTaxWording(transactionTypeDto.getDescription()));
    }

    //<editor-fold desc="helper functions" defaultstate="collapsed">


    private void buildAndSaveReport(Long quotationId, Long policyBatchNo, String policyCurrentStatus, String policyNumber,
                                    Long organizationId, String quotationStatus, String fileUrl, QuotationReportType quotationReportType,
                                    String mimeType, int fileSize, String fileName, boolean isMailable) {
        QuotationReports quotationReports = QuotationReports.builder()
                .quotationId(quotationId)
                .policyId(policyBatchNo)
                .policyCurrentStatus(policyCurrentStatus)
                .policyNumber(policyNumber)
                .organizationId(organizationId)
                .quotationStatus(quotationStatus)
                .fileUrl(fileUrl)
                .fileCategory(quotationReportType)
                .emailRetryCount(0)
                .maxRetryCount(3)
                .mimeType(mimeType)
                .fileSize(fileSize)
                .fileName(fileName)
                .mailable(isMailable)
                .build();

        quotationReportsRepository.save(quotationReports);
    }


    /**
     * Set the valuation firm details on the risk
     *
     * @param quoteRisk           the quote risk
     * @param insurerOrganization the associated insurer organization
     */
    private void setValuationFirms(QuoteRisk quoteRisk, OrganizationDto insurerOrganization, String issueDate) {
        if (quoteRisk.getValuerOrgId() != null) {
            Optional<OrganizationDto> organizationDto = Optional.ofNullable(organizationClient.findById(quoteRisk.getValuerOrgId()));
            if (organizationDto.isPresent()) {
                Optional<EntitiesDto> entitiesDto = Optional.of(entitiesClient.find(organizationDto.get().getEntityId()));
                entitiesDto.ifPresent(dto -> {
                    QuoteValuerInfo quoteValuerInfo = new QuoteValuerInfo();
                    quoteValuerInfo.setValuerEmail(dto.getEmailAddress());
                    quoteValuerInfo.setValuerName(dto.getOrganizationName());
                    quoteValuerInfo.setValuerTelephone(dto.getPhoneNumber());
                    quoteValuerInfo.setValuerPhysicalAddress(dto.getPhysicalAddress());
                    quoteValuerInfo.setDateOfIssue(issueDate);

                    quoteRisk.setQuotationValuationInfo(quoteValuerInfo);
                });
            }

            //Set Service Provider Notes based on the insurer
            Optional.of(insurerOrganization)
                    .ifPresent(insurer -> {
                        Optional<List<ServiceProviderNotesDto>> notes = Optional.ofNullable(serviceProviderNotesClient.getByInsurer(insurer.getId()));
                        if (notes.isPresent()) {
                            List<String> valuerNotes = notes.get().stream()
                                    .map(ServiceProviderNotesDto::getNotes)
                                    .filter(serviceProviderNotesDtoNotes -> !serviceProviderNotesDtoNotes.isEmpty())
                                    .distinct()
                                    .collect(Collectors.toList());
                            if (valuerNotes.isEmpty()) {
                                quoteRisk.getQuotationValuationInfo().setValuerNote(null);
                            } else {
                                quoteRisk.getQuotationValuationInfo().setValuerNote(valuerNotes);
                            }
                        }
                    });

        }
    }


    /**
     * Uplaods generated report for storage
     *
     * @param fileData     the document as a byte array
     * @param fileName     the document name
     * @param insurerOrgId the associated insurer
     * @param clientId     the associated client
     * @param policyNo     the associated policy no, this value is optional
     * @return {@link S3Object}
     */
    private List<S3Object> uploadDocument(byte[] fileData, String fileName, Long insurerOrgId, Long clientId, String policyNo) {
        try {
            Path tempFile = Files.write(Paths.get(fileName), fileData, StandardOpenOption.CREATE);

            MultiValueMap<String,Object> body = new LinkedMultiValueMap<>();
            body.add("file",new FileSystemResource(tempFile.toFile()));
            return docsServiceClient.saveAttachment(body, insurerOrgId, "REPORTS", clientId, policyNo);
        } catch (IOException ex) {
            throw new DocumentException(ex.getMessage(), ex);
        }

    }

    /**
     * Gets an agent name
     *
     * @param organizationId agent organization Id
     * @return agent name
     */

    private String getAgentName(Long organizationId) {
        logger.info("Getting Agent Name...");
        if (organizationId != null) {
            Optional<OrganizationDto> organizationDto = Optional.ofNullable(organizationClient.findById(organizationId));
            if (organizationDto.isPresent()) {
                Optional<EntitiesDto> entitiesDto = Optional.ofNullable(entitiesClient.find(organizationDto.get().getEntityId()));
                if (entitiesDto.isPresent()) {
                    return WordUtils.capitalizeFully(entitiesDto.get().getFirstName() + " " + entitiesDto.get().getLastName());
                }
            }
        }

        return "";
    }

    /**
     * Gets the currency symbol
     *
     * @param currencyId currency Id
     * @return String
     */

    private String getCurrencySymbol(Long currencyId) {
        if (currencyId != null) {
            Optional<CurrencyDto> currencyDto = Optional.ofNullable(currencyClient.findById(currencyId));
            if (currencyDto.isPresent()) {
                return currencyDto.get().getSymbol();
            }
        }

        return "";
    }

    /**
     * Get the client full names
     *
     * @param clientId Client Id
     * @return String
     */

    private String getClientName(Long clientId) {
        if (clientId != null) {
            Optional<ClientDto> client = Optional.ofNullable(clientDataClient.findById(clientId));

            if (client.isPresent()) {
                //Check for empty last name
                if (client.get().getLastName() == null) {
                    client.get().setLastName("");
                }

                String firstName = client.get().getClientType() == ClientTypes.INDIVIDUAL ? client.get().getFirstName()
                        : client.get().getCompanyName();
                return WordUtils.capitalizeFully(firstName + " " + client.get().getLastName());
            }
        }

        return "";
    }

    /**
     * Gets the client details
     *
     * @param clientId the client Id
     * @return ClientDto
     */
    private ClientDto getClientDetails(Long clientId) {
        if (clientId != null) {
            return clientDataClient.findById(clientId);
        }
        return null;
    }

    /**
     * Get the quotation Id in an associated policy
     *
     * @param batchNo the policy batch no
     * @return Long
     */
    private Long getQuotationIdOnPolicy(Long batchNo) {
        if (batchNo != null) {
            Optional<PolicyDto> policy = Optional.ofNullable(policyClient.findPolicyByBatchNumber(batchNo));
            if (policy.isPresent()) {
                logger.info("Policy quotation no: {}", policy.get().getQuotationId());
                return policy.get().getQuotationId() == null ? -1L : policy.get().getQuotationId();
            }
        }
        logger.debug("Policy quotation not found");
        return -1L;
    }

    /**
     * Formats a number to a String currency format
     *
     * @param number the BigDecimal to be formated
     * @return String
     */
    private String formatNumberToCurrency(BigDecimal number) {
        if (number != null) {
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###,##0.00");
            return decimalFormat.format(number);
        }
        return "";
    }

    /**
     * Gets the insurer organization details
     *
     * @param panelId        the panel to which the agent belongs to
     * @param organizationId the agent organization Id
     * @return OrganizationDto
     */
    private OrganizationDto getInsurerOrganization(Long panelId, Long organizationId) {
        if (panelId != null) {
            return Optional.ofNullable(agencyClient.findByPanelIdAndOrganizationId(panelId, organizationId))
                    .map(agencyDto -> organizationClient.findById(agencyDto.getInsurerId()))
                    .orElse(null);
        }

        return null;
    }

    private String getDay(int dayOfMonth) {
        if (dayOfMonth >= 1 && dayOfMonth <= 31) {
            int remainder = dayOfMonth % 10;

            //To handle 11,12,13
            if (dayOfMonth > 10 && dayOfMonth < 14) {
                remainder = 4;
            }

            switch (remainder) {
                case 1:
                    return dayOfMonth + "st";
                case 2:
                    return dayOfMonth + "nd";
                case 3:
                    return dayOfMonth + "rd";
                default:
                    return dayOfMonth + "th";
            }
        }

        return "";
    }

    private Long getInsurerOrganizationId(Long panelId, Long organizationId) {
        if (panelId != null) {
            return Optional.ofNullable(agencyClient.findByPanelIdAndOrganizationId(panelId, organizationId))
                    .map(AgencyDto::getInsurerId)
                    .orElse(-1L);
        }

        return -1L;
    }

    private String formatMillisecondsToDate(String pattern, Long milliseconds) {

        if (milliseconds != null && pattern != null) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);

            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault());
            Date date = new Date(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            return localDateTime.format(dateTimeFormatter);
        }

        return "";

    }

    //</editor-fold>

}
