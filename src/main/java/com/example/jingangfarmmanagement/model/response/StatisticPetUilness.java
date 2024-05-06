package com.example.jingangfarmmanagement.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StatisticPetUilness {
    private Long uilness;
    private String name;
    private int quantity;
    private int score;
}
