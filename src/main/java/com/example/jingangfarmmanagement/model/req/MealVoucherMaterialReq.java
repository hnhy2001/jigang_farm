package com.example.jingangfarmmanagement.model.req;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MealVoucherMaterialReq {
    Long materialId;
    Double quantity;
}
