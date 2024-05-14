package com.example.jingangfarmmanagement.uitl;

import org.springframework.data.jpa.domain.Specification;

public class SpecUtil<T> {
    private Specification<T> withStartEndDate(Long startDate, Long endDate){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.and(criteriaBuilder.lessThan(root.get("created_date"), endDate), criteriaBuilder.greaterThan(root.get("created_date"), startDate)));
    }
}
