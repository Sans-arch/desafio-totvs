package com.github.sansarch.desafio_totvs.infrastructure.persistence.filter;

import com.github.sansarch.desafio_totvs.infrastructure.persistence.model.PayableModel;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class PayableSpecification {

    public static Specification<PayableModel> hasDueDate(LocalDateTime dueDate) {
        return (root, query, criteriaBuilder) ->
                dueDate == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("dueDate"), dueDate);
    }

    public static Specification<PayableModel> hasDescription(String description) {
        return (root, query, criteriaBuilder) ->
                (description == null || description.isBlank()) ? criteriaBuilder.conjunction() :
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + description.toLowerCase() + "%");
    }
}
