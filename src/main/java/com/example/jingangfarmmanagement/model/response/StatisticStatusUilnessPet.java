package com.example.jingangfarmmanagement.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticStatusUilnessPet {
    private int point;
    private String name;
    private int totalPet;
    private int totalMale;
    private int totalFemale;
}
