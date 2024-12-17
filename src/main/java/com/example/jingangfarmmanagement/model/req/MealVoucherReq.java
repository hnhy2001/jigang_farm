package com.example.jingangfarmmanagement.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealVoucherReq {
    String code;
    String note;
    Long createDate;
    List<Long> petIds;
}
