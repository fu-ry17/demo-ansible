package com.turnkey.turnquest.gis.quotation.dto.quotation;

import com.turnkey.turnquest.gis.quotation.model.Quotation;
import lombok.Data;

@Data
public class SaveQuickQuoteDto {

    private Quotation quotationDto;

    private QuotationRiskDto quotationRiskDto;
}
