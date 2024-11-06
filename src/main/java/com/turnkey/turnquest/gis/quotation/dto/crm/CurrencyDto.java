package com.turnkey.turnquest.gis.quotation.dto.crm;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CurrencyDto implements Serializable {
    private static final long serialVersionUID = 450360645747891390L;
    private Long id;

    private String code;

    private String symbol;

    private String name;

    private BigDecimal roundingOff;

    private String numberWord;

    private String decimalWord;
}
