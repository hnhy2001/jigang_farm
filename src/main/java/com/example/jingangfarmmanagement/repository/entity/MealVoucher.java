package com.example.jingangfarmmanagement.repository.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MealVoucher extends BaseEntity{
    String code;
    String note;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "meal_voucher_pet",
            joinColumns = @JoinColumn(name = "meal_voucher_id"),
            inverseJoinColumns = @JoinColumn(name = "pet_id")
    )
    @JsonManagedReference
    private List<Pet> pets = new ArrayList<>();
}
