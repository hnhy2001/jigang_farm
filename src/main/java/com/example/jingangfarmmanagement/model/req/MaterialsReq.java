package com.example.jingangfarmmanagement.model.req;

import com.example.jingangfarmmanagement.repository.entity.Materials;
import com.example.jingangfarmmanagement.repository.entity.Warehouse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialsReq {
    Materials materials;
    Long estimateQuantity = 0L;
    Long actualQuantity = 0L;
    Long unitPridce = 0L;
    Long price = 0L;
    Warehouse warehouse;
    Long expirationDate;
}
