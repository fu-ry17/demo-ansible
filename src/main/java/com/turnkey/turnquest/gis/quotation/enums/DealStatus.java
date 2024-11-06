package com.turnkey.turnquest.gis.quotation.enums;

public enum DealStatus {

    WON("won"),LOST("lost"),PENDING("pending");

    private String status;

    DealStatus(String status){
        this.status = status;
    }

    private String getStatus(){return status;}


    @Override
    public String toString() {
        return getStatus();
    }
}
