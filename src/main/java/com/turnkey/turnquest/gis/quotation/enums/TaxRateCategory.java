package com.turnkey.turnquest.gis.quotation.enums;

public enum TaxRateCategory {

    TX("Tax"),AC("Additional Charge"),D("Deductible");

    private final String description;

    TaxRateCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }



}
