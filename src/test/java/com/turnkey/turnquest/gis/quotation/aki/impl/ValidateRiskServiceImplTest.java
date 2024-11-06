package com.turnkey.turnquest.gis.quotation.aki.impl;

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
import com.turnkey.turnquest.gis.quotation.dto.crm.OrganizationDto;
import com.turnkey.turnquest.gis.quotation.dto.gis.CoverTypeDto;
import com.turnkey.turnquest.gis.quotation.dto.gis.ProductInstallmentDto;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
class ValidateRiskServiceImplTest {

    @Mock
    private QuotationRepository quotationRepository;

    @Mock
    private NotificationProducer notificationProducer;

    @Mock
    private QuotationProductRepository quotationProductRepository;

    @Mock
    private AgencyClient agencyClient;

    @Mock
    private ClientDataClient clientDataClient;

    @Mock
    private CoverTypeClient coverTypeClient;

    @Mock
    private ProductInstallmentClient productInstallmentClient;

    @Mock
    private SubClassCoverTypeClient subClassCoverTypeClient;

    @Mock
    private DMVICService dmvicService;

    @InjectMocks
    private ValidateRiskServiceImpl validateRiskService;


    @BeforeEach
    void setUp() {
        // Initialize any necessary setup
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("Should return empty validation response when quotation is not present")
    void shouldReturnEmptyValidationResponseWhenQuotationIsNotPresent() {
        // Arrange
        Long quotationId = 1L;
        when(quotationRepository.findById(quotationId)).thenReturn(Optional.empty());

        // Act
        ValidationResponseDto response = validateRiskService.validateRisk(quotationId);

        // Assert
        assertNotNull(response);
        assertTrue(response.getErrors() == null || response.getErrors().isEmpty());
        verify(quotationRepository, times(1)).findById(quotationId);
    }

    @Test
    void testGetCertificateDates() throws Exception {
        boolean isInstallmentAllowed = true;
        QuotationRisk quotationRisk = new QuotationRisk();
        quotationRisk.setWithEffectFromDate(1625097600000L); // 2021-07-01
        quotationRisk.setWithEffectToDate(1656633600000L);   // 2022-07-01

        Map<String, Long> result = (Map<String, Long>) ReflectionTestUtils.invokeMethod(validateRiskService, "getCertificateDates", isInstallmentAllowed, quotationRisk);

        assertEquals(1625097600000L, result.get("wef"));
        assertEquals(1656633600000L, result.get("wet"));
    }

    @Test
    void testSendAkiErrorNotification() throws Exception {
        // Arrange
        Long quotationId = 123L;
        String errorNotifications = "Test error";
        Long orgId = 456L;

        // Use reflection to access the private method
        Method sendAkiErrorNotificationMethod = ValidateRiskServiceImpl.class.getDeclaredMethod(
                "sendAkiErrorNotification", Long.class, String.class, Long.class);
        sendAkiErrorNotificationMethod.setAccessible(true);

        // Act
        sendAkiErrorNotificationMethod.invoke(validateRiskService, quotationId, errorNotifications, orgId);

        // Assert
        ArgumentCaptor<PushNotificationDto> notificationCaptor = ArgumentCaptor.forClass(PushNotificationDto.class);
        verify(notificationProducer, times(1)).queuePushNotification(notificationCaptor.capture());

        PushNotificationDto capturedNotification = notificationCaptor.getValue();
        assertNotNull(capturedNotification);
        assertEquals(String.valueOf(quotationId), capturedNotification.getId());
        assertEquals(orgId, capturedNotification.getOrganizationId());
        assertEquals("AKI_WARNING", capturedNotification.getTemplateCode());

        Map<String, Object> attributes = capturedNotification.getAttributes();
        assertNotNull(attributes);
        assertEquals(errorNotifications, attributes.get("[ERROR_TEXT]"));
    }


    @Test
    void testGetVerificationResponse() throws Exception {
        // Arrange
        CertificateRequestDto certificateRequestDto = CertificateRequestDto.builder()
                .memberCompanyId(123L)
                .registrationNumber("ABC123")
                .bodyType("Sedan")
                .chassisNumber("CHASSIS123")
                .commencingDate("01/01/2023")
                .email("test@example.com")
                .engineNumber("ENGINE123")
                .expiringDate("31/12/2023")
                .hudumaNumber("HUDUMA123")
                .insuredPin("PIN123")
                .licensedToCarry(5)
                .phoneNumber("1234567890")
                .policyHolder("John Doe")
                .policyNumber("POL123")
                .sumInsured(new BigDecimal("50000"))
                .tonnageCarryingCapacity(1000)
                .type("TYPE_A")
                .typeOfCertificate("1")
                .typeOfCover("COMPREHENSIVE")
                .vehicleMake("Toyota")
                .vehicleModel("Corolla")
                .vehicleType("Private")
                .yearOfManufacture(2020)
                .yearOfRegistration(2021)
                .policyBatchNo(-1L)
                .quotationId(456L)
                .insurerOrganizationId(789L)
                .build();

        CertificateResponseDto expectedResponse = new CertificateResponseDto();
        // Set up expectedResponse with necessary data if needed

        when(dmvicService.validateRisk(any(CertificateRequestDto.class))).thenReturn(expectedResponse);

        // Use reflection to access the private method
        Method getVerificationResponseMethod = ValidateRiskServiceImpl.class.getDeclaredMethod(
                "getVerificationResponse", CertificateRequestDto.class);
        getVerificationResponseMethod.setAccessible(true);

        // Act
        CertificateResponseDto result = (CertificateResponseDto) getVerificationResponseMethod.invoke(
                validateRiskService, certificateRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);

        // Verify that dmvicService.validateRisk was called with the correct argument
        verify(dmvicService, times(1)).validateRisk(certificateRequestDto);
    }

    @Test
    void testGetClient() throws Exception {
        // Arrange
        Long clientId = 1L;
        ClientDto expectedClientDto = new ClientDto();
        // Set up expectedClientDto with necessary data if needed

        when(clientDataClient.findById(clientId)).thenReturn(expectedClientDto);

        // Use reflection to access the private method
        Method getClientMethod = ValidateRiskServiceImpl.class.getDeclaredMethod("getClient", Long.class);
        getClientMethod.setAccessible(true);

        // Act
        ClientDto result = (ClientDto) getClientMethod.invoke(validateRiskService, clientId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedClientDto, result);

        // Verify that clientDataClient.findById was called with the correct argument
        verify(clientDataClient, times(1)).findById(clientId);
    }


    @Test
    void testGetEntity() throws Exception {
        // Arrange
        Long organizationId = 1L;
        Long panelId = 1L;
        EntitiesDto expectedEntitiesDto = new EntitiesDto();
        // Set up expectedEntitiesDto with necessary data if needed

        AgencyDto agencyDto = new AgencyDto();
        agencyDto.setOrganization(new OrganizationDto());
        agencyDto.getOrganization().setEntities(expectedEntitiesDto);

        when(agencyClient.findByPanelIdAndOrganizationId(panelId, organizationId)).thenReturn(agencyDto);

        // Use reflection to access the private method
        Method getEntityMethod = ValidateRiskServiceImpl.class.getDeclaredMethod("getEntity", Long.class, Long.class);
        getEntityMethod.setAccessible(true);

        // Act
        EntitiesDto result = (EntitiesDto) getEntityMethod.invoke(validateRiskService, organizationId, panelId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedEntitiesDto, result);

        // Verify that agencyClient.findByPanelIdAndOrganizationId was called with the correct arguments
        verify(agencyClient, times(1)).findByPanelIdAndOrganizationId(panelId, organizationId);
    }

    @Test
    void testRiskRequiresValuation() throws Exception {
        // Arrange
        QuotationRisk quoteRisk = new QuotationRisk();
        quoteRisk.setProductSubClassId(1L);
        quoteRisk.setCoverTypeId(1L);

        when(subClassCoverTypeClient.requiresValuation(quoteRisk.getProductSubClassId(), quoteRisk.getCoverTypeId())).thenReturn(YesNo.Y);

        // Use reflection to access the private method
        Method riskRequiresValuationMethod = ValidateRiskServiceImpl.class.getDeclaredMethod("riskRequiresValuation", QuotationRisk.class);
        riskRequiresValuationMethod.setAccessible(true);

        // Act
        boolean result = (boolean) riskRequiresValuationMethod.invoke(validateRiskService, quoteRisk);

        // Assert
        assertTrue(result);

        // Verify that subClassCoverTypeClient.requiresValuation was called with the correct arguments
        verify(subClassCoverTypeClient, times(1)).requiresValuation(quoteRisk.getProductSubClassId(), quoteRisk.getCoverTypeId());
    }

    @Test
    void testValidateRequestPayload() throws Exception {
        // Arrange
        CertificateRequestDto certificateRequestDto = CertificateRequestDto.builder()
                .memberCompanyId(123L)
                .registrationNumber("ABC123")
                .bodyType("Sedan")
                .chassisNumber("CHASSIS123")
                .commencingDate("01/01/2023")
                .email("test@example.com")
                .engineNumber("ENGINE123")
                .expiringDate("31/12/2023")
                .hudumaNumber("HUDUMA123")
                .insuredPin("PIN123")
                .licensedToCarry(5)
                .phoneNumber("1234567890")
                .policyHolder("John Doe")
                .policyNumber("POL123")
                .sumInsured(new BigDecimal("50000"))
                .tonnageCarryingCapacity(1000)
                .type("TYPE_A")
                .typeOfCertificate("1")
                .vehicleMake("Toyota")
                .vehicleModel("Corolla")
                .typeOfCover("120")
                .vehicleType("Private")
                .yearOfManufacture(2020)
                .yearOfRegistration(2021)
                .policyBatchNo(-1L)
                .quotationId(456L)
                .insurerOrganizationId(789L)
                .build();

        // Set up certificateRequestDto with necessary data if needed

        CertificateType certificateType = CertificateType.TYPE_A; // or any other type you want to test

        // Use reflection to access the private method
        Method validateRequestPayloadMethod = ValidateRiskServiceImpl.class.getDeclaredMethod("validateRequestPayload", CertificateRequestDto.class, CertificateType.class);
        validateRequestPayloadMethod.setAccessible(true);

        // Act
        List<ErrorDto> result = (List<ErrorDto>) validateRequestPayloadMethod.invoke(validateRiskService, certificateRequestDto, certificateType);

        // Assert
        assertNotNull(result);
        // Add more assertions based on your expectations
    }

    @Test
    void testValidateRequestPayload_TypeB() throws Exception {
        // Arrange
        CertificateRequestDto certificateRequestDto = createCertificateRequestDto();
        certificateRequestDto.setType("TYPE_B");

        // Use reflection to access the private method
        Method validateRequestPayloadMethod = ValidateRiskServiceImpl.class.getDeclaredMethod("validateRequestPayload", CertificateRequestDto.class, CertificateType.class);
        validateRequestPayloadMethod.setAccessible(true);

        // Act
        List<ErrorDto> result = (List<ErrorDto>) validateRequestPayloadMethod.invoke(validateRiskService, certificateRequestDto, CertificateType.TYPE_B);

        // Assert
        assertNotNull(result);
        // Add more assertions based on your expectations for TYPE_B
    }

    @Test
    void testValidateRequestPayload_MissingPin() throws Exception {
        // Arrange
        CertificateRequestDto certificateRequestDto = createCertificateRequestDto();
        certificateRequestDto.setInsuredPin(null); // Set PIN to null to trigger the condition

        // Use reflection to access the private method
        Method validateRequestPayloadMethod = ValidateRiskServiceImpl.class.getDeclaredMethod("validateRequestPayload", CertificateRequestDto.class, CertificateType.class);
        validateRequestPayloadMethod.setAccessible(true);

        // Act
        List<ErrorDto> result = (List<ErrorDto>) validateRequestPayloadMethod.invoke(validateRiskService, certificateRequestDto, CertificateType.TYPE_A);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Missing PIN for the insured.", result.get(0).getErrorText());
    }

    @Test
    void testSendWarningNotifications() throws Exception {
        // Arrange
        Long quotationId = 1L; // Set a valid quotation ID
        Set<ErrorDto> warningResponse = new HashSet<>();
        warningResponse.add(new ErrorDto("Error 1", "Code 1"));
        warningResponse.add(new ErrorDto("Error 2", "Code 2"));
        Optional<Quotation> quotation = Optional.of(new Quotation());
        quotation.get().setInsurerOrgId(123L); // Set a valid insurer organization ID

        // Use reflection to access the private method
        Method sendWarningNotificationsMethod = ValidateRiskServiceImpl.class.getDeclaredMethod("sendWarningNotifications", Long.class, Set.class, Optional.class);
        sendWarningNotificationsMethod.setAccessible(true);

        // Act
        sendWarningNotificationsMethod.invoke(validateRiskService, quotationId, warningResponse, quotation);
        // Assert
        // Since the method does not return a value, we can only verify that it was executed without throwing an exception.
        // If the method interacts with other objects, we can use Mockito to verify that the interactions occurred as expected.
        // For example, if the sendAkiErrorNotification method sends a notification, we can verify that the notification was sent.
        // In this case, we would need to mock the object that sends the notification and use Mockito's verify method to check that it was called with the correct arguments.
    }

    @Test
    void testValidateRequestPayload_TypeDOrA_MissingTypeOfCertificate() throws Exception {
        // Arrange
        CertificateRequestDto certificateRequestDto = createCertificateRequestDto();
        certificateRequestDto.setTypeOfCertificate(null); // Set TypeOfCertificate to null to trigger the condition

        // Use reflection to access the private method
        Method validateRequestPayloadMethod = ValidateRiskServiceImpl.class.getDeclaredMethod("validateRequestPayload", CertificateRequestDto.class, CertificateType.class);
        validateRequestPayloadMethod.setAccessible(true);

        // Act & Assert for TYPE_D
        List<ErrorDto> resultD = (List<ErrorDto>) validateRequestPayloadMethod.invoke(validateRiskService, certificateRequestDto, CertificateType.TYPE_D);
        assertNotNull(resultD);
        assertEquals(2, resultD.size());

        // Act & Assert for TYPE_A
        List<ErrorDto> resultA = (List<ErrorDto>) validateRequestPayloadMethod.invoke(validateRiskService, certificateRequestDto, CertificateType.TYPE_A);
        assertNotNull(resultA);
        assertEquals(2, resultA.size());
    }

    @Test
    void testValidateRequestPayload_TypeA_MissingLicensedToCarry() throws Exception {
        // Arrange
        CertificateRequestDto certificateRequestDto = createCertificateRequestDto();
        certificateRequestDto.setLicensedToCarry(null); // Set LicensedToCarry to null to trigger the condition

        // Act & Assert
        testValidateRequestPayload(certificateRequestDto, CertificateType.TYPE_A, "Carrying capacity is required for " + CertificateType.TYPE_A.getName() + " cover.");
    }

    @Test
    void testValidateRequestPayload_TypeB_MissingVehicleType() throws Exception {
        // Arrange
        CertificateRequestDto certificateRequestDto = createCertificateRequestDto();
        certificateRequestDto.setVehicleType(null); // Set VehicleType to null to trigger the condition

        // Act & Assert
        testValidateRequestPayload(certificateRequestDto, CertificateType.TYPE_B, "Vehicle Type is required for " + CertificateType.TYPE_B.getName() + " cover.");
    }

    @Test
    void testValidateRequestPayload_TypeB_MissingTonnageCarryingCapacity() throws Exception {
        // Arrange
        CertificateRequestDto certificateRequestDto = createCertificateRequestDto();
        certificateRequestDto.setTonnageCarryingCapacity(null); // Set TonnageCarryingCapacity to null to trigger the condition

        // Act & Assert
        testValidateRequestPayload(certificateRequestDto, CertificateType.TYPE_B, "Tonnage Carrying Capacity is required for " + CertificateType.TYPE_B.getName() + " cover.");
    }

    @Test
    void testValidateRequestPayload_MissingPhoneNumber() throws Exception {
        // Arrange
        CertificateRequestDto certificateRequestDto = createCertificateRequestDto();
        certificateRequestDto.setPhoneNumber(null); // Set PhoneNumber to null to trigger the condition

        // Act & Assert
        testValidateRequestPayload(certificateRequestDto, CertificateType.TYPE_A, "Client phone number is required");
    }

    private CertificateRequestDto createValidCertificateRequestDto() {
        return CertificateRequestDto.builder()
                // Set all fields to valid values
                .build();
    }

    private void testValidateRequestPayload(CertificateRequestDto certificateRequestDto, CertificateType certificateType, String expectedErrorMessage) throws Exception {
        // Use reflection to access the private method
        Method validateRequestPayloadMethod = ValidateRiskServiceImpl.class.getDeclaredMethod("validateRequestPayload", CertificateRequestDto.class, CertificateType.class);
        validateRequestPayloadMethod.setAccessible(true);

        // Act
        List<ErrorDto> result = (List<ErrorDto>) validateRequestPayloadMethod.invoke(validateRiskService, certificateRequestDto, certificateType);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testBuildCertificateRequestPayload() throws Exception {
        // Arrange
        QuotationRisk mockQuotationRisk = mock(QuotationRisk.class);
        EntitiesDto mockEntitiesDto = mock(EntitiesDto.class);
        ClientDto mockClientDto = mock(ClientDto.class);
        QuotationProduct mockQuotationProduct = mock(QuotationProduct.class);
        Quotation mockQuotation = mock(Quotation.class);
        MotorSchedules mockMotorSchedules = mock(MotorSchedules.class);
        CoverTypeDto mockCoverTypeDto = mock(CoverTypeDto.class);


        when(mockQuotationRisk.getMotorSchedules()).thenReturn(mockMotorSchedules);
        when(mockMotorSchedules.getCertificateType()).thenReturn(CertificateType.TYPE_A.getName());
        when(mockQuotationProduct.getInstallmentAllowed()).thenReturn(YesNo.Y);
        when(mockQuotationProduct.getNextInstallmentNo()).thenReturn(2L);
        when(mockQuotationRisk.getValuationStatus()).thenReturn(ValuationStatus.CLOSED);
        when(mockClientDto.getClientType()).thenReturn(ClientTypes.INDIVIDUAL);
        when(mockClientDto.getFirstName()).thenReturn("John");
        when(mockClientDto.getLastName()).thenReturn("Doe");
        when(mockClientDto.getPhoneNumber()).thenReturn("1234567890");
        when(mockQuotation.getQuotationNo()).thenReturn("QUOTE123");
        when(mockQuotationProduct.getTotalSumInsured()).thenReturn(new BigDecimal("50000"));
        when(mockMotorSchedules.getTonnage()).thenReturn("1000");
        when(mockMotorSchedules.getMake()).thenReturn("Toyota");
        when(mockMotorSchedules.getModel()).thenReturn("Corolla");
        when(mockMotorSchedules.getYearOfManufacture()).thenReturn(2020L);
        when(mockMotorSchedules.getYearOfRegistration()).thenReturn(2021L);
        when(mockQuotation.getId()).thenReturn(456L);
        when(mockQuotation.getInsurerOrgId()).thenReturn(789L);
        when(mockQuotationRisk.getCoverTypeCode()).thenReturn(CoverTypeCode.COMP.name());
        when(coverTypeClient.findById(anyLong())).thenReturn(mockCoverTypeDto);
        when(mockCoverTypeDto.getCode()).thenReturn("COMP");


        // Use reflection to access the private method
        Method buildCertificateRequestPayloadMethod = ValidateRiskServiceImpl.class.getDeclaredMethod("buildCertificateRequestPayload",
                QuotationRisk.class, EntitiesDto.class, ClientDto.class, QuotationProduct.class, Quotation.class);
        buildCertificateRequestPayloadMethod.setAccessible(true);

        // Act
        CertificateRequestDto result = (CertificateRequestDto) buildCertificateRequestPayloadMethod.invoke(validateRiskService,
                mockQuotationRisk, mockEntitiesDto, mockClientDto, mockQuotationProduct, mockQuotation);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getPolicyHolder());
        assertEquals("QUOTE123", result.getPolicyNumber());
        assertEquals(new BigDecimal("50000"), result.getSumInsured());
        assertEquals("Toyota", result.getVehicleMake());
        assertEquals("Corolla", result.getVehicleModel());
        assertEquals(2020, result.getYearOfManufacture().intValue());
        assertEquals(2021, result.getYearOfRegistration().intValue());
        assertEquals(456L, result.getQuotationId().longValue());
        assertEquals(789L, result.getInsurerOrganizationId().longValue());
    }

    @Test
    void testBuildCertificateRequestPayload_NullMotorSchedules() throws Exception {
        QuotationRisk mockQuotationRisk = mock(QuotationRisk.class);
        when(mockQuotationRisk.getMotorSchedules()).thenReturn(null);

        Method buildCertificateRequestPayloadMethod = ValidateRiskServiceImpl.class.getDeclaredMethod("buildCertificateRequestPayload",
                QuotationRisk.class, EntitiesDto.class, ClientDto.class, QuotationProduct.class, Quotation.class);
        buildCertificateRequestPayloadMethod.setAccessible(true);

        try {
            buildCertificateRequestPayloadMethod.invoke(validateRiskService,
                    mockQuotationRisk, mock(EntitiesDto.class), mock(ClientDto.class), mock(QuotationProduct.class), mock(Quotation.class));
            fail("Expected AKIValidationException to be thrown");
        } catch (InvocationTargetException e) {
            assertInstanceOf(AKIValidationException.class, e.getCause());
        }
    }

    @Test
    void testBuildCertificateRequestPayload_InvalidCertificateType() throws Exception {
        QuotationRisk mockQuotationRisk = mock(QuotationRisk.class);
        MotorSchedules mockMotorSchedules = mock(MotorSchedules.class);
        when(mockQuotationRisk.getMotorSchedules()).thenReturn(mockMotorSchedules);
        when(mockMotorSchedules.getCertificateType()).thenReturn("INVALID_TYPE");

        Method buildCertificateRequestPayloadMethod = ValidateRiskServiceImpl.class.getDeclaredMethod("buildCertificateRequestPayload",
                QuotationRisk.class, EntitiesDto.class, ClientDto.class, QuotationProduct.class, Quotation.class);
        buildCertificateRequestPayloadMethod.setAccessible(true);

        try {
            buildCertificateRequestPayloadMethod.invoke(validateRiskService,
                    mockQuotationRisk, mock(EntitiesDto.class), mock(ClientDto.class), mock(QuotationProduct.class), mock(Quotation.class));
            fail("Expected AKIValidationException to be thrown");
        } catch (InvocationTargetException e) {
            assertInstanceOf(AKIValidationException.class, e.getCause());
        }
    }

    @Test
    void testGetCertificateType() throws Exception {
        // Arrange
        String certType = "TYPE_A"; // or any other type you want to test

        // Use reflection to access the private method
        Method getCertificateTypeMethod = ValidateRiskServiceImpl.class.getDeclaredMethod("getCertificateType", String.class);
        getCertificateTypeMethod.setAccessible(true);

        // Act
        CertificateType result = (CertificateType) getCertificateTypeMethod.invoke(validateRiskService, CertificateType.TYPE_A.getName());

        // Assert
        assertNotNull(result);
        assertEquals(CertificateType.TYPE_A, result);
    }

    @Test
    void testIsLastInstallment() throws Exception {
        // Arrange
        QuotationProduct quotationProduct = new QuotationProduct();
        quotationProduct.setProductInstallmentId(1L); // Set a valid product installment ID

        Long installmentNo = 3L; // Set a valid installment number

        ProductInstallmentDto productInstallmentDto = new ProductInstallmentDto();
        productInstallmentDto.setCalculation(InstallmentCalculation.RAT);
        productInstallmentDto.setCertificateDistribution("1:2:3");

        when(productInstallmentClient.getInstallmentsById(quotationProduct.getProductInstallmentId())).thenReturn(productInstallmentDto);

        // Use reflection to access the private method
        Method isLastInstallmentMethod = ValidateRiskServiceImpl.class.getDeclaredMethod("isLastInstallment", QuotationProduct.class, Long.class);
        isLastInstallmentMethod.setAccessible(true);

        // Act
        boolean result = (boolean) isLastInstallmentMethod.invoke(validateRiskService, quotationProduct, installmentNo);

        // Assert
        assertTrue(result);

        // Verify that productInstallmentClient.getInstallmentsById was called with the correct argument
        verify(productInstallmentClient, times(1)).getInstallmentsById(quotationProduct.getProductInstallmentId());
    }

    @Test
    void testGetCertType() throws Exception {
        // Arrange
        CertificateType certificateType = CertificateType.TYPE_A;
        CertificateType certificateTypeB = CertificateType.TYPE_B;
        CertificateType certificateTypeC = CertificateType.TYPE_C;
        CertificateType certificateTypeD = CertificateType.TYPE_D;

        // Use reflection to access the private method
        Method getCertTypeMethod = ValidateRiskServiceImpl.class.getDeclaredMethod("getCertType", CertificateType.class);
        getCertTypeMethod.setAccessible(true);

        // Act
        String result_A = (String) getCertTypeMethod.invoke(validateRiskService, certificateType);
        String result_B = (String) getCertTypeMethod.invoke(validateRiskService, certificateTypeB);
        String result_C = (String) getCertTypeMethod.invoke(validateRiskService, certificateTypeC);
        String result_D = (String) getCertTypeMethod.invoke(validateRiskService, certificateTypeD);

        // Assert
        assertNotNull(result_A);
        assertNotNull(result_B);
        assertNotNull(result_C);
        assertNotNull(result_D);
        assertEquals("1", result_A);
        assertEquals("6", result_B);
        assertEquals("7", result_C);
        assertEquals("8", result_D);
    }

    @Test
    void testGetEndDate() throws Exception {
        // Arrange
        Long withEffectFromDate = System.currentTimeMillis();
        int monthsFromStartDate = 6; // or any other number you want to test

        // Use reflection to access the private method
        Method getEndDateMethod = ValidateRiskServiceImpl.class.getDeclaredMethod("getEndDate", Long.class, int.class);
        getEndDateMethod.setAccessible(true);

        // Act
        Long result = (Long) getEndDateMethod.invoke(validateRiskService, withEffectFromDate, monthsFromStartDate);

        // Assert
        assertNotNull(result);
        assertTrue(result > withEffectFromDate);
    }

    @Test
    void testOffSetDateByMonths() throws Exception {
        // Arrange
        Long dateValue = System.currentTimeMillis();
        int months = 6; // or any other number you want to test

        // Use reflection to access the private method
        Method offSetDateByMonthsMethod = ValidateRiskServiceImpl.class.getDeclaredMethod("offSetDateByMonths", Long.class, int.class);
        offSetDateByMonthsMethod.setAccessible(true);

        // Act
        Long result = (Long) offSetDateByMonthsMethod.invoke(validateRiskService, dateValue, months);

        // Assert
        assertNotNull(result);
        assertTrue(result > dateValue);
    }


    private CertificateRequestDto createCertificateRequestDto() {
        return CertificateRequestDto.builder()
                .memberCompanyId(123L)
                .registrationNumber("ABC123")
                .bodyType("Sedan")
                .chassisNumber("CHASSIS123")
                .commencingDate("01/01/2023")
                .email("test@example.com")
                .engineNumber("ENGINE123")
                .expiringDate("31/12/2023")
                .hudumaNumber("HUDUMA123")
                .insuredPin("PIN123")
                .licensedToCarry(5)
                .phoneNumber("1234567890")
                .policyHolder("John Doe")
                .policyNumber("POL123")
                .sumInsured(new BigDecimal("50000"))
                .tonnageCarryingCapacity(1000)
                .type("TYPE_A")
                .typeOfCertificate("1")
                .vehicleMake("Toyota")
                .vehicleModel("Corolla")
                .typeOfCover("120")
                .vehicleType("Private")
                .yearOfManufacture(2020)
                .yearOfRegistration(2021)
                .policyBatchNo(-1L)
                .quotationId(456L)
                .insurerOrganizationId(789L)
                .build();
    }


}

