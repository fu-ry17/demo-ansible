package com.turnkey.turnquest.gis.quotation.enums;

public enum InstallmentCoverGaps {

    NONE("No cover period gap"),CERT("Maintain cover period gap on the certificate"),
    ALL("Maintain a cover period gap on the policy/risk/certificate");

    private String description;

    InstallmentCoverGaps(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
