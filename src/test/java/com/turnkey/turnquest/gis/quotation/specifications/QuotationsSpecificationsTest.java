package com.turnkey.turnquest.gis.quotation.specifications;

import com.turnkey.turnquest.gis.quotation.model.Quotation;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Titus Murithi Bundi
 */
class QuotationsSpecificationsTest {

    @Mock
    private Root<Quotation> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder builder;

    private QuotationsSpecifications<Quotation> quotationsSpecifications;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        quotationsSpecifications = new QuotationsSpecifications<>();
    }

    @Test
    @DisplayName("Should create specification with long value equals")
    void shouldCreateSpecificationWithLongValueEquals() {
        when(builder.equal(any(), any())).thenReturn(null);
        Specification<Quotation> specification = quotationsSpecifications.longValueEquals("attribute", 1L);
        specification.toPredicate(root, query, builder);
    }

    @Test
    @DisplayName("Should handle null long value")
    void shouldHandleNullLongValue() {
        when(builder.equal(any(), any())).thenReturn(null);
        Specification<Quotation> specification = quotationsSpecifications.longValueEquals("attribute", null);
        specification.toPredicate(root, query, builder);
    }

    @Test
    @DisplayName("Should create specification with string value equals")
    void shouldCreateSpecificationWithStringValueEquals() {
        when(builder.equal(any(), any())).thenReturn(null);
        Specification<Quotation> specification = quotationsSpecifications.stringValueEquals("attribute", "value");
        specification.toPredicate(root, query, builder);
    }

    @Test
    @DisplayName("Should handle null string value")
    void shouldHandleNullStringValue() {
        when(builder.equal(any(), any())).thenReturn(null);
        Specification<Quotation> specification = quotationsSpecifications.stringValueEquals("attribute", null);
        specification.toPredicate(root, query, builder);
    }

    @Test
    @DisplayName("Should create specification with long value between")
    void shouldCreateSpecificationWithLongValueBetween() {
        when(builder.between(any(), any(Long.class), any(Long.class))).thenReturn(null);
        Specification<Quotation> specification = quotationsSpecifications.longValueBetween("attribute", 1L, 2L);
        specification.toPredicate(root, query, builder);
    }

    @Test
    @DisplayName("Should handle null long values for between")
    void shouldHandleNullLongValuesForBetween() {
        when(builder.between(any(), any(Long.class), any(Long.class))).thenReturn(null);
        Specification<Quotation> specification = quotationsSpecifications.longValueBetween("attribute", null, null);
        specification.toPredicate(root, query, builder);
    }

    @Test
    @DisplayName("Should create specification with BigDecimal value between")
    void shouldCreateSpecificationWithBigDecimalValueBetween() {
        when(builder.between(any(), any(BigDecimal.class), any(BigDecimal.class))).thenReturn(null);
        Specification<Quotation> specification = quotationsSpecifications.bigDecimalValueBetween("attribute", BigDecimal.ONE, BigDecimal.TEN);
        specification.toPredicate(root, query, builder);
    }

    @Test
    @DisplayName("Should handle null BigDecimal values for between")
    void shouldHandleNullBigDecimalValuesForBetween() {
        when(builder.between(any(), any(BigDecimal.class), any(BigDecimal.class))).thenReturn(null);
        Specification<Quotation> specification = quotationsSpecifications.bigDecimalValueBetween("attribute", null, null);
        specification.toPredicate(root, query, builder);
    }

}
