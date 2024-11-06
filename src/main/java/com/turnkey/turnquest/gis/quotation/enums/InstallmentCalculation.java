package com.turnkey.turnquest.gis.quotation.enums;

public enum InstallmentCalculation {

    RAT("Ratio"),FREQ("Frequency");

    private String description;

    InstallmentCalculation(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
