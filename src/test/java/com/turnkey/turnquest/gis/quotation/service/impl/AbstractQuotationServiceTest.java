package com.turnkey.turnquest.gis.quotation.service.impl;

import com.turnkey.turnquest.gis.quotation.enums.ValuationStatus;
import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import com.turnkey.turnquest.gis.quotation.model.QuotationProduct;
import com.turnkey.turnquest.gis.quotation.model.QuotationRisk;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AbstractQuotationServiceTest {

    private AbstractQuotationService absQuotation;

    AbstractQuotationServiceTest() {
        this.absQuotation = Mockito.mock(AbstractQuotationService.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    void checkAndRemoveDuplicateRisks() {
        var risks = absQuotation.checkAndRemoveDuplicateRisks(Arrays.asList(
                testRiskDates(ValuationStatus.OPEN, YesNo.N),
                testRiskDates(ValuationStatus.OPEN, YesNo.N)));

        assertEquals(1, risks.size());
    }


    QuotationProduct testQuotationProductDates() {
        var qp = new QuotationProduct();
        qp.setPolicyCoverFrom(Timestamp.valueOf("2029-2-2 00:00:00").getTime());
        qp.setPolicyCoverTo(Timestamp.valueOf("2030-2-1 00:00:00").getTime());
        qp.setWithEffectFromDate(Timestamp.valueOf("2029-1-2 00:00:00").getTime());
        qp.setWithEffectToDate(Timestamp.valueOf("2030-1-1 00:00:00").getTime());
        return qp;
    }

    QuotationRisk testRiskDates(ValuationStatus valuationStatus, YesNo isInstallment) {
        var qr = new QuotationRisk();

        qr.setWithEffectFromDate(Timestamp.valueOf("2029-2-2 00:00:00").getTime());
        qr.setWithEffectToDate(Timestamp.valueOf("2029-3-1 00:00:00").getTime());
        qr.setRiskId("KDJ 777K");
        qr.setValuationStatus(valuationStatus);
        qr.setInstallmentAllowed(isInstallment);

        return qr;
    }
}