package com.example.jingangfarmmanagement.model.response;

import com.example.jingangfarmmanagement.repository.entity.Cage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultStatisticByCageRes {
    private Cage cage;
    private int totalPet;
    private int totalMale;
    private int totalFemale;
    private int petDie;
    private int petLive;
//    private int petUilness;
//    private int petNomal;
    private List<StatisticPetByAge> statisticPetByAgeList;
    private List<StatisticPetByWeight> statisticPetByWeightList;
    private List<StatisticStatusUilnessPet> statisticStatusUilnessPetList;
}
