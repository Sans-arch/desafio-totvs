package com.github.sansarch.desafio_totvs.infrastructure.persistence.filter;

import com.github.sansarch.desafio_totvs.domain.entity.PayableStatus;
import com.github.sansarch.desafio_totvs.infrastructure.persistence.model.PayableModel;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class PayableSpecification {

    public static Specification<PayableModel> hasDueDate(LocalDateTime dueDate) {
        return (root, query, criteriaBuilder) ->
                dueDate == null ? criteriaBuilder.conjunction() : criteriaBuilder.lessThanOrEqualTo(root.get("dueDate"), dueDate);
    }

    public static Specification<PayableModel> hasDescription(String description) {
        return (root, query, criteriaBuilder) ->
                (description == null || description.isBlank()) ? criteriaBuilder.conjunction() :
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + description.toLowerCase() + "%");
    }

    public static Specification<PayableModel> hasStatusPaid() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), PayableStatus.PAID);
    }

    public static Specification<PayableModel> hasDueDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return criteriaBuilder.conjunction();
            } else if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("dueDate"), startDate, endDate);
            } else if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("dueDate"), startDate);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("dueDate"), endDate);
            }
        };
    }
}
