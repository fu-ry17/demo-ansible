package com.turnkey.turnquest.gis.quotation.dto.endorsement;

import com.turnkey.turnquest.gis.quotation.enums.YesNo;

public record DocumentDto (

    String document,

    Long organizationId,

    YesNo isValuationLetter

){}
