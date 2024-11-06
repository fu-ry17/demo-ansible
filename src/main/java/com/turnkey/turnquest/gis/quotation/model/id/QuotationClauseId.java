package com.turnkey.turnquest.gis.quotation.model.id;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuotationClauseId implements Serializable {

    private static final long serialVersionUID = 6333668510063029909L;

    private Long clauseId;

    private Long quotationProductId;
}
