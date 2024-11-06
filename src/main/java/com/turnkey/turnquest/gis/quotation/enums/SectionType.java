package com.turnkey.turnquest.gis.quotation.enums;

public enum SectionType {
    SS("SS", "Section Sum Insured"),
    SI("SI", ""),
    EL("EL", "Extension Limit"),
    EC("EC", ""),
    DO("DO", ""),
    ES("ES", ""),
    EQ("EQ", "Earthquake"),
    NP("NP", ""),
    DS("DS", "Discount"),
    ND("ND", "No Claim Discount"),
    RS("RS", " Rider Section"),
    LO("LO", "Loading");

    private String literal;
    private String description;

    SectionType(String literal, String description) {
        this.literal = literal;
        this.description = description;
    }

    public String getLiteral() {
        return literal;
    }

    public void setLiteral(String literal) {
        this.literal = literal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
