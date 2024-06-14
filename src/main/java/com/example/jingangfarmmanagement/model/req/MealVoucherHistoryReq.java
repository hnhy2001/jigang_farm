package com.example.jingangfarmmanagement.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealVoucherHistoryReq {
    private Long mealVoucherId;
    private Long checkingDate;
    List<MealVoucherMaterialReq> materials;
}
