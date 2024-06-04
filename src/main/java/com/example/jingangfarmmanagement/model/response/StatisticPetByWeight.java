package com.example.jingangfarmmanagement.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticPetByWeight {
    private int from;
    private int to;
    private String unit;
    private int quantity;
}
