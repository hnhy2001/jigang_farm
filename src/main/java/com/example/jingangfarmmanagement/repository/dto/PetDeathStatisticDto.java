package com.example.jingangfarmmanagement.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PetDeathStatisticDto {
    private String  date;
    private Long maleDeaths;
    private Long femaleDeaths;
    private Long naDeaths;
}
