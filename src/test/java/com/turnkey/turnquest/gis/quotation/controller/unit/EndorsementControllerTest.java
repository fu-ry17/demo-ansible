package com.turnkey.turnquest.gis.quotation.controller.unit;

import com.turnkey.turnquest.gis.quotation.controller.EndorsementController;
import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import com.turnkey.turnquest.gis.quotation.model.Quotation;
import com.turnkey.turnquest.gis.quotation.service.EndorsementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author Titus Murithi Bundi
 */
class EndorsementControllerTest {

    @Mock
    private EndorsementService endorsementService;

    @InjectMocks
    private EndorsementController endorsementController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveAnnualQuotationRisk() {
        // Given
        Quotation quotation = new Quotation();
        when(endorsementService.saveAnnualQuotationRisk(quotation, YesNo.Y)).thenReturn(quotation);

        // When
        ResponseEntity<Quotation> response = endorsementController.saveAnnualQuotationRisk(quotation);

        // Then
        assertEquals(quotation, response.getBody());
    }

    @Test
    void shouldComputeFirstInstallment() {
        // Given
        Quotation quotation = new Quotation();
        when(endorsementService.computeFirstInstallment(quotation)).thenReturn(quotation);

        // When
        ResponseEntity<Quotation> response = endorsementController.computeFirstInstallment(quotation);

        // Then
        assertEquals(quotation, response.getBody());
    }

    @Test
    void shouldComputeNextInstallment() {
        // Given
        String quotationNumber = "123";
        Quotation quotation = new Quotation();
        when(endorsementService.getQuotationToPay(quotationNumber)).thenReturn(quotation);

        // When
        ResponseEntity<Quotation> response = endorsementController.computeNextInstallment(quotationNumber);

        // Then
        assertEquals(quotation, response.getBody());
    }
}
