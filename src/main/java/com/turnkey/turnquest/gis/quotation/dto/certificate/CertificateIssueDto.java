/**
 *
 */
package com.turnkey.turnquest.gis.quotation.dto.certificate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Paul Gichure
 *
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
//@ApiModel(value = "Certificate Issue", description = "The certificate requesting details")
public class CertificateIssueDto {

    //	@ApiModelProperty(required = true, allowEmptyValue = false, name = "riskId", notes = "The vehicle registration number")
    private String vehicleRegistrationNumber;

    //	@ApiModelProperty(required = false, name = "startDate", notes = "The certificate cover start date")
    private Date startDate;

    //	@ApiModelProperty(required = false, name = "endDate", notes = "The certificate cover end date")
    private Date endDate;

    //	@ApiModelProperty(required = false, name = "policyId", notes = "The policyId of the policy to which the risk belongs to")
    private Long policyId;

}
