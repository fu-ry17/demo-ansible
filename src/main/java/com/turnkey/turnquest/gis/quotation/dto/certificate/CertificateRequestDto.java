package com.turnkey.turnquest.gis.quotation.dto.certificate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CertificateRequestDto {
    
    private String type;
   
    private String TypeOfCertificate;
   
    private String IntermediaryIRANumber;
   
    private String Typeofcover;
    
    private String Policyholder;
    
    private String policynumber;
    
    private String Commencingdate;
    
    private String Expiringdate;
    
    private String Registrationnumber;
    
    private String Chassisnumber;
    
    private String Phonenumber;
    
    private String Bodytype;
    
    private Integer Licensedtocarry;
    
    private String Vehiclemake;
    
    private String Vehiclemodel;
    
    private Integer Yearofregistration;
    
    private String Enginenumber;
    
    private String Email;
    
    private BigDecimal SumInsured;
    
    private String InsuredPIN;
    
    private Integer Yearofmanufacture;
    
    private String HudumaNumber;
    
    private String VehicleType;
    
    private Integer TonnageCarryingCapacity;


}
