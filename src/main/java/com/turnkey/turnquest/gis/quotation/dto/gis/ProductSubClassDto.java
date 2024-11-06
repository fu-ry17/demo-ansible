package com.turnkey.turnquest.gis.quotation.dto.gis;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProductSubClassDto implements Serializable {

    private static final long serialVersionUID = -8060051804305378804L;
    private Long id;

    private ProductDto product;

    private SubClassDto subClass;

  /*  @OneToOne
    private Binder binder;*/

    private Date effectiveFrom;

    private Date effectiveTo;
}
