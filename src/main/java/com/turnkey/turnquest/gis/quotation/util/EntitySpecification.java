package com.turnkey.turnquest.gis.quotation.util;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class EntitySpecification<T> {
    public Specification<T> textInAllColumns(String text) {

        if (!text.contains("%")) {
            text = "%" + text + "%";
        }
        final String finalText = text;

        return (root, cq, builder) -> builder.or(
                root.getModel().getDeclaredSingularAttributes().stream()
                        .filter(a -> a.getJavaType().getSimpleName().equalsIgnoreCase("String"))
                        .map(a -> builder.like(root.get(a.getName()), finalText))
                        .toArray(Predicate[]::new)
        );
    }

}
