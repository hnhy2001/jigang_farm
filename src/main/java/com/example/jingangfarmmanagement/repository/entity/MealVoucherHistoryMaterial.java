package com.example.jingangfarmmanagement.repository.entity;

import lombok.*;

import javax.persistence.Entity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MealVoucherHistoryMaterial extends BaseEntity{
    private Long mealVoucherHistoryId;
    private Long materialId;
    private Long quantity;
}
