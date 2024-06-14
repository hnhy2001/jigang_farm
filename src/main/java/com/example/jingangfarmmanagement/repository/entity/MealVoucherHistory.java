package com.example.jingangfarmmanagement.repository.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MealVoucherHistory extends BaseEntity{
    private Long mealVoucherId;
    private Long checkingDate;
}
