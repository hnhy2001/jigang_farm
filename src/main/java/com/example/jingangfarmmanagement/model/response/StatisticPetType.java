package com.example.jingangfarmmanagement.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StatisticPetType {
    private Long type;
    private String name;
    private int quantity;
}
