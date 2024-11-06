package com.turnkey.turnquest.gis.quotation.enums;

public enum TaxRateInstallmentType {

    NONE("Not applicable to taxes. Validated against the product"),
    EVEN(" Distributed evenly across all values"),
    FIRST("Entirely loaded on 1st Instalment amount");

    private final String description;

    TaxRateInstallmentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
