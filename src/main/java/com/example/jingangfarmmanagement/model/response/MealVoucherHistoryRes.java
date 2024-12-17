package com.example.jingangfarmmanagement.model.response;

import com.example.jingangfarmmanagement.repository.entity.MealVoucherHistory;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Getter
public class MealVoucherHistoryRes {
    private List<MaterialRes> material;
    private MealVoucherHistory mealVoucherHistory;
}
