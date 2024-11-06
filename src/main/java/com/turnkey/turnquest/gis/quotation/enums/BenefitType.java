package com.turnkey.turnquest.gis.quotation.enums;

public enum BenefitType {
    LIMIT("LIM"),

    AMOUNT("AMT"),

    RATE("RAT");

    private String benefitType;

    BenefitType(String value) {
        benefitType = value;
    }

    private void setBenefitType(String benefitType) {
        this.benefitType = benefitType;
    }

    public String getBenefitType() {
        return benefitType;
    }
}
