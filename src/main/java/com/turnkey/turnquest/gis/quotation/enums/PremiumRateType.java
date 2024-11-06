package com.turnkey.turnquest.gis.quotation.enums;

import lombok.Getter;

@Getter
public enum PremiumRateType {
    FXD("FXD", "Fixed"),
    RCU("RCU", "Recursive"),
    RT("RT", "Rate Table"),
    ARG("ARG", "Absolute Ranges"),
    SRG("SRG", "Step Ranges");

    private final String literal;
    private final String description;

    PremiumRateType(String literal, String description) {

        this.literal = literal;
        this.description = description;
    }

}
