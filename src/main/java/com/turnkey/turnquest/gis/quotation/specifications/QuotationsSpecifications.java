package com.turnkey.turnquest.gis.quotation.specifications;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class QuotationsSpecifications<T> {

    public Specification<T> longValueEquals(String modelAttribute, Long value){
        return (root, query, criteriaBuilder) -> {
            if(value != null){
                return criteriaBuilder.equal(root.get(modelAttribute),value);
            }else{
                return null;
            }
        };
    }

    public Specification<T> stringValueEquals(String modelAttribute, String value){
        return (root, query, criteriaBuilder) -> {
            if(value != null){
                return criteriaBuilder.equal(root.get(modelAttribute),value);
            }else{
                return null;
            }
        };
    }

    public Specification<T> longValueBetween(String modelAttribute,Long from, Long to){
        return (root, query, criteriaBuilder) -> {
            if(from != null && to != null){
                return criteriaBuilder.between(root.get(modelAttribute),from,to);
            }else{
                return null;
            }
        };
    }

    public Specification<T> bigDecimalValueBetween(String modelAttribute,BigDecimal start, BigDecimal end){
        return (root, query, criteriaBuilder) -> {
            if(start != null && end != null){
                return criteriaBuilder.between(root.get(modelAttribute),start,end);
            }else{
                return null;
            }
        };
    }

}
