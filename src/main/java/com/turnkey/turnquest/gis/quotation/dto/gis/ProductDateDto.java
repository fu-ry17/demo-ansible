package com.turnkey.turnquest.gis.quotation.dto.gis;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
public class ProductDateDto implements Serializable {
    private static final long serialVersionUID = 1643642675002420622L;
    private Date date;
}
