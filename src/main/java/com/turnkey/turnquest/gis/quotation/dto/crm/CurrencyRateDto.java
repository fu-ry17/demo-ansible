package com.turnkey.turnquest.gis.quotation.dto.crm;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class CurrencyRateDto implements Serializable {
    private static final long serialVersionUID = 8535695989139032185L;
    private Long id;

    private Date withEffectFrom;

    private Date withEffectTo;

    private BigDecimal rate;

}
