package com.turnkey.turnquest.gis.quotation.enums;

public enum LinkType {
    QUOTATION("quotation"),
    RENEWAL("renewal"),
    POLICY("policy"),
    CLAIM("claim"),
    DEAL("deal"),
    CLIENT("client"),
    OTHER("other");

    private String LinkType;

    LinkType(String linkType) {
        this.LinkType = linkType;
    }

    public String getLinkType() {
        return LinkType;
    }
}
