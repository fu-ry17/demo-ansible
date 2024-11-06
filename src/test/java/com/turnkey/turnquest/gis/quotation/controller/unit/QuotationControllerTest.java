package com.turnkey.turnquest.gis.quotation.controller.unit;

import com.turnkey.turnquest.gis.quotation.auth.TokenUtils;
import com.turnkey.turnquest.gis.quotation.config.DmvicValidation;
import com.turnkey.turnquest.gis.quotation.controller.QuotationController;
import com.turnkey.turnquest.gis.quotation.dto.ScheduleDetailsDto;
import com.turnkey.turnquest.gis.quotation.dto.computation.ComputationResponse;
import com.turnkey.turnquest.gis.quotation.dto.crm.ClientDto;
import com.turnkey.turnquest.gis.quotation.dto.gis.ComputationRequest;
import com.turnkey.turnquest.gis.quotation.dto.quotation.QuotationDto;
import com.turnkey.turnquest.gis.quotation.enums.ClientTypes;
import com.turnkey.turnquest.gis.quotation.enums.SortType;
import com.turnkey.turnquest.gis.quotation.exception.error.QuoteCreationException;
import com.turnkey.turnquest.gis.quotation.model.MotorSchedules;
import com.turnkey.turnquest.gis.quotation.model.Quotation;
import com.turnkey.turnquest.gis.quotation.model.QuotationProduct;
import com.turnkey.turnquest.gis.quotation.model.QuotationRisk;
import com.turnkey.turnquest.gis.quotation.service.QuotationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class QuotationControllerTest {

    @Mock
    QuotationService quotationService;

    @Mock
    DmvicValidation dmvicValidation;

    @Mock
    private Authentication authentication;
    @Mock
    TokenUtils tokenUtils;

    @InjectMocks
    QuotationController quotationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        quotationController = new QuotationController(quotationService, tokenUtils, dmvicValidation);
    }

    @Test
    void testFindScheduleDetails() {
        String propertyId = "testPropertyId";

        ScheduleDetailsDto scheduleDetailsDto = new ScheduleDetailsDto();
        scheduleDetailsDto.setPolicyNo("testPolicyNo");
        scheduleDetailsDto.setInsurerOrgId(1L);

        when(quotationService.findScheduleDetails(propertyId)).thenReturn(scheduleDetailsDto);

        ResponseEntity<ScheduleDetailsDto> result = quotationController.findScheduleDetails(propertyId);

        assertEquals(scheduleDetailsDto, result.getBody());
    }

    @Test
    void testFindQuotationByQuotationNo() {
        String quotationNo = "Q/TEST/001/2023";

        when(quotationService.findByQuotationNo(quotationNo)).thenReturn(testQuotationData());

        ResponseEntity<List<Quotation>> result = quotationController.getQuotationsByQuotationNo(quotationNo);

        assertEquals(testQuotationData(), result.getBody());
    }

    @Test
    void shouldReturnOkStatusWhenQuotationCreatedSuccessfully() throws Exception {
        // Arrange
        Quotation quotation = testQuotationData().getFirst();
        Authentication authentication = mock(Authentication.class);

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.create(any(Quotation.class))).thenReturn(quotation);

        // Act
        ResponseEntity<Quotation> result = quotationController.create(quotation, authentication);

        // Assert
        verify(tokenUtils).init(authentication);
        verify(quotationService).create(quotation);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(quotation, result.getBody());
    }

    @Test
    void shouldThrowExceptionWhenQuotationCreationFails() {
        // Arrange
        Quotation quotation = testQuotationData().getFirst();
        Authentication authentication = mock(Authentication.class);

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.create(any(Quotation.class))).thenThrow(new RuntimeException());

        // Act and Assert
        assertThrows(Exception.class, () -> quotationController.create(quotation, authentication));
    }

    @Test
    void shouldReturnOkStatusWhenQuotationSavedSuccessfully() throws Exception {
        Quotation quotation = testQuotationData().get(0);
        when(quotationService.save(any(Quotation.class))).thenReturn(quotation);
        ResponseEntity<Quotation> result = quotationController.create(quotation);
        verify(quotationService).save(quotation);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(quotation, result.getBody());
    }

    @Test
    void shouldThrowExceptionWhenQuotationSaveFails() {
        Quotation quotation = testQuotationData().get(0);
        when(quotationService.save(any(Quotation.class))).thenThrow(new QuoteCreationException("Error", new RuntimeException()));
        assertThrows(QuoteCreationException.class, () -> quotationController.create(quotation));
    }

    @Test
    void shouldReturnQuotationWhenFound() {
        Long id = 1L;
        Authentication authentication = mock(Authentication.class);
        Quotation expectedQuotation = new Quotation();

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.findById(id)).thenReturn(Optional.of(expectedQuotation));

        ResponseEntity<Quotation> result = quotationController.find(id, authentication);

        verify(tokenUtils).init(authentication);
        verify(quotationService).findById(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedQuotation, result.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenQuotationNotPresent() {
        Long id = 1L;
        Authentication authentication = mock(Authentication.class);

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Quotation> result = quotationController.find(id, authentication);

        verify(tokenUtils).init(authentication);
        verify(quotationService).findById(id);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void shouldReturnQuotationWhenFoundById() {
        Long id = 1L;
        Quotation expectedQuotation = new Quotation();

        when(quotationService.findById(id)).thenReturn(Optional.of(expectedQuotation));

        ResponseEntity<Quotation> result = quotationController.find(id);

        verify(quotationService).findById(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedQuotation, result.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenQuotationByIdNotPresent() {
        Long id = 1L;

        when(quotationService.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Quotation> result = quotationController.find(id);

        verify(quotationService).findById(id);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void shouldReturnAllQuotationsWhenPresent() {
        Authentication authentication = mock(Authentication.class);
        Pageable pageable = PageRequest.of(0, 50, Sort.by("createdDate").descending());
        List<Quotation> expectedQuotations = Arrays.asList(new Quotation(), new Quotation());

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.findAllQuotations(anyLong(), any(Pageable.class))).thenReturn(expectedQuotations);

        ResponseEntity<List<Quotation>> result = quotationController.all(pageable, authentication);

        verify(tokenUtils).init(authentication);
        verify(quotationService).findAllQuotations(1L, pageable);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedQuotations, result.getBody());
    }

    @Test
    void shouldReturnEmptyListWhenNoQuotationsPresent() {
        Authentication authentication = mock(Authentication.class);
        Pageable pageable = PageRequest.of(0, 50, Sort.by("createdDate").descending());

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.findAllQuotations(anyLong(), any(Pageable.class))).thenReturn(Collections.emptyList());

        ResponseEntity<List<Quotation>> result = quotationController.all(pageable, authentication);

        verify(tokenUtils).init(authentication);
        verify(quotationService).findAllQuotations(1L, pageable);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isEmpty());
    }

    @Test
    void shouldReturnQuotationsWhenClientIdsProvided() {
        List<Long> clientIds = Arrays.asList(1L, 2L, 3L);
        Authentication authentication = mock(Authentication.class);
        Pageable pageable = PageRequest.of(0, 50, Sort.by("createdDate").descending());
        List<Quotation> expectedQuotations = Arrays.asList(new Quotation(), new Quotation());

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.findByClientIds(clientIds, 1L, pageable)).thenReturn(expectedQuotations);

        ResponseEntity<List<Quotation>> result = quotationController.findQuotationsByClientIds(pageable, clientIds, authentication);

        verify(tokenUtils).init(authentication);
        verify(quotationService).findByClientIds(clientIds, 1L, pageable);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedQuotations, result.getBody());
    }

    @Test
    void shouldReturnEmptyListWhenNoQuotationsForClientIds() {
        List<Long> clientIds = Arrays.asList(1L, 2L, 3L);
        Authentication authentication = mock(Authentication.class);
        Pageable pageable = PageRequest.of(0, 50, Sort.by("createdDate").descending());

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.findByClientIds(clientIds, 1L, pageable)).thenReturn(Collections.emptyList());

        ResponseEntity<List<Quotation>> result = quotationController.findQuotationsByClientIds(pageable, clientIds, authentication);

        verify(tokenUtils).init(authentication);
        verify(quotationService).findByClientIds(clientIds, 1L, pageable);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isEmpty());
    }

    @Test
    void shouldReturnRenewalsWhenClientIdsProvided() {
        List<Long> clientIds = Arrays.asList(1L, 2L, 3L);
        Authentication authentication = mock(Authentication.class);
        Pageable pageable = PageRequest.of(0, 50, Sort.by("createdDate").descending());
        List<Quotation> expectedQuotations = Arrays.asList(new Quotation(), new Quotation());

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.findClientRenewals(clientIds, 1L, pageable)).thenReturn(expectedQuotations);

        ResponseEntity<List<Quotation>> result = quotationController.findRenewalsByClientIds(pageable, clientIds, authentication);

        verify(tokenUtils).init(authentication);
        verify(quotationService).findClientRenewals(clientIds, 1L, pageable);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedQuotations, result.getBody());
    }

    @Test
    void shouldReturnEmptyListWhenNoRenewalsForClientIds() {
        List<Long> clientIds = Arrays.asList(1L, 2L, 3L);
        Authentication authentication = mock(Authentication.class);
        Pageable pageable = PageRequest.of(0, 50, Sort.by("createdDate").descending());

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.findClientRenewals(clientIds, 1L, pageable)).thenReturn(Collections.emptyList());

        ResponseEntity<List<Quotation>> result = quotationController.findRenewalsByClientIds(pageable, clientIds, authentication);

        verify(tokenUtils).init(authentication);
        verify(quotationService).findClientRenewals(clientIds, 1L, pageable);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isEmpty());
    }

    @Test
    void shouldUpdateQuotationSuccessfully() {
        Long id = 1L;
        QuotationDto quotationDto = new QuotationDto();
        Quotation expectedQuotation = new Quotation();
        Authentication authentication = mock(Authentication.class);

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.update(id, quotationDto, 1L)).thenReturn(expectedQuotation);

        Quotation result = quotationController.update(quotationDto, id, authentication);

        verify(tokenUtils).init(authentication);
        verify(quotationService).update(id, quotationDto, 1L);

        assertEquals(expectedQuotation, result);
    }

    @Test
    void shouldThrowExceptionWhenUpdateFails() {
        Long id = 1L;
        QuotationDto quotationDto = new QuotationDto();
        Authentication authentication = mock(Authentication.class);

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.update(id, quotationDto, 1L)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> quotationController.update(quotationDto, id, authentication));
    }

    @Test
    void shouldDeleteQuotationSuccessfully() {
        Long id = 1L;

        doNothing().when(quotationService).deleteById(id);

        ResponseEntity<String> result = quotationController.delete(id);

        verify(quotationService).deleteById(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Quotation deleted successfully", result.getBody());
    }

    @Test
    void shouldThrowExceptionWhenDeleteFails() {
        Long id = 1L;

        doThrow(new RuntimeException()).when(quotationService).deleteById(id);

        assertThrows(RuntimeException.class, () -> quotationController.delete(id));
    }

    @Test
    void shouldReturnQuotationProductsWhenPresent() {
        Long id = 1L;
        Authentication authentication = mock(Authentication.class);
        List<QuotationProduct> expectedQuotationProducts = Arrays.asList(new QuotationProduct(), new QuotationProduct());

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.findQuotationProducts(id, 1L)).thenReturn(expectedQuotationProducts);

        ResponseEntity<List<QuotationProduct>> result = quotationController.getQuotationProducts(id, authentication);

        verify(tokenUtils).init(authentication);
        verify(quotationService).findQuotationProducts(id, 1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedQuotationProducts, result.getBody());
    }

    @Test
    void shouldReturnEmptyListWhenNoQuotationProductsPresent() {
        Long id = 1L;
        Authentication authentication = mock(Authentication.class);

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.findQuotationProducts(id, 1L)).thenReturn(Collections.emptyList());

        ResponseEntity<List<QuotationProduct>> result = quotationController.getQuotationProducts(id, authentication);

        verify(tokenUtils).init(authentication);
        verify(quotationService).findQuotationProducts(id, 1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isEmpty());
    }

    @Test
    void shouldConvertToPoliciesSuccessfully() {
        Long id = 1L;
        Authentication authentication = mock(Authentication.class);

        doNothing().when(quotationService).convertQuotationToPolicies(id, 1L);

        ResponseEntity<String> result = quotationController.convertToPolicies(id, authentication);

        verify(tokenUtils).init(authentication);
        verify(quotationService).convertQuotationToPolicies(id, 0L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Quote conversion started successfully", result.getBody());
    }

    @Test
    void shouldThrowExceptionWhenConversionFails() {
        Long id = 1L;
        Authentication authentication = mock(Authentication.class);

        doThrow(new RuntimeException()).when(quotationService).convertQuotationToPolicies(id, 0L);

        assertThrows(RuntimeException.class, () -> quotationController.convertToPolicies(id, authentication));
    }

    @Test
    void saveQuickQuote() {
        var quotation = testQuotationData().getFirst();
        Authentication authentication = mock(Authentication.class);

        when(quotationService.saveQuickQuotation(any(Quotation.class), anyLong())).thenReturn(quotation);
        when(tokenUtils.getOrganizationId()).thenReturn(2L);


        var result = quotationController.saveQuickQuotation(quotation, authentication);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(tokenUtils.getOrganizationId(), Objects.requireNonNull(result.getBody()).getOrganizationId());

    }

    @Test
    void shouldFindQuotationsByClientIds() {
        Long clientId = 1L;
        Long organizationId = 1L;

        when(quotationService.getQuotationsByClientIdAndOrgId(clientId, organizationId)).thenReturn(Collections.singletonList(new Quotation()));

        ResponseEntity<List<Quotation>> result = quotationController.findQuotationsByClientIds(clientId, organizationId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertFalse(result.getBody().isEmpty());
    }

    @Test
    void shouldVerifyQuotation() {
        Long quotationId = 1L;
        Authentication authentication = mock(Authentication.class);

        when(dmvicValidation.getValidation()).thenReturn(true);
        when(quotationService.verifyQuotation(quotationId)).thenReturn(true);

        ResponseEntity<Boolean> result = quotationController.verifyQuotation(quotationId, authentication);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody());
    }

    @Test
    void shouldFindUnreadQuotes() {
        Authentication authentication = mock(Authentication.class);

        when(quotationService.findUnreadQuotes(anyLong())).thenReturn(Mono.just(10));

        ResponseEntity<Mono<Integer>> result = quotationController.findUnreadQuotes(authentication);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void shouldGetQuotationsByPolicyNo() {
        String policyNo = "P/TEST/001/2023";
        Authentication authentication = mock(Authentication.class);

        when(quotationService.getByPolicyNo(policyNo)).thenReturn(Collections.singletonList(new Quotation()));

        List<Quotation> result = quotationController.getQuotationsByPolicyNo(policyNo, authentication);

        assertFalse(result.isEmpty());
    }

    @Test
    void shouldDeleteQuotationsByPolicyNo() {
        String policyNo = "P/TEST/001/2023";
        Authentication authentication = mock(Authentication.class);

        when(quotationService.deleteByPolicyNo(policyNo)).thenReturn(true);

        ResponseEntity<Boolean> result = quotationController.deleteQuotationsByPolicyNo(policyNo, authentication);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody());
    }

    @Test
    void shouldFindQuotationsByPolicyNo() {
        String policyNo = "P/TEST/001/2023";
        Authentication authentication = mock(Authentication.class);

        when(quotationService.findByPolicyNo(policyNo)).thenReturn(new Quotation());

        ResponseEntity<Quotation> result = quotationController.findQuotationsByPolicyNo(policyNo, authentication);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void shouldGetQuotationsByQuotationNo() {
        String quotationNo = "Q/TEST/001/2023";

        when(quotationService.findByQuotationNo(quotationNo)).thenReturn(Collections.singletonList(new Quotation()));

        ResponseEntity<List<Quotation>> result = quotationController.getQuotationsByQuotationNo(quotationNo);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertFalse(result.getBody().isEmpty());
    }

    @Test
    void shouldDeleteRisk() {
        Long riskId = 1L;
        Long quotationId = 1L;

        when(quotationService.deleteQuotationRisk(riskId, quotationId)).thenReturn(new Quotation());

        ResponseEntity<Quotation> result = quotationController.deleteRisk(riskId, quotationId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void shouldFindScheduleDetails() {
        String propertyId = "testPropertyId";

        when(quotationService.findScheduleDetails(propertyId)).thenReturn(new ScheduleDetailsDto());

        ResponseEntity<ScheduleDetailsDto> result = quotationController.findScheduleDetails(propertyId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void shouldCreateComputationQuotation() {
        ComputationResponse computationResponse = new ComputationResponse();
        Long quotationId = 1L;
        Authentication authentication = mock(Authentication.class);

        when(quotationService.createComputationQuotation(any(ComputationResponse.class), anyLong(), anyLong())).thenReturn(new Quotation());

        ResponseEntity<Quotation> result = quotationController.createComputationQuotation(computationResponse, quotationId, authentication);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void shouldCreateComparisonQuoteRequestObject() {
        Long quotationId = 1L;
        Authentication authentication = mock(Authentication.class);

        when(quotationService.createComparisonRequestObject(quotationId)).thenReturn(Collections.singletonList(new ComputationRequest()));

        ResponseEntity<List<ComputationRequest>> result = quotationController.createComparisonQuoteRequestObject(quotationId, authentication);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertFalse(result.getBody().isEmpty());
    }

    @Test
    void shouldReturnCountWhenQuotationsExist() {
        // Mocking
        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.count(1L)).thenReturn(5L);

        // Calling the method and asserting the result
        Long result = quotationController.count(authentication);
        assertEquals(5L, result);
    }

    @Test
    void shouldReturnZeroWhenNoQuotationsExist() {
        // Mocking
        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.count(1L)).thenReturn(0L);

        // Calling the method and asserting the result
        Long result = quotationController.count(authentication);
        assertEquals(0L, result);
    }

    @Test
    void shouldReturnDraftEndorsementsWhenPresent() {
        // Given
        Pageable pageable = PageRequest.of(0, 50, Sort.by("createdDate").descending());
        List<Quotation> expectedQuotations = Arrays.asList(new Quotation(), new Quotation());

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.findAllByCurrentStatusAndStatus("D", "EN", 1L, pageable)).thenReturn(expectedQuotations);

        // When
        ResponseEntity<List<Quotation>> result = quotationController.findAllDraftEndorsements(pageable, authentication);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedQuotations, result.getBody());
    }

    @Test
    void shouldReturnEmptyListWhenNoDraftEndorsementsPresent() {
        // Given
        Pageable pageable = PageRequest.of(0, 50, Sort.by("createdDate").descending());

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.findAllByCurrentStatusAndStatus("D", "EN", 1L, pageable)).thenReturn(Collections.emptyList());

        // When
        ResponseEntity<List<Quotation>> result = quotationController.findAllDraftEndorsements(pageable, authentication);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isEmpty());
    }

    @Test
    void shouldReturnQuotationWhenDraftExists() {
        String policyNo = "P/TEST/001/2023";
        Quotation expectedQuotation = new Quotation();

        when(quotationService.findQuotationDraft(policyNo)).thenReturn(Optional.of(expectedQuotation));

        ResponseEntity<Quotation> result = quotationController.getDraftQuotation(policyNo);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedQuotation, result.getBody());
    }

    @Test
    void shouldReturnNoContentWhenNoDraftExists() {
        String policyNo = "P/TEST/001/2023";

        when(quotationService.findQuotationDraft(policyNo)).thenReturn(Optional.empty());

        ResponseEntity<Quotation> result = quotationController.getDraftQuotation(policyNo);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void shouldReturnQuotationsWhenQuotationNoExists() {
        String quotationNo = "Q/TEST/001/2023";
        List<Quotation> expectedQuotations = Arrays.asList(new Quotation(), new Quotation());

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.findByQuotationNo(quotationNo, 1L)).thenReturn(expectedQuotations);

        ResponseEntity<List<Quotation>> result = quotationController.findByQuotationNo(quotationNo, authentication);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedQuotations, result.getBody());
    }

    @Test
    void shouldReturnEmptyListWhenNoQuotationsForQuotationNo() {
        String quotationNo = "Q/TEST/001/2023";

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.findByQuotationNo(quotationNo, 1L)).thenReturn(Collections.emptyList());

        ResponseEntity<List<Quotation>> result = quotationController.findByQuotationNo(quotationNo, authentication);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isEmpty());
    }

    @Test
    void shouldConvertToPoliciesSuccessfully2() {
        Long id = 1L;

        doNothing().when(quotationService).convertQuotationToPolicies(id);

        ResponseEntity<String> result = quotationController.convertToPolicies(id);

        verify(quotationService).convertQuotationToPolicies(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Quote conversion started successfully", result.getBody());
    }

    @Test
    void shouldHandleExceptionWhenConversionFails() {
        Long id = 1L;

        doThrow(new RuntimeException()).when(quotationService).convertQuotationToPolicies(id);

        assertThrows(RuntimeException.class, () -> quotationController.convertToPolicies(id));
    }

    @Test
    void shouldSaveQuickQuotationWhenOrganizationIdExists() {
        Quotation quotation = new Quotation();
        quotation.setOrganizationId(1L);

        when(quotationService.saveQuickQuotation(quotation, 1L)).thenReturn(quotation);

        ResponseEntity<Quotation> result = quotationController.saveQuickQuotation(quotation, authentication);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(quotation, result.getBody());
    }

    @Test
    void shouldSaveQuickQuotationWhenOrganizationIdIsNull() {
        Quotation quotation = new Quotation();

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.saveQuickQuotation(quotation, 1L)).thenReturn(quotation);

        ResponseEntity<Quotation> result = quotationController.saveQuickQuotation(quotation, authentication);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(quotation, result.getBody());
    }

    @Test
    void shouldThrowExceptionWhenSaveQuickQuotationFails() {
        Quotation quotation = new Quotation();

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.saveQuickQuotation(quotation, 1L)).thenThrow(new QuoteCreationException("Error", new RuntimeException()));

        assertThrows(QuoteCreationException.class, () -> quotationController.saveQuickQuotation(quotation, authentication));
    }

    @Test
    void shouldReturnQuotationsWhenSortAndFilterParametersAreProvided() {
        String searchText = "test";
        Long clientId = 1L;
        Long insOrgId = 1L;
        Long dateFrom = 1633046400000L; // 1st Oct 2021
        Long dateTo = 1635724800000L; // 1st Nov 2021
        BigDecimal priceFrom = new BigDecimal("1000.00");
        BigDecimal priceTo = new BigDecimal("2000.00");
        SortType sortBy = SortType.ASC;
        SortType sortPrice = SortType.DESC;
        int page = 0;
        int size = 50;

        List<Quotation> expectedQuotations = Arrays.asList(new Quotation(), new Quotation());

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.sortFilterAndSearch(searchText, clientId, insOrgId, dateFrom, dateTo, priceFrom,
                priceTo, sortBy, sortPrice, page, size, tokenUtils.getOrganizationId(), "NB")).thenReturn(expectedQuotations);

        ResponseEntity<List<Quotation>> result = quotationController.sortAndFilter(searchText, clientId, insOrgId, dateFrom, dateTo, priceFrom,
                priceTo, sortBy, sortPrice, page, size, authentication);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedQuotations, result.getBody());
    }

    @Test
    void shouldReturnEmptyListWhenNoQuotationsMatchSortAndFilterParameters() {
        String searchText = "test";
        Long clientId = 1L;
        Long insOrgId = 1L;
        Long dateFrom = 1633046400000L; // 1st Oct 2021
        Long dateTo = 1635724800000L; // 1st Nov 2021
        BigDecimal priceFrom = new BigDecimal("1000.00");
        BigDecimal priceTo = new BigDecimal("2000.00");
        SortType sortBy = SortType.ASC;
        SortType sortPrice = SortType.DESC;
        int page = 0;
        int size = 50;

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.sortFilterAndSearch(searchText, clientId, insOrgId, dateFrom, dateTo, priceFrom,
                priceTo, sortBy, sortPrice, page, size, tokenUtils.getOrganizationId(), "NB")).thenReturn(Collections.emptyList());

        ResponseEntity<List<Quotation>> result = quotationController.sortAndFilter(searchText, clientId, insOrgId, dateFrom, dateTo, priceFrom,
                priceTo, sortBy, sortPrice, page, size, authentication);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isEmpty());
    }

    private List<Quotation> testQuotationData() {
        Quotation quotation = new Quotation();
        quotation.setId(1L);
        quotation.setQuotationNo("Q/TEST/001/2023");
        quotation.setPolicyNo("P/TEST/001/2023");
        quotation.setClient(testClientData());
        quotation.setOrganizationId(2L);

        var quotationList = new ArrayList<Quotation>();
        quotationList.add(quotation);

        return quotationList;
    }


    private List<QuotationRisk> testQuotationRiskData() {
        QuotationRisk quotationRisk = new QuotationRisk();
        quotationRisk.setId(1L);
        quotationRisk.setQuotationProductId(1L);
        quotationRisk.setRiskId("AGN 001N");
        quotationRisk.setCoverTypeId(1L);
        quotationRisk.setCoverTypeCode("COMP");
        quotationRisk.setWithEffectFromDate(LocalDateTime.now().atZone(TimeZone.getTimeZone("Africa/Nairobi").toZoneId()).toInstant().toEpochMilli());
        quotationRisk.setWithEffectToDate(LocalDateTime.now().plusMonths(1).atZone(TimeZone.getTimeZone("Africa/Nairobi").toZoneId()).toInstant().toEpochMilli());

        var quotationRiskList = new ArrayList<QuotationRisk>();
        quotationRiskList.add(quotationRisk);

        return quotationRiskList;
    }


    private ClientDto testClientData() {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);
        clientDto.setFirstName("John");
        clientDto.setLastName("Doe");
        clientDto.setClientType(ClientTypes.INDIVIDUAL);
        clientDto.setKraPin("A033244243Z");
        clientDto.setEmailAddress("agencify@agent.com");

        return clientDto;

    }

    private MotorSchedules testScheduleData() {
        MotorSchedules schedules = new MotorSchedules();
        schedules.setId(1L);
        schedules.setMake("Toyota");
        schedules.setModel("Corolla");
        schedules.setYearOfManufacture(2023L);
        schedules.setYearOfRegistration(2023L);
        schedules.setChasisNo("AJFLLSDKKLESD");

        return schedules;
    }

    private ScheduleDetailsDto testScheduleDetailsData() {
        ScheduleDetailsDto scheduleDetailsDto = new ScheduleDetailsDto();
        var schedule = testScheduleData();
        var client = testClientData();
        var quoteRisk = testQuotationRiskData().get(0);
        scheduleDetailsDto.setVehicleMake(schedule.getMake());
        scheduleDetailsDto.setVehicleModel(schedule.getModel());
        scheduleDetailsDto.setVehicleManufactureYear(schedule.getYearOfManufacture());
        scheduleDetailsDto.setVehicleRegistrationYear(schedule.getYearOfRegistration());
        scheduleDetailsDto.setChassisNumber(schedule.getChasisNo());
        scheduleDetailsDto.setBodyType(schedule.getBodyType());
        scheduleDetailsDto.setClientEmail(client.getEmailAddress());
        scheduleDetailsDto.setClientPIN(client.getKraPin());
        scheduleDetailsDto.setCoverTypeId(quoteRisk.getCoverTypeId());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        scheduleDetailsDto.setCommencingDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(quoteRisk.getWithEffectFromDate()), TimeZone.getTimeZone("Africa/Nairobi").toZoneId()).format(dtf));
        scheduleDetailsDto.setExpiryDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(quoteRisk.getWithEffectToDate()), TimeZone.getTimeZone("Africa/Nairobi").toZoneId()).format(dtf));

        return scheduleDetailsDto;
    }


}
